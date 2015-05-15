package recommender;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import models.*;

import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;

public class DataLoader {



	public static final String rutaUsuarios = "./data/yelp_academic_dataset_user.json";
	public static final String rutaNegocios = "./data/yelp_academic_dataset_business.json";
    private static final String MOVIES_PATH = "./data/movies.csv";
    private static final String RATINGS_PATH = "./data/ratings.csv";

    private static String[] genres={"Action","Adventure","Animation","Children's","Comedy","Crime","Documentary","Drama","Fantasy","Film-Noir","Horror","Musical","Mystery","Romance","Sci-Fi","Thriller","War","Western"};
	//user,movie,rating,timestamp
    public static final String rutaArchivoDataModel = "./data/ratings.csv";

    public static void main(String[] args0) {
		System.out.println("Carga Completa");
	}

    public static void loadAllDB() throws IOException {
        loadFeatures();
        loadMovies();
        loadRatings();//load table with movies and ratings, and feed table with features of user
        generateContentModel();
    }

    private static void loadRatings() throws IOException {
        if(Rating.find.all().size()>0)
        {
            return;
        }
        System.out.println("loading ratings");
        //userId,movieId,rating,timestamp
        BufferedReader br=new BufferedReader(new FileReader(RATINGS_PATH));
        String ln=br.readLine();
        while((ln=br.readLine())!=null) {
            String[] partes=ln.split(",");
            Rating r=new Rating();
            r.userid=Long.parseLong(partes[0]);
            r.movieid=Long.parseLong(partes[1]);
            r.rating=Double.parseDouble(partes[2]);
            r.timestamp=Long.parseLong(partes[3]);
            r.save();
            
            //load user:
            User found=User.find.byId(r.userid);
            if(found==null)
            {
                found=new User();
                found.user_id=r.userid;
                found.save();
            }

        }
    }

    private static void loadFeatures()
    {
        if(Feature.find.all().size()>0)
            return;
        System.out.println("loading features");
        loadGenres();
        //loadDirectors();//etc,etc...


    }

    private static void loadGenres() {
        String tipoAttr="genre";
        for (int i = 0; i < genres.length; i++) {
            Feature f=new Feature();
            f.setID(genres[i].hashCode());
            f.setName(genres[i]);
            f.type="genre";
            f.save();
        }
    }

    private static void loadMovies() throws IOException {
        if(Movie.find.all().size()>0)
        {
            return;
        }
        System.out.println("loading movies");
        BufferedReader br=new BufferedReader(new FileReader(MOVIES_PATH));
        String ln=br.readLine();
        while((ln=br.readLine())!=null)
        {
            String[] partes=ln.split(",");//id,nombre,géneros (sep x |)
            long idMov=Long.parseLong(partes[0]);
            String name,genresM;
            if(partes.length>3)
            {
                name="";
                for (int i = 1; i <partes.length-1 ; i++) {
                    name+=","+partes[i];
                }
                name=name.substring(1);
                genresM=partes[partes.length-1];

            }
            else{
                name=partes[1];
                genresM=partes[2];
            }
            String[] partesGenres=genresM.split(Pattern.quote("|"));
            Movie newM=new Movie();
            newM.id=idMov;
            newM.title=name;

            //carga géneros:
            for (String g:partesGenres)
            {
                Feature f=Feature.find.byId((long) g.hashCode());
                if(f!=null)
                {
                    newM.features.add(f);
                }
            }
            newM.save();
        }
        br.close();
    }

    private static void generateContentModel() {
        //TODO fixme

        System.out.println("generating content model");

    }
}
