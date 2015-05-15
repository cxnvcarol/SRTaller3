package models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by carol on 14/05/15.
 */
@Entity
public class UserFeatureRating {
    @Id
    public Feature feature;
    public double cumulativeRating;
    private int countRating;
    public UserFeatureRating()
    {
        countRating=0;
    }
    public int getCountRating()
    {
        return countRating;
    }

    public void addRating(double rating) {
        countRating++;
        cumulativeRating+=rating;


    }
}
