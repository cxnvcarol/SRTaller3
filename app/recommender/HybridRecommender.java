package recommender;

import models.EvaluationResult;
import models.Movie;
import models.Recommendation;
import models.User;
import org.apache.mahout.cf.taste.common.TasteException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by carol on 8/04/15.
 */
public class HybridRecommender {

    public static final int RADIO_FILTRO = 25000;
	private static HybridRecommender instance;
    private static CollaborativeRecommender colaborativo;
    private static ContentRecommender contenido;
    public static final int MAX_RECOMMENDATIONS = 20;

    
    public HybridRecommender()
    {
    	colaborativo = CollaborativeRecommender.getInstance();
    	contenido =  ContentRecommender.getInstance();
    }

    public static ArrayList<Recommendation> recommend(User user, long timestamp)
    {
        if(user==null)
        {
            return popularMovies(MAX_RECOMMENDATIONS);
        }

    	//TODO reponderar modelos
    	ArrayList<Recommendation> collabRecs = getCollaborativeRecommendations( user==null?0:user.user_id, timestamp);
        //ArrayList<Recommendation> collabRecs =new ArrayList<>();
    	ArrayList<Recommendation> contentRecs = getContentRecommendations(user);
        
    	ArrayList<Recommendation> finalRecs = new ArrayList<Recommendation>();
    	int ultimaPos=collabRecs.size()+contentRecs.size();
    	
    	//Los llena de nulos para 'reservar' el espacio y poder acceder a las posiciones especificas
    	for( int i = 0 ; i<ultimaPos ; i++)
    	{
    		finalRecs.add(null);
    	}
    	
    	int posIntro;
    	for( int i = 0 ; i < collabRecs.size(); i ++)
    	{
    		Recommendation collab = collabRecs.get(i);
    		
    		boolean termino = false;
    		for( int j = 0 ; j<contentRecs.size() && !termino ; j ++){
    			Recommendation content = contentRecs.get(j);
    			
    			if(content.getMovie()!=null){
	    			if(collab.getMovie().id==content.getMovie().id){
	    				//Promedio de posicion entre las dos listas
	    				posIntro = (i+j)/2;
	    				contentRecs.set(j, null);
	    				boolean intro = introducirAntes(collab, posIntro, finalRecs);
	    				if(!intro){
	    					introducirDespues(collab, posIntro, finalRecs);
	    				}
	    				termino = true;
	    			}
    			}
    		}
    		if(!termino)
    			finalRecs.add(collab);
    	}
    	for(int i= 0 ; i< contentRecs.size(); i++){
    		if(contentRecs.get(i)==null){
    			contentRecs.remove(i);
    			System.out.println("NULL CONTENT RECOMMENDATION REMOVED");
    		}
    	}
    	for(int i= 0 ; i< contentRecs.size(); i++){
    		finalRecs.add(contentRecs.get(i));
    	}
    	for(int i=finalRecs.size()-1 ; i>=0; i--){
    		if(finalRecs.get(i)==null){
    			finalRecs.remove(i);
    			System.out.println("NULL RECOMMENDATION REMOVED");
    		}
    	}
    	
    	for(int i = 0; i< finalRecs.size() ; i++){
    		System.out.println(finalRecs.get(i).movie.title);
    	}
    	return finalRecs;
        //return collabRecs;
    }

    private static ArrayList<Recommendation> popularMovies(int maxRecommendations) {
        ArrayList<Recommendation> returned=new ArrayList<>();
        List<Movie> listMovies = Movie.find.all();
        Movie[] allMovies = listMovies.toArray(new Movie[listMovies.size()]);
        Arrays.sort(allMovies);

        for (int i = Math.max(0,(allMovies.length-maxRecommendations)); i < allMovies.length; i++) {
            returned.add(new Recommendation(allMovies[i],allMovies[i].average_rating));
        }

        return returned;
    }


    private static boolean introducirAntes(Recommendation rec, int posIntro, ArrayList<Recommendation> finalRecs) {
		boolean termino = false;
		boolean respuesta = false;
		
		if(posIntro<0)
			respuesta = false;
		else if(finalRecs.get(posIntro)==null){
			finalRecs.set(posIntro, rec);
			System.out.println("Added Antes: "+rec.movie.title+" en "+posIntro);
			respuesta = true;
		}
		else{
			termino = introducirAntes(finalRecs.get(posIntro), posIntro-1, finalRecs);
			if(termino){
				finalRecs.set(posIntro, rec);
				System.out.println("Added Antes 2: "+rec.movie.title+" en "+posIntro);
				respuesta = true;
			}
			else
				respuesta = false;
		}
    	return respuesta;
	}
    
    private static boolean introducirDespues(Recommendation rec, int posIntro, ArrayList<Recommendation> finalRecs) {
		try{
			finalRecs.add(posIntro, rec);
			System.out.println("Added Despues: "+rec.movie.title+" en "+posIntro);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private static ArrayList<Recommendation> getContentRecommendations(User user) {
		ArrayList<Recommendation> returned = new ArrayList<Recommendation>();
//    	

        //TODO
        returned=contenido.recommend(user);
    	return returned;
	}

	private static ArrayList<Recommendation> getCollaborativeRecommendations(long user_id, long timestamp) {
        //TODO
		int neighbors = 10;
		int similarityMethod = CollaborativeRecommender.EUCLIDEAN;

        if(user_id==0)
            return new ArrayList<Recommendation>();
        //TODO timestamp
		return colaborativo.executeRecommender(user_id, (int)CollaborativeRecommender.MAX_RECOMMENDATIONS, neighbors, similarityMethod, 100000000);
	}

	public static EvaluationResult evaluate (double radioLoc,String hour, double trainingPercentage,int evalMethod)
    {
        //TODO
        return new EvaluationResult();
    }

    public static HybridRecommender getInstance() {
        if(instance==null)
            instance=new HybridRecommender();
        return instance;
    }


}
