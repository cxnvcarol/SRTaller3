package evaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.classifier.ConfusionMatrix;

import controllers.EvaluationController;

import recommender.CollaborativeRecommender;

public class CollaborativeEvaluator {

	protected static final int neighbourhoodSize = 100;
	public static RecommenderEvaluator evaluador1;
	public static RecommenderEvaluator evaluador2 = new AverageAbsoluteDifferenceRecommenderEvaluator();
	private static String rutaRatings = "./data/ratings.dat";

	private static DataModel dataModel;
	public static ConfusionMatrix cm;
	/**
	 * An MemoryIDMigrator which is able to create for every string a long
	 * representation. Further it can store the string which were put in and it
	 * is possible to do the mapping back.
	 */
	private static MemoryIDMigrator thing2long = new MemoryIDMigrator();

	public static void main(String[] args) {
		int minTimestamp = 956703932;
		int lowTimestamp = 964473816;
		int avgtimestamp = 972243701;
		int highTimestamp = 1009349145;
		
		System.out.println("Min Timestamp:" + minTimestamp);
		EvaluationController.evaluateRecommenders(minTimestamp);
		System.out.println("Low Timestamp:" + lowTimestamp);
		EvaluationController.evaluateRecommenders(lowTimestamp);
		System.out.println("Avg Timestamp:" + avgtimestamp);
		EvaluationController.evaluateRecommenders(avgtimestamp);
		System.out.println("High Timestamp: " + highTimestamp);
		EvaluationController.evaluateRecommenders(highTimestamp);
	}

	private static void createDataModel() 
	{
		dataModel = CollaborativeRecommender.dataModel;
//			dataModel.get
//			// create a file out of the resource
//			File data = new File(rutaRatings);
//
//			// create a map for saving the preferences (likes) for
//			// a certain person
//			Map<Long, List<Preference>> preferecesOfUsers = new HashMap<Long, List<Preference>>();
//
//			// Read File
//			BufferedReader br = new BufferedReader(new FileReader(data));
//			PrintWriter pw = new PrintWriter(new File(translatedData));
//
//			String line;
//
//			// go through every line
//			while ((line = br.readLine()) != null) {
//				String[] parts = line.split(";");
//				String person = parts[0].trim();
//				String likeName = parts[1].trim();
//				float preference = Float.parseFloat(parts[2].trim());
//
//				// create a long from the person name
//				long userLong = thing2long.toLongID(person);
//
//				// store the mapping for the user
//				thing2long.storeMapping(userLong, person);
//
//				// create a long from the like name
//				long itemLong = thing2long.toLongID(likeName);
//
//				// store the mapping for the item
//				thing2long.storeMapping(itemLong, likeName);
//				pw.println(userLong+","+itemLong+","+preference);
//
////				List<Preference> userPrefList;
////
////				// if we already have a userPrefList use it
////				// otherwise create a new one.
////				if ((userPrefList = preferecesOfUsers.get(userLong)) == null) {
////					userPrefList = new ArrayList<Preference>();
////					preferecesOfUsers.put(userLong, userPrefList);
////				}
////				// add the like that we just found to this user
////				userPrefList.add(new GenericPreference(userLong, itemLong,preference));
////
////				// System.out.println("Adding " + person + "(" + userLong
////				// + ") to " + likeName + "(" + itemLong
////				// + ") with preference " + preference);
//			}
//
//			// create the corresponding mahout data structure from the map
////			FastByIDMap<PreferenceArray> preferecesOfUsersFastMap = new FastByIDMap<PreferenceArray>();
////			for (Entry<Long, List<Preference>> entry : preferecesOfUsers.entrySet()) {
////				preferecesOfUsersFastMap.put(entry.getKey(),new GenericUserPreferenceArray(entry.getValue()));
////			}
//			pw.close();
//			// create a data model
////			FileDataModel.determineDelimiter(";");
//			dataModel = new FileDataModel(new File(ratings));
//			System.out.println("DataModel Created");
//			System.out.println("Numero de Usuarios: " + dataModel.getNumUsers()
//					+ "Numero de Items: " + dataModel.getNumItems());
	}

	public static void evaluateUserBased(DataModel dataModel) {
		try {
			System.out.println("Going to Evaluate the recommender...");
			RecommenderBuilder userSimRecBuilder = new MyRecommenderBuilder();

			RecommenderEvaluator evaluador1 = new RMSRecommenderEvaluator();
			RecommenderEvaluator evaluador2 = new AverageAbsoluteDifferenceRecommenderEvaluator();

			System.out.println("Before Evaluation...");

//			double userSimEvaluationScore = evaluador1.evaluate(
//					userSimRecBuilder, null, dataModel, 0.2, 0.5);
//			System.out.println("Collaborative Filtering RMS Evaluator Result: "
//					+ userSimEvaluationScore);
//
//			double userSimEval2 = evaluador2.evaluate(userSimRecBuilder, null,
//					dataModel, 0.2, 0.5);
//			System.out
//					.println("Collaborative Filtering Average Distance Evaluator Result: "
//							+ userSimEval2);

			System.out.println("Stats Evaluation ");
			RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
			IRStatistics stats = statsEvaluator.evaluate(userSimRecBuilder,
					null, dataModel, null, 5,
					0, 1.0);

			System.out.println(stats.getPrecision());
			System.out.println(stats.getRecall());
		} catch (TasteException e) {
			System.out.println("Exception: " + e.getClass() + ": "
					+ e.getMessage());
			e.printStackTrace();
		}

	}
}
