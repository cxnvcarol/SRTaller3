//package evaluator;
//
//import java.util.Iterator;
//
//import org.apache.mahout.cf.taste.common.TasteException;
//import org.apache.mahout.cf.taste.eval.DataModelBuilder;
//import org.apache.mahout.cf.taste.eval.IRStatistics;
//import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
//import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
//import org.apache.mahout.cf.taste.impl.common.FastIDSet;
//import org.apache.mahout.cf.taste.impl.eval.IRStatisticsImpl;
//import org.apache.mahout.cf.taste.model.DataModel;
//import org.apache.mahout.cf.taste.model.PreferenceArray;
//import org.apache.mahout.cf.taste.recommender.IDRescorer;
//import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
//
//import controllers.EvaluationController;
//
//import recommender.CollaborativeRecommender;
//import recommender.EvaluationResult;
//
//public class HandMadeEvaluator {
//	public int numItems;
//	public double precision;
//	public double recall;
//	private DataModel dataModel;
//	private CollaborativeRecommender collabRec;
//
//	public HandMadeEvaluator(DataModel dataModelP, double neighbors,
//			double porcentajeEval, int maxPrefs, int relevantUsers) {
//		String[] uids = EvaluationController.findPopularUsers(relevantUsers);
//
//		int tpTotal = 0;
//		int esperadoTotal = 0;
//
//		long t1 = 0, averageTime = 0;
//		for (int i = 0; i < uids.length; i++) {
//			int[] result = recommendEval(uids[i], neighbors);// result[#esperado][#tp
//																// -
//																// intersección]
//			esperadoTotal += result[0];
//			tpTotal += result[1];
//		}
//		dataModel = dataModelP;
//		averageTime = (System.currentTimeMillis() - t1) / uids.length;
//		EvaluationResult er = new EvaluationResult();
//		er.precision = tpTotal / (maxRecommendations * totalUsers);
//		er.recall = tpTotal / (esperadoTotal);
//		er.description = "Recomendador de contenido: { porcentaje entrenamiento= "
//				+ (int) (trainingPercentage * 100)
//				+ "%,negocios revisados="
//				+ maxBusinessNeighb + ",filtra por radio=" + radio + "}";
//		er.time = averageTime;
//
//	}
//
//	private int[] recommendEval(long user_id, int neighbors) {
//		
//	}
//
//}
