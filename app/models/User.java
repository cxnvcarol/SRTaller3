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
	public String user_id;
	public String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "userfeatures")
    public List<UserFeatureRating> features;


    public void updateFeatures() {
        if(features==null||features.isEmpty())
        {
            features=new ArrayList<UserFeatureRating>();

            List<SqlRow> q = Ebean
                    .createSqlQuery(" select distinct category_category_id as cid from review join businesscategories on businesscategories.business_business_id = review.business_id where user_id=\""+user_id+"\"")
                    .findList();
            for (SqlRow row:q)
            {
                try{
                    //fixme if not featureid found add it
                    //fixme then sum the rating for this movie
                    //features.add(Feature.finder.byId(row.getLong("cid")));
                }
                catch(Exception ex){}

            }
            this.update();
        }

    }

    public String[] getFeaturesStr() {

        updateFeatures();
        String ansa="";
        for (UserFeatureRating c:features)
        {
            ansa+=","+c.feature.getName();
        }
        return ansa.length()==0?new String[0]:ansa.substring(1).split(",");
    }
}
