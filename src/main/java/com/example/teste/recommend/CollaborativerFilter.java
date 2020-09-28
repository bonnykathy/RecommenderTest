package com.example.teste.recommend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

import com.examplee.teste.modelo.dao.Connection;

public class CollaborativerFilter {

	private HashMap<String, UVSimilarity> similarities = new HashMap<>();
	private final String POI_NAME = "poi_name";
	private final String TABLE = "fc_teste";

	private Connection con = new Connection();

	public List<String> computeSimilairty(String u) throws SQLException {

		/**
		 * Recupera todos os owners (V's) da base que classificaram algum lugar em comum
		 * com o usuário U
		 **/
		String query = "select distinct owner from " + TABLE + " where " + POI_NAME + " in" + "(select " + POI_NAME
				+ " from " + TABLE + " where owner = '" + u + "') and owner != '" + u + "'";

		ResultSet rs_owners = con.executeSearch(query);

		ResultSet dados;

		String v; // usuário comparativo
		String poi;// local
		int qtdLv = 0;// quantidade de locais visitados pelo usuário comparativo
		int qtdLu = 0;// quantidade de pois visitados pelo usuário alvo
		double rU = 0;// classificação do usuário alvo
		double rV = 0;// classificaçao do usuário comparativo
		double mGU = 0;// media geral das classificações de u
		double mU = 0;// media das classificaçõe de U em conjunto com V
		double mV = 0;// media das classificaçõe de V em conjunto com U
		double sRuv = 0;
		double rUi = 0;
		double rVi = 0;
		double sRui = 0;
		double sRvi = 0;
		double simUV = 0; // Correlacao de Pearson - similaridade entre U e V

		/**
		 * recupera a média de classificação do usuário e a quantidade de locais para
		 * onde ele foi para que tal valor sirva como medida de maior similaridade entre
		 * os V's mais similares
		 */
		// TODO: CORRIGIR PARA UTILIZAR RATES APENAS DOS ITENS EM COMUNS COM USERS
		// SIMILARES
		query = "select sum(rate)/count(" + POI_NAME + ") media, count (" + POI_NAME + ") qtd_locais from " + TABLE
				+ " where owner = '" + u + "'";
		dados = con.executeSearch(query);

		if (dados.next()) {
			mGU = dados.getDouble("media"); // necessário para o cálculo das previsões
			qtdLu = dados.getInt("qtd_locais");

			System.out.println("\n\n\nTotal de Locais visitados = " + qtdLu);
		}

		while (rs_owners.next()) {

			v = rs_owners.getString("owner");// owner = "49715745@N05"; teste para divisao por zero na CP
			/**
			 * selecionar os destinos em comum entre o usuário e o owner
			 */
			query = "select " + POI_NAME + " from " + TABLE + " where owner = '" + v + "' and " + POI_NAME
					+ " in (select " + POI_NAME + " from " + TABLE + " where owner = '" + u + "')";
			ResultSet rs_destinos = con.executeSearch(query);

			// busca os dois registros de média de usuário alvo e comparativo
			query = "select owner,sum(rate)/ count(*) media, count(*) from " + TABLE + " where owner = '" + u + "' and "
					+ POI_NAME + " in (select " + POI_NAME + " from " + TABLE + " where owner = '" + v
					+ "') or owner = '" + v + "' and " + POI_NAME + "" + " in (select " + POI_NAME + " from " + TABLE
					+ " where owner = '" + u + "') group by (owner)";

			dados = con.executeSearch(query);

			while (dados.next()) {

				if (dados.getString("owner").equals(u))
					mU = dados.getDouble("media");
				else
					mV = dados.getDouble("media");

				if (dados.isFirst()) // necessário pegar o count apenas uma vez. Necessário para definir o peso da
										// similaridade
					qtdLv = dados.getInt("count");
			}

			while (rs_destinos.next()) {

				poi = rs_destinos.getString(POI_NAME);

				/** Recuperacão das classificacões de U e V para o poi */

				query = "select owner,rate from " + TABLE + " where owner in ('" + v + "', '" + u + "')" + " and "
						+ POI_NAME + " = '" + poi + "'";
				dados = con.executeSearch(query);

				while (dados.next()) {

					if (dados.getString("owner").equals(u))
						rU = dados.getDouble("rate");
					else
						rV = dados.getDouble("rate");

				}

				/** Cálculo da similaridade = Correlação de Pearson */
				rUi = rU - mU;
				rVi = rV - mV;
				sRuv += rUi * rVi;

				sRui += Math.pow(rUi, 2);
				sRvi += Math.pow(rVi, 2);

			}

			simUV = sRuv / Math.sqrt(sRui * sRvi);

			if (!Double.isNaN(simUV)) /** CP retorna valores entre -1 e 1 */
			{
				UVSimilarity uv = new UVSimilarity(u, v, simUV, mV, mU, qtdLv, qtdLu);
				similarities.put(v, uv);
				System.out.println(uv);
			}

			sRui = 0;
			sRvi = 0;
			sRuv = 0;
			qtdLv = 0;

		}

		return computeRate(u, 0.5);

	}

	private List<String> computeRate(String u, double limit) throws SQLException {

		double p = 0;
		double avg_ra = 0;
		int count_ra = 0;

		double rate;
		double sum = 0;
		double sumW = 0;

		List<String> locais = new ArrayList<String>();
		List<String> previsions = new ArrayList<String>();

		/** recupera todos os locais onde u não esteve */
		String query = "select distinct " + POI_NAME + " from " + TABLE + " where " + POI_NAME + " not in" + "(select "
				+ POI_NAME + " from " + TABLE + " where owner = '" + u + "') and "
				+ "owner in(select distinct owner from " + TABLE + " where " + POI_NAME + " in " + "(select " + POI_NAME
				+ " from " + TABLE + " where owner = '" + u + "')) order by(" + POI_NAME + ")";

		ResultSet rs = con.executeSearch(query);
		String j = "";
		while (rs.next()) {

			/**
			 * Calcula a média de classificação do usuário considerando os usuários mais
			 * similares
			 */
			 j = rs.getString("poi_name");
			query = "select poi_name, rate from " + TABLE + "  where  owner = '" + u + "'";

			ResultSet rs_ra = con.executeSearch(query);

			while (rs_ra.next()) {

				query = "select owner from " + TABLE + " where poi_name = '" + j + "'";

				ResultSet rs_sim_owner = con.executeSearch(query);

				while (rs_sim_owner.next()) {

					UVSimilarity s = similarities.get(rs_sim_owner.getString("owner"));

					if (s != null && s.simUV >= limit) { // verifica se v pertence a lista de usuários mais similares a
															// u
						avg_ra += rs_ra.getDouble("rate");
						count_ra++;

					}

				}

			}

			locais.add(j);
		}

		avg_ra = avg_ra / count_ra;
		System.out.println("Média do usuário  = " + avg_ra);

		for (String l : locais) {

			/**
			 * Recupera as classificações dos usuários similares
			 */
			query = "select owner, rate from " + TABLE + "  where  " + POI_NAME + " = '" + l + "'	and owner != '" + u
					+ "'";
			rs = con.executeSearch(query);

			while (rs.next()) {

				UVSimilarity s = similarities.get(rs.getString("owner"));// recupera através da chave

				if (s != null) { // verifica se v pertence a lista de usuários mais similares a u

					rate = rs.getDouble("rate");
					sum +=  (rate - s.mV) * s.simUV;
					sumW += Math.abs(s.simUV);
				}
			}

			p = avg_ra + (sum / sumW); // calcula a predição para l

			if (p > 2) {
				previsions.add(l);
				System.out.println(l + " = " + p);
			}

			sum = 0;
			sumW = 0;

		}

		return previsions;

	}

	private class UVSimilarity {

		private String u; // identificador do owner
		private String v;
		private double simUV;// valor da correlação de pearson
		private double mV;// media de v
		private double mU;// media de u
		private int qtdLv; // quantidade de locais visitados de v
		private double qtdLu;// quantidade de locais visitados de u
		private double weight; // peso para definir relevância nas recomendações

		public UVSimilarity(String u, String v, double simUV, double mV, double mU, int qtdLv, double qtdLu) {
			this.u = u;
			this.v = v;
			this.simUV = simUV;
			this.mV = mV;
			this.mU = mU;
			this.qtdLv = qtdLv;
			this.qtdLu = qtdLu;

			weight = this.qtdLv / this.qtdLu; // normalização

		}

		@Override
		public String toString() {
			DecimalFormat df = new DecimalFormat("#.###");
			return "owner = " + v + "     similaridade = " + df.format(simUV) + "       qdtGeral = " + qtdLu
					+ "      qdt = " + qtdLv + "      peso = " + df.format(weight);
		}

	}

	public static void main(String[] args) throws SQLException {

		CollaborativerFilter m = new CollaborativerFilter();
		//m.computeSimilairty("49414881@N05");
	//	m.computeSimilairty("38583880@N00");
		m.computeSimilairty("USER5");
		

		// m.computeSimilairty("52476605@N08");

	}

}
