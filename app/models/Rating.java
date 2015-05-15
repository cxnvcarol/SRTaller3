package models;

import play.db.ebean.Model;

import javax.persistence.Entity;

/**
 * Created by carol on 15/05/15.
 */
@Entity
public class Rating extends Model{
    public long userid;
    public long movieid;
    public double rating;
    public long timestamp;



    public static Finder<Long, Rating> find = new Finder<Long, Rating>(
            Long.class, Rating.class
    );
}
