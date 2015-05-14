package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;

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


    public static Finder<Long, Movie> find = new Finder<Long, Movie>(
            Long.class, Movie.class
    );

}
