package recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import models.EvaluationResult;
import models.Movie;
import models.Recommendation;

import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.avaje.ebean.Ebean;

import controllers.EvaluationController;
import play.Logger;
import play.db.DB;

public class CollaborativeRecommender {
    public static final int PEARSON = 1;
    public static final int SPEARMAN = 2;

    /**
     * The name of the file used for loading.
     */
    public static final int EUCLIDEAN = 3;
    public static final long user_id_test = 5;
    public static final int MAX_RECOMMENDATIONS = 20;
    //private static final String RATINGS_PATH = "data/ratings.csv";
    private static final String RATINGS_PATH = "data/ratings.dat";
    private static final String RATINGS_PATH_OUT = "data/ratings_out.dat";
    /**
     * Recommender which will be hold by this session bean.
     */
    private static ItemBasedRecommender recommender = null;
    /**
     * An MemoryIDMigrator which is able to create for every string a long
     * representation. Further it can store the string which were put in and it
     * is possible to do the mapping back.
     */
    private static MemoryIDMigrator thing2long = new MemoryIDMigrator();
    /**
     * A data model which is needed for the recommender implementation. It
     * provides a standardized interface for using the recommender. The data
     * model can be become quite memory consuming. In our case it will be around
     * 2 mb.
     */
    private static  DataModel dataModel = null;
    private static GenericBooleanPrefItemBasedRecommender recommenderGBIR;
    private static CollaborativeRecommender instance;

    public static void main(String[] args) {
       // generateDataModel(968302205, 0);
//        EvaluationResult resCollab = CollaborativeRecommender.evaluate(50, 100, CollaborativeRecommender.EUCLIDEAN, 0.5);
//        System.out.println(resCollab.description);
//        System.out.println("Precision: " + resCollab.precision);
//        System.out.println("Recall: " + resCollab.recall);
//        System.out.println("Time: " + resCollab.time);
    }

    private static boolean isContainedIn(long business_id,
                                         ArrayList<Movie> businesses) {
        boolean termino = false;
        for (int i = 0; i < businesses.size() && !termino; i++) {
            if (businesses.get(i).id == business_id) {
                return true;
            }
        }
        return false;
    }

    public static void generateDataModel(long userTsmp) {
        long beforeData = System.currentTimeMillis();
        //TODO generate and use a view table filtering the timestamp of the user
        //dataModel=new MySQLJDBCDataModel(DB.getDataSource(),"ratings_user_view","","","","");
        //dataModel = new MySQLJDBCDataModel(DB.getDataSource(), "rating", "userid", "movieid", "rating", "timestamp");
        FileDataModel dataModel2 = null;
        try {
            //dataModel=new FileDataModel(new File(RATINGS_PATH),",");
        	//dataModel2=new FileDataModel(new File(RATINGS_PATH),"::");
        	
            BufferedReader br = new BufferedReader(new FileReader(new File(RATINGS_PATH)));
            PrintWriter pw = new PrintWriter(new File(RATINGS_PATH_OUT));
            String ln;
            while((ln=br.readLine())!=null){
            	String[] tags = ln.split("::");
            	
            	long userID = Long.parseLong(tags[0]);
            	            	
            	long itemID = Long.parseLong(tags[1]);
            	double rating = Double.parseDouble(tags[2]);
            	long timeStamp = Long.parseLong(tags[3]);
            	
            	Timestamp ts = new Timestamp(timeStamp*1000L);
            	Timestamp compare = new Timestamp(userTsmp*1000L);
            	if(ts.after(compare))
            	{
            		pw.println(userID+"::"+itemID+"::"+rating+"::"+timeStamp);
            		//dataModel.removePreference(userID, itemID);
            	}
            }
            pw.close();
            dataModel=new FileDataModel(new File(RATINGS_PATH_OUT),"::");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            int nitems = dataModel.getNumItems();
            int nusers = dataModel.getNumUsers();

            System.out.println("num items: "+nitems);
            System.out.println("num users: "+nusers);

            System.out.println("Original model_____");

        } catch (TasteException e) {
            e.printStackTrace();
        }
        System.out.println("Model generation was " + (System.currentTimeMillis() - beforeData) + " ms");
    }

    public static ArrayList<Recommendation> executeRecommender(long userID,int numberOfRecommendations, int neighbors, int similarityMethod, long timestamp) {
        try {
            generateDataModel(timestamp);
            long recommendTimeStart = System.currentTimeMillis();
            // System.out.println("Recommendation time start: "+recommendTimeStart);

            ArrayList<Recommendation> result = new ArrayList<Recommendation>();

            System.out.println("Numero de Usuarios: " + dataModel.getNumUsers()
                    + "Numero de Items: " + dataModel.getNumItems());

            UserSimilarity similarity;
            ItemSimilarity itemsimil=new PearsonCorrelationSimilarity(dataModel);
            if (similarityMethod == PEARSON) {
                // Pearson
                similarity = new PearsonCorrelationSimilarity(dataModel);
                itemsimil=new PearsonCorrelationSimilarity(dataModel);
            } else if (similarityMethod == SPEARMAN) {
                similarity = new SpearmanCorrelationSimilarity(dataModel);
                similarity = new SpearmanCorrelationSimilarity(dataModel);
            } else {
                similarity = new EuclideanDistanceSimilarity(dataModel);
                similarity = new SpearmanCorrelationSimilarity(dataModel);
            }


            recommender = new GenericItemBasedRecommender(dataModel,itemsimil);

            //UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighbors, similarity, dataModel);

            // Instantiate the recommender

            //recommender = new GenericUserBasedRecommender(dataModel,neighborhood, similarity);

            System.out.println("Getting the recommendations...");

            List<RecommendedItem> recommendations = recommendThings(userID,
                    numberOfRecommendations);

            if (recommendations.size() == 0)
                System.out.println("No recommendations");


            System.out.println("ITEM-BASED");
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
                }else
                	System.out.println("Movie not found: "+rec.getItemID());
            }
            System.out.println("All collaborative recommendations added");
            long timeElapsed = System.currentTimeMillis() - recommendTimeStart;
            System.out.println("Time elapsed: " + timeElapsed);
            return result;

        } catch (TasteException e) {
            System.out.println("Exception: " + e.getClass() + ": "
                    + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Returns up to 10 recommendations for a certain person as a string array.
     * If less then 10 things are found the array will contain less elements. If
     * no recommendations are found the array will contain 0 elements.
     *
     * @return a string array with recommendations
     * @throws TasteException If anything goes wrong a TasteException is thrown
     */
    public static List<RecommendedItem> recommendThings(long user_id,
                                                        int numberOfRecommendations) throws TasteException {
        long t1 = System.currentTimeMillis();
        // List<String> recommendations = new ArrayList<String>();
        List<RecommendedItem> items = null;
        try {
            items = recommender.recommend(user_id,
                    numberOfRecommendations);
            // for (RecommendedItem item : items) {
            // recommendations.add(thing2long.toStringID(item.getItemID()));
            // }
        }
        catch(TasteException e){
        	
        }
        System.out.println("\n ATTENTION!! getting collaborative recommendations "+ (System.currentTimeMillis() - t1) + "ms\n\n");
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
        }
        return instance;
    }

    public static EvaluationResult evaluate(int neighbors, int relevantUsers,
                                            int similarityMethod, double trainingPercentage) {
        //TODO Check the timestamp implication here
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
            ItemSimilarity itemsimil=new PearsonCorrelationSimilarity(dataModel);
            if (similarityMethod == PEARSON) {
                // Pearson
                similarity = new PearsonCorrelationSimilarity(dataModel);
                itemsimil=new PearsonCorrelationSimilarity(dataModel);
            } else if (similarityMethod == SPEARMAN) {
                similarity = new SpearmanCorrelationSimilarity(dataModel);
                similarity = new SpearmanCorrelationSimilarity(dataModel);
            } else {
                similarity = new EuclideanDistanceSimilarity(dataModel);
                similarity = new SpearmanCorrelationSimilarity(dataModel);
            }


            recommender = new GenericItemBasedRecommender(dataModel,itemsimil);

            //UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighbors, similarity, dataModel);

            // Instantiate the recommender

            //recommender = new GenericUserBasedRecommender(dataModel,neighborhood, similarity);

            double tpTotal = 0;
            double esperadoTotal = 0;

            long t1 = 0, averageTime = 0;
            System.out.println("Tam Arreglo: " + uids.length);
            for (int i = 0; i < uids.length && i < relevantUsers; i++) {
                System.out.println(i + "   " + uids[i]);
                if (uids[i] != null || !uids[i].trim().equals("null")) {
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
                    / (MAX_RECOMMENDATIONS * (double) dataModel.getNumUsers());
            er.recall = tpTotal / (esperadoTotal);
            String simString = "";
            if (similarityMethod == EUCLIDEAN) {
                simString = "Euclidean";
            } else if (similarityMethod == PEARSON) {
                simString = "Pearson";
            } else if (similarityMethod == SPEARMAN) {
                simString = "Spearman";
            }
            er.description = "Recomendador colaborativo: { porcentaje entrenamiento= "
                    + (int) (trainingPercentage * 100)
                    + "%, usuarios revisados="
                    + uids.length
                    + " usuarios relevantes:"
                    + relevantUsers
                    + ", similaridad: " + simString + "}";
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

    public void filtrarTiempoGiovanni(String usuario, String añoDesde) throws IOException, ParseException, NumberFormatException, TasteException {
        BufferedReader readerNegocios = new BufferedReader(new FileReader(new File("./data/ratings.csv")));
        String tempStringNegocios = null;
        while ((tempStringNegocios = readerNegocios.readLine()) != null) {
            String[] tag = tempStringNegocios.split(",");
            if (tag[0].equalsIgnoreCase(usuario)) {
                int time = Integer.parseInt(tag[3]);
                Date date = new Date(time * 1000L);
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(date);
                System.out.println("Converted UTC TIME (using Format method) : " + dateString);
                Date desde = new SimpleDateFormat("dd/MM/yyyy").parse(añoDesde);
                if (date.before(desde)) {

                    dataModel.removePreference(Long.parseLong(usuario), Long.parseLong(tag[1]));
                }
            }
        }
    }
}
