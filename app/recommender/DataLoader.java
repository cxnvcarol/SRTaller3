package recommender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import models.*;

import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataLoader {



	public static final String rutaUsuarios = "./data/yelp_academic_dataset_user.json";
	public static final String rutaNegocios = "./data/yelp_academic_dataset_business.json";

    private static String[] genres={"Action","Adventure","Animation","Children's","Comedy","Crime","Documentary","Drama","Fantasy","Film-Noir","Horror","Musical","Mystery","Romance","Sci-Fi","Thriller","War","Western"};
	//user,movie,rating,timestamp
    public static final String rutaArchivoDataModel = "./data/ratings.csv";

    public static void main(String[] args0) {
		System.out.println("Carga Completa");
	}

    public static void loadAllDB()
    {
        loadFeatures();
        loadUsers();
        loadMovies();
        loadRatings();
        generateContentModel();
    }

    private static void loadRatings() {

    }

    private static void loadFeatures()
    {
        loadGenres();
        //loadDirectors();//etc,etc...
    }

    private static void loadGenres() {
        String tipoAttr="genre";
        for (int i = 0; i < genres.length; i++) {
            Feature f=new Feature();
            f.setID(genres[i].hashCode());
            f.setName(genres[i]);
            f.save();
        }
    }

    private static void loadMovies() {
    }

    private static void generateContentModel() {
        //TODO
        try {
            List<SqlRow> q = Ebean.createSqlQuery("select count(*) as count from item_content").findList();
            SqlQuery qtemp = Ebean
                    .createSqlQuery(" select * from businesscategories");
            qtemp.setMaxRows(200000);

            List<SqlRow> q2 = qtemp
                    .findList();
            System.out.println("\nATTENTION... THERE ARE "+q2.size()+" ROWS IN BUSINESSCATEGORIES....\n");

            if(q.get(0).getInteger("count")>0)
                return;
        }
        catch(Exception e)
        {
            //does not exist
        }

        SqlQuery sqlQuery = Ebean.createSqlQuery(" select * from businesscategories");
        sqlQuery.setMaxRows(3000000);

        List<SqlRow> q2 = sqlQuery
                .findList();

        MemoryIDMigrator thing2long = new MemoryIDMigrator();

        for (SqlRow row:q2)
        {
            try{
                String sid=row.getString("business_business_id");
                ItemContent ic=new ItemContent(sid,thing2long.toLongID(sid),row.getInteger("category_category_id"),1);
                ic.save();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }
	private static void loadUsers() {
        //usuario.save();

        //TODO load userid and load ratedmovies (use rating value??)
	}
}
