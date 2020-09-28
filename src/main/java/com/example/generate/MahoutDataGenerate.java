package com.example.generate;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.examplee.teste.modelo.dao.Connection;

public class MahoutDataGenerate {
	
	private static String rate = "rate_relevance";

	public static void main(String[] args) {
	
		try {
			
			String TABLE = "select A.user_id, A.poi_id, "+rate+" from rc_rate_error_test A inner join (select * from rc_users) B on " + 
					"A.owner = B.owner and B.perfil_g = 'turista'";
			
			
//			String table = "rc_rate_error_test";
			generate(TABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void generate(String table) throws SQLException, InterruptedException {
		
		Connection con = new Connection();
		
//		String query = "select a.id , b.poi_id , a.rate  from table_user_item_fc as a  inner join \n" + 
//				"(select * from mahout_pois ) b on "
//				+ "a.poi_name = b.poi_name order by a.id";
		
//		String query = "select * from "+table+" order by user_id";
		String query = table;
		
		ResultSet rs = con.executeSearch(query);
		
		while(rs.next()) {
			
			
			System.out.println(rs.getInt("user_id")+","+rs.getInt("poi_id")+","+rs.getFloat(rate));
			//Thread.sleep(1000);
		}
		
		
		
		
		
		
	} 

}
