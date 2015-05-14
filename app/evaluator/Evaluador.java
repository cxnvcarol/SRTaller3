package evaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.RandomUtils;
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
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class Evaluador {

	private static MemoryIDMigrator thing2long = new MemoryIDMigrator();
	
	public static void main(String[] args) throws IOException, TasteException {
		File data = new File("./data/reviews_info.txt");

		// create a map for saving the preferences (likes) for
		// a certain person
		Map<Long,List<Preference>> preferecesOfUsers = new HashMap<Long,List<Preference>>();

		// Read File
		BufferedReader br = new BufferedReader(new FileReader(data));

		String line;
		int counter = 0;
		// go through every line
		while ((line = br.readLine()) != null && counter<500000) {
			counter++;
			String[] parts = line.split(";");
			String person = parts[0];
			String likeName = parts[1];
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
			if((userPrefList = preferecesOfUsers.get(userLong)) == null) {
				userPrefList = new ArrayList<Preference>();
				preferecesOfUsers.put(userLong, userPrefList);
			}
			// add the like that we just found to this user
			userPrefList.add(new GenericPreference(userLong, itemLong, preference));
		}

		// create the corresponding mahout data structure from the map
		FastByIDMap<PreferenceArray> preferecesOfUsersFastMap = new FastByIDMap<PreferenceArray>();
		for(Entry<Long, List<Preference>> entry : preferecesOfUsers.entrySet()) {
			preferecesOfUsersFastMap.put(entry.getKey(), new GenericUserPreferenceArray(entry.getValue()));
		}
		
		org.apache.mahout.cf.taste.model.DataModel  mod = new GenericDataModel(preferecesOfUsersFastMap);
		
		RecommenderEvaluator evaluatorMahout1 = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderBuilder builder = new MyRecommenderBuilder();
		
		System.out.println("Evaluating...");
		//El evaluador se construye con 90% de datos de prueba y 10% de datos de entrenamiento
		//El resultado obtenido, entre más bajo, mejor. 

		//First test case
		double trainingP=0.5;
		double testP = 1.0;
		
		double result = evaluatorMahout1.evaluate(builder, null, mod, trainingP ,testP );
		System.out.println("Collaborative Filtering Average Distance Evaluator Result: "+result + " training%:"+trainingP+ " test%:"+testP +" Euclidean with 100 neighbors");
		
		RecommenderEvaluator evaluatorMahout2 = new  RMSRecommenderEvaluator();
		double resulte = evaluatorMahout2.evaluate(builder, null, mod, trainingP, testP);
		System.out.println("Collaborative Filtering RMS Evaluator Result: "+resulte+ " training%:"+trainingP+ " test%:"+testP +" Euclidean with 100 neighbors");
				
		//Second test case
		trainingP = 0.4;
		testP = 0.9;
		double result2 = evaluatorMahout1.evaluate(builder, null, mod, trainingP ,testP );
		System.out.println("Collaborative Filtering Average Distance Evaluator Result: "+result2 + " training%:"+trainingP+ " test%:"+testP +" Euclidean with 100 neighbors");
		
		double resulte2 = evaluatorMahout2.evaluate(builder, null, mod, trainingP, testP);
		System.out.println("Collaborative Filtering RMS Evaluator Result: "+resulte2+ " training%:"+trainingP+ " test%:"+testP +" Euclidean with 100 neighbors");
		
		//Second test case
		trainingP = 0.4;
		testP = 0.9;
		double result3 = evaluatorMahout1.evaluate(builder, null, mod,
				trainingP, testP);
		System.out
				.println("Collaborative Filtering Average Distance Evaluator Result: "
						+ result3
						+ " training%:"
						+ trainingP
						+ " test%:"
						+ testP
						+ " Euclidean with 100 neighbors");

		double resulte3 = evaluatorMahout2.evaluate(builder, null, mod,
				trainingP, testP);
		System.out.println("Collaborative Filtering RMS Evaluator Result: "+resulte3+ " training%:"+trainingP+ " test%:"+testP +" Euclidean with 100 neighbors");
				
		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator ();
		IRStatistics  stats = null;
		try{
		stats = evaluator.evaluate(builder, null, mod, null, 2,3,0.5);
		}
		catch(TasteException e){
			e.printStackTrace();
		}
		System.out.println(stats.getPrecision());
		System.out.println(stats.getRecall());
		
	}

}

