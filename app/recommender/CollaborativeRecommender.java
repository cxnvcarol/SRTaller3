package recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.EvaluationResult;
import models.Movie;
import models.Recommendation;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import controllers.EvaluationController;

public class CollaborativeRecommender {
	/**
	 * Recommender which will be hold by this session bean.
	 */
	private static UserBasedRecommender recommender = null;

	/**
	 * An MemoryIDMigrator which is able to create for every string a long
	 * representation. Further it can store the string which were put in and it
	 * is possible to do the mapping back.
	 */
	private static MemoryIDMigrator thing2long = new MemoryIDMigrator();

	/**
	 * The name of the file used for loading.
	 */
	private static String rutaReviewInfo = "./data/reviews_info.txt";

	/**
	 * A data model which is needed for the recommender implementation. It
	 * provides a standardized interface for using the recommender. The data
	 * model can be become quite memory consuming. In our case it will be around
	 * 2 mb.
	 */
	private static DataModel dataModel = null;

	private static GenericBooleanPrefItemBasedRecommender recommenderGBIR;

	private static CollaborativeRecommender instance;

	public static final int PEARSON = 1;
	public static final int SPEARMAN = 2;
	public static final int EUCLIDEAN = 3;

	public static final long user_id_test = 5;

	private static final String rutaReviewInfoLocationBased = null;

	public static final double MAX_RECOMMENDATIONS = 20;

	public static void main(String[] args) {
		// generateDataModel();
		EvaluationResult resCollab = CollaborativeRecommender.evaluate(50, 100, CollaborativeRecommender.EUCLIDEAN, 0.5);
		System.out.println(resCollab.description);
		System.out.println("Precision: "+resCollab.precision);
		System.out.println("Recall: "+resCollab.recall);
		System.out.println("Time: "+resCollab.time);
		

	}
	private static boolean isContainedIn(long business_id,
			ArrayList<Movie> businesses) {
		boolean termino = false;
		for (int i = 0; i < businesses.size() && !termino; i++) {
			if (businesses.get(i).id==business_id) {
				return true;
			}
		}
		return false;
	}

	public static void generateDataModel() {
		try {
			long beforeData = System.currentTimeMillis();
			// create a file out of the resource
			File data = new File(rutaReviewInfo);

			// create a map for saving the preferences (likes) for
			// a certain person
			Map<Long, List<Preference>> preferecesOfUsers = new HashMap<Long, List<Preference>>();

			// use a CSV parser for reading the file
			// use UTF-8 as character set
			BufferedReader br = new BufferedReader(new FileReader(data));

			String line;

			// go through every line
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(";");
				String person = parts[0].trim();
				String likeName = parts[1].trim();
				float preference = Float.parseFloat(parts[2]);

				// create a long from the person name
				long userLong = thing2long.toLongID(person);

				// store the mapping for the user
				thing2long.storeMapping(userLong, person);

				// create a long from the like name
				long itemLong = thing2long.toLongID(likeName);

				// store the mapping for the item
				thing2long.storeMapping(itemLong, likeName);

				List<Preference> userPrefList;

				// if we already have a userPrefList use it
				// otherwise create a new one.
				if ((userPrefList = preferecesOfUsers.get(userLong)) == null) {
					userPrefList = new ArrayList<Preference>();
					preferecesOfUsers.put(userLong, userPrefList);
				}
				// add the like that we just found to this user
				userPrefList.add(new GenericPreference(userLong, itemLong,
						preference));
				System.out.println("Adding " + person + "(" + userLong
						+ ") to " + likeName + "(" + itemLong
						+ ") with preference " + preference);
			}

			// create the corresponding mahout data structure from the map
			FastByIDMap<PreferenceArray> preferecesOfUsersFastMap = new FastByIDMap<PreferenceArray>();
			for (Entry<Long, List<Preference>> entry : preferecesOfUsers
					.entrySet()) {
				preferecesOfUsersFastMap.put(entry.getKey(),
						new GenericUserPreferenceArray(entry.getValue()));
			}

			// create a data model
			dataModel = new GenericDataModel(preferecesOfUsersFastMap);

			FileOutputStream fos = new FileOutputStream("dataModel.model");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(dataModel);
			System.out.println("DataModel Generated!");
			long elapsedDataTime = System.currentTimeMillis() - beforeData;
			System.out.println("DataModel Generation took at: "
					+ elapsedDataTime);

			oos.close();

		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass() + ": "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	public static ArrayList<Recommendation> executeRecommender(String userID,
			int numberOfRecommendations, int neighbors, int similarityMethod) {
		try {

			long recommendTimeStart = System.currentTimeMillis();
			// System.out.println("Recommendation time start: "+recommendTimeStart);

			ArrayList<Recommendation> result = new ArrayList<Recommendation>();

			// FileInputStream fis = new FileInputStream("dataModel.model");
			FileInputStream fis = new FileInputStream("dataModelLocation.model");
			ObjectInputStream ois = new ObjectInputStream(fis);

			dataModel = (DataModel) ois.readObject();

			ois.close();

			System.out.println("Numero de Usuarios: " + dataModel.getNumUsers()
					+ "Numero de Items: " + dataModel.getNumItems());

			UserSimilarity similarity;
			if (similarityMethod == PEARSON) {
				// Pearson
				similarity = new PearsonCorrelationSimilarity(dataModel);
			} else if (similarityMethod == SPEARMAN) {
				similarity = new SpearmanCorrelationSimilarity(dataModel);
			} else {
				similarity = new EuclideanDistanceSimilarity(dataModel);
			}

			UserNeighborhood neighborhood = new NearestNUserNeighborhood(
					neighbors, similarity, dataModel);

			// Instantiate the recommender

			recommender = new GenericUserBasedRecommender(dataModel,
					neighborhood, similarity);

			// recommenderGBIR = new
			// GenericBooleanPrefItemBasedRecommender(dataModel, new
			// LogLikelihoodSimilarity(dataModel));

			System.out.println("Getting the recommendations...");

			List<RecommendedItem> recommendations = recommendThings(userID,
					numberOfRecommendations);

			if (recommendations.size() == 0)
				System.out.println("No recommendations");

			// List<String> recommendationsBooleanPref =
			// booleanRecommendThings(user.user_id, numberOfRecommendations);

			System.out.println("USER-BASED");
			for (RecommendedItem rec : recommendations) {
				System.out.println("Got recommendations..."
						+ recommendations.size());

				Movie found = Movie.find.byId(rec
						.getItemID());
				if (found != null) {
					System.out.println("Found movie..." + found.title
							+ " " + found.id);

					Recommendation recom = new Recommendation(found,
							rec.getValue());
					result.add(recom);
				}
			}
			System.out.println("All collaborative recommendations added");
			long timeElapsed = System.currentTimeMillis() - recommendTimeStart;
			System.out.println("Time elapsed: " + timeElapsed);
			return result;

		} catch (TasteException e) {
			System.out.println("Exception: " + e.getClass() + ": "
					+ e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Exception: " + e.getClass() + ": "
					+ e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Exception: " + e.getClass() + ": "
					+ e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns up to 10 recommendations for a certain person as a string array.
	 * If less then 10 things are found the array will contain less elements. If
	 * no recommendations are found the array will contain 0 elements.
	 * 
	 * @return a string array with recommendations
	 * @throws TasteException
	 *             If anything goes wrong a TasteException is thrown
	 */
	public static List<RecommendedItem> recommendThings(String user_id,
			int numberOfRecommendations) throws TasteException {
		long t1 = System.currentTimeMillis();
		// List<String> recommendations = new ArrayList<String>();
		List<RecommendedItem> items = null;
		try {
			items = recommender.recommend(thing2long.toLongID(user_id),
					numberOfRecommendations);
			// for (RecommendedItem item : items) {
			// recommendations.add(thing2long.toStringID(item.getItemID()));
			// }
		} catch (TasteException e) {
			throw e;
		}
		System.out.println("\n ATTENTION!! getting geolocation filter took "
				+ (System.currentTimeMillis() - t1) + "ms\n\n");
		return items;
	}

	public static List<String> booleanRecommendThings(String user_id,
			int numberOfRecommendations) throws TasteException {
		List<String> recommendations = new ArrayList<String>();
		try {
			List<RecommendedItem> items = recommenderGBIR.recommend(
					thing2long.toLongID(user_id), numberOfRecommendations);
			for (RecommendedItem item : items) {
				recommendations.add(thing2long.toStringID(item.getItemID()));
			}
		} catch (TasteException e) {
			throw e;
		}
		return recommendations;
	}

	public static CollaborativeRecommender getInstance() {
		if (instance == null) {
			instance = new CollaborativeRecommender();
			// CollaborativeRecommender.generateDataModel();
		}
		return instance;
	}

	public static EvaluationResult evaluate( int neighbors, int relevantUsers,
			int similarityMethod, double trainingPercentage) {
		EvaluationResult er = new EvaluationResult();
		er.precision = 0;
		er.recall = 0;
		er.description = "";
		er.time = 0;
		try {
			String[] uids = EvaluationController
					.findPopularUsers(relevantUsers);

//			generateDataModel();

			UserSimilarity similarity;
			if (similarityMethod == PEARSON) {
				// Pearson
				similarity = new PearsonCorrelationSimilarity(dataModel);
			} else if (similarityMethod == SPEARMAN) {
				similarity = new SpearmanCorrelationSimilarity(dataModel);
			} else {
				similarity = new EuclideanDistanceSimilarity(dataModel);
			}

			UserNeighborhood neighborhood = new NearestNUserNeighborhood(
					neighbors, similarity, dataModel);

			// Instantiate the recommender

			recommender = new GenericUserBasedRecommender(dataModel,
					neighborhood, similarity);
			double tpTotal = 0;
			double esperadoTotal = 0;

			long t1 = 0, averageTime = 0;
			System.out.println("Tam Arreglo: "+uids.length);
			for (int i = 0; i < uids.length && i<relevantUsers; i++) {
				System.out.println(i+"   "+uids[i]);
				if(uids[i]!=null || !uids[i].trim().equals("null")){
					double[] result = recommendEval(thing2long.toLongID(uids[i]),
							neighbors);// result[#esperado][#tp
					// -
					// intersección]
					esperadoTotal += result[0];
					tpTotal += result[1];
				}
			}
			averageTime = (System.currentTimeMillis() - t1) / uids.length;
			er.precision = tpTotal
					/ (MAX_RECOMMENDATIONS * (double)dataModel.getNumUsers());
			er.recall = tpTotal / (esperadoTotal);
			String simString ="";
			if(similarityMethod==EUCLIDEAN){
				simString = "Euclidean";
			}
			else if(similarityMethod==PEARSON){
				simString = "Pearson";
			}
			else if(similarityMethod==SPEARMAN){
				simString = "Spearman";
			}
			er.description = "Recomendador colaborativo: { porcentaje entrenamiento= "
					+ (int) (trainingPercentage * 100)
					+ "%, usuarios revisados="
					+ uids.length
					+ " usuarios relevantes:"
					+relevantUsers
					+ ", similaridad: "+simString+"}";
			er.time = averageTime;
			
		} catch (TasteException e) {
			e.printStackTrace();
		}
		return er;
	}

	private static double[] recommendEval(long l, double neighbors) {
		double[] respuesta = new double[2];
		respuesta[0] = 0;
		respuesta[1] = 0;
		try {
			FastIDSet fs = dataModel.getItemIDsFromUser(l);
			Iterator<Long> iter = fs.iterator();
			while (iter.hasNext()) {
				Long itemId = iter.next();
				respuesta[0] += recommender.estimatePreference(l, itemId);
				respuesta[1] += dataModel.getPreferenceValue(l, itemId);

			}
			return respuesta;
		} catch (TasteException e) {
			e.printStackTrace();
		}
		return respuesta;
	}

	public GenericUserBasedRecommender darRecomendador(DataModel data,
			int neighbors, int similarityMethod) {
		dataModel = data;
		UserSimilarity similarity;
		try {
			if (similarityMethod == PEARSON) {
				// Pearson
				similarity = new PearsonCorrelationSimilarity(dataModel);
			} else if (similarityMethod == SPEARMAN) {
				similarity = new SpearmanCorrelationSimilarity(dataModel);
			} else {
				similarity = new EuclideanDistanceSimilarity(dataModel);
			}
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(
					neighbors, similarity, data);

			return new GenericUserBasedRecommender(data, neighborhood,
					similarity);
		} catch (TasteException e) {
			e.printStackTrace();
		}
		return null;

	}
}
