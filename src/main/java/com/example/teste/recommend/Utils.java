package com.example.teste.recommend;



public class Utils {
	
//public	static final String TABLE_NAIVE = "rc_nb_evaluate_01";
public	static final String TABLE_FC_EDIT_USER ="rc_table_fc_rate_novo";
public	static final String ITENS_SIM_PC ="itens_sim_pc";
public	static final String TABLE_USERS ="rc_users";
public static final int LIMIT = 5;

	static final int ALIMENTACAO = 0;
	static final int AR_LIVRE = 1;
	static final int LAZER = 2;
	static final int CULTURA = 3;
	static final int RELIGIAO = 4;
	static final int BARES= 5;
	static final int LOJAS= 6;
	

	static final String ALIMENTACAO_NAME = "alimentacao";
	static final String AR_LIVRE_NAME = "ar livre e recreacao";
	static final String LAZER_NAME = "lazer";
	static final String CULTURA_NAME = "arte e cultura";
	static final String RELIGIAO_NAME = "centros religiosos";
	static final String BARES_NAME = "bares";
	static final String LOJAS_NAME = "lojas e servicos";
	
	private static final String FOLDER = "NB_ARFFS/";
	
	public static final String PROFILE_T = "turista";
	public static final String PROFILE_R = "residente";

	public static final String url_alocentrico = "/home/bonny/Documentos/Recommendation System/alocentrics/";
	public static final String url_alocentrico_classes = "/home/bonny/Documentos/Recommendation System/alocentrics/classes/";
	public static final String url_temporary = "/home/bonny/Documentos/Recommendation System/temporary/";
	public static final String url_arffs_classes = "/home/bonny/Documentos/Recommendation System/system/classes/";
	public static final String url_arffs_classes_reader = "/home/bonny/Documentos/Recommendation System/system/classes/"+FOLDER;
	//public static final String url_arffs_classes_reader_evaluate = "/home/bonny/Documentos/Recommendation System/system/";
	public static final String url_arffs = "/home/bonny/Documentos/Recommendation System/system/";
	public static final String url_arffs_reader = "/home/bonny/Documentos/Recommendation System/system/"+FOLDER;
	public static final String url_arffs_reader_evaluate = "/home/bonny/Documentos/Recommendation System/system/";
	public static final String url_FCCSV = "/home/bonny/Documentos/Recommendation System/FC/";
	
	public static  String	FILE_ALL = "";
	public static  String T_FILE_PROFILE = "";
	public static  String T_FILE_ALL = "";

	public static  String R_FILE_PROFILE = "";
	public static  String R_FILE_ALL = "";
	
	public static String dbNaive = "";
	
	public static String EVALUATION = "";
	public static String EVALUATION_ALL = "";
	

	static final String categories[] = { ALIMENTACAO_NAME, AR_LIVRE_NAME, LAZER_NAME, CULTURA_NAME, RELIGIAO_NAME, BARES_NAME, LOJAS_NAME};
	static final String[] turno = { "manhã", "tarde", "noite", "madrugada" };
	static final String[] perfil = { "turista", "residente" };
	public static final String[] motivation = {"turistic","criative"};

	public static String getCategory(int c) {

		switch (c) {

		case ALIMENTACAO:
			return Utils.ALIMENTACAO_NAME;
		case AR_LIVRE:
			return Utils.AR_LIVRE_NAME;
		case LAZER:
			return Utils.LAZER_NAME;
		case CULTURA:
			return Utils.CULTURA_NAME;
		case RELIGIAO:
			return Utils.RELIGIAO_NAME;
		case BARES:
			return Utils.BARES_NAME;
		case LOJAS:
			return Utils.LOJAS_NAME;

		default:
			return "";

		}

	}
	
	
	public static int getCategory(String c) {

		switch (c) {

		case ALIMENTACAO_NAME:
			return ALIMENTACAO;
		case AR_LIVRE_NAME:
			return AR_LIVRE;
		case LAZER_NAME:
			return LAZER;
		case CULTURA_NAME:
			return CULTURA;
		case RELIGIAO_NAME:
			return RELIGIAO;
		case BARES_NAME:
			return BARES;
		case LOJAS_NAME:
			return LOJAS;

		default:
			return 0;

		}

	}
	
	public static String getMonth(String m) {
		
		String month = "";
		
		if(m.equals("janeiro"))
			month = "1";
		else if(m.equals("fevereiro"))
				month = "2";
		else if(m.equals("março"))
			month = "3";
		else if(m.equals("abril"))
			month = "4";
		else if(m.equals("maio"))
			month = "5";
		else if(m.equals("junho"))
			month = "6";
		else if(m.equals("julho"))
			month = "7";
		else if(m.equals("agosto"))
			month = "8";
		else if(m.equals("setembro"))
			month = "9";
		else if(m.equals("outubro"))
			month = "10";
		else if(m.equals("novembro"))
			month = "11";
		else if(m.equals("dezembro"))
			month = "12";
		return month;
		
	}
	

}
