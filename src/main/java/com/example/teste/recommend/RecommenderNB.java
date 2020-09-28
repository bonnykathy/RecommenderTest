package com.example.teste.recommend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.example.teste.modelo.Local;
import com.examplee.teste.modelo.dao.LocalDAO;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class RecommenderNB {

	private Instances instances;
	private final int AMOUNT = 10;
	private double avg;
	private List<Local> predictions;

	public static void main(String[] args) {

		RecommenderNB nb = new RecommenderNB();

		String perfil = "turista";
		String perpective = "turista";
		String month = "outubro";
		String hourDay = "manhã";
		String day = "terça";
		int userId = 1;
		String[] array = { "1" };

		List<Local> locais = nb.getRecommend(perfil, perpective, Utils.getMonth(month), hourDay, day, userId, array);

		for (Local l : locais) {

			System.out.println(l.getName() + ", " + l.getPercent());
		}

	}

	/**
	 * @p1 profile
	 * @p2 perspective
	 */
	public List<Local> getRecommend(String p1, String p2, String month, String hourDay, String day, int userId,
			String... categories) {
		avg = 0;

//		List<Local> locais;
//		HashMap<Integer, Local> visited = new HashMap<>();

		p1 = p1.equals(p2) ? p1 : p2;

		try {

			List<Local> recommends = new ArrayList<>();
			String url, urlClass;

			url = Utils.url_arffs_reader;
			urlClass = Utils.url_arffs_classes_reader;
			
			/**
			 * código abaixo busca todos os lugares já visitados pelo usuário
			 * */

//			if (userId == 0) {
//				System.out.println("Usuário vazio!");
//
//			} else { // não será mais usado por motivos de desempenho uma vez que a alteração de um
//						// dado deverá ser refletido em todos os demais arffs
//				System.out.println("Buscando locais que o usuário " + userId + " visitou...");
//
//				locais = new LocalDAO().getAlreadyVisited(userId);
//
//				for (Local l : locais) {
////					System.out.println("visited: " + l.getName() + ", " + l.getId());
//					visited.put(l.getId(), l);
//				}
//
//			}

			String category;
			for (int c = 0; c < categories.length; c++) {

				category = Utils.getCategory(Integer.parseInt(categories[c]));

				Instances instances;
				
//					System.out.println(url);

				DataSource ds = new DataSource(url + category + ".arff");
				instances = ds.getDataSet();

				instances.setClassIndex(instances.numAttributes() - 1);

				String urlClassName = urlClass + category + ".txt";
				String urlPoiId = urlClass + category + "_ids.txt";

				String[] localNames = getTextInformation(urlClassName, true);
				String[] poi_ids = getTextInformation(urlPoiId, false);

				NaiveBayes nb = new NaiveBayes();
				nb.buildClassifier(instances);

				Instance instance = new DenseInstance(instances.numAttributes());
				instance.setDataset(instances);
				instance.setValue(0, p1);
				instance.setValue(1, month);
				instance.setValue(2, hourDay);
				instance.setValue(3, day);

				double resultado[] = nb.distributionForInstance(instance);

				predictions = new ArrayList<>();

				for (int i = 0; i < resultado.length; i++) {
					Local l = new Local(localNames[i], resultado[i], category);
					l.setId(Integer.parseInt(poi_ids[i]));
					avg += resultado[i];

					//if (!visited.containsKey(l.getId()))
						predictions.add(l);
				}

				new LocalDAO().getAVGRecommend(predictions);

				Collections.sort(predictions);
				
//				for(int i=0;i<predictions.size();i++)
//					System.out.println((i+1)+" - "+predictions.get(i).getName());

				int cont = 0;
				
//				List<Local> poiRecommendations = new ArrayList();
//				
//				for (Local l : predictions) {
//					if(++cont<=Utils.LIMIT) {		
//
//					poiRecommendations.add(l);
//					System.out.println(l);
//					System.out.println(l.getPercent());
//					}
//					
//				}

			recommends.addAll(predictions);
//				recommends.addAll(poiRecommendations);
			} // for categories
			return recommends;
		} catch (Exception e) {

			e.printStackTrace();
			return null;

		}

	}

	public List<Local> getRecommendAll(String p1, String p2, String month, String hourDay, String day, int userId,
			String... categories) {
		avg = 0;
		
		List<Local> locais;

		p1 = p1.equals(p2) ? p1 : p2;

		try {

			List<Local> recommends = new ArrayList<>();
			String url, urlClass;

			url = Utils.url_arffs_reader;
			urlClass = Utils.url_arffs_classes_reader;

			if (userId == 0) {
				System.out.println("Usuário vazio!");
			}

			String category;
			for (int c = 0; c < categories.length; c++) {

				category = Utils.getCategory(Integer.parseInt(categories[c]));

				Instances instances;

				System.out.println(url);

				DataSource ds = new DataSource(url + category + ".arff");
				instances = ds.getDataSet();

				instances.setClassIndex(instances.numAttributes() - 1);

				String urlClassName = urlClass + category + ".txt";
				String urlPoiId = urlClass + category + "_ids.txt";

				String[] localNames = getTextInformation(urlClassName, true);
				String[] poi_ids = getTextInformation(urlPoiId, false);

				NaiveBayes nb = new NaiveBayes();
				nb.buildClassifier(instances);

				Instance instance = new DenseInstance(instances.numAttributes());
				instance.setDataset(instances);
				instance.setValue(0, p1);
				instance.setValue(1, month);
				instance.setValue(2, hourDay);
				instance.setValue(3, day);

				double resultado[] = nb.distributionForInstance(instance);

				predictions = new ArrayList<>();

				for (int i = 0; i < resultado.length; i++) {
					Local l = new Local(localNames[i], resultado[i], category);
					l.setId(Integer.parseInt(poi_ids[i]));
					predictions.add(l);
					avg += resultado[i];
				}

				new LocalDAO().getAVGRecommend(predictions);

				Collections.sort(predictions);

				recommends.addAll(predictions);
			}
			return recommends;
		} catch (Exception e) {

			e.printStackTrace();
			return null;

		}

	}

	public double getAVG() {

		return avg / predictions.size();
	}

	private String[] getTextInformation(String urlClassName, boolean replace) throws IOException {

		File file = new File(urlClassName);
		FileReader fr = new FileReader(file);

		BufferedReader br = new BufferedReader(fr);

		String line = "";

		if (replace)
			while (br.ready())
				line = br.readLine().replace("\"", "");

		else
			line = br.readLine();

		br.close();
		fr.close();

		String[] localNames = line.split(",");

		return localNames;
	}

	private DataSource charge(String arquivo) {

		try {
			DataSource ds = new DataSource(Utils.url_arffs + arquivo + ".arff");
			instances = ds.getDataSet();
			instances.setClassIndex(instances.numAttributes() - 1);

			return ds;
			// System.out.println(instances.toString());

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Método construído para executar o Evaluate
	 * **/
	
	public List<Local> getRecommendEvaluate(String p1, String p2, String month, String hourDay, String day, int userId,
			boolean cod, String folder, String... categories) {
		avg = 0;

		
		String FOLDER = ""+folder+"/";
		
		String directory = (cod)? Utils.EVALUATION: Utils.EVALUATION_ALL;
		
		FOLDER += directory;
		
		List<Local> locais;
		HashMap<Integer, Local> visited = new HashMap<>();

		if(cod)
			p1 = p1.equals(p2) ? p1 : p2;

		try {

			List<Local> recommends = new ArrayList<>();
			String url, urlClass;

			url = Utils.url_arffs+FOLDER;
			urlClass = Utils.url_arffs+folder+"/classes/"+directory+"/";


			String category;
			for (int c = 0; c < categories.length; c++) {

				category = Utils.getCategory(Integer.parseInt(categories[c]));

				Instances instances;
			
				System.out.println(url+""+category);

				DataSource ds = new DataSource(url + category + ".arff");
				instances = ds.getDataSet();

				instances.setClassIndex(instances.numAttributes() - 1);

				String urlClassName = urlClass + category + ".txt";
				String urlPoiId = urlClass + category + "_ids.txt";

				String[] localNames = getTextInformation(urlClassName, true);
				String[] poi_ids = getTextInformation(urlPoiId, false);

				NaiveBayes nb = new NaiveBayes();
				nb.buildClassifier(instances);

				Instance instance = new DenseInstance(instances.numAttributes());
				instance.setDataset(instances);
				
				if(cod) {
					instance.setValue(0, p1);
					instance.setValue(1, month);
					instance.setValue(2, hourDay);
					instance.setValue(3, day);
					
				}else {
				
					instance.setValue(0, month);
					instance.setValue(1, hourDay);
					instance.setValue(2, day);
				}

				double resultado[] = nb.distributionForInstance(instance);

				predictions = new ArrayList<>();

				for (int i = 0; i < resultado.length; i++) {
					Local l = new Local(localNames[i], resultado[i], category);
					l.setId(Integer.parseInt(poi_ids[i]));
					avg += resultado[i];

					//if (!visited.containsKey(l.getId()))
						predictions.add(l);
				}

				new LocalDAO().getAVGRecommend(predictions);

				Collections.sort(predictions);
				
//				for(int i=0;i<predictions.size();i++)
//					System.out.println((i+1)+" - "+predictions.get(i).getName());

				int cont = 0;
				
//				List<Local> poiRecommendations = new ArrayList();
//				
//				for (Local l : predictions) {
//					if(++cont<=Utils.LIMIT) {		
//
//					poiRecommendations.add(l);
//					System.out.println(l);
//					System.out.println(l.getPercent());
//					}
//					
//				}

			recommends.addAll(predictions);
//				recommends.addAll(poiRecommendations);
			} // for categories
			return recommends;
		} catch (Exception e) {

			e.printStackTrace();
			return null;

		}

	}

}
