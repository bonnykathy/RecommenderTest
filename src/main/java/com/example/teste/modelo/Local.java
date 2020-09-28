package com.example.teste.modelo;

public class Local implements Comparable<Local> {

	private int id;
	private int position;
	private String name; // nome do local
	private String profile;//perfil do usuário
	private double percent;// usado na recomendação criada neste projeto, mas que não será utilizada
	
	private String hourDay;// turno
	private String category;//categoria do local
	private double avg;//media geral

	public Local(){}
	
	public Local(String name, double percent) {

		this.name = name;
		this.percent = percent;

	}
	
	
	public Local(String name, double percent, String category) {

		this.name = name;
		this.percent = percent;
		this.category = category;

	}



	public void setName(String name) {

		this.name = name;
	}

	public String getName() {

		return this.name;
	}
	
	public void setPercent(double percent) {
		this.percent = percent;
	}

	public Double getPercent() {
		return this.percent;
	}

	public int compareTo(Local other) {

		if (this.percent > other.percent)
			return -1;

		if (this.percent < other.percent)
			return 1;

		return 0;
	}

	@Override
	public String toString() {
	
		return name;
	}

	public String getHourDay() {
		return hourDay;
	}

	public void setHourDay(String hourDay) {
		this.hourDay = hourDay;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public String getPerfil() {
		return profile;
	}

	public void setPerfil(String perfil) {
		this.profile = perfil;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
