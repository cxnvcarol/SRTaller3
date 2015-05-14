/**
 * 
 */
package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author juancamiloortiz
 *
 */
@Entity
public class ItemContent extends Model{

	/**
	 * Default Serial Version
	 */
	private static final long serialVersionUID = 1L;
	public long itemlong_id;
    public long feature_id;//category id;
    public float rating;//1 if exists

    public String item_id;

    public ItemContent(String iid,long ilid,long fid,float rat)
    {
        item_id=iid;
        feature_id=fid;
        rating=rat;
        itemlong_id=ilid;
    }
}
