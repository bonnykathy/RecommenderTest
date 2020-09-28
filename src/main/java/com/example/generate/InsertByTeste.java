package com.example.generate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.teste.modelo.Local;
import com.example.teste.recommend.RecommenderNB;
import com.example.teste.recommend.Utils;
import com.examplee.teste.modelo.dao.Connection;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class InsertByTeste {

	public static void main(String[] args) {
//		String hourDay[] = { "madrugada", "manhã", "tarde", "noite" };
//		String days[] = { "segunda", "terça", "quarta", "quinta", "sexta", "sábado", "domingo" };
//		
//		String months[] = { "janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro",
//				"outubro", "novembro", "dezembro" };
		String c = "2";
		
		Thread t1 = new Thread(new InsertByTeste().new  MyTask("janeiro",c));
		Thread t2= new Thread(new InsertByTeste().new  MyTask("fevereiro",c));
		Thread t3 = new Thread(new InsertByTeste().new  MyTask("março",c));
		Thread t4 = new Thread(new InsertByTeste().new  MyTask("abril",c));
		Thread t5 = new Thread(new InsertByTeste().new  MyTask("maio",c));
		Thread t6 = new Thread(new InsertByTeste().new  MyTask("junho",c));
		Thread t7 = new Thread(new InsertByTeste().new  MyTask("julho",c));
		Thread t8 = new Thread(new InsertByTeste().new  MyTask("agosto",c));
		Thread t9 = new Thread(new InsertByTeste().new  MyTask("setembro",c));
		Thread t10 = new Thread(new InsertByTeste().new  MyTask("outubro",c));
		Thread t11 = new Thread(new InsertByTeste().new  MyTask("novembro",c));
		Thread t12 = new Thread(new InsertByTeste().new  MyTask("dezembro",c));
		
		
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
		t11.start();
		t12.start();

//		for (int m = 0; m < months.length; m++) {
//			String month = Utils.getMonth(months[m]);
//
//			for (int i = 0; i < days.length; i++) {
//
//				for (int j = 0; j < hourDay.length; j++) {
//
//					insertTeste(month, hourDay[j], days[i], "NB", "1");
//				}
//
//			}
//		}

	}

	private  void insertTeste(String month, String hourDay, String day, String table, String... categories) {

		try {
			
			String url, urlClass;	
			url = Utils.url_arffs_reader;
			urlClass = Utils.url_arffs_classes_reader;


//			if (user.equals("")) {
//				url = Utils.url_arffs;
//				urlClass = Utils.url_arffs_classes;
//
//			} else {
//				System.out.println("ENTROU AQUI" + user);
//				url = Utils.url_arffs + user + "/";
//				urlClass = Utils.url_arffs_classes + user + "/";
//			}

			String category;
			for (int c = 0; c < categories.length; c++) {

				category = Utils.getCategory(Integer.parseInt(categories[c]));

				Instances instances;

				System.out.println(url);

				DataSource ds = new DataSource(url + category + ".arff");
				instances = ds.getDataSet();

				instances.setClassIndex(instances.numAttributes() - 1);

				File file = new File(urlClass + category + ".txt");
				FileReader fr = new FileReader(file);

				BufferedReader br = new BufferedReader(fr);

				String line = "";

				while (br.ready()) {
					line = br.readLine().replace("\"", "");
				}

				br.close();
				fr.close();

				String[] localNames = line.split(",");

				NaiveBayes nb = new NaiveBayes();
				nb.buildClassifier(instances);

				String perfis[] = { "turista", "residente"};

				Instance instance = new DenseInstance(instances.numAttributes());

				instance.setDataset(instances);
				instance.setValue(0, perfis[0]);
				instance.setValue(1, month);
				instance.setValue(2, hourDay);
				instance.setValue(3, day);

				double resultado[] = nb.distributionForInstance(instance);

				instance.setDataset(instances);
				instance.setValue(0, perfis[1]);
				instance.setValue(1, month);
				instance.setValue(2, hourDay);
				instance.setValue(3, day);

				double resultado2[] = nb.distributionForInstance(instance);

				List<Local> locais = new ArrayList<>();
				List<Local> locais2 = new ArrayList<>();

				for (int i = 0; i < resultado.length; i++) {
					Local l = new Local(localNames[i], resultado[i], category);
					locais.add(l);
				}

				Collections.sort(locais);

				for (int i = 0; i < resultado2.length; i++) {
					Local l = new Local(localNames[i], resultado2[i], category);
					locais2.add(l);
				}

				Collections.sort(locais2);

				StringBuilder query = new StringBuilder();

				Connection connection = new Connection();

				for (int i = 0; i < locais.size(); i++) {

					query.append(
							"insert into "+table+"(month, day, hour_day, category,rank, turista, t_poi_name, t_percent, residente, r_poi_name, r_percent) values ");

					query.append("(").append("'" + month + "'").append(",").append("'" + day + "'").append(",")
							.append("'" + hourDay + "'").append(",").append("'" + category + "'").append(",").append(i)
							.append(",").append("'turista'").append(",").append("'" + locais.get(i).getName() + "'")
							.append(",").append(locais.get(i).getPercent()).append(",").append("'residente'")
							.append(",").append("'" + locais2.get(i).getName() + "'").append(",")
							.append(locais2.get(i).getPercent()).append(")");

					System.out.println(query);
					connection.execute(query.toString());

					query.delete(0, query.length());
				}

				/// recommends.addAll(locais);
			} // for categories

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	
	private class MyTask implements  Runnable {
		
		String month;
		String category;
		
		public MyTask(String month, String category){
			
			this.month = month;
			this.category = category;
			
		}
		
		public void run() {
			
			String hourDay[] = { "madrugada", "manhã", "tarde", "noite" };
			String days[] = { "segunda", "terça", "quarta", "quinta", "sexta", "sábado", "domingo" };
			
			String number = Utils.getMonth(month);

			for (int i = 0; i < days.length; i++) {

				for (int j = 0; j < hourDay.length; j++) {

					insertTeste(number, hourDay[j], days[i], "rc_probabilities_g_ed" , category);
				}

			}
		}
	}
}
