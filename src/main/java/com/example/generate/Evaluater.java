package com.example.generate;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.teste.modelo.Local;
import com.example.teste.recommend.RecommenderMahoutByItem;
import com.example.teste.recommend.RecommenderNB;
import com.example.teste.recommend.Utils;
import com.examplee.teste.modelo.dao.Connection;

public class Evaluater {

	private List<LocalEvaluate> results = new ArrayList();
	private static String dbInsert = "";

	private class LocalEvaluate {

		private String localName;
		private boolean result;
		private int positonOfRank;

		private LocalEvaluate(String localName, boolean result, int positionOfRank) {

			this.localName = localName;
			this.result = result;
			this.positonOfRank = positionOfRank;

		}

	}

	/*
	 * steps (obs: usar threads)
	 * 
	 * 1 - pegar do histórico do usuário as informações do contexto temporal 2 -
	 * chamar NB 3 - chamar a FC 4 - buscar local dentro dos top 5 ou 10 recuperados
	 * pelo NB 6 - buscar local entre os 5 itens retornados pela FC 7 - contabilizar
	 * acerto ou erro 8 - classificar na tabela de FC referente ao perfil do
	 * usuário.
	 * 
	 */

	private void getUserHistory(int userId, boolean cod, int top, String folder) throws IOException, SQLException {

		Map<Integer, Float> classified = new HashMap();

		WriterCSVFCDataBase fcWriter = new WriterCSVFCDataBase();

		String query = "select A.poi_name, A.poi_id, date_time_original, day, hour_day, month, A.category, perfil_g, rate_relevance  from rc_naive_classification_ed A inner JOIN"
				+ " (select * from rc_table_fc_rate_novo) B ON " + "A.user_id = " + userId
				+ " and A.user_id = B.user_id and " + "A.poi_id = B.poi_id"
				+ " order by (CAST(date_time_original as DATE), CAST(date_time_original as TIME)) asc";

		Connection con = new Connection();
		ResultSet rs = con.executeSearch(query);

		String localName = null, profile = null, month = null, hourDay = null, day = null, category = null;
		float rate = 0;
		int poiId = 0;
		int NB = 0;
		int FC = 0;

		System.out.println(" ----------------- " + userId + " --------------------------- ");

		while (rs.next()) {

			localName = rs.getString("poi_name");
			profile = rs.getString("perfil_g");
			day = rs.getString("day");
			hourDay = rs.getString("hour_day");
			month = rs.getString("month");
			category = rs.getString("category");
			rate = rs.getFloat("rate_relevance");
			poiId = rs.getInt("poi_id");

			/** reconstroi a tabela da FC para testes com esse usuário **/
			if (rs.isFirst())
				fcWriter.writeIndividualEvaluate(userId, profile, cod, folder);

			int position = getRecommendsByNBPosition(localName, profile, month, hourDay, day, category, cod, folder);

			boolean result;

			result = (position <= top) ? true : false;

			if (!result) { // caso o NB não seja capaz de retornar uma recomendação, a FC é consultada
				result = getRecommendationsByFC(classified, poiId, profile, userId, top, cod, folder+"/");
				System.out.println("***FC***********FC*********************"+result+" ***********FC***********FC***********");
				if(result)
					FC++;
			}else {
				NB++;
			}
			LocalEvaluate le = new LocalEvaluate(localName, result, position);
			results.add(le);

			/** classica o local na tabela FC correspondente ao perfil **/
			if (!classified.containsKey(poiId)) {
				classified.put(poiId, rate);
				ratePOI(profile, rate, poiId, userId, cod, folder);
			}
		} // Laço principal

		int count = 0;
		for (LocalEvaluate le : results) {
			System.out.println(le.localName + " - " + le.positonOfRank + " = " + le.result);
			if (le.result)
				count++;

		}

		String insert = "insert into "+dbInsert+" values" + "(" + userId + "," + poiId + ", '" + profile + "',"
				+ ((float) count / results.size()) + "," + top + "," + cod + ","+NB+","+FC+")";

		con.execute(insert);

		System.out.println(insert);
	}

	/**
	 * método que utiliza o NB
	 */
	private int getRecommendsByNBPosition(String localName, String profile, String month, String hourDay, String day,
			String category, boolean cod, String folder) {

		RecommenderNB nb = new RecommenderNB();

		List<Local> locais = nb.getRecommendEvaluate(profile, profile, month, hourDay, day, 0, cod,folder,
				"" + Utils.getCategory(category));

		int positionRank = 0;

		for (int i = 0; i < locais.size(); i++) {

			if (localName.contentEquals(locais.get(i).getName())) {
				positionRank = i + 1;
				break;
			}
		}

		return positionRank;
	}

	/**
	 * método que utiliza FC
	 */
	private boolean getRecommendationsByFC(Map classified, int poiId, String profile, int userId, int qtdRetorns,
			boolean cod, String folder) throws IOException {

		if (!classified.containsKey(poiId)) {// se não foi classificado, executa

			List<Local> locais = new RecommenderMahoutByItem().recommendEvaluate(userId, qtdRetorns,
					cod, folder);

			if (locais != null) {

				for (Local l : locais) {
					System.out.println("---->"+l.getName());

					if (l.getId() == poiId) {
						System.err.println("ENTROU");
						return true;
					}
				}
			}

		}
		return false;

	}

	private void ratePOI(String profile, float rate, int poiId, int userId, boolean cod, String folder) throws IOException {

		WriterCSVFCDataBase fcWriter = new WriterCSVFCDataBase();
		fcWriter.writeEvaluate(userId, poiId, rate, profile, cod,  folder);

	}

	private class ExecuteThreads implements Runnable {

		private boolean cod;
		private int top;
		private String query;
		private String folder;
		private ExecuteThreads(boolean cod, int top, String query,String folder) {

			this.cod = cod;
			this.top = top;
			this.query = query;
			this.folder = folder;
		}

		@Override
		public void run() {

			Evaluater e = new Evaluater();

			Connection con = new Connection();
			ResultSet rs = con.executeSearch(query);
			try {

				int userId = 0;

				while (rs.next()) {

					userId = rs.getInt("user_id");
					e.getUserHistory(userId, cod, top, folder);
				}

			} catch (IOException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	public static void main(String[] args) {
		
		boolean isPOI = true;
		String query;
		String f = "01";
		String f2 = "01_1";
		
		int minInterval = 15,maxInterval = 20;
		
		
		
		
		if(isPOI) {
			
//			 Utils.T_FILE_PROFILE = "t_rate_rel_evaluate_edit_pois_profile.csv";// necessário aterar
//			 Utils.T_FILE_ALL = "t_rate_rel_evaluate_edit_pois_all.csv";// necessário aterar
			
			Utils.FILE_ALL = "all_pois.csv";

			 Utils.R_FILE_PROFILE = "residente_pois.csv";// necessário aterar
//			 Utils.R_FILE_ALL = "r_rate_rel_evaluate_edit_pois_all.csv";// necessário aterar
			
			 Utils.dbNaive  = "rc_nb_evaluate_pois_"+f;// necessário aterar
			 
			 dbInsert = "rc_evaluate_pois_"+f2;// necessário aterar
			 
			 Utils.EVALUATION = "EVALUATION POIS/"; Utils.EVALUATION_ALL = "EVALUATION ALL POIS/";// necessário aterar
			 
			 /** execução por número de locais **/
				query = "select * from view_evaluate_nb_pois where qtd >= "+minInterval+" and qtd <="+maxInterval+"";// necessário aterar
		}
		else {
//			 Utils.T_FILE_PROFILE = "t_rate_rel_evaluate_edit_profile.csv";// necessário aterar
//			 Utils.T_FILE_ALL = "t_rate_rel_evaluate_edit_all.csv";// necessário aterar

			Utils.FILE_ALL = "all.csv";
			
			 Utils.R_FILE_PROFILE = "residente.csv";// necessário aterar
//			 Utils.R_FILE_ALL = "r_rate_rel_evaluate_edit_all.csv";// necessário aterar
			
			 Utils.dbNaive  = "rc_nb_evaluate_"+f;// necessário aterar
			 
			 dbInsert = "rc_evaluate_"+f2;// necessário aterar
			 
			 Utils.EVALUATION = "EVALUATION/"; Utils.EVALUATION_ALL = "EVALUATION ALL/";// necessário aterar
			 
				/** execução por número de visitas **/
				 query = "select * from view_evaluate_nb where qtd >="+minInterval+" and qtd <="+maxInterval+"";// necessário aterar
		}
			 
	 

		Evaluater e = new Evaluater();

		int top = 3;// necessário aterar
		String folder = "1.1";//necessário

		Thread t1 = new Thread(e.new ExecuteThreads(false, top, query, folder));
		Thread t2 = new Thread(e.new ExecuteThreads(true, top,query,folder));

		t1.start();
		t2.start();

	}

}
