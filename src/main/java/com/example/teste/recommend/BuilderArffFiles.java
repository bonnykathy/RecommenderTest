package com.example.teste.recommend;

/**
 * CLASSE QUE GERA OS ARQUIVOS ARFFS USADOS PELO SISTEMA PELO NAIVE BAYES
 * 
 * */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.teste.modelo.ArrayLocais;
import com.example.teste.modelo.Local;
import com.examplee.teste.modelo.dao.Connection;

public class BuilderArffFiles {

	private static String TABLE_NAIVE;

	private Connection con = new Connection();

	public void createFiles(int folder, String directory, String col, boolean isAll) {

		StringBuilder[] builders = { new StringBuilder(), new StringBuilder(), new StringBuilder(), new StringBuilder(),
				new StringBuilder(), new StringBuilder(), new StringBuilder() };

		StringBuilder[] builders_poiIds = { new StringBuilder(), new StringBuilder(), new StringBuilder(),
				new StringBuilder(), new StringBuilder(), new StringBuilder(), new StringBuilder() };

		ArrayLocais arrayLocais = new ArrayLocais();

		// busca por todos locais
		List<Local> locais;

		locais = configLocalRecommend(directory);

		// armazenar as classes para criar arquivo txt de configuração
		List<String> strClasses = new ArrayList<>();

		StringBuilder head = new StringBuilder();
		/* Monta a String com o nome das classes para cada arquivo arff a ser gerado */
		for (int i = 0; i < locais.size(); i++) {

			if (locais.get(i).getCategory().equals(Utils.ALIMENTACAO_NAME)) {
				builders[Utils.ALIMENTACAO].append("\"").append(locais.get(i).getName()).append("\"").append(",");
				arrayLocais.get(Utils.ALIMENTACAO).add(locais.get(i).getName());

				builders_poiIds[Utils.ALIMENTACAO].append(locais.get(i).getId()).append(",");

			} else if (locais.get(i).getCategory().equals(Utils.AR_LIVRE_NAME)) {
				builders[Utils.AR_LIVRE].append("\"").append(locais.get(i).getName()).append("\"").append(",");
				arrayLocais.get(Utils.AR_LIVRE).add(locais.get(i).getName());

				builders_poiIds[Utils.AR_LIVRE].append(locais.get(i).getId()).append(",");

			} else if (locais.get(i).getCategory().equals(Utils.LAZER_NAME)) {
				builders[Utils.LAZER].append("\"").append(locais.get(i).getName()).append("\"").append(",");
				arrayLocais.get(Utils.LAZER).add(locais.get(i).getName());

				builders_poiIds[Utils.LAZER].append(locais.get(i).getId()).append(",");

			} else if (locais.get(i).getCategory().equals(Utils.CULTURA_NAME)) {
				builders[Utils.CULTURA].append("\"").append(locais.get(i).getName()).append("\"").append(",");
				arrayLocais.get(Utils.CULTURA).add(locais.get(i).getName());

				builders_poiIds[Utils.CULTURA].append(locais.get(i).getId()).append(",");

			} else if (locais.get(i).getCategory().equals(Utils.RELIGIAO_NAME)) {
				builders[Utils.RELIGIAO].append("\"").append(locais.get(i).getName()).append("\"").append(",");
				arrayLocais.get(Utils.RELIGIAO).add(locais.get(i).getName());

				builders_poiIds[Utils.RELIGIAO].append(locais.get(i).getId()).append(",");

			} else if (locais.get(i).getCategory().equals(Utils.BARES_NAME)) {
				builders[Utils.BARES].append("\"").append(locais.get(i).getName()).append("\"").append(",");
				arrayLocais.get(Utils.BARES).add(locais.get(i).getName());

				builders_poiIds[Utils.BARES].append(locais.get(i).getId()).append(",");

			} else if (locais.get(i).getCategory().equals(Utils.LOJAS_NAME)) {
				builders[Utils.LOJAS].append("\"").append(locais.get(i).getName()).append("\"").append(",");
				arrayLocais.get(Utils.LOJAS).add(locais.get(i).getName());

				builders_poiIds[Utils.LOJAS].append(locais.get(i).getId()).append(",");
			}

		}

		try {
			for (int b = 0; b < builders.length; b++) {

				if (builders[b].length() == 0)
					continue;

				builders[b].deleteCharAt(builders[b].length() - 1);
				builders_poiIds[b].deleteCharAt(builders_poiIds[b].length() - 1);

				System.out.println(builders[b].toString());

				head.append("@relation caracateristicas\n\n");

				if (!isAll)
					head.append("@attribute perfil {residente, turista}\n"); // profile

				head.append("@attribute month {1,2,3,4,5,6,7,8,9,10,11,12}\n");
				head.append("@attribute hour_day {madrugada,manhã, tarde,noite}\n");
				head.append("@attribute day {segunda, terça, quarta,quinta,sexta,sábado,domingo}\n");
				head.append("@attribute class {").append(builders[b]).append("}").append("\n\n");
				head.append("@data\n");

				// configura o conjunto de classes para serem usados nos
				// arquivos arff
				// strClasses.add(categories[b] + ":" + builders[b].toString());

				createConfigClassArffFiles(Utils.categories[b], builders[b].toString(), folder, directory);
				createPoiIdsFiles(Utils.categories[b], builders_poiIds[b].toString(), folder, directory);

				builders[b].delete(0, builders[b].length());
				builders[b].append(head);

				for (String l : arrayLocais.get(b)) {

					String query = "select * from " + TABLE_NAIVE + " where" + " poi_name = '" + l + "'";

					System.out.println(query);

					ResultSet rs = con.executeSearch(query);

					while (rs.next()) {

						if (!isAll)
							builders[b].append(rs.getString(col)).append(",");// profile

						builders[b].append(rs.getString("month")).append(",");
						builders[b].append(rs.getString("hour_day")).append(",");
						builders[b].append(rs.getString("day")).append(",");
						builders[b].append("\"").append(rs.getString("poi_name")).append("\"").append("\n");

					}
				}

				String url;

//				if (directory.equals("")) {
//					url = Utils.url_arffs + Utils.categories[b] + ".arff";
//
//				} else {
				new File(Utils.url_arffs + folder + "/" + directory).mkdir();
				url = Utils.url_arffs + folder + "/" + directory + "/" + Utils.categories[b] + ".arff";

//				}

				File file = new File(url);

				FileOutputStream fos = new FileOutputStream(file);
				fos.write(builders[b].toString().getBytes());
				fos.close();
				head.delete(0, head.length());

			} // fim for

		} catch (SQLException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private List<Local> configLocalRecommend(String d) {

		List<Local> locais = new ArrayList<>();

		String query = "select distinct poi_name, poi_id, category from " + TABLE_NAIVE + " order by poi_name";

		ResultSet rs = con.executeSearch(query);

		try {
			while (rs.next()) {

				Local l = new Local();
				l.setName(rs.getString("poi_name"));
				l.setCategory(rs.getString("category"));
				l.setId(rs.getInt("poi_id"));
				locais.add(l);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return locais;
	}

	private void createConfigClassArffFiles(String c, String locais, int folder, String directory) {
		try {
			File file;

			System.out.println(directory);
//			if (id.equals("")) {
//
//				file = new File(Utils.url_arffs_reader_evaluate + c + ".txt");
//			} else {

			new File(Utils.url_arffs + folder + "/classes/" + directory).mkdir();
			file = new File(Utils.url_arffs + folder + "/classes/" + directory + "/" + c + ".txt");

//			}

			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(locais);
			bw.newLine();

			bw.close();
			fw.close();

			System.out.println("CREATE CLASSES");

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void createPoiIdsFiles(String c, String poiIds, int folder, String directory) {
		try {
			File file;

			System.out.println(directory);
//			if (directory.equals("")) {
//
//				file = new File(Utils.url_arffs_classes + c + "_ids.txt");
//			} else {

			new File(Utils.url_arffs + folder + "/classes/" + directory).mkdir();
			file = new File(Utils.url_arffs + folder + "/classes/" + directory + "/" + c + "_ids.txt");

//			}

			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(poiIds);
			bw.newLine();

			bw.close();
			fw.close();

			System.out.println("CREATE POI IDS");

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		TABLE_NAIVE = "rc_nb_evaluate_pois_05"; // precisa configurar
		int folder = 5; // precisa configurar
		boolean isAll = true;//precisa configurar
		
		//new BuilderArffFiles().createFiles(folder, "EVALUATION ALL POIS", "perfil_g", isAll);
	}

}
