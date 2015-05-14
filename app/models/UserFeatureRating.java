package models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by carol on 14/05/15.
 */
@Entity
public class UserFeatureRating {
    public User user;
    @Id
    public Feature feature;
    public double rating;
}
