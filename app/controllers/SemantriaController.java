package controllers;

import com.semantria.SentimentAnalizer;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import recommender.DataLoader;

public class SemantriaController extends Controller {


    public static Result index() {

        SentimentAnalizer sa=SentimentAnalizer.getInstance();
        sa.analizeText();
        return ok(index.render(null));
    }

    public static Result loadData()
    {
        DataLoader.loadAllDB();
        return ok("All loaded");
    }

    public static Result sentiment() {

        SentimentAnalizer sa=SentimentAnalizer.getInstance();
        String t=request().getQueryString("text");
        if(t==null||t.isEmpty())
            t="all good";
        sa.getSentiment(t);

            //DataLoader.cargarTips();

        return ok(index.render(null));
    }

}
