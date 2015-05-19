/**
 *
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Filter;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import controllers.Application;
import play.db.ebean.Model;

/**
 * @author juancamiloortiz
 *
 */
@EntityConcurrencyMode(ConcurrencyMode.NONE)
@Entity
public class User extends Model{

    /**
     * Default Serial Version ID
     */

    @Id
    public long user_id;


    @Transient
    public List<UserFeatureRating> features;

    public User()
    {
        if(features==null)
            features=new ArrayList<>();
    }

    public static Finder<Long, User> find = new Finder<Long, User>(
            Long.class, User.class
    );

    public List<UserFeatureRating> getFeatures()
    {
        if(features.size()==0)
            updateFeatures();
        return features;
    }


    public void updateFeatures()
    {
        features=new ArrayList<>();
        List<Rating> myratings =
                Ebean.find(Rating.class)
                        .where().eq("userid", user_id)
                        //.where().ge("timestamp", Application.timestamp)//TODO
                        .findList();

        for(Rating r:myratings)
        {
            Movie m=Ebean.find(Movie.class).fetch("features").setId(r.movieid).findUnique();

            if(m!=null)
            {

                List<Feature> movFeatures = m.features;
                for(Feature f:movFeatures)
                {
                    setOrUpdateFeature(f, r.rating);
                }
            }
        }
    }

    public void setOrUpdateFeature(Feature f,double rating) {

        ////for each feature review if exists in the user, if not add it
        //add the rating to the added feature.
        boolean encontro=false;
        UserFeatureRating fuser=null;
        for (int i = 0; i < features.size() && !encontro; i++) {
            fuser=features.get(i);
            if(fuser.feature.id==f.id)
            {
                encontro=true;
            }
        }
        if(!encontro)
        {
            fuser=new UserFeatureRating();
            fuser.feature=f;
            features.add(fuser);
        }
        fuser.addRating(rating);
    }

    public List<UserFeatureRating> getFeatures(String typeFeature) {
        ArrayList<UserFeatureRating> r=new ArrayList<>();
        for (UserFeatureRating f:features)
        {
            if(f.feature.type.equals(typeFeature))
                r.add(f);
        }
        return r;

    }
}
