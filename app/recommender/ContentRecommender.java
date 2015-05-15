package recommender;

import models.Recommendation;
import models.User;

import java.util.ArrayList;

/**
 * Created by carol on 15/05/15.
 */
public class ContentRecommender {
    private static ContentRecommender instance;

    public static ContentRecommender getInstance() {
        if(instance==null)
            instance=new ContentRecommender();
        return instance;
    }


    public ArrayList<Recommendation> recommend(User user, Object o) {
        return new ArrayList<>();
    }
}
