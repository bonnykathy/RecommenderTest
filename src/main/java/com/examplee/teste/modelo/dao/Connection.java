package com.examplee.teste.modelo.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connection {

	private final String URL = "jdbc:postgresql://localhost:5432/poi_rj";
	private final String USER = "postgres";
	private final String PASSWORD ="postgres";
	
	private java.sql.Connection con;
	
	
	public void getConnection(){
		
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(URL, USER, PASSWORD);
		
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.err.println("PROBELMAS AO CONECTAR A BASE DE DADOS");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	
	}
	
	
	public ResultSet executeSearch(String query){
		
		try {
			getConnection();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(query);
			con.close();
			return rs;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public void execute(String query){
		
		try {
			getConnection();
			Statement stm = con.createStatement();
			stm.executeUpdate(query);
			con.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	
		
	}
	
}
