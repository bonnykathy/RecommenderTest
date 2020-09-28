/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.generate;

import com.example.teste.recommend.Utils;
import com.examplee.teste.modelo.dao.Connection;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import java.util.List;

/**
 *
 * @author Bonny
 */
public class WriterCSVFCDataBase {

	private final String T_FILE = "t_rate_rel_evaluate_edit.csv";

	private final String R_FILE = "r_rate_rel_evaluate_edit.csv";

	public static void main(String[] args) {

//        try {
//        	
//            new WriterCSVFCDataBase().write(9090, 10000, 5.0, Utils.PROFILE_T);
//        } catch (IOException ex) {
//            Logger.getLogger(WriterCSVFCDataBase.class.getName()).log(Level.SEVERE, null, ex);
//        }
	}

	public void write(int userId, int poiId, double rate, String profile) throws IOException {

		String[] nextLine;
		List<String[]> all = new ArrayList<>();

		String p = profile.equals(Utils.PROFILE_T) ? T_FILE : R_FILE;

		CSVReader reader = new CSVReader(new FileReader(Utils.url_FCCSV + p));

		while ((nextLine = reader.readNext()) != null) {

			String[] str = { nextLine[0], nextLine[1], nextLine[2] };

			all.add(str);
		}

		String[] line = { "" + userId, "" + poiId, "" + rate };
		all.add(line);

		Writer writer = Files.newBufferedWriter(Paths.get(Utils.url_FCCSV + p));

		CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

		csvWriter.writeAll(all);

		writer.flush();

//        writer.close();
	}

	public void writeEvaluate(int userId, int poiId, double rate, String profile, boolean cod, String folder)
			throws IOException {

		String[] nextLine;
		List<String[]> all = new ArrayList<>();

		String p = "";
		if (cod)
			p = Utils.R_FILE_PROFILE;
		else
			p = Utils.FILE_ALL;

		CSVReader reader = new CSVReader(new FileReader(Utils.url_FCCSV + folder + "/" + p));

		while ((nextLine = reader.readNext()) != null) {

			String[] str = { nextLine[0], nextLine[1], nextLine[2] };

			all.add(str);
		}

		String[] line = { "" + userId, "" + poiId, "" + rate };
		all.add(line);

		Writer writer = Files.newBufferedWriter(Paths.get(Utils.url_FCCSV + folder + "/" + p));

		CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

		csvWriter.writeAll(all);

		writer.flush();

//        writer.close();
	}

	public void writeIndividualEvaluate(int userId, String profile, boolean cod, String folder)
			throws IOException, SQLException {

		List<String[]> all = new ArrayList<>();

		String arquivo = "";

		String query = "select A.user_id, A.poi_id, rate_relevance  from " + Utils.dbNaive + " A inner JOIN"
				+ " (select distinct user_id, rate_relevance, poi_id from rc_table_fc_rate_novo ) B ON"
				+ " A.user_id = B.user_id and" + " A.poi_id = B.poi_id ";

		if (cod) {// recomendações para residentes
			arquivo = Utils.R_FILE_PROFILE;

			query += "and A.perfil_g = '" + Utils.PROFILE_R
					+ "' group by(A.user_id, A.poi_id,rate_relevance) order by A.user_id";
			
		} else { // recomendações gerais
			arquivo = Utils.FILE_ALL;
		query += " group by(A.user_id, A.poi_id,rate_relevance) order by A.user_id";
		}
		Connection con = new Connection();
		ResultSet rs = con.executeSearch(query);

		int user_id = 0, poiId = 0;
		float rate = 0;

		while (rs.next()) {

			user_id = rs.getInt("user_id");
			poiId = rs.getInt("poi_id");
			rate = rs.getFloat("rate_relevance");

			String[] str = { user_id + "", poiId + "", "" + rate };

			all.add(str);
		}

		Writer writer = Files.newBufferedWriter(Paths.get(Utils.url_FCCSV + folder + "/" + arquivo));

		CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

		csvWriter.writeAll(all);

		writer.flush();

//        writer.close();
	}

	public void writeIndividual(int userId, String profile, boolean cod, int folder) throws IOException, SQLException {

		List<String[]> all = new ArrayList<>();

		String p;

		if (cod)
			p = profile.equals(Utils.PROFILE_T) ? Utils.T_FILE_PROFILE : Utils.R_FILE_PROFILE;
		else
			p = profile.equals(Utils.PROFILE_T) ? Utils.T_FILE_ALL : Utils.R_FILE_ALL;

		String query = "select A.user_id, A.poi_id, rate_relevance  from " + Utils.dbNaive + " A inner JOIN"
				+ " (select distinct user_id, rate_relevance, poi_id from rc_table_fc_rate_novo ) B ON"
				+ " A.user_id = B.user_id and" + " A.poi_id = B.poi_id and A.perfil_g = '" + profile + "'"
				+ " group by(A.user_id, A.poi_id,rate_relevance) order by A.user_id";

		Connection con = new Connection();
		ResultSet rs = con.executeSearch(query);

		int user_id = 0, poiId = 0;
		float rate = 0;

		while (rs.next()) {

			user_id = rs.getInt("user_id");
			poiId = rs.getInt("poi_id");
			rate = rs.getFloat("rate_relevance");

			String[] str = { user_id + "", poiId + "", "" + rate };

			all.add(str);
		}

		Writer writer = Files.newBufferedWriter(Paths.get(Utils.url_FCCSV + folder + "/" + p));

		CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

		csvWriter.writeAll(all);

		writer.flush();

//        writer.close();
	}

}
