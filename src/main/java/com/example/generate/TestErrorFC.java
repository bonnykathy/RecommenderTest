package com.example.generate;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class TestErrorFC {

	public static void main(String[] args) {
		try {
			test();
		} catch (IOException | TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void test() throws IOException, TasteException {

		RandomUtils.useTestSeed();

		DataModel model = new FileDataModel(new File("/home/bonny/Documentos/Recommendation System/FC/fc_new/all_rel.csv"));

		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderEvaluator rmse = new RMSRecommenderEvaluator();
			
		RecommenderIRStatsEvaluator evaluatorF = new GenericRecommenderIRStatsEvaluator();

		RecommenderBuilder rb = new RecommenderBuilder() {

			@Override
			public Recommender buildRecommender(DataModel model) throws TasteException {

//				ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
//				ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
//				TanimotoCoefficientSimilarity similarity =  new TanimotoCoefficientSimilarity(model);
//				UncenteredCosineSimilarity similarity = new UncenteredCosineSimilarity(model);
				EuclideanDistanceSimilarity similarity = new EuclideanDistanceSimilarity(model);
				
//				UserNeighborhood neighborhood = new NearestNUserNeighborhood(300, similarity, model);		
//				Recommender recommender =  new GenericUserBasedRecommender(model, neighborhood, similarity);

				ItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);

				return recommender;

			}

		};
		
		double i = 0.99;
		double score1 = evaluator.evaluate(rb, null, model, i, 1.0);
	
		double score2 = rmse.evaluate(rb, null, model, i, 1.0);

		System.out.println(score1);
		System.out.println(score2);
		

		
//		IRStatistics stats = evaluatorF.
//				evaluate(rb, null, model, null, 5, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1);
//		System.out.println(stats.getPrecision());
//		System.out.println(stats.getRecall());
//		System.out.println(+stats.getF1Measure());
		

	}

}
