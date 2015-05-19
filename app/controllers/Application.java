package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import models.Movie;
import models.Recommendation;
import models.User;
import org.json.simple.JSONObject;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;

import views.html.*;
import recommender.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Application extends Controller {

    public static User getLoggedUser()
    {
        String uid="";
        try
        {
            uid=request().cookies().get("user_id").value();
        }
        catch(Exception e)
        {
            
        }

        if(uid!=null&&!uid.isEmpty())
            return User.find.byId(Long.parseLong(uid));
        return null;
    }
    public static Result randomUserId()
    {
        try{
        List<SqlRow> q = Ebean.createSqlQuery("select * from user order by rand() limit 1").findList();
        return ok(q.get(0).getString("user_id"));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ok("");
        }
    }


    public static Result searchGet()
    {
    	// MovieLoader.crearScriptMovie();
        String msg="";
        DynamicForm data = Form.form().bindFromRequest();
        String user_id="";
        User logged = null;

        try
        {
            user_id=data.get("userid");
        }
        catch(Exception e)
        {
        }

        if(user_id!=null&&!user_id.isEmpty()) {
            try {
                logged = User.find.byId(Long.parseLong(user_id));
            }
            catch(Exception e)
            {
                logged=getLoggedUser();
                msg+="\n"+"User error...user_id:"+user_id;
            }
        }
        else{
            logged=getLoggedUser();
        }
        if(logged!=null)
            response().setCookie("user_id",""+logged.user_id);
        else
            msg+="\nWithout user";

        //TODO
        long timestamp=0;
        String fecha = data.get("datefield");
        if(fecha!=null){
        	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        	Date date = null;
			try {
				date = dateFormat.parse(fecha);
			} catch (ParseException e) {
	            msg+="\nCould not parse date";
			}
			if(date!=null)
				timestamp = date.getTime();
        	
        }
        else
            msg+="\nWithout date";
                
        List<Recommendation> items = new ArrayList<Recommendation>();
        items=HybridRecommender.getInstance().recommend(logged, timestamp);


        return ok(search.render(msg,logged,items));

    }
    public static Result getMovie(String id) {
        JSONObject jo=new JSONObject();
        return ok(movie.render(Movie.find.byId(Long.parseLong(id))));
    }
    public static Result loadAll() {
        //DataThread dthread=new DataThread();
        //dthread.start();
        DataLoader dl=new DataLoader();
        try {
            dl.loadAllDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok("Data loading adressed...");
    }
}

