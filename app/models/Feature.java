/**
 * 
 */
package models;

import javax.persistence.*;

import play.db.ebean.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juancamiloortiz
 *
 */
@Entity
public class Feature extends Model{

	/**
	 * Default Serial Version
	 */
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
	public long id;
	public String name;
    public String type;

    public static Finder<Long, Feature> find = new Finder<Long, Feature>(
            Long.class, Feature.class
    );

	public Feature()
	{
		
	}
	
	public void setName(String nameP)
	{
		this.name = nameP;
	}
	public void setID(long idP)
	{
		this.id = idP;
	}
	
	public long getID()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}


    public static List<String> getAll()
    {

        List<Feature> r = Feature.find.select("name").findList();

        List<String> strings= new ArrayList<String>();
        for (Feature c:r)
        {
            strings.add(c.getName());
        }
        return strings;
    }
}
