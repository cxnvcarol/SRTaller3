package models;

import com.avaje.ebean.Ebean;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carol on 14/05/15.
 */
@Entity
public class Movie extends Model implements  Comparable<Movie>{
    @Id
    public long id;
    public long imdb_id;
    public String dbpedia_uri;
    public String title;
    public int num_ratings;
    public double average_rating;
    public static final int POPULARITY_COMPARISION = 0;
    public static final int RATING_COMPARISION = 1;
    public static int compare_method=POPULARITY_COMPARISION;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Feature> features;

    public Movie()
    {
        num_ratings=0;
        average_rating=0;
        if(features==null)
            features=new ArrayList<>();
    }

    public static void setCompareMethod(int newM)
    {
        compare_method=newM;
    }


    public static Finder<Long, Movie> find = new Finder<Long, Movie>(
            Long.class, Movie.class
    );

    public void addRating(double rating) {
        average_rating=average_rating*num_ratings+rating;
        num_ratings++;
        average_rating/=num_ratings;
    }

    @Override
    public int compareTo(Movie movie) {
        if(compare_method==POPULARITY_COMPARISION)
        {
            if(num_ratings>movie.num_ratings)
                return 1;
            else if(num_ratings==movie.num_ratings)
                return 0;
            else return -1;
        }
        else if(compare_method==RATING_COMPARISION)
        {
            if(average_rating>movie.average_rating)
                return 1;
            else if(average_rating==movie.average_rating)
                return 0;
            else return -1;

        }
        else return 0;
    }

    public List<Feature> getFeatures(String typeFeature) {
        ArrayList<Feature> r=new ArrayList<>();
        for (Feature f:features)
        {
            if(f.type.equals(typeFeature))
                r.add(f);
        }
        return r;
    }
}
