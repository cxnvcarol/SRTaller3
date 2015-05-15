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

import java.util.ArrayList;
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

    public static Result index() {

        User logged=getLoggedUser();
        if(logged!=null)
            response().setCookie("user_id",""+logged.user_id);

        return ok(index.render(logged));//"Hybrid recommender system"));
    }
    
    public static Result indexPost() {

        DynamicForm data = Form.form().bindFromRequest();
    	String user_id="";
        String useridTemp=user_id;

        User logged = null;
		
        try
        {
            user_id=data.get("userid");
        }
        catch(Exception e)
        {
        }
        if(user_id==null)
        {
            user_id=useridTemp;

        }
        if(!user_id.isEmpty()) {
            try {
                logged = User.find.byId(Long.parseLong(user_id));
            }
            catch(Exception e)
            {
            	
            }
        }

        response().setCookie("user_id",user_id);
        
        return ok(index.render(logged));//"Hybrid recommender system"));
    }
    public static Result searchGet()
    {
        String user_id="";
        String msg="";
        User logged=null;
        try{
            user_id = request().cookie("user_id").value();
        }
        catch(Exception e)
        {
            msg+="\nWithout user";
        }
        if(!user_id.isEmpty()) {
            try {
                logged = User.find.byId(Long.parseLong(user_id));
            }
            catch(Exception e)
            {
                msg+="\n"+"User error...user_id:"+user_id;
            }
        }

        response().setCookie("user_id",user_id);


        ArrayList<Recommendation> items = new ArrayList<Recommendation>();//avoid recommend with get!
        // /HybridRecommender.getInstance().recommend(null, null, logged, null, null);


        String[] categoriesList=new String[0];
        //if(logged!=null)
            //categoriesList=logged.getCategoriesStr();//fixme

        ArrayList<String> cstr=new ArrayList<String>();
        for (String cs:categoriesList)
        {
            cstr.add(cs);
        }

        return ok(search.render(msg,logged,items,cstr));

    }
    public static Result search()
    {
        DynamicForm data = null;
        double lat=0;
        double longi=0;
        String hora="";
        String user_id="";

        String msg="";

        String[] categoriesList=new String[0];
        String[] attributesList=new String[0];

        try {
            data = Form.form().bindFromRequest();
        }
        catch (Exception e)
        {
            msg+="loading form..."+e.getMessage();
        }
        if(data!=null)
        {
            try{
                lat = Double.parseDouble(data.get("latitude"));
                longi= Double.parseDouble(data.get("longitude"));
            }
            catch(Exception e)
            {
                msg+="\nWithout geolocation";
            }

            try{
                hora = data.get("hour");
            }
            catch(Exception e)
            {
                msg+="\nWithout hour";
            }

            try{
                categoriesList= data.get("categories").split(",");
            }
            catch(Exception e)
            {
                msg+="\nWithout explicit categories";
            }

            try{
                attributesList= data.get("attributes").split(",");
            }
            catch(Exception e)
            {
                msg+="\nWithout explicit attributes";
            }

            try{
                user_id = request().cookie("user_id").value();
            }
            catch(Exception e)
            {
                msg+="\nWithout user";
            }

        }

        HybridRecommender hr=HybridRecommender.getInstance();

        double[] pos=new double[2];
        pos[0]=lat;
        pos[1]=longi;
        User logged=null;
        String useridTemp=user_id;

        try
        {
            user_id=data.get("userid");
        }
        catch(Exception e)
        {
        }
        if(user_id==null)
        {
            user_id=useridTemp;

        }
        if(!user_id.isEmpty()) {
            try {
                logged = User.find.byId(Long.parseLong(user_id));
            }
            catch(Exception e)
            {
                msg+="\n"+"User error...user_id:"+user_id;
            }
        }

        response().setCookie("user_id",user_id);
        if(logged == null){
        	System.out.println("EL USUARIO ES NULO!");
        }
        ContentRecommender cr=ContentRecommender.getInstance();
        ArrayList<Recommendation> items = hr.recommend(pos, hora, logged, categoriesList, attributesList);

        //ArrayList<Recommendation> items = cr.recommend(pos, hora, logged, categoriesList, attributesList);


        //

        //if((categoriesList==null||categoriesList.length==0)&&logged!=null)
            //categoriesList=logged.getCategoriesStr();//fixme

        ArrayList<String> cstr=new ArrayList<String>();
        for (String cs:categoriesList)
        {
            cstr.add(cs);
        }

        return ok(search.render(msg,logged,items,cstr));
    }


    public static Result getMovie(String id) {
        JSONObject jo=new JSONObject();
        return ok(movie.render(Movie.find.byId(Long.parseLong(id))));
    }

    public static Result loadAll() {
        DataThread dthread=new DataThread();
        dthread.start();
        return ok("Data loading adressed...");
    }
}

