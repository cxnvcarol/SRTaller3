package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carol on 14/05/15.
 */
@Entity
public class Movie extends Model{
    @Id
    public long id;
    public long imdb_id;
    public String dbpedia_uri;
    public String title;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Feature> features;

    public Movie()
    {
        if(features==null)
            features=new ArrayList<>();
    }


    public static Finder<Long, Movie> find = new Finder<Long, Movie>(
            Long.class, Movie.class
    );

}
