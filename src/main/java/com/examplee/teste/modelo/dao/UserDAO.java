package com.examplee.teste.modelo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.teste.recommend.Utils;

public class UserDAO {

	private Connection con = new Connection();

	public boolean setUser(String perfil, String login, String password) {

		String query = "insert into "+Utils.TABLE_USERS+" (owner,login, password, perfil_g) values ('" + login + "', '" + login + "','"
				+ password + "','" + perfil + "')";

		try {
			con.execute(query);
			return true;
		} catch (Exception e) {
			System.err.println("USER NOT INSERT!");
			return false;
		}

	}

	public boolean verifyRegiste(String login) {

		String query = "select * from "+Utils.TABLE_USERS+" where login = '" + login + "'";
		ResultSet rs = con.executeSearch(query);

		try {
			if (rs.next())
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String> login(String login, String password) {
		
		String query = "select * from "+Utils.TABLE_USERS+" where login = '" + login + "' and password = '" + password + "'";

		ResultSet rs = con.executeSearch(query);


		List<String> dados = new ArrayList<>();
		
		try {
			if (rs.next()) {
				dados.add(rs.getString("login"));
				dados.add(rs.getInt("id") + "");
			}
			else
				dados.add("Nothing");
		
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return dados;
	}

}
