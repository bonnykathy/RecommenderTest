package com.examplee.teste.modelo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.teste.modelo.Local;
import com.example.teste.recommend.Utils;

public class LocalDAO {

	private Connection con = new Connection();

	public static void main(String[] args) {
		
//		LocalDAO d  = new LocalDAO();
//		d.toRate("USER_A", "turista", 9090, "LOCAL", 909090,"AR LIVRE", 5, "segunda", "manhã", "janeiro");
		

	}

	/**
	 * Método que retorna a média das classificações dos usuário junto com categoria
	 * e percentual de visita de turistas ou residentes
	 */
	public void getAVGRecommend(List<Local> list) {

		String query = "";

		for (Local local : list) {

			query = "select poi_id,poi_name, sum(rate_relevance)/count(*) avg,category from " + Utils.TABLE_FC_EDIT_USER + " "
					+ "where poi_name ='" + local.getName() + "' group by poi_name,category,poi_id";

			ResultSet rs = con.executeSearch(query);

			try {
				if (rs.next()) {
					local.setAvg(rs.getDouble("avg"));
					local.setCategory(rs.getString("category"));
					local.setId(rs.getInt("poi_id"));
				}
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}

	}

	public void toRate(String login, String profile, int userId, String poiName, int poiId, String category, double rate,
			String day, String hourDay, String month) {

		/**
		 * Insere os dados na tabela de classificação do Naive Bayes
		 */
		String query = "insert into  " + "TABLE_NAIVE_EDIT_USER"
				+ " (user_id, owner, perfil_g, poi_name, poi_id, category, day, hour_day, month) " + "values (" + userId + ",'"
				+ login + "','" + profile + "','" + poiName + "',"+poiId+",'" + category + "','" + day + "','" + hourDay + "','"
				+ Utils.getMonth(month) + "')";
		System.out.println(query);

		con.execute(query);

		/**
		 * Insere os dados na tabela de classificação da Filtragem Colaborativa
		 */
//		query = "insert into " + Utils.TABLE_FC_EDIT_USER
//				+ "(user_id, owner,poi_id,poi_name, category,rate_avg) values (" + userId + ", '" + login + "',' "
//				+ poiId + "," + poiName + "','" + category + "'," + rate + ")";
//		con.execute(query);
//
//		System.out.println(query);

	}

	public List<Local> getAlreadyVisited(int userId) {

		List<Local> locais = new ArrayList<>();

		String query = "select poi_id, poi_name, category from " + "TABLE_NAIVE_EDIT_USER"+ " where user_id = "
				+ userId + " ";

//		String query = "select A.poi_name, A.category, B.rate_avg from " + Utils.TABLE_NAIVE_EDIT_USER + " A "
//				+ "inner join (select * from " + Utils.TABLE_FC_EDIT_USER + ") B on " + "A.user_id = " + UserId + " and "
//				+ "A.user_d = B.user_id and " + "A.poi_name = B.poi_name";

		Connection con = new Connection();

		ResultSet rs = con.executeSearch(query);

		try {
			while (rs.next()) {

				Local l = new Local();
				l.setId(rs.getInt("poi_id"));
				l.setName(rs.getString("poi_name"));
				l.setCategory(rs.getString("category"));
//				l.setAvg(rs.getFloat("rate_relevance"));

				locais.add(l);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return locais;
	}
}
