package com.example.generate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.examplee.teste.modelo.dao.Connection;

import weka.knowledgeflow.Data;

public class DataGenerate {

	// private CategoryDefinitionTest getCategory = new CategoryDefinitionTest();
	private Connection con = new Connection();
	private String[] locations = {};
	private List<String> categories = new ArrayList<String>();
	// private String CLIENT_ID =
	// "DLNKR11EEWXLW0JFUYBDC3FMJMQFJIPRUPV0CUCVGNTON2ST";
	// private String CLIENT_SECRET =
	// "YRLA2SX0BJTLZBTT0THTEMSAPA5XLBPANXLUTU2W53MAIHIY";
	private final String CALLBACK_URL = "https://api.foursquare.com/v2/venues/VENUE_ID";

	public static void main(String[] args) throws SQLException, ParseException {

//		new DataGenerate(). setNewPopularityByYear();

//	 new DataGenerate().setPopularityLocation();
//		new DataGenerate().updateUser();
//		new DataGenerate().updatePoiId("table2_user_item_fc");

		// new DataGenerate().createrArffFile("lojas e servicos",
		// "nova_classification_ed");
		// 2546

//		String table = "nova_classification";
//		 Thread t1 = new Thread(new DataGenerate().new UpdatePerfil(1, 200,table ));
//		 Thread t2 = new Thread(new DataGenerate().new UpdatePerfil(201,
//		 400,table ));
//		 Thread t3 = new Thread(new DataGenerate().new UpdatePerfil(401,
//		 600,table ));
//		 Thread t4 = new Thread(new DataGenerate().new UpdatePerfil(601,
//		 800,table ));
//		 Thread t5 = new Thread(new DataGenerate().new UpdatePerfil(801,
//		 1000,table ));
//		 Thread t6 = new Thread(new DataGenerate().new UpdatePerfil(1001,
//		 1200,table ));
//		 Thread t7 = new Thread(new DataGenerate().new UpdatePerfil(1201,
//		 1400,table ));
//		 Thread t8 = new Thread(new DataGenerate().new UpdatePerfil(1401,
//		 1600,table ));
//		 Thread t9 = new Thread(new DataGenerate().new UpdatePerfil(1601,
//				 1800,table ));
//		 Thread t10 = new Thread(new DataGenerate().new UpdatePerfil(1801,
//				 2000,table ));
//		 
//		 Thread t11 = new Thread(new DataGenerate().new UpdatePerfil(2001,
//				 2200,table ));
//		 
//		 Thread t12 = new Thread(new DataGenerate().new UpdatePerfil(2201,
//				 2546,table ));

//		 Thread t1 = new Thread(new DataGenerate().new UpdateDay(1, 10000,"nova_classification"));
//		 Thread t2 = new Thread(new DataGenerate().new UpdateDay(10001,
//		 20000,"nova_classification"));
//		 Thread t3 = new Thread(new DataGenerate().new UpdateDay(20001,
//		 30000,"nova_classification"));
//		 Thread t4 = new Thread(new DataGenerate().new UpdateDay(30001,
//		 40000,"nova_classification"));
//		 Thread t5 = new Thread(new DataGenerate().new UpdateDay(40001,
//		 50000,"nova_classification"));
//		 Thread t6 = new Thread(new DataGenerate().new UpdateDay(50001,
//		 60000,"nova_classification"));
//		 Thread t7 = new Thread(new DataGenerate().new UpdateDay(60001,
//		 70000,"nova_classification"));
//		 Thread t8 = new Thread(new DataGenerate().new UpdateDay(70001,
//		 83302,"nova_classification"));

//		Thread t1 = new Thread(new DataGenerate().new UpdateFCTable2(1, 250));
//		Thread t2 = new Thread(new DataGenerate().new UpdateFCTable2(251, 501));
//		Thread t3 = new Thread(new DataGenerate().new UpdateFCTable2(502, 752));
//		Thread t4 = new Thread(new DataGenerate().new UpdateFCTable2(753, 1003));
//		Thread t5 = new Thread(new DataGenerate().new UpdateFCTable2(1004, 1253));
//		Thread t6 = new Thread(new DataGenerate().new UpdateFCTable2(1254, 1504));
//		Thread t7 = new Thread(new DataGenerate().new UpdateFCTable2(1505, 1755));
//		Thread t8 = new Thread(new DataGenerate().new UpdateFCTable2(1756, 2006));
//		Thread t9 = new Thread(new DataGenerate().new UpdateFCTable2(2007, 2257));
//		Thread t10 = new Thread(new DataGenerate().new UpdateFCTable2(2258, 2546));
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
//		 t11.setName("T11");
//		 t12.setName("T12");
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
//		 t11.start();
//		 t12.start();

	}

	/**
	 * ATUALIZAR CATEGORIA RJ_POIS_BASER
	 * 
	 * @throws SQLException
	 */

	private void configPerfil(int begin, int end, String table) throws SQLException {

		String perfil = "";
		String query = "select * from nova_owner where id >= " + begin + " and id <= " + end + "";

		ResultSet rs = con.executeSearch(query);

		int qtdMeses = 0;
		String owner = "";

		int[] months = new int[3];

		while (rs.next()) {
			owner = rs.getString("owner");

			// System.out.println(owner);

			query = "select date_part('year',date_time_original) anos, count(distinct date_part('month',date_time_original)) mes  from "
					+ "" + table + " where owner = '" + owner + "' group by(anos)";

			ResultSet rss = con.executeSearch(query);
			exit: while (rss.next()) {

				qtdMeses = rss.getInt("mes");
				// System.out.println(rss.getString("anos") + " - " + qtdMeses);

				if (qtdMeses > 3) {
					perfil = "residente";
					break;
				} else if (qtdMeses == 3) {

					query = "select distinct date_part('month',date_time_original) meses from " + table
							+ " where owner = '" + owner + "' and  date_part('year',date_time_original) = "
							+ rss.getInt("anos") + " order by(meses)";

					ResultSet rsP = con.executeSearch(query);
					int i = 0;
					while (rsP.next()) {
						months[i++] = rsP.getInt("meses");
					}

					if (Math.abs(months[0] - months[1]) == 1 && Math.abs(months[1] - months[2]) == 1) {
						System.out.println("3 meses segudos");
						perfil = "residente";
						break exit;
					} else if (Math.abs(months[0] - months[1]) == 1) {

						String min = "select min(distinct date_part('day',date_time_original)) dias from " + table
								+ " where date_part('month',date_time_original) = " + months[0] + " and owner = '"
								+ owner + "'";

						String max = "select max(distinct date_part('day',date_time_original)) dias from " + table
								+ " where date_part('month',date_time_original) = " + months[1] + " and owner = '"
								+ owner + "'";

						ResultSet rsC = con.executeSearch(min);
						rsC.next();
						int a = rsC.getInt("dias");

						rsC = con.executeSearch(max);
						rsC.next();
						int b = rsC.getInt("dias");

						if (30 - a + b > 30) {
							// System.out.println(
							// a + " até " + b + " = " + (30 - a + b) + " " +
							// months[0] + " - " + months[1]);
							perfil = "residente";
							break exit;
						} else
							perfil = "turista";

					} else if (Math.abs(months[1] - months[2]) == 1) {
						String min = "select min(distinct date_part('day',date_time_original)) dias from " + table
								+ " where date_part('month',date_time_original) = " + months[1] + " and owner = '"
								+ owner + "'";

						String max = "select max(distinct date_part('day',date_time_original)) dias from " + table
								+ " where date_part('month',date_time_original) = " + months[2] + " and owner = '"
								+ owner + "'";

						ResultSet rsC = con.executeSearch(min);
						rsC.next();
						int a = rsC.getInt("dias");

						rsC = con.executeSearch(max);
						rsC.next();
						int b = rsC.getInt("dias");

						if (30 - a + b > 30) {
							// System.out.println(
							// a + " até " + b + " = " + (30 - a + b) + " " +
							// months[1] + " - " + months[2]);
							perfil = "residente";
							break exit;
						} else
							perfil = "turista";
					}

				} else if (qtdMeses == 2) {

					query = "select distinct date_part('month',date_time_original) meses from " + table
							+ " where owner = '" + owner + "' and  date_part('year',date_time_original) = "
							+ rss.getInt("anos") + " order by(meses)";

					ResultSet rsP = con.executeSearch(query);
					rsP.next();
					int m1 = rsP.getInt("meses");

					rsP.next();
					int m2 = rsP.getInt("meses");

					if (Math.abs(m1 - m2) > 1) {
						perfil = "turista";
						// System.out.println("2 meses distantes");
					} else {

						String min = "select min(distinct date_part('day',date_time_original)) dias from " + table
								+ " where date_part('month',date_time_original) = " + m1 + " and owner = '" + owner
								+ "'";

						String max = "select max(distinct date_part('day',date_time_original)) dias from " + table
								+ " where date_part('month',date_time_original) = " + m2 + " and owner = '" + owner
								+ "'";

						ResultSet rsC = con.executeSearch(min);
						rsC.next();
						int a = rsC.getInt("dias");

						rsC = con.executeSearch(max);
						rsC.next();
						int b = rsC.getInt("dias");

						if (30 - m1 + m2 > 30) {
							perfil = "residente";
							// System.out.println("tempo : " + (30 - m1 + m2) +
							// " " + m1 + " - " + m2);
							break exit;
						} else
							perfil = "turista";

						// System.out.println("tempo : " + (30 - m1 + m2) + " "
						// + m1 + " - " + m2);
					}

				} else
					perfil = "turista";
			}

			// System.out.println("Perfil = " + perfil);

			String update = "update " + table + " set perfil = '" + perfil + "' where owner = '" + owner + "'";
			System.out.println(Thread.currentThread().getName() + " - " + update);
			con.execute(update);
		}

	}

	public void updateCategories() {

		String[] categories = { "nenhum", "nenhum", "alimentacao", "nenhum", "ar livre e recreacao", "nenhum", "nenhum",
				"nenhum", "nenhum", "nenhum", "lazer", "lazer", "arte e cultura", "alimentacao", "nenhum", "bares",
				"nenhum", "ar livre e recreacao", "nenhum", "ar livre e recreacao", "bares", "nenhum", "nenhum",
				"lojas e servicos", "nenhum", "nenhum", "nenhum", "arte e cultura", "arte e cultura", "nenhum",
				"alimentacao", "lazer", "lojas e servicos", "nenhum", "alimentacao", "ar livre e recreacao",
				"alimentacao", "ar livre e recreacao", "nenhum", "nenhum", "lazer", "ar livre e recreacao", "bares",
				"arte e cultura", "bares", "arte e cultura", "centros religiosos", "centros religiosos", "nenhum",
				"nenhum", "nenhum", "arte e cultura", "arte e cultura", "arte e cultura", "arte e cultura",
				"arte e cultura", "nenhum", "lazer", "nenhum", "lazer", "lazer", "nenhum", "nenhum", "lazer", "lazer",
				"ar livre e recreacao", "lojas e servicos", "lazer", "lazer", "lazer", "lazer", "lojas e servicos",
				"lazer", "nenhum", "nenhum", "nenhum", "nenhum", "centros religiosos", "nenhum", "nenhum",
				"arte e cultura", "lojas e servicos", "nenhum", "nenhum", "nenhum", "nenhum", "nenhum", "nenhum",
				"nenhum", "nenhum", "nenhum", "ar livre e recreacao", "arte e cultura", "nenhum", "nenhum", "nenhum",
				"nenhum", "nenhum", "nenhum", "arte e cultura", "nenhum", "nenhum", "nenhum", "nenhum",
				"centros religiosos", "lazer", "lazer", "arte e cultura", "arte e cultura", "arte e cultura",
				"arte e cultura", "arte e cultura", "nenhum", "nenhum", "ar livre e recreacao", "ar livre e recreacao",
				"nenhum", "lojas e servicos", "lojas e servicos", "nenhum", "bares", "alimentacao", "lazer", "nenhum",
				"nenhum", "lazer", "nenhum", "nenhum", "nenhum", "centros religiosos", "centros religiosos",
				"centros religiosos", "centros religiosos", "centros religiosos", "centros religiosos",
				"centros religiosos", "centros religiosos", "centros religiosos", "centros religiosos",
				"centros religiosos", "centros religiosos", "centros religiosos", "centros religiosos",
				"centros religiosos", "ar livre e recreacao", "nenhum", "nenhum", "ar livre e recreacao", "nenhum",
				"nenhum", "ar livre e recreacao", "lazer", "ar livre e recreacao", "lazer", "nenhum", "lazer",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "alimentacao", "nenhum", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "nenhum", "alimentacao", "lazer", "lazer", "alimentacao",
				"ar livre e recreacao", "lojas e servicos", "nenhum", "lazer", "arte e cultura", "arte e cultura",
				"nenhum", "nenhum", "nenhum", "nenhum", "nenhum", "nenhum", "ar livre e recreacao", "nenhum",
				"ar livre e recreacao", "ar livre e recreacao", "arte e cultura", "arte e cultura", "arte e cultura",
				"arte e cultura", "arte e cultura", "arte e cultura", "arte e cultura", "lojas e servicos",
				"lojas e servicos", "nenhum", "nenhum", "nenhum", "ar livre e recreacao", "alimentacao",
				"arte e cultura", "lazer", "arte e cultura", "nenhum", "arte e cultura", "arte e cultura",
				"ar livre e recreacao", "lojas e servicos", "centros religiosos", "nenhum", "arte e cultura",
				"ar livre e recreacao", "arte e cultura", "lazer", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "lazer", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"nenhum", "arte e cultura", "nenhum", "nenhum", "ar livre e recreacao", "nenhum", "nenhum", "lazer",
				"nenhum", "nenhum", "nenhum", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao",
				"ar livre e recreacao", "ar livre e recreacao", "ar livre e recreacao", "nenhum", "arte e cultura",
				"nenhum", "nenhum", "nenhum", "ar livre e recreacao", "nenhum", "lazer", "ar livre e recreacao",
				"nenhum", "arte e cultura", "nenhum", "nenhum", "nenhum", "nenhum", "nenhum", "nenhum", "nenhum",
				"lazer", "lojas e servicos", "nenhum", "bares", "nenhum", "lojas e servicos", "nenhum", "nenhum",
				"nenhum", "nenhum", "lazer", "nenhum", "centros religiosos", "nenhum", "nenhum", "nenhum",
				"lojas e servicos", "lojas e servicos", "lojas e servicos", "lojas e servicos", "lojas e servicos",
				"lojas e servicos", "lojas e servicos", "nenhum", "alimentacao", "lazer", "nenhum", "lojas e servicos",
				"nenhum", "arte e cultura", "nenhum", "nenhum", "nenhum", "arte e cultura", "arte e cultura", "nenhum",
				"nenhum", "nenhum", "nenhum", "nenhum", "nenhum", "nenhum", "nenhum", "nenhum", "nenhum", "nenhum",
				"nenhum", "nenhum", "nenhum", "nenhum", "lazer", "nenhum" };

		int[] ids = { 19281, 25142, 1113, 20013, 20805, 18606, 19815, 19814, 18066, 18067, 19158, 21117, 6028, 22818,
				29323, 24310, 11409, 18058, 19914, 18080, 3423, 10762, 26492, 19041, 25053, 23721, 18229, 18993, 18974,
				18952, 7022, 18202, 18943, 3375, 8988, 22367, 12259, 18922, 19289, 19278, 26203, 19797, 3910, 18960,
				11519, 19154, 18936, 18643, 24929, 18183, 26126, 22971, 18962, 18961, 20769, 22359, 20681, 19818, 18245,
				25468, 25648, 21361, 24629, 22768, 20804, 19865, 18051, 24814, 18295, 18629, 20542, 18981, 18210, 23552,
				26372, 21164, 20788, 18634, 7202, 20568, 19315, 19038, 18918, 22360, 23306, 18992, 22358, 22361, 18913,
				25680, 19221, 18552, 21894, 20770, 18914, 19055, 19246, 18126, 28731, 18674, 19910, 20466, 21299, 24097,
				18074, 18107, 24813, 11464, 4034, 1081, 470, 84, 24863, 24540, 18086, 26138, 211, 28033, 18891, 25444,
				6952, 12885, 18193, 23807, 982, 22004, 23304, 23303, 24631, 21337, 18624, 20768, 18972, 18938, 21335,
				19028, 1112, 21336, 24213, 19271, 25690, 18646, 18959, 5409, 18287, 18639, 18189, 28642, 21779, 20550,
				24971, 19799, 20919, 25089, 20388, 18827, 18219, 18081, 18237, 18106, 18085, 18949, 4876, 19062, 19059,
				21893, 21553, 18934, 26533, 731, 28306, 18605, 18117, 17569, 11766, 5114, 21778, 18211, 3427, 27976,
				21211, 18991, 4859, 13083, 24258, 20404, 20812, 20374, 20384, 19960, 20403, 24943, 19970, 19813, 19812,
				19245, 18939, 18911, 21612, 21313, 18645, 18971, 19040, 20472, 931, 20395, 26122, 22366, 12135, 18203,
				20543, 18850, 18990, 25673, 19086, 3287, 21460, 20743, 20393, 26440, 22552, 21315, 18627, 26146, 19816,
				19811, 18844, 28342, 18475, 18059, 19882, 18659, 18068, 19810, 18580, 18628, 19795, 19798, 26189, 24634,
				19961, 18052, 18607, 21554, 24881, 23316, 103, 18986, 23962, 24501, 18845, 21856, 21780, 28303, 18985,
				25498, 19415, 19419, 28485, 28381, 28372, 26642, 22584, 20651, 18987, 19282, 24528, 18839, 19212, 22006,
				18669, 19165, 22441, 18292, 27408, 27800, 20485, 18937, 18843, 18851, 21851, 18233, 25118, 21442, 19789,
				28100, 18660, 28641, 18846, 24613, 18849, 18852, 28366, 18858, 18472, 18836, 20464, 18550, 18242, 18829,
				19202, 21781, 18833, 18551, 18823, 18824, 18835, 18474, 18834, 24244, 18830, 24500, 308, 18602, 24417,
				24340, 19800, 21543, 11079, 18566, 18994, 21319, 12261, 4161, 12200, 16310, 12193, 12177, 12319, 20546,
				20574, 24840, 26125, 24638, 18872, 19920, 25652, 1084, 15972, 19063, 3429, 17692, 880, 20547, 24329,
				21318, 19211, 26271, 20541, 18904, 22942, 19262, 25678, 3430, 20561, 1080, 8217, 12771, 20538, 21796,
				21797, 29183, 21803, 20521, 21181, 18989, 20081, 18900, 18907, 20767, 18583, 18131, 23711, 25123, 19922,
				13409, 19923, 29260, 23862, 21070, 3729 };

		for (int i = 0; i < categories.length; i++) {

			String query = "update nova_photo_exif_c11014_r5 set category= '" + categories[i] + "' where poi_id = "
					+ ids[i] + "";

			System.out.println(query);

			con.execute(query);

		}

	}

	/** ATUALIZAR NOMES DOS LOCAIS EM RJ_POIS_BASE */
	private void updateNames() {

		String[] names = { "Área de Proteção Ambiental das Pontas de Copacabana e Arpoador e seus Entornos",
				"Área de Proteção Ambiental de São José", "Área de Proteção Ambiental do Morro da Viúva",
				"Área de Proteção Ambiental dos Morros da Babilônia e São João",
				"Área de Proteção Ambiental dos Morros do Leme e Urubu", "Às Vítimas da Intentona Comunista", "ACM",
				"Aeroporto Santos Dumont", "Amarelinho", "André Cavalcanti", "Arcos da Lapa", "Armazém 2", "Armazém 3",
				"Assador", "Associação São Martinho", "Astor Bar", "atelier hostel", "Aterro do Flamengo",
				"Baía de Guanabara", "Babilônia", "Bar do Mineiro", "Barca Táxi", "Barra Bonita", "BarraShopping",
				"Base Aérea de Santa Cruz", "Base Aérea do Galeão", "Base Aérea dos Afonsos", "Biblioteca Nacional",
				"Biblioteca Parque Estadual", "Bolsa de Valores do Rio de Janeiro", "Bonde Sucos",
				"Botafogo de Futebol e Regatas", "Botafogo Praia Shopping", "BR Mania", "Brasserie Rosário",
				"Bromeliário", "Câmara Municipal do Rio de Janeiro", "Cais de Oriente", "Cais Pharoux",
				"Caixa Econômica Federal", "Campo de Beisebol da Lagoa", "Campo de Santana",
				"Círculo Militar da Praia Vermelha", "Carlito's", "Casa França-Brasil", "Casa Momus",
				"Castelo da Ilha Fiscal", "Catedral de São Sebastião", "Catedral Presbiteriana do Rio de Janeiro",
				"Cedae - Estação Leblon", "Cemitério de São João Batista", "Centro Candido Mendes",
				"Centro Cultural Ação da Cidadania", "Centro Cultural Banco do Brasil", "Centro Cultural Correios",
				"Centro Cultural da Justiça Eleitoral", "Centro Cultural Justiça Federal",
				"Centro de Convenções Sul América", "Centro Luiz Gonzaga de Tradições Nordestinas",
				"Centro Metropolitano", "Centro Olímpico de Tênis", "Cidade do Samba", "Cidade Jardim",
				"Ciep José Pedro Varela", "Cine Odeon", "Circo Voador", "Città  America",
				"Club de Regatas Vasco da Gama", "Clube de Regatas do Flamengo", "Clube dos Caiçaras",
				"Clube Monte Líbano", "Cobal do Humaitá", "Complexo Esportivo do Maracanã", "Condomínio Alfa Barra II",
				"Condomínio Morada do Sol", "Condomínio Piazza Verde", "Condomínio Rio 2", "Convento de Santa Teresa",
				"Copacabana Palace", "Correios e Telégraphos", "Cosme Velho", "Downtown", "Edifício Avenida Central",
				"Edifício Bokel", "Edifício Itu", "Edifício Linneo de Paula Machado", "Edifício Municipal",
				"Edifício Paulino Ribeiro Campos", "Edifício Praia Vermelha", "Edifício RE", "Edifício Santos Dumont",
				"Enseada de Botafogo", "Escadaria Selarón", "Escola de Cinema Darcy Ribeiro",
				"Escola de Comando e Estado-Maior do Exército", "Escola de Guerra Naval", "Escola do Olhar",
				"Escola Eleva", "Espaço Cria", "Esplanada Luiz Paulo Horta", "Estácio",
				"Estádio Olímpico Nilton Santos", "Estádio Vasco da Gama", "Estátua de Antônio Carlos Jobim",
				"Estátua de Carlos Drummond de Andrade", "Estátua de Carlos Drummond de Andrade",
				"Estátua de Carlos Drummond de Andrade", "Estátua de Dorival Caymmi", "Estátua do Bellini",
				"Estátua Princesa Isabel", "Estação Barão de Mauá", "Estação I", "Estação Praça XV",
				"Estacionamento da Catedral (mudar para 'Catedral')", "Excelsior Copacabana Hotel", "Fiocruz",
				"Floresta da Tijuca", "Forte Duque de Caxias", "Fundição Progresso", "Gávea Golf and Country Club",
				"Galeria River", "Galeria São Luiz", "Galeria Sul América", "Garota de Ipanema",
				"Gastrononomia Da Lapa", "Hotel Caesar Park", "Hotel Porto Bay Rio Internacional",
				"Iate Clube do Rio de Janeiro", "Ibis Budget Rio de Janeiro Centro", "Ibis Rio de Janeiro Centro",
				"Icono Full Life Flamengo", "Igreja da Ordem Terceira de São Francisco da Penitência",
				"Igreja da Ordem Terceira do Carmo", "Igreja da Santa Cruz dos Militares",
				"Igreja de Nossa Senhora da Candelária", "Igreja de Nossa Senhora da Glória do Outeiro",
				"Igreja de Nossa Senhora da Lapa dos Mercadores", "Igreja de Nossa Senhora da Penha",
				"Igreja de Nossa Senhora do Carmo da Lapa do Desterro", "Igreja de Nossa Senhora do Monte do Carmo",
				"Igreja de São Francisco de Paula", "Igreja de São Jorge", "Igreja de Santa Luzia",
				"Igreja e Convento de Santo Antônio", "Igreja e Mosteiro de São Bento", "Igreja Metodista",
				"Ilha do Governador", "Instituto de Filosofia e Ciências Sociais - UFRJ",
				"Instituto Militar de Engenharia", "Jardim Botanico", "Jardim de Alah", "Jardim Pernambuco",
				"Jardim Sensorial", "Jardim Zoológico", "Jardins Suspensos do Valongo", "Jeunesse Arena",
				"Joaquim de Queiróz", "Jockey Club Brasileiro", "Lago Frei Leandro", "Lagoa da Tijuca",
				"Lagoa de Jacarepaguá", "Lagoa de Marapendi", "Lagoa Rodrigo de Freitas", "Lagoon", "Lapa",
				"Largo da Carioca", "Largo da Glória", "Largo da Lapa", "Largo de São Francisco da Prainha",
				"Largo de São Francisco de Paula", "Largo do Boticário", "Largo do Guimarães", "Largo do Machado",
				"Largo do Millôr - (praia do Apoador)", "Largo do Paço", "Largo dos Guimarães", "Livraria Bolívar",
				"Makoto", "Maracanã", "Maracanã", "Marcô", "Marina da Glória", "Mercado Popular da Uruguaiana",
				"Ministério da Fazenda", "Mirante do Leblon", "Monumento a Carlos Gomes",
				"Monumento Nacional aos Mortos da Segunda Guerra Mundial", "Morro da Baiana", "Morro da Providência",
				"Morro do Adeus", "Morro do Alemão", "Morro do Cantagalo", "Morro do Piancó", "Morro Dois Irmãos",
				"Morro Santa Marta", "Morros Pão de Açúcar, Urca e Cara de Cão",
				"Morros Pão de Açúcar, Urca e Cara de Cão", "Museu de Arte do Rio", "Museu de Arte Moderna",
				"Museu de Ciências da Terra", "Museu do Trem", "Museu Internacional de Arte NaÃ¯f",
				"Museu Nacional de Belas Artes", "Museu Naval", "New York City Center", "Norte Shopping", "Nosso Hotel",
				"Nosso Hotel", "Nova Brasília", "Oi", "Orquidário", "Os Ximenes", "Pátio Lily e Roberto Marinho",
				"Pátio Musal", "Pão de Açúcar", "Paço Imperial", "Paissandu Atlético Clube", "Palácio do Catete",
				"Palácio Gustavo Capanema", "Palácio Maçônico do Lavradio", "Palácio São Joaquim",
				"Paróquia Nossa Senhora da Paz", "Park Shopping Campo Grande", "Parque Alvorada",
				"Parque da Casa de Rui Barbosa", "Parque da Catacumba", "Parque das Ruínas", "Parque dos Atletas",
				"Parque dos Patins", "Parque Eduardo Guinle", "Parque Garota de Ipanema", "Parque Henrique Lage",
				"Parque Madureira", "Parque Municipal da Reserva", "Parque Nacional da Tijuca",
				"Parque Natural Municipal Bosque da Barra", "Parque Natural Municipal Chico Mendes",
				"Parque Natural Municipal de Marapendi", "Parque Natural Municipal do Penhasco Dois Irmãos",
				"Parque Olímpico do Rio de Janeiro", "Parque Urbano Prefeito Luiz Paulo Conde", "Parque Yitzhak Rabin",
				"Passeio Público", "Pavão-Pavãozinho", "Pavilhão Reitor João Lyra Filho", "Pedra do Arpoador",
				"Pedra do Sal", "Península", "Pestana Rio Atlântica", "Petrobras", "Petrobras - Edifício Sede",
				"Pontificia Universidade Católica do Rio de Janeiro", "Porto do Rio de Janeiro", "Praça Afonso Viseu",
				"Praça Almirante Julio de Noronha", "Praça Almirante Saldanha da Gama", "Praça Cardeal Arco Verde",
				"Praça Cardeal Câmara", "Praça Carlos Alberto Torres", "Praça Demétrio Ribeiro", "Praça do Lido",
				"Praça do Palácio Tiradentes", "Praça Duque de Caxias", "Praça Emílio Garrastazul Médici",
				"Praça Emilinha Borba", "Praça Emilinha Borba", "Praça Estado da Guanabara", "Praça Floriano Peixoto",
				"Praça General Osório", "Praça General Pedra", "Praça General Tibúrcio", "Praça Juarez Tavora",
				"Praça Juliano Moreira", "Praça Mahatma Gandhi", "Praça Marechal Ãncora", "Praça Marinha do Brasil",
				"Praça Mauá", "Praça Medalha Milagrosa", "Praça Mercado Municipal", "Praça Nelson Mandela",
				"Praça Nossa Senhora da Glória", "Praça Nossa Senhora da Paz", "Praça Paris", "Praça Pimentel Duarte",
				"Praça Pio X", "Praça Pistóia", "Praça Presidente Aguirre Cerda", "Praça Professor José Bernardino",
				"Praça Quinze de Novembro", "Praça Quinze de Novembro", "Praça Sáenz PeÃ±a", "Praça São Judas Tadeu",
				"Praça São Salvador", "Praça Santos Dumont", "Praça Senador Salgado Filho", "Praça Serzedelo Correa",
				"Praça Tiradentes", "Praia da Barra da Tijuca", "Praia da Joatinga", "Praia da Urca",
				"Praia de Botafogo", "Praia de Copacabana", "Praia de Fora", "Praia de Ipanema", "Praia do Arpoador",
				"Praia do Diabo", "Praia do Flamengo", "Praia do Leblon", "Praia do Leme", "Praia do Pepino",
				"Praia do Recreio dos Bandeirantes", "Praia do Vidigal", "Praia dos Amores", "Praia Vermelha",
				"Primeiro Distrito Naval", "Princesa Isabel - estátua", "Projac",
				"Quartel do Comando Geral do Corpo de Bombeiros do Estado do Rio de Janeiro",
				"Quartel General da Polícia Militar do Estado do Rio de Janeiro", "Quinta da Boa Vista",
				"Quitanda 55 Park", "Raízes da Lapa", "Rampa A - Oeste", "Rústico", "RB1",
				"Real Gabinete Português de Leitura", "Retorno", "Rio's Hotel", "Rio a pé", "Rio a pé", "Rio a pé",
				"Rio a pé", "Rio a pé", "Rio de Janeiro Country Club", "Rio Design Center Leblon", "Rio Othon Palace",
				"Rio Scenarium", "Riocentro", "RioSul", "Rocinha", "Rodoviária Novo Rio", "Rota 66",
				"Sambódromo da Marquês de Sapucaí", "Santa Arte", "Santa Teresa - Catedral de São Sebastião",
				"Santander", "Selva de Pedra", "Sheraton Rio Hotel & Resort", "Shopping Cassino Atlântico",
				"Shopping da Gávea", "Shopping Leblon", "Shopping Leblon", "Shopping Nova América",
				"Shopping Rio Design Barra", "Shopping Tijuca", "Sobradão das Chaves", "Sobrenatural",
				"Sociedade Hípica Brasileira", "Sofitel", "South - Centro", "Sugar Lofy", "Teatro João Caetano",
				"Terminal 1", "Terminal 2", "Terminal Garagem Menezes Côrtes", "Terreirão do Samba",
				"Theatro Municipal", "Tribunal Regional do Trabalho",
				"Tribunal Regional do Trabalho - Fórum Ministro Arnaldo SÃ¼ssekind", "Tuiuti",
				"UFRJ - Campus Praia Vermelha", "UNIRIO - Campus V", "Universidade Candido Mendes",
				"Universidade do Estado do Rio de Janeiro - Campus Maracanã", "Universidade Federal do Rio de Janeiro",
				"Ventura Corporate Towers", "Via Parque Shopping", "Vidigal",
				"Vigilância Sanitária Municipal - Subsecretaria de Vigilância, Fiscalização Sanitária e Controle de Zoonoses",
				"Vila Canoas", "Villa Aymoré", "Village Mall", "Vivo Rio", "Windsor Atlântica" };

		int[] ids = { 18606, 19815, 19814, 18066, 18067, 6028, 19281, 25142, 1113, 20013, 20805, 19158, 21117, 22818,
				29323, 24310, 11409, 18058, 18080, 19914, 3423, 10762, 26492, 19041, 25053, 23721, 18229, 18993, 18974,
				18952, 7022, 18202, 18943, 3375, 8988, 22367, 19278, 12259, 18922, 19289, 26203, 19797, 19865, 3910,
				18960, 11519, 19154, 18936, 18643, 24929, 18183, 26126, 22971, 18962, 18961, 20769, 22359, 20681, 19818,
				18245, 25468, 25648, 21361, 24629, 22768, 20804, 18051, 24814, 18295, 18629, 20542, 18981, 18210, 23552,
				26372, 21164, 20788, 18634, 7202, 20568, 19315, 19038, 18918, 22360, 23306, 18992, 22358, 22361, 18913,
				25680, 19221, 18552, 21894, 20770, 18914, 19055, 19246, 18126, 28731, 18674, 24097, 18107, 24813, 11464,
				4034, 4034, 4034, 1081, 470, 84, 19910, 20466, 21299, 18074, 24863, 24540, 18086, 26138, 211, 18193,
				28033, 18891, 25444, 6952, 12885, 23807, 982, 22004, 23304, 23303, 24631, 21337, 18624, 20768, 18972,
				18938, 21335, 19028, 1112, 21336, 19271, 25690, 24213, 18646, 18959, 5409, 18287, 18639, 18189, 28642,
				21779, 20550, 24971, 19799, 20919, 25089, 20388, 18827, 18219, 18081, 18237, 18106, 18085, 18949, 4876,
				19062, 19059, 21893, 21553, 18934, 26533, 731, 28306, 18605, 18117, 17569, 11766, 5114, 18211, 21778,
				3427, 27976, 21211, 18991, 4859, 13083, 24258, 20404, 20812, 20374, 20384, 19960, 20403, 24943, 19970,
				19812, 19813, 19245, 18939, 18911, 21612, 21313, 18645, 18971, 19040, 20472, 931, 931, 20395, 26122,
				22366, 12135, 26189, 24634, 3287, 18203, 20543, 18850, 18990, 25673, 19086, 20743, 21460, 20393, 26440,
				22552, 21315, 18627, 26146, 19816, 19811, 18844, 28342, 18475, 18059, 19882, 18659, 18068, 19810, 18580,
				18628, 19795, 19798, 19961, 18052, 18607, 21554, 24881, 23316, 103, 18986, 23962, 24501, 18845, 21856,
				21780, 28303, 18985, 25498, 19415, 19419, 28485, 28381, 22584, 26642, 28372, 20651, 18987, 19282, 24528,
				18839, 19212, 22006, 18669, 19165, 22441, 18292, 27408, 27800, 20485, 18937, 18843, 18851, 21851, 18233,
				25118, 21442, 19789, 18660, 28100, 28641, 24613, 18849, 18846, 18852, 28366, 18858, 18472, 18836, 20464,
				18550, 18242, 18829, 19202, 21781, 18833, 18551, 18823, 18824, 18835, 18474, 18834, 24244, 18830, 24500,
				308, 18602, 24417, 24340, 19800, 21543, 11079, 18566, 15972, 18994, 21319, 12261, 4161, 12177, 12193,
				12200, 12319, 16310, 20546, 20574, 24840, 26125, 24638, 18872, 19920, 25652, 1084, 19063, 3429, 17692,
				880, 20547, 24329, 21318, 19211, 20541, 26271, 18904, 22942, 19262, 25678, 3430, 20561, 1080, 8217,
				12771, 20538, 21796, 21797, 29183, 21803, 20521, 21181, 18989, 20081, 18900, 18907, 20767, 18583, 18131,
				23711, 25123, 19922, 13409, 19923, 29260, 23862, 21070, 3729 };

		for (int i = 0; i < names.length; i++) {

			String query = "update photo_exif_c11014 set poi_name = '" + names[i] + "' where poi_id = " + ids[i] + "";

			System.out.println(query);

			con.execute(query);

		}

	}

	private void getClassCategoris(String c, String table) throws SQLException {

		String q = "select distinct poi_name from  " + table + " where category = '" + c + "' order by(poi_name)";

		ResultSet r = con.executeSearch(q);

		StringBuilder b = new StringBuilder();
		while (r.next()) {
			b.append("\"").append(r.getString("poi_name")).append("\"");
			if (!r.isLast())
				b.append(",");
		}

		System.out.println(b.toString());
	}

	public DataGenerate() {

		categories.add("alimentacao");
		categories.add("ar_livre");
		categories.add("aventura");
		categories.add("cultura");
		categories.add("religiao");
		categories.add("recreacao");
		categories.add("vida_noturna");
		categories.add("natureza");

	}

	public void setOwnerCategoryPreferences() {

		String query = "select distinct owner from category_photos";

		ResultSet rs = con.executeSearch(query);

		String owner = "";

		List<Integer> values = new ArrayList();
		List<String> categories = new ArrayList();

		try {
			while (rs.next()) {

				owner = rs.getString("owner");

				query = "select distinct category, count(*) qtd from category_photos where category != 'null' and owner = '"
						+ owner + "' group by(category)";

				ResultSet rs_insert = con.executeSearch(query);

				while (rs_insert.next()) {

					categories.add(rs_insert.getString("category"));
					values.add(Integer.parseInt(rs_insert.getString("qtd")));

				}

				if (!categories.isEmpty() || !values.isEmpty()) {

					StringBuilder builder_categories = new StringBuilder();
					StringBuilder builder_values = new StringBuilder();

					Iterator i = categories.iterator();

					while (i.hasNext()) {

						builder_categories.append(i.next());

						if (i.hasNext())
							builder_categories.append(",");

					}

					i = values.iterator();

					while (i.hasNext()) {

						builder_values.append(i.next());

						if (i.hasNext())
							builder_values.append(",");

					}

					query = "insert into \"2014_category_owners_preferences\"(owner," + builder_categories + ")values('"
							+ owner + "'," + builder_values + ")";

					System.out.println(query);
					con.execute(query);

				}

				categories.clear();
				values.clear();

			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void setTableClassifier() {

		String TABLE = "category_photos_min_local_visited";

		int[] qtd_categorias = new int[8];

		List<String> turnos_begin = new ArrayList<String>();
		List<String> turnos_end = new ArrayList<String>();
		List<String> turnos = new ArrayList<String>();
		turnos_begin.add("05:00:00");
		turnos_begin.add("12:00:00");
		turnos_begin.add("18:00:00");

		turnos_end.add("11:59:00");
		turnos_end.add("17:59:00");
		turnos_end.add("23:59:00");

		turnos.add("Manha");
		turnos.add("Tarde");
		turnos.add("Noite");

		float qtd = 0.0f;
		float total = 0.0f;
		int month = 0;
		int total_fotos = 0;

		// PREENCHER A TABELA DE RECOMENDAÃ‡ÃƒO

		// TODO: 1) Selecionar todos dos Owners

		try {
			String query = "select distinct  owner from " + TABLE + " where category != 'null'";
			ResultSet rs = con.executeSearch(query);
			String owner = "";
			while (rs.next()) {

				owner = rs.getString("owner");

				// TODO: 2) selecionar os meses para cada registro de owner

				query = "select distinct date_part('month',date_time)as month from " + TABLE + " where owner = '"
						+ owner + "'";
				ResultSet rs_month = con.executeSearch(query);

				while (rs_month.next()) {

					month = Integer.parseInt(rs_month.getString("month"));

					for (int i = 0; i < categories.size(); i++) {
						query = "select count(*) as qtd from " + TABLE + " where owner = '" + owner
								+ "' and date_part('month',date_time) = " + month + " and category = '"
								+ categories.get(i) + "'";

						ResultSet rs_qtd = con.executeSearch(query);

						if (rs_qtd.next()) {

							qtd_categorias[i] += Integer.parseInt(rs_qtd.getString("qtd"));
							// total_fotos += qtd;
						}

					}

					// TODO: Definir locais por turno
					for (int i = 0; i < 3; i++) {
						query = "select distinct local_name, cast(date_time as date) as date from " + TABLE
								+ " where (cast(date_time as time) >= '" + turnos_begin.get(i)
								+ "' and cast(date_time as time) <= '" + turnos_end.get(i) + "')and  owner = '" + owner
								+ "' and date_part('month',date_time) = " + month + " and category !=  'null'";
						// System.out.println(query);
						ResultSet rs_turno = con.executeSearch(query);

						while (rs_turno.next()) {

							String season = getSeason(rs_turno.getString("date"));

							query = "insert into \"classification_2014_min_local_visited\"(owner,alimentacao,ar_livre,aventura,cultura,religiao,recreacao,vida_noturna,natureza,estacao, turno, destino)"
									+ "values('" + owner + "'," + qtd_categorias[0] + "," + qtd_categorias[1] + ","
									+ qtd_categorias[2] + "," + qtd_categorias[3] + "," + qtd_categorias[4] + ","
									+ qtd_categorias[5] + "," + qtd_categorias[6] + "," + qtd_categorias[7] + ",'"
									+ season + "','" + turnos.get(i) + "','" + rs_turno.getString("local_name") + "')";

							System.out.println(query);
							con.execute(query);
						}

					}

					// total_fotos = 0;
				}

				// Reseta o array acumulador de quantidade de fotos tiradas por
				// usuÃ¡rio para cada mÃªs
				for (int i = 0; i < qtd_categorias.length; i++)
					qtd_categorias[i] = 0;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		// TODO: Setar a estaÃ§Ã£o correspondente a partir da data

	}

	public void classificationStandardized() {
		/**
		 * CONFIGURAÃ‡Ã”ES PARA VALORES EM *CATEGORIAS* = NENHUM, POUCO, MODERADO OU
		 * MUITO
		 */
		String TABLE = "\"classification_2014_min_local_visited_delete_duplicate\"";

		// String interest[] = new String[8];

		/**
		 * CONFIGURAÃ‡Ã”ES PARA VALORES EM *CATEGORIAS* = PERCENTAGEM
		 */
		String INSERT_TABLE = "\"classification_2014_percent\"";
		float interest[] = new float[8];

		float total_photos = 0.0f;
		float qtd = 0.0f;
		float percent = 0.0f;

		final String POUCO = "Pouco";
		final String NENHUM = "Nenhum";
		final String MODERADO = "Moderado";
		final String MUITO = "Muito";

		try {

			String query = "select id, owner, aventura,ar_livre,alimentacao,aventura,cultura, religiao,natureza, recreacao, vida_noturna,(sum(ar_livre)+sum(alimentacao)+sum(aventura)+sum(cultura)+sum(religiao)+sum(natureza)+sum(vida_noturna)+sum(recreacao)) as total_photos,"
					+ " estacao, turno,destino from " + TABLE + " where id in" + " (select id from " + TABLE
					+ ") group by(id,owner,aventura,ar_livre,alimentacao,aventura,cultura, religiao,natureza, recreacao, vida_noturna,estacao, turno,destino) order by(id)";

			ResultSet rs = con.executeSearch(query);

			while (rs.next()) {

				total_photos = Float.parseFloat(rs.getString("total_photos"));

				for (int i = 0; i < categories.size(); i++) {

					qtd = Float.parseFloat(rs.getString(categories.get(i)));

					percent = qtd / total_photos * 100;

					// if(percent >= 1 && percent < 30)
					// interest[i] = POUCO;
					// else if(percent >=30 && percent < 70)
					// interest[i] = MODERADO;
					// else if(percent >=70 && percent <= 100)
					// interest[i] = MUITO;
					// else
					// interest[i] = NENHUM;

					interest[i] = percent;

				}

				// query = "insert into
				// "+INSERT_TABLE+"(owner,alimentacao,ar_livre,aventura,cultura,
				// religiao,natureza, recreacao, vida_noturna, estacao, turno,
				// destino) "
				// + "values('"+rs.getString("owner")+"',"
				// + "'"+interest[0]+"',"
				// + "'"+interest[1]+"',"
				// + "'"+interest[2]+"',"
				// + "'"+interest[3]+"',"
				// + "'"+interest[4]+"',"
				// + "'"+interest[5]+"',"
				// + "'"+interest[6]+"',"
				// + "'"+interest[7]+"',"
				// + "'"+rs.getString("estacao")+"',"
				// +
				// "'"+rs.getString("turno")+"','"+rs.getString("destino")+"')";

				query = "insert into " + INSERT_TABLE
						+ "(owner,alimentacao,ar_livre,aventura,cultura, religiao,natureza, recreacao, vida_noturna, estacao, turno, destino) "
						+ "values('" + rs.getString("owner") + "'," + "" + interest[0] + "," + "" + interest[1] + ","
						+ "" + interest[2] + "," + "" + interest[3] + "," + "" + interest[4] + "," + "" + interest[5]
						+ "," + "" + interest[6] + "," + "" + interest[7] + "," + "'" + rs.getString("estacao") + "',"
						+ "'" + rs.getString("turno") + "','" + rs.getString("destino") + "')";

				System.out.println(query);

				con.execute(query);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void preencherTxt() {

		// String query = "select * from \"classification_2014_reduced_01\"
		// where category = 'religiao'";

		String query = "select distinct destino from \"classification_2014_reduced_01\" order by(destino)";

		// String query = "select * from \"classification_2014_percent\"";

		// String query = "select distinct destino from
		// \"classification_2014_standardized\"";

		ResultSet rs = con.executeSearch(query);

		String str = "";
		try {
			while (rs.next()) {

				str += "\"" + rs.getString("destino") + "\"" + ",";
				// str =
				// rs.getString("perfil")+","+rs.getString("alimentacao")+","+rs.getString("ar_livre")+","+rs.getString("aventura")+","+rs.getString("cultura")+","
				// +
				// rs.getString("religiao")+","+rs.getString("natureza")+","+rs.getString("recreacao")+","+rs.getString("vida_noturna")+","+
				// rs.getString("estacao")+","+rs.getString("turno")+","+"\""+rs.getString("destino")+"\"";

				// str = rs.getString("perfil") +","+rs.getString("estacao")
				// +","+rs.getString("turno") +","+rs.getString("category")
				// +",\""+rs.getString("destino")+"\"";

				// str = rs.getString("perfil") +","+rs.getString("estacao")
				// +","+rs.getString("turno")
				// +",\""+rs.getString("destino")+"\"";

				// System.out.println(str);

			}
			System.out.println(str);

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/**
	 * MÃ©todo utilizado para configurar a popularidade para cada local percente a
	 * base de dados. Este passo Ã© importante para estimar o nÃ­vel de interesse de
	 * cada usuÃ¡rio no local visitado
	 **/

	public void builderCollborartiveTable() {

		final double MEDIA = 2.5;
		/*
		 * CriaÃ§Ã£o da tabela de recomendaÃ§Ã£o
		 */
		// StringBuilder builder = new StringBuilder("create table
		// collaborative_filter(owner character varying(30) NOT NULL,");
		//
		// for(int i = 0; i<locations.length; i++){
		// builder.append("\""+locations[i]+"\"");
		// builder.append(" double precision");
		//
		// if(i < locations.length - 1)
		// builder.append(",");
		// }
		//
		// builder.append(")");

		StringBuilder builder = new StringBuilder("insert into collaborative_filter(owner,");

		for (int i = 0; i < locations.length; i++) {
			builder.append("\"" + locations[i] + "\"");

			if (i < locations.length - 1)
				builder.append(",");
		}

		builder.append(") values ");

		String query = "select distinct owner from local_classification_aux";
		ResultSet rsOwners = con.executeSearch(query);

		List<String> owners = new ArrayList();
		List<String> locais = new ArrayList();
		List<Double> qtds = new ArrayList();

		int qtd = 0;
		double classification = 0;

		try {

			while (rsOwners.next()) {
				builder.append("('");
				builder.append(rsOwners.getString("owner"));
				builder.append("'");
				builder.append(",");

				query = "select *from local_classification_aux where owner = '" + rsOwners.getString("owner") + "'";
				ResultSet rsLocais = con.executeSearch(query);

				while (rsLocais.next()) {
					locais.add(rsLocais.getString("local_name"));
					// qtds.add(rsLocais.getInt("qdt_photos"));
					qtds.add(rsLocais.getDouble("classification"));
				}

				for (int i = 0; i < locations.length; i++) {

					if (locais.contains(locations[i])) {

						// qtd = qtds.get(locais.indexOf(locations[i]));
						// classification = qtd*MEDIA/popularities.get(i).value;
						classification = qtds.get(locais.indexOf(locations[i]));
						builder.append(classification);

					} else
						builder.append("0");

					if (i < locations.length - 1)
						builder.append(",");
				}

				builder.append(")");
				if (!rsOwners.isLast())
					builder.append(",");

				locais.clear();
				qtds.clear();
			}

			con.execute(builder.toString());
			System.out.println(builder);

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	private String getSeason(String date) {
		String season = "";

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dateC = df.parse(date);

			if (dateC.after(df.parse("2014-09-21")) && dateC.before(df.parse("2014-12-20")))
				season = "Primavera";
			else if (dateC.after(df.parse("2014-03-21")) && dateC.before(df.parse("2014-06-20")))
				season = "Outono";
			else if (dateC.after(df.parse("2014-06-21")) && dateC.before(df.parse("2014-09-20")))
				season = "Inverno";
			else // if(dateC.after(df.parse("2014-12-21")) &&
					// dateC.before(df.parse("2014-03-20")))
				season = "Verão";

		} catch (ParseException e) {

			e.printStackTrace();

		}
		return season;

	}

	private class LocationPopularity {

		private String name;
		private double value;

		public LocationPopularity(String name, double value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String toString() {

			return name + " - " + value;
		}

	}

	private void createTableClassification02() throws SQLException {

		String query = "select * from category_photos_naivebayes_aux2";
		ResultSet rs = con.executeSearch(query);

		StringBuilder builder = new StringBuilder();

		builder.append("insert into classification_nb02 values");

		while (rs.next()) {

			builder.append("(");
			builder.append("'").append(rs.getString("owner")).append("',");
			builder.append("'").append(rs.getString("date_time")).append("',");
			builder.append("'").append(rs.getString("date_time")).append("',");
			builder.append("'").append(rs.getString("date_time")).append("',");
			builder.append("'").append(rs.getString("local_name")).append("',");
			builder.append("'").append(rs.getString("category")).append("',");
			builder.append("'").append("?").append("')");

			if (!rs.isLast())
				builder.append(",\n");

		}

		System.out.println(builder);

		// con.execute(builder.toString());

	}

	class UpdatePerfil implements Runnable {
		int begin;
		int end;
		String table;

		public UpdatePerfil(int begin, int end, String table) {

			this.begin = begin;
			this.end = end;
			this.table = table;
		}

		@Override
		public void run() {

			try {
				configPerfil(begin, end, table);
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	class UpdateDay implements Runnable {

		int begin;
		int end;
		String table;

		public UpdateDay(int begin, int end, String table) {

			this.begin = begin;
			this.end = end;
			this.table = table;
		}

		@Override
		public void run() {

			try {
				updateDay(begin, end, table);
			} catch (SQLException | ParseException e) {

				e.printStackTrace();
			}

		}

	}

	private void updateDay(int begin, int end, String table) throws SQLException, ParseException {

		String query = "select id, cast(date_time_original as date) as date from " + table + " where id >= " + begin
				+ " and id <=" + end + "";
		ResultSet rs = con.executeSearch(query);

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date d = null;

		System.out.println(c.get(Calendar.DAY_OF_WEEK));
		String day = "";
		String update = "";

		while (rs.next()) {
			d = sdf.parse(rs.getString("date"));
			c.setTime(d);

			switch (c.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				day = "segunda";
				break;
			case Calendar.TUESDAY:
				day = "terça";
				break;
			case Calendar.WEDNESDAY:
				day = "quarta";
				break;
			case Calendar.THURSDAY:
				day = "quinta";
				break;
			case Calendar.FRIDAY:
				day = "sexta";
				break;
			case Calendar.SATURDAY:
				day = "sábado";
				break;
			case Calendar.SUNDAY:
				day = "domingo";
				break;
			}

			update = "update " + table + " set day = '" + day + "' where id = " + rs.getString("id") + " ";

			System.out.println(Thread.currentThread().getName() + " - " + update);
			con.execute(update);

		}

	}

	private void createrArffFile(String category, String table) throws SQLException {

		String query = "select perfil, month, hour_day, day, poi_name from " + table + " where category = '" + category
				+ "'";
		ResultSet rs = con.executeSearch(query);

		StringBuilder builder = new StringBuilder();
		while (rs.next()) {

			builder.append(rs.getString(1)).append(",");
			builder.append(rs.getString(2)).append(",");
			builder.append(rs.getString(3)).append(",");
			builder.append(rs.getString(4)).append(",");
			builder.append("\"").append(rs.getString(5)).append("\"");

			System.out.println(builder.toString());
			builder.delete(0, builder.length());
		}

		System.out.println("");

		query = "select distinct poi_name from " + table + " where category = '" + category + "' order by(poi_name)";
		rs = con.executeSearch(query);

		while (rs.next()) {

			builder.append("\"").append(rs.getString(1)).append("\"");

			if (!rs.isLast())
				builder.append(",");
		}

		System.out.println(builder.toString());

	}

	private void updateUser() throws SQLException {

		String query = "select owner, id from users";
		ResultSet rs = con.executeSearch(query);
		String owner;
		int id;
		while (rs.next()) {
			owner = rs.getString("owner");
			id = rs.getInt("id");
			query = "update location_popularity_aux set id = " + id + " where owner = '" + owner + "'";

			System.out.println(query);
			con.execute(query);
		}

	}

	/** popula tabela fc de user considerando sua média **/

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
					 insertTableUserItemRateByPhotos(begin, end);
					updateTableUserItemRateByPhotos(begin, end);
//				configRelevanceByCategory(begin, end);
//				updateAvgRelevanceByPhotos(begin, end);
				

			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

	}

	private void insertTableUserItemRateByPhotos(int begin, int end) throws SQLException {

		Connection con = new Connection();

		ResultSet users = con.executeSearch("select * from users where id >= " + begin + " and id <= " + end + "");

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

					// System.out.println(rs_avg.getString("poi_name")+" ---
					// "+rs_avg.getInt("qtd_photos"));
//					query = "insert into table2_user_item_fc(id,owner,poi_name,category,qtd_photos) values ("+id+",'"+user+"','"+rs_avg.getString("poi_name")+"','"+category+"',"+rs_avg.getInt("qtd_photos")+")";
//					System.out.println(Thread.currentThread().getName()+" - "+query);
//					con.execute(query);
				}

				avg = qtd_photos / count;

				// System.out.println(" ********* Média para a categoria "+category+" = "+avg+"
				// **************");

				query = "update rc_location_popularity_aux set avg = " + avg + " where id = " + id + " and category = '"
						+ category + "'";
				System.out.println(Thread.currentThread().getName() + " - " + query);
				con.execute(query);

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

				query = "update rc_location_popularity_aux set relevance = " + (c / p) + " where owner = '" + owner
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

			query = "select * from rc_location_popularity_aux where user_id = " + id + "";

			ResultSet result = con.executeSearch(query);

			while (result.next()) {

				poi_name = result.getString("poi_name");
				avg = result.getInt("avg");
				popularity = result.getInt("new_popularity");
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
				query = "update rc_location_popularity_aux set rate_avg_newpop = " + rate + " where poi_name = '" + poi_name
						+ "' and user_id = " + id + "";
				System.out.println(Thread.currentThread().getName() + " - " + query);
				con.execute(query);
				rate = 0;
			}

		}

	}

	/**
	 * MÃ©todo utilizado para configurar a popularidade para cada local percente a
	 * base de dados. Este passo Ã© importante para estimar o nÃ­vel de interesse de
	 * cada usuÃ¡rio no local visitado
	 **/
	public void setPopularityLocation() {
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

	private void setNewPopularityByYear() throws SQLException {
		
		int[] years = {2011,2012,2013,2014};
		
		String query = "select distinct poi_name from rc_photo_exif_c11014_rn order by poi_name";
		ResultSet locals = con.executeSearch(query);
		
		String poiName = "";
		double avg = 0;
		double log10 = 0;
		double qtdPhotos=0;
		double logPhotos = 0;
		double contPhotos=0;
		int cont = 0;
		int contYear = 0;
		int popularity = 0;
		
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
						log10 += logPhotos;
					
						System.out.println(years[i]);
						System.out.println("Média -> "+avg);
						System.out.println("Log : -> "+ log10);
						
						contYear++;
					}
					
				}

				
				contPhotos = 0;
				logPhotos = 0;
				cont = 0;
				
				
			}
			
			avg =  avg/contYear;
			log10 = (log10/contYear);
			
			
			if((int) log10 == 0)
				popularity = (int) avg;
			else if(log10 > avg)
				popularity = (int) avg;
			else
				popularity = (int) log10;
			
			
			
//			query = "update rc_location_popularity_aux set new_popularity = "+popularity+" where poi_name = '"+poiName+"'";
			System.out.println(" ------------------------ LOCAL = "+poiName+" -------------------------------");
			System.out.println("MÉDIA = "+avg);
			System.out.println("LOG 10 = "+log10);
			System.out.println("POP = "+popularity);
			System.out.println(" -----------------------------------------------------------------------------");
//			con.execute(query);
//			System.out.println(query);
	
			avg = 0;
			log10 = 0;
			contYear = 0;
		}
		
		
		
		
		
	}
	
public void updatePoiId(String table) throws SQLException {

		String query = "select * from mahout_pois";
		ResultSet rs = con.executeSearch(query);
		String poi;
		int id;
		while (rs.next()) {
			poi = rs.getString("poi_name");
			id = rs.getInt("poi_id");
			query = "update " + table + " set poi_id = " + id + " where poi_name = '" + poi + "'";

			System.out.println(query);
			con.execute(query);
		}

	}

}
