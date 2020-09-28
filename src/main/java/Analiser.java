import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.example.teste.modelo.Local;
import com.example.teste.recommend.RecommenderNB;
import com.example.teste.recommend.Utils;
import com.examplee.teste.modelo.dao.Connection;

public class Analiser {

	private Connection con = new Connection();

	public static void main(String[] args) throws SQLException {
//		Analiser o = new Analiser();
//		o.getDefinition();
		
		
	//	new RecommenderNB().getRecommend("turista", "janeiro", "tarde", "quarta", 1);

		 String [] month =
		 {"janeiro","fevereiro","março","abril","maio","junho","julho","agosto","setembro",
		 "outubro","novembro","dezembro"};
		
		 int c = 3;
		 String p = "turista";
		
		 Thread t1 = new Thread(new Analiser().new Work(month[0], p, c));
		 Thread t2 = new Thread(new Analiser().new Work(month[1],p,c));
		 Thread t3 = new Thread(new Analiser().new Work(month[2],p,c));
		 Thread t4 = new Thread(new Analiser().new Work(month[3],p,c));
		 Thread t5 = new Thread(new Analiser().new Work(month[4],p,c));
		 Thread t6 = new Thread(new Analiser().new Work(month[5],p,c));
		 Thread t7 = new Thread(new Analiser().new Work(month[6],p,c));
		 Thread t8 = new Thread(new Analiser().new Work(month[7],p,c));
		 Thread t9 = new Thread(new Analiser().new Work(month[8],p,c));
		 Thread t10 = new Thread(new Analiser().new Work(month[9],p,c));
		 Thread t11 = new Thread(new Analiser().new Work(month[10],p,c));
		 Thread t12 = new Thread(new Analiser().new Work(month[11],p,c));
		
		
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
		 t11.setName("T11");
		 t12.setName("T12");
		
//		 t1.start();
//		 t2.start();
//		 t3.start();
//		 t4.start();
//		 t5.start();
//		 t6.start();
//		 t7.start();
//		 t8.start();
//		 t9.start();
//		 t10.start();
//		 t11.start();
//		 t12.start();
		

	}

	private class Work implements Runnable {

		private String m;
		private String p;
		private int c;

		public Work(String m, String p, int c) {
			this.m = m;
			this.p = p;
			this.c = c;
		}

		@Override
		public void run() {
			analiser(m, p, c);
		}

	}

	private void analiser(String m, String p, int c) {

		RecommenderNB nb = new RecommenderNB();

		String[] days = { "segunda", "terça", "quarta", "quinta", "sexta", "sábado", "domingo" };
		String[] hour_day = { "manhã", "tarde", "noite", "madrugada" };

		for (int d = 0; d < days.length; d++) {

			for (int h = 0; h < hour_day.length; h++) {

				List<Local> locais = nb.getRecommend(p,"", m, hour_day[h], days[d],1, ""+c);
				String query = "";
				for (int i = 0; i < locais.size(); i++) {

					query = "insert into analiser_full values ('" + locais.get(i).getName() + "',"
							+ locais.get(i).getPercent() + "" + ",'" + p + "','" + days[d] + "','" + hour_day[h] + "','"
							+ m + "','" + Utils.getCategory(c) + "', " + locais.get(i).getAvg() + " )";

					System.out.println(Thread.currentThread().getName() + " - " + query);
					con.execute(query);

				}
			}
		}

	}

	private void getDefinition() throws SQLException {

		String query = "select * from locais";
		ResultSet rss;

		ResultSet rs = con.executeSearch(query);

		while (rs.next()) {

			query = "select count(*) from analiser_full a inner join (select * from  analiser_full) b"
					+ " on a.poi_name = '"+rs.getString("poi_name")+"'"
					+ " and b.poi_name = a.poi_name "
					+ "and b.perfil = 'turista' "
					+ "and a.perfil = 'residente'"
					+ " and a.day = b.day"
					+ " and a.month = b.month"
					+ " and a.hour_day = b.hour_day"
					+ " and a.prob < b.prob";

			rss = con.executeSearch(query);
		//+" - "+rss.getString(1)
			while(rss.next()){
				System.out.println(rss.getString(1));
				
			}
			

//			if (rss.getFetchSize() > 0) {
//
//				System.out.println(
//						rss.getString("day") + ", " + rss.getString("month") + ", " + rss.getString("day_hour"));
//
//			}

		}
	}

}
