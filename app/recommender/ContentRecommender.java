package recommender;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by carol on 15/05/15.
 */
public class ContentRecommender {
    private int MAX_REVIEWED;
    private static ContentRecommender instance;
    private int maxRecommendations;

    public void setMaxRecommendations(int maxRecommendations) {
        this.maxRecommendations = maxRecommendations;
    }

    public static ContentRecommender getInstance() {
        if(instance==null)
            instance=new ContentRecommender();
        return instance;
    }

    public ArrayList<Recommendation> recommend(User user) {
        long t1=System.currentTimeMillis();
        Feature[] cs = new Feature[0];

        if (user != null) {
            user.updateFeatures();
            cs = user.features.toArray(new Feature[user.features.size()]);
        }
        ArrayList<Recommendation> returned = new ArrayList<Recommendation>();

        if(cs.length>0)
        {
            long[] bids=getAllPosibleSimilarMovies(cs);
            Double[][] similB=new Double[bids.length][2];
            int i=0;
            for (i = 0; i < similB.length&&i<MAX_REVIEWED; i++)
            {
                List<Feature> clist = Movie.find.byId(bids[i]).features;
                Feature[] cl2 = clist.toArray(new Feature[clist.size()]);
                similB[i][1]=getJaccardSimilarity(cl2,cs);
                similB[i][0]=(double)i;
            }
            for (; i < similB.length; i++)
            {
                similB[i][1]=new Double(0);
                similB[i][0]=(double)i;
            }
            Double[][] ordered=orderAll(similB);

            for (int j = 0; j < maxRecommendations &&j<bids.length; j++) {
                Recommendation r = new Recommendation();
                int index=ordered[j][0].intValue();
                r.setMovie(Movie.find.byId(bids[index]));
                r.setEstimatedRating(ordered[j][1]*5);
                returned.add(r);
            }
        }
        System.out.println("\n\n!!!HIGH ATTENTION HERE... RECOMMENDING BY CONTENT TOOK "+(System.currentTimeMillis()-t1)+"ms!!!\n\n");
        return returned;
    }

    private Double[][] orderAll(Double[][] similB) {
        Comparator<Double[]> arrayComparator = new Comparator<Double[]>() {
            @Override
            public int compare(Double[] o1, Double[] o2) {
                return o2[1].compareTo(o1[1]);
            }
        };
        Arrays.sort(similB, arrayComparator);
        return similB;
    }

    public double getJaccardSimilarity(long[] c1,long[] c2)
    {
        int cojoin=0,cot=0;
        if(c1.length==0|| c2.length==0)
        {
            return 0;
        }
        String completeQuery="select count(*) as co from category where ";
        String where1= "where genre_id=3 or genre_id=4";
        String where2="where genre_id=3 or genre_id=4";

        if(c1.length>0)
        {
            where1=" where ";
            String tqWhere="";
            for (int i = 0; i < c1.length; i++) {
                tqWhere+= " or genre_id=" + c1[i];
            }
            where1+=tqWhere.substring(3);

            completeQuery+=tqWhere.substring(3);
        }
        if(c2.length>0)
        {
            where2=" where ";
            String tqWhere="";
            for (int i = 0; i < c2.length; i++) {
                tqWhere+=" or genre_id="+c2[i];
            }

            where2+=tqWhere.substring(3);
            if(completeQuery.contains("where"))
                completeQuery+=tqWhere;
            else
                completeQuery+=tqWhere.substring(3);
        }
        String queryJoin="select count(*) co from (select genre_id cid from category"+where1+") c2 join category on cid=genre_id "+where2;

        cot = Ebean.createSqlQuery(completeQuery).findList().get(0).getInteger("co");
        cojoin= Ebean.createSqlQuery(queryJoin).findList().get(0).getInteger("co");

        return (double)cojoin/(double)cot;
    }
    public double getJaccardSimilarity(Feature[] c1, Feature[]  c2)
    {
        long[] cs1=new long[c1.length];
        for (int i = 0; i < cs1.length; i++) {
            if(c1[i]!=null)
                cs1[i]=c1[i].getID();
        }
        long[] cs2=new long[c2.length];
        for (int i = 0; i < cs2.length; i++) {
            if(c2[i]!=null)
                cs2[i]=c2[i].getID();
        }
        return getJaccardSimilarity(cs1,cs2);
    }

    public EvaluationResult evaluate (boolean radio,int maxBusinessNeighb, double trainingPercentage, int nUsers,int maxrec)
    {
        EvaluationResult er=new EvaluationResult();
        er.description="Recomendador de contenido: {filtra por radio="+radio+
                "%,negocios revisados="+maxBusinessNeighb
                +", porcentaje entrenamiento= "+(int)(trainingPercentage*100)
                +", #usuarios prueba= "+nUsers
                +", numero de recomendaciones x usuario= "+maxrec+"}";
        /*


        maxRecommendations=maxrec;
        //1. Get users with most visited places (100?) - or with more stars??
        //2. split according to trainingPercentage (reviews)
        //3. in limitedRandomBusiness make sure to include the target movie (set special movie in recommender each time and use it)
        //4.recommend for all users
        //5. calculate precision and recall.
        //User.find.where()

        int totalUsers=nUsers;
        String[] uids= EvaluationController.findPopularUsers(totalUsers);
        //TODO
        //setLimitedUsers(uids);
        //setMaxBusinessReviewed(maxBusinessNeighb);
        int tpTotal=0;
        int esperadoTotal=0;

        long t1=0,averageTime=0;
        t1=System.currentTimeMillis();
        for (int i = 0; i < uids.length; i++) {
            User u=User.find.byId(uids[i]);
            String[] splitedCats = getSplitedCategories(u, trainingPercentage);
            t1=System.currentTimeMillis();
            //recommend based in splitted categories not in user:
            //ArrayList<Recommendation> res = recommend(radio ? u.getMedianLocation() : null, null, null, splitedCats, null);
            //TODO
            ArrayList<Recommendation> res = recommend(null);
            averageTime+=System.currentTimeMillis()-t1;
            int[] result=getEval(res);//result[#esperado][#tp - intersección]
            esperadoTotal+=result[0];
            tpTotal+=result[1];
        }
        averageTime/=uids.length;

        er.precision=(double)(((double)tpTotal)/(double)(maxRecommendations*totalUsers));
        er.recall=(double)(((double)tpTotal)/(double)(esperadoTotal));
        er.time=averageTime;

        */
        return er;


    }


    public long[] getAllPosibleSimilarMovies(Feature[] cs)
    {
        String theQuery="";
        if(cs.length>0)
        {
            theQuery="select distinct movie_id from movie_feature where feature_id in (";
            String tqWhere="";
            for (int i = 0; i < cs.length; i++) {
                if(cs[i]!=null)
                    tqWhere+=","+cs[i].getID();
                else System.err.println("Trying use an unknown feature...");
            }
            theQuery+=tqWhere.substring(1);
            theQuery+=") order by rand() limit "+MAX_REVIEWED;
        }

        List<SqlRow> re = Ebean.createSqlQuery(theQuery).findList();
        long[] returned=new long[re.size()];

        for (int i = 0; i < returned.length; i++) {
            returned[i]=Long.parseLong(re.get(i).getString("movie_id"));
        }
        return returned;
    }
}
