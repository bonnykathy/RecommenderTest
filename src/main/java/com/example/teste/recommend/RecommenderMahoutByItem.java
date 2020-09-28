package com.example.teste.recommend;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.MultidimensionalCounter.Iterator;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.hadoop.similarity.item.ItemSimilarityJob;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;

import com.example.teste.modelo.Local;
import com.examplee.teste.modelo.dao.Connection;

import weka.core.pmml.jaxbbindings.Euclidean;

public class RecommenderMahoutByItem {

	private String file;

	public static void main(String[] args) throws IOException, TasteException {

		for (Local l : new RecommenderMahoutByItem().recommend("turista", "turista", 1257, 5))
			System.out.println(l.getName());
	}

	public List<Local> recommend(String p1, String p2, int userId, int amount) throws IOException {

		p1 = p1.equals(p2) ? p1 : p2;
		DataModel model;
		ItemBasedRecommender recommender;

		try {

			if (p1.equals(Utils.PROFILE_T)) {
				file = "t_rate_rel_evaluate_edit.csv";// melhor resultado
				model = new FileDataModel(new File(Utils.url_FCCSV + file));
				UncenteredCosineSimilarity similarity = new UncenteredCosineSimilarity(model);
				recommender = new GenericItemBasedRecommender(model, similarity);

			} else {
				file = "r_rate_rel_evaluate_edit.csv"; //melhor resultado
				model = new FileDataModel(new File(Utils.url_FCCSV + file));
				TanimotoCoefficientSimilarity similarity = new TanimotoCoefficientSimilarity(model);
				recommender = new GenericItemBasedRecommender(model, similarity);

			}

//				ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
//				ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
//				TanimotoCoefficientSimilarity similarity = new TanimotoCoefficientSimilarity(model);
//				UncenteredCosineSimilarity similarity = new UncenteredCosineSimilarity(model);
//			EuclideanDistanceSimilarity similarity = new EuclideanDistanceSimilarity(model);

//		ItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);

//	List<RecommendedItem> list = 	recommender.mostSimilarItems(1113, 10);// traz os items mais similares

			int cont = 0;

			List<RecommendedItem> list = recommender.recommend(userId, amount); // traz as predições

			for (RecommendedItem i : list) {

//				System.out.println(i);
				cont++;
			}
//			System.out.println("QUANTIDADE: " + cont);

			if (list.isEmpty())
				return null;

			return getPoiName(list);

		}

		catch (SQLException e) {
			e.printStackTrace();
			return null;

		} catch (TasteException e) {
			System.out.println("NO FC Recommendations!");
			return null;
		}

	}

	public List<Local> recommendEvaluate(String p1, String p2, int userId, int amount, boolean cod) throws IOException {

		p1 = p1.equals(p2) ? p1 : p2;
		DataModel model;
		ItemBasedRecommender recommender;

		try {

			if (p1.equals(Utils.PROFILE_T)) {
				
					file = (cod)? Utils.R_FILE_PROFILE : Utils.T_FILE_ALL;
			
//				file = "t_rate_rel_evaluate_edit.csv";// melhor resultado
				model = new FileDataModel(new File(Utils.url_FCCSV +file));
				UncenteredCosineSimilarity similarity = new UncenteredCosineSimilarity(model);
				recommender = new GenericItemBasedRecommender(model, similarity);

			} else if (p1.equals(Utils.PROFILE_R)){
				
				file = (cod)? Utils.R_FILE_PROFILE : Utils.R_FILE_ALL;
				//file = "r_rate_rel_evaluate_edit.csv"; //melhor resultado
				model = new FileDataModel(new File(Utils.url_FCCSV + file));
				TanimotoCoefficientSimilarity similarity = new TanimotoCoefficientSimilarity(model);
				recommender = new GenericItemBasedRecommender(model, similarity);

			}
			else {
				
				recommender = null;
			}

			int cont = 0;

			List<RecommendedItem> list = recommender.recommend(userId, amount); // traz as predições

			for (RecommendedItem i : list) {

				cont++;
			}

			if (list.isEmpty())
				return null;

			return getPoiName(list);

		}

		catch (SQLException e) {
			e.printStackTrace();
			return null;

		} catch (TasteException e) {
			System.out.println("NO FC Recommendations!");
			return null;
		}

	}

	
	public List<Local> recommendEvaluate( int userId, int amount, boolean cod, String folder) throws IOException {

		
		DataModel model;
		ItemBasedRecommender recommender;

		try {
			file = (cod)? Utils.R_FILE_PROFILE : Utils.FILE_ALL;

			if (!cod) {
				System.out.println(Utils.url_FCCSV +folder + file);
				
				model = new FileDataModel(new File(Utils.url_FCCSV +folder + file));
				EuclideanDistanceSimilarity similarity = new EuclideanDistanceSimilarity(model);
				recommender = new GenericItemBasedRecommender(model, similarity);

			} else {
				
				System.out.println(Utils.url_FCCSV +folder + file);

				model = new FileDataModel(new File(Utils.url_FCCSV + folder+ file));
				TanimotoCoefficientSimilarity similarity = new TanimotoCoefficientSimilarity(model);
				recommender = new GenericItemBasedRecommender(model, similarity);

			}
			

			int cont = 0;

			List<RecommendedItem> list = recommender.recommend(userId, amount); // traz as predições

			System.out.println(list);
			
//			for (RecommendedItem i : list) {
//
//				cont++;
//			}

			if (list.isEmpty())
				return null;

			return getPoiName(list);

		}

		catch (SQLException e) {
			e.printStackTrace();
			return null;

		} catch (TasteException e) {
			System.out.println("NO FC Recommendations!");
			return null;
		}

	}
	
	private List<Local> getPoiName(List<RecommendedItem> recommends) throws SQLException {

		List<Local> locais = new ArrayList<>();
//		StringBuilder builder = new StringBuilder();
		String query = "";

		for (RecommendedItem i : recommends) {

//			builder.append(i.getItemID()).append(",");
//		    builder.delete(builder.length() - 1, builder.length());

//			String query = "select poi_name from rc_mahout_pois where poi_id in(" + builder + ")";

			/** define se o valor da predição permite a recomendação **/
			if (i.getValue() >= 2) {

				query = "select distinct poi_name, poi_id, category from rc_naive_classification_edit_user where poi_id = "
						+ i.getItemID() + "";

				Connection con = new Connection();
				ResultSet rs = con.executeSearch(query);

				while (rs.next()) {

					Local l = new Local();
					l.setName(rs.getString("poi_name"));
					l.setId(rs.getInt("poi_id"));
					l.setCategory(rs.getString("category"));
					locais.add(l);
				}

			}
		}

		return locais;

	}

}
