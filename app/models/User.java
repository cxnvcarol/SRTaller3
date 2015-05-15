/**
 * 
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
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
	private static final long serialVersionUID = 1L;

    @Id
	public long user_id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "userfeatures")
    public List<UserFeatureRating> features;

    public User()
    {
        if(features==null)
            features=new ArrayList<>();
    }

    public static Finder<Long, User> find = new Finder<Long, User>(
            Long.class, User.class
    );

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
}
