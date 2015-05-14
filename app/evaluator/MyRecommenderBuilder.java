package evaluator;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class MyRecommenderBuilder implements RecommenderBuilder {
	
	public Recommender buildRecommender(org.apache.mahout.cf.taste.model.DataModel  modelo) throws TasteException 
	{
		int neighbourhoodSize = 100;

		UserSimilarity similarity = new EuclideanDistanceSimilarity(modelo);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighbourhoodSize, similarity, modelo);
		UserBasedRecommender recommender = new GenericUserBasedRecommender(modelo, neighborhood, similarity);
		
//		ItemSimilarity similarity2 = new EuclideanDistanceSimilarity(modelo);
//		GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(modelo, similarity2);
//		
		return recommender;
	}
}