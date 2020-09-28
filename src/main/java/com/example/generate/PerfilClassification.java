package com.example.generate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.examplee.teste.modelo.dao.Connection;

public class PerfilClassification {

	private Connection con = new Connection();
	private int[] months = new int[3];
	private int nDays = 0;
	private final static String TABLE = "rc_photo_exif_c11014";

	private class UpdateFCTable2 implements Runnable {

		int begin;
		int end;

		public UpdateFCTable2(int begin, int end) {

			this.begin = begin;
			this.end = end;
		}

		@Override
		public void run() {

			try {
				configPerfil(begin, end, TABLE);
//				configPerfilGirardin(begin, end, TABLE);
//				configPerfilTimeWindow(begin, end, TABLE);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws SQLException {
		
		new PerfilClassification().PrintDiferent();
		
//		new PerfilClassification().configPerfilGirardin(0, 0, TABLE);

//		new PerfilClassification().updatePerfilNaiveClassification("perfil_my");

//		Thread t1 = new Thread(new PerfilClassification().new UpdateFCTable2(1, 250));
//		Thread t2 = new Thread(new PerfilClassification().new UpdateFCTable2(251, 501));
//		Thread t3 = new Thread(new PerfilClassification().new UpdateFCTable2(502, 752));
//		Thread t4 = new Thread(new PerfilClassification().new UpdateFCTable2(753, 1003));
//		Thread t5 = new Thread(new PerfilClassification().new UpdateFCTable2(1004, 1253));
//		Thread t6 = new Thread(new PerfilClassification().new UpdateFCTable2(1254, 1504));
//		Thread t7 = new Thread(new PerfilClassification().new UpdateFCTable2(1505, 1755));
//		Thread t8 = new Thread(new PerfilClassification().new UpdateFCTable2(1756, 2006));
//		Thread t9 = new Thread(new PerfilClassification().new UpdateFCTable2(2007, 2257));
//		Thread t10 = new Thread(new PerfilClassification().new UpdateFCTable2(2258, 2546));
//
//		t1.setName("T1");
//		t2.setName("T2");
//		t3.setName("T3");
//		t4.setName("T4");
//		t5.setName("T5");
//		t6.setName("T6");
//		t7.setName("T7");
//		t8.setName("T8");
//		t9.setName("T9");
//		t10.setName("T10");
//
//		t1.start();
//		t2.start();
//		t3.start();
//		t4.start();
//		t5.start();
//		t6.start();
//		t7.start();
//		t8.start();
//		t9.start();
//		t10.start();

	}

	private void configPerfil(int begin, int end, String table) throws SQLException {

		String perfil = "";
		String query = "select * from rc_users where id >= " + begin + " and id <= " + end + "";

//		String query = "select * from rc_users";

		ResultSet rs = con.executeSearch(query);

		int qtdMeses = 0;
		String owner = "";

		while (rs.next()) {
			owner = rs.getString("owner");

			System.out.println(owner);

			query = "select date_part('year',date_time_original) anos, count(distinct date_part('month',date_time_original)) mes from "
					+ "" + table + " where owner = '" + owner + "' group by(anos)";

			ResultSet years = con.executeSearch(query);

			exit: while (years.next()) {

				qtdMeses = years.getInt("mes");
				System.out.println(years.getString("anos") + " - " + qtdMeses);

				if (qtdMeses > 3) {
					perfil = "residente";
					break;

				} else if (qtdMeses == 3) {

					query = "select distinct date_part('month',date_time_original) meses from " + table
							+ " where owner = '" + owner + "' and  date_part('year',date_time_original) = "
							+ years.getInt("anos") + " order by(meses)";

					ResultSet rsP = con.executeSearch(query);
					int i = 0;
					while (rsP.next()) {
						months[i++] = rsP.getInt("meses");
					}

					if (Math.abs(months[0] - months[1]) == 1 && Math.abs(months[1] - months[2]) == 1) {
						System.out.println("3 meses seguidos");
						perfil = "residente";
						break exit;
					} else if (Math.abs(months[0] - months[1]) == 1) {

						System.out.println(months[0] + " e " + months[1]);

						perfil = metodGirardin(months[0], months[1], owner, table);

						if (perfil.equals("residente"))
							break exit;

						else {

							int days = getNumberDay3Month(table, owner, months[2], years.getInt("anos"));
							System.out.println("3ª mês - " + days);

							if (days + nDays > 30) {
								System.out.println("3 meses com permanência maior que 30 dias");
								perfil = "residente";
								break exit;

							}

						}

					} else if (Math.abs(months[1] - months[2]) == 1) {

						System.out.println(months[1] + " e " + months[2]);

						perfil = metodGirardin(months[1], months[2], owner, table);

						if (perfil.equals("residente"))
							break exit;

						else {
							int days = getNumberDay3Month(table, owner, months[0], years.getInt("anos"));

							System.out.println("3ª mês - " + days);

							if (days + nDays > 30) {
								System.out.println("3 meses com permanência maior que 30 dias");
								perfil = "residente";
								break exit;

							}

						}

					} else {
						System.out.println("Sem meses seguidos");
						perfil = "residente";
						break exit;
					}
				} else if (qtdMeses == 2) {

					query = "select distinct date_part('month',date_time_original) meses from " + table
							+ " where owner = '" + owner + "' and  date_part('year',date_time_original) = "
							+ years.getInt("anos") + " order by(meses)";

					ResultSet months = con.executeSearch(query);
					months.next();
					int m1 = months.getInt("meses");

					months.next();
					int m2 = months.getInt("meses");

					if (Math.abs(m1 - m2) > 1) { // meses não consecutivos
						System.out.println("Meses separados - " + m1 + " e " + m2);

						// APLICAR MÈTODO DA JANELA DE TEMPO

						perfil = "turista";

						String min1 = "select min(distinct cast(date_time_original as date)) dias from " + table
								+ " where date_part('month',date_time_original) = " + m1 + " and owner = '" + owner
								+ "' and  date_part('year',date_time_original) = " + years.getInt("anos") + "";

						String max1 = "select max(distinct cast(date_time_original as date)) dias from " + table
								+ " where date_part('month',date_time_original) = " + m1 + " and owner = '" + owner
								+ "' and  date_part('year',date_time_original) = " + years.getInt("anos") + "";

						ResultSet rsC = con.executeSearch(min1);
						rsC.next();
						String d1 = rsC.getString("dias");

						rsC = con.executeSearch(max1);
						rsC.next();

						String d2 = rsC.getString("dias");

						if (getNumberDays(d1, d2) <= 15) {
							System.out.println(
									"Primeiro mês tem até 15 dias: " + d1 + " e " + d2 + "=" + getNumberDays(d1, d2));

							String min2 = "select min(distinct cast(date_time_original as date)) dias from " + table
									+ " where date_part('month',date_time_original) = " + m2 + " and owner = '" + owner
									+ "' and  date_part('year',date_time_original) = " + years.getInt("anos") + "";

							String max2 = "select max(distinct cast(date_time_original as date)) dias from " + table
									+ " where date_part('month',date_time_original) = " + m2 + " and owner = '" + owner
									+ "' and  date_part('year',date_time_original) = " + years.getInt("anos") + "";

							rsC = con.executeSearch(min2);
							rsC.next();
							String d3 = rsC.getString("dias");

							rsC = con.executeSearch(max2);
							rsC.next();

							String d4 = rsC.getString("dias");

							if (getNumberDays(d3, d4) <= 15) {

								System.out.println("Segundo mês tem até 15 dias: " + d3 + " e " + d4 + " = "
										+ getNumberDays(d3, d4));

								System.out.println("Intervalo de dias = " + getNumberDays(d1, d4));
								if (getNumberDays(d1, d4) < 30) {

									perfil = "residente";
									break exit;
								}
								perfil = "turista";

							} else {
								perfil = "residente";
								break exit;
							}

						} else {
							perfil = "residente";
							break exit;
						}
					} else { // meses consecutivos

						perfil = metodGirardin(m1, m2, owner, table);
						if (perfil.equals("residente"))
							break exit;

					}

				} else // passou apenas 1 mês
					perfil = "turista";
			}

			System.out.println("Perfil = " + perfil);

			String update = "update rc_users set perfil_my = '" + perfil + "' where owner = '" + owner
					+ "'";
			System.out.println(Thread.currentThread().getName() + " - " + update);
			con.execute(update);
		}

	}

	private void configPerfilGirardin(int begin, int end, String table) throws SQLException {

		String perfil = "";
//		String query = "select * from rc_users where id >= " + begin + " and id <= " + end + "";

		String query = "select * from rc_users";

		ResultSet rs = con.executeSearch(query);

		int qtdMeses = 0;
		String owner = "";

		while (rs.next()) {
			owner = rs.getString("owner");

			System.out.println(owner);

			query = "select date_part('year',date_time_original) anos, count(distinct date_part('month',date_time_original)) mes from "
					+ "" + table + " where owner = '" + owner + "' group by(anos)";

			ResultSet years = con.executeSearch(query);

			exit: while (years.next()) {

				qtdMeses = years.getInt("mes");
				System.out.println(years.getString("anos") + " - " + qtdMeses);

				if (qtdMeses > 2) {
					perfil = "residente";
					break;

				} else if (qtdMeses == 2) {

					query = "select distinct date_part('month',date_time_original) meses from " + table
							+ " where owner = '" + owner + "' and  date_part('year',date_time_original) = "
							+ years.getInt("anos") + " order by(meses)";

					ResultSet rsP = con.executeSearch(query);
					int i = 0;
					while (rsP.next()) {
						months[i++] = rsP.getInt("meses");
					}

					if (Math.abs(months[0] - months[1]) > 1) {
						System.out.println("2 meses não seguidos");
						perfil = "residente";
						break exit;
					} else {

						System.out.println(months[0] + " e " + months[1]);

						perfil = metodGirardin(months[0], months[1], owner, table);

						if (perfil.equals("residente"))
							break exit;
					}
				} else if (qtdMeses == 1) {

					perfil = "turista";
				}
			}

			System.out.println("Perfil = " + perfil);
//
//				String update = "update rc_users set perfil_g = '" + perfil + "' where owner = '"
//						+ owner + "'";
//				System.out.println(Thread.currentThread().getName() + " - " + update);
//				con.execute(update);

		}

	}

	private void configPerfilTimeWindow(int begin, int end, String table) throws SQLException {

		String perfil = "";
		String query = "select * from rc_users where id >= " + begin + " and id <= " + end + "";

//		String query = "select * from rc_users";

		ResultSet rs = con.executeSearch(query);

		int qtdMeses = 0;
		String owner = "";

		while (rs.next()) {
			owner = rs.getString("owner");

			System.out.println(owner);

			query = "select date_part('year',date_time_original) anos, count(distinct date_part('month',date_time_original)) mes from "
					+ "" + table + " where owner = '" + owner + "' group by(anos)";

			ResultSet years = con.executeSearch(query);

			exit: while (years.next()) {

				qtdMeses = years.getInt("mes");
				System.out.println(years.getString("anos") + " - " + qtdMeses);

				if (qtdMeses > 2) {
					perfil = "residente";
					break;

				} else if (qtdMeses == 2) {

					query = "select distinct date_part('month',date_time_original) meses from " + table
							+ " where owner = '" + owner + "' and  date_part('year',date_time_original) = "
							+ years.getInt("anos") + " order by(meses)";

					ResultSet months = con.executeSearch(query);
					months.next();
					int m1 = months.getInt("meses");

					months.next();
					int m2 = months.getInt("meses");

					System.out.println("Meses - " + m1 + " e " + m2);

					// APLICAR MÈTODO DA JANELA DE TEMPO

					String min1 = "select min(distinct cast(date_time_original as date)) dias from " + table
							+ " where date_part('month',date_time_original) = " + m1 + " and owner = '" + owner
							+ "' and  date_part('year',date_time_original) = " + years.getInt("anos") + "";

					String max1 = "select max(distinct cast(date_time_original as date)) dias from " + table
							+ " where date_part('month',date_time_original) = " + m1 + " and owner = '" + owner
							+ "' and  date_part('year',date_time_original) = " + years.getInt("anos") + "";

					ResultSet rsC = con.executeSearch(min1);
					rsC.next();
					String d1 = rsC.getString("dias");

					rsC = con.executeSearch(max1);
					rsC.next();

					String d2 = rsC.getString("dias");

					if (getNumberDays(d1, d2) <= 15) {
						System.out.println(
								"Primeiro mês tem até 15 dias: " + d1 + " e " + d2 + "=" + getNumberDays(d1, d2));

						String min2 = "select min(distinct cast(date_time_original as date)) dias from " + table
								+ " where date_part('month',date_time_original) = " + m2 + " and owner = '" + owner
								+ "' and  date_part('year',date_time_original) = " + years.getInt("anos") + "";

						String max2 = "select max(distinct cast(date_time_original as date)) dias from " + table
								+ " where date_part('month',date_time_original) = " + m2 + " and owner = '" + owner
								+ "' and  date_part('year',date_time_original) = " + years.getInt("anos") + "";

						rsC = con.executeSearch(min2);
						rsC.next();
						String d3 = rsC.getString("dias");

						rsC = con.executeSearch(max2);
						rsC.next();

						String d4 = rsC.getString("dias");

						if (getNumberDays(d3, d4) <= 15) {

							System.out.println(
									"Segundo mês tem até 15 dias: " + d3 + " e " + d4 + " = " + getNumberDays(d3, d4));

							System.out.println("Intervalo de dias = " + getNumberDays(d1, d4));
							if (getNumberDays(d1, d4) < 30) {

								perfil = "residente";
								break exit;
							}
							perfil = "turista";

						} else {
							perfil = "residente";
							break exit;
						}

					} else {
						perfil = "residente";
						break exit;
					}

				} else if (qtdMeses == 1) {

					perfil = "turista";
				}
			}
				System.out.println("Perfil = " + perfil);

				String update = "update rc_users set perfil_tw = '" + perfil + "' where owner = '"
						+ owner + "'";
				System.out.println(Thread.currentThread().getName() + " - " + update);
				con.execute(update);
			
		}

	}

	private void PrintDiferent() throws SQLException {
		
		String query = "select perfil_tw, perfil_my from rc_users order by owner";
		
		ResultSet rs = con.executeSearch(query);
		
		String p1, p2;
		
		while(rs.next()) {
			
			p1 = rs.getString(1);
			p2 = rs.getString(2);
			
			if(p1.equals(p2))
				System.out.println("1");
			else
				System.out.println("0");
			
		}
		
		
	}
	
	
	
	private void updatePerfilNaiveClassification(String col) throws SQLException {
		
		
		String query = "select * from rc_users";
		
		ResultSet rs = con.executeSearch(query);
		String owner = "";
		String perfil = "";
		
			while(rs.next()) {
				
				owner = rs.getString("owner");
				perfil = rs.getString(col);

				query = "UPDATE rc_naive_classification_ed SET "+col+" = '"+perfil+"' where owner = '"+owner+"'";	
				con.execute(query);
				
				System.out.println(query);
				
				
			}
		
	}
	
	private String metodGirardin(int x, int y, String owner, String table) {

		try {

			String d1 = "select min(distinct cast(date_time_original as date)) dias from " + table
					+ " where date_part('month',date_time_original) = " + x + " and owner = '" + owner + "'";

			String d2 = "select max(distinct cast(date_time_original as date)) dias from " + table
					+ " where date_part('month',date_time_original) = " + y + " and owner = '" + owner + "'";

			ResultSet rsC = con.executeSearch(d1);
			rsC.next();
			d1 = rsC.getString("dias");

			rsC = con.executeSearch(d2);
			rsC.next();

			d2 = rsC.getString("dias");

			nDays = getNumberDays(d1, d2);
			System.out.println("passou " + nDays + " dias");

			if (nDays > 30) {
				return "residente";
			} else
				return "turista";

		} catch (SQLException e) {
			e.printStackTrace();
			return "erro";
		}

	}

	private int getNumberDays(String d1, String d2) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar data1 = Calendar.getInstance();
		Calendar data2 = Calendar.getInstance();

		try {
			data1.setTime(sdf.parse(d1));
			data2.setTime(sdf.parse(d2));

			return data2.get(Calendar.DAY_OF_YEAR) - data1.get(Calendar.DAY_OF_YEAR);

		} catch (ParseException e) {

			e.printStackTrace();
			return 0;
		}

	}

	private int getNumberDay3Month(String table, String owner, int month, int year) throws SQLException {

		String min = "select min(distinct cast(date_time_original as date)) dias from " + table
				+ " where date_part('month',date_time_original) = " + month + " and owner = '" + owner
				+ "' and  date_part('year',date_time_original) = " + year + "";

		String max = "select max(distinct cast(date_time_original as date)) dias from " + table
				+ " where date_part('month',date_time_original) = " + month + " and owner = '" + owner
				+ "' and  date_part('year',date_time_original) = " + year + "";

		ResultSet rsC = con.executeSearch(min);
		rsC.next();
		String d1 = rsC.getString("dias");

		rsC = con.executeSearch(max);
		rsC.next();

		String d2 = rsC.getString("dias");

		return getNumberDays(d1, d2);

	}

}
