package com.example.generate;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.generate.DataGenerate.UpdateFCTable2;
import com.examplee.teste.modelo.dao.Connection;

public class ConfigRate {

	private Connection con = new Connection();
	
	public static void main(String[] args) throws SQLException {
		
		ConfigRate rate = new ConfigRate();
	
		
//	rate.setPopularityByYear();
		
		Thread t1 = new Thread(new ConfigRate().new UpdateFCTable2(1, 250));
		Thread t2 = new Thread(new ConfigRate().new UpdateFCTable2(251, 501));
		Thread t3 = new Thread(new ConfigRate().new UpdateFCTable2(502, 752));
		Thread t4 = new Thread(new ConfigRate().new UpdateFCTable2(753, 1003));
		Thread t5 = new Thread(new ConfigRate().new UpdateFCTable2(1004, 1253));
		Thread t6 = new Thread(new ConfigRate().new UpdateFCTable2(1254, 1504));
		Thread t7 = new Thread(new ConfigRate().new UpdateFCTable2(1505, 1755));
		Thread t8 = new Thread(new ConfigRate().new UpdateFCTable2(1756, 2006));
		Thread t9 = new Thread(new ConfigRate().new UpdateFCTable2(2007, 2257));
		Thread t10 = new Thread(new ConfigRate().new UpdateFCTable2(2258, 2546));

		t1.setName("T1");
		t2.setName("T2");
		t3.setName("T3");
		t4.setName("T4");
		t5.setName("T5");
		t6.setName("T6");
		t7.setName("T7");
		t8.setName("T8");
		t9.setName("T9");
		t10.setName("T10");

				
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();
		t9.start();
		t10.start();
		
	

	}
	//usa log na base 2
	private void setPopularity() {
		double log = 0;
		double avg = 0;

		String destino = "";

		/*
		 * Para que o cÃ³digo desta funÃ§Ã£o seja executado, Ã© necessÃ¡rio criar uma
		 * tabela temporÃ¡ria na base de dados com o scrip a seguir: create table
		 * location_popularity_aux as select destino, owner, count(*) as qtd from
		 * "classification_2014_reduced_01" where destino in (select distinct destino
		 * from "classification_2014_reduced_01") group by(owner, destino) order
		 * by(destino)
		 */

		try {

			String query = "select distinct poi_name from location_popularity_aux";

			ResultSet local = con.executeSearch(query);

			int popularity = 0;

			while (local.next()) {
				destino = local.getString("poi_name");
				query = "select (sum(log(2,qtd_photos + 1))) as log from location_popularity_aux where poi_name  = '"
						+ destino + "'";

				ResultSet rsQtd = con.executeSearch(query);
				if (rsQtd.next())
					log = Double.parseDouble(rsQtd.getString("log"));

				query = "select (sum(qtd_photos)/ count(qtd_photos)) as avg from  location_popularity_aux where poi_name  = '"
						+ destino + "'";

				rsQtd = con.executeSearch(query);
				if (rsQtd.next())
					avg = Double.parseDouble(rsQtd.getString("avg"));

				if (log > avg)
					popularity = (int) avg;
				else
					popularity = (int) log;

				query = "update  location_popularity_aux set popularity =" + popularity + " where poi_name = '"
						+ destino + "'";
				con.execute(query);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setPopularityByYear() throws SQLException {
		
		int[] years = {2011,2012,2013,2014};
		
		String query = "select distinct poi_name from rc_photo_exif_c11014_rn order by poi_name";
		ResultSet locals = con.executeSearch(query);
		
		String poiName = "";
		double avg = 0;
		double log = 0;
		double qtdPhotos=0;
		double logPhotos = 0;
		double contPhotos=0;
		int cont = 0;
		int contYear = 0;
		long popularity = 0;
		
		while(locals.next()) {
			
			poiName = locals.getString(1);
//		poiName = "Shopping da Gávea";
		
			for(int i=0; i< years.length;i++) {
				
				query = "select owner, EXTRACT(year from date_time_original) ano, count(*) from rc_photo_exif_c11014_rn where poi_name = '"+poiName+"'" + 
						" and date_part('year',date_time_original) = "+years[i]+" group by owner, ano";
				
				ResultSet poiYears = con.executeSearch(query);
				
				while(poiYears.next()) {
					
					
					qtdPhotos = poiYears.getInt(3);

					contPhotos += qtdPhotos;
					logPhotos += Math.log(qtdPhotos+1);
					
					cont++;
					
					if(poiYears.isLast()) {
						
						avg += (contPhotos/cont);
						log += logPhotos;
					
						System.out.println(years[i]);
						System.out.println("Média -> "+avg);
						System.out.println("Log : -> "+ log);
						
						contYear++;
					}
					
				}

				
				contPhotos = 0;
				logPhotos = 0;
				cont = 0;
				
				
			}
			
			avg =  avg/contYear;
			log = (log/contYear);
			
		
			if((int) log == 0)
				popularity = Math.round(avg);
			else if(log > avg)
				popularity = Math.round(avg);
			else
				popularity = Math.round(log);
			
				
			query = "update rc_rate_error_test set pop = "+popularity+" where poi_name = '"+poiName+"'";
//			System.out.println(" ------------------------ LOCAL = "+poiName+" -------------------------------");
//			System.out.println("MÉDIA = "+avg);
//			System.out.println("LOG 10 = "+log10);
//			System.out.println("POP = "+popularity);
//			System.out.println(" -----------------------------------------------------------------------------");
			con.execute(query);
			System.out.println(query);
	
			avg = 0;
			log = 0;
			contYear = 0;
		}
		
		
		
		
		
	}
	

	
	/* Métodos para calcular e inserir rates nas tabelas*/
	private void insertAVGByCategory(int begin, int end) throws SQLException {

		Connection con = new Connection();

		ResultSet users = con.executeSearch("select * from rc_users where id >= " + begin + " and id <= " + end + "");

		String query = "";
		String user = "";
		int id = 0;
		String category = "";
		int count = 0;
		int qtd_photos = 0;
		int avg = 0;

		while (users.next()) {

			user = users.getString("owner");
			id = users.getInt("id");

			// System.out.println("----------------------
			// "+users.getString("owner")+"----------------------------");

			query = "select distinct category  from rc_photo_exif_c11014_rn where owner = '" + user + "'";
			ResultSet categories = con.executeSearch(query);

			while (categories.next()) {

				category = categories.getString("category");

				query = "select poi_name, count(*) qtd_photos from rc_photo_exif_c11014_rn where owner = '" + user
						+ "' and category = '" + category + "'" + "group by (poi_name)";

				ResultSet rs_avg = con.executeSearch(query);

				while (rs_avg.next()) {

					count++;
					qtd_photos += rs_avg.getInt("qtd_photos");

				}

				avg = qtd_photos / count;

				// System.out.println(" ********* Média para a categoria "+category+" = "+avg+"
				// **************");

				query = "update rc_rate_error_test set avg_cat = " + avg + " where user_id = " + id + " and category = '"
						+ category + "'";
				con.execute(query);
				
				System.out.println(Thread.currentThread().getName() + " - " + query);

				qtd_photos = 0;
				count = 0;
				avg = 0;

			}

		}

	}

	private void configRelevanceByCategory(int begin, int end) throws SQLException {

		String query = "select owner from rc_users where id >= " + begin + " and id <= " + end + "";

		ResultSet users = con.executeSearch(query);

		String owner = "";
		double p = 0;
		double c;

		while (users.next()) {
			owner = users.getString("owner");

			query = "select count(*) qtd from rc_photo_exif_c11014_rn where owner = '" + owner + "'";

			ResultSet qtd_photos = con.executeSearch(query);

			if (qtd_photos.next())
				p = qtd_photos.getInt("qtd");

			query = "select category, count(*) qtd from rc_photo_exif_c11014_rn where owner = '" + owner
					+ "'group by category";

			ResultSet categories = con.executeSearch(query);

			while (categories.next()) {

				c = categories.getInt("qtd");

				query = "update rc_rate_error_test set relevance = " + (c / p) + " where owner = '" + owner
						+ "' and category = '" + categories.getString(1) + "'";
				con.execute(query);
				System.out.println(Thread.currentThread().getName() + " - " + query);

			}

		}

	}

	private void updateAvgRelevanceByPhotos(int begin, int end) throws SQLException {

		String query = "select * from rc_users where id >= " + begin + " and id <= " + end + "";

		ResultSet users = con.executeSearch(query);

		String owner = "";
		int id = 0;

		while (users.next()) {
			owner = users.getString("owner");
			id = users.getInt("id");

			query = "update rc_location_popularity_aux set avg_relevance= ((qtd_photos / new_popularity) * relevance * 2.5 )"
					+ " where owner = '"+owner+"' and user_id = "+id+"";
			
			con.execute(query);
			System.out.println(Thread.currentThread().getName()+" - "+query);
	

		}

	}

	private void updateTableUserItemRateByPhotos(int begin, int end) throws SQLException {

		Connection con = new Connection();

		ResultSet users = con.executeSearch("select * from rc_users where id >= " + begin + " and id <= " + end + "");

		String query = "";
		int id = 0;
		String poi_name = "";
		double popularity = 0;
		double qtd_photos = 0;
		double avg = 0;
		double rate = 0;
		double rAvg = 0;
		double rPop = 0;
		final int RATE = 5;

		while (users.next()) {

			id = users.getInt("id");

//			System.out.println("---------------------- "+users.getString("owner")+"----------------------------");

//			query = "select A.poi_name,avg, popularity, A.qtd_photos from table_user_item_fc A inner join " + 
//					"(select * from table2_user_item_fc) B on " + 
//					"A.id = "+id+" and " + 
//					"A.id = B.id and " + 
//					"A.poi_name = B.poi_name";

			query = "select * from rc_rate_error_test where user_id = " + id + "";

			ResultSet result = con.executeSearch(query);

			while (result.next()) {

				poi_name = result.getString("poi_name");
				avg = result.getInt("avg_cat");
				popularity = result.getInt("pop");
				qtd_photos = result.getInt("qtd_photos");

				if (qtd_photos > 2 * avg && qtd_photos > 2 * popularity)
					rate = RATE;

				else {

					if (qtd_photos > 2 * avg)
						rAvg = RATE;
					else
						rAvg = (qtd_photos * RATE) / (2 * avg);

					if (qtd_photos > 2 * popularity)
						rPop = RATE;
					else
						rPop = (qtd_photos * RATE) / (2 * popularity);

					if (avg > popularity)
						rate = rAvg + (Math.abs(rAvg - rPop) / RATE);
					else
						rate = rAvg - (Math.abs(rAvg - rPop) / RATE);
				}
				query = "update rc_rate_error_test set rate_sub = " + rate + " where poi_name = '" + poi_name
						+ "' and user_id = " + id + "";
				System.out.println(Thread.currentThread().getName() + " - " + query);
				con.execute(query);
				rate = 0;
			}

		}

	}


	class UpdateFCTable2 implements Runnable {

		int begin;
		int end;

		public UpdateFCTable2(int begin, int end) {

			this.begin = begin;
			this.end = end;
		}

		@Override
		public void run() {

			try {
//					 insertAVGByCategory(begin, end);
					updateTableUserItemRateByPhotos(begin, end);
//				configRelevanceByCategory(begin, end);
//				updateAvgRelevanceByPhotos(begin, end);
//				

			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

	}

}
