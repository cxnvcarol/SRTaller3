package com.semantria;

import com.semantria.interfaces.ISerializer;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.Configuration;
import com.semantria.mapping.output.DocAnalyticData;
import com.semantria.serializer.JsonSerializer;

import static java.lang.System.out;

/**
 * Created by carol on 3/04/15.
 */
public class SentimentAnalizer {

    private static SentimentAnalizer singleton=null;

    public static SentimentAnalizer getInstance()
    {
        if(singleton==null)
        {
            singleton=new SentimentAnalizer();
        }
        return singleton;
    }

    private static String semantria_key = "2dc67421-a5cf-4262-bae0-20fbebe2d25c";
    private static String semantria_secret = "e7bd1789-f5d5-434c-960e-e112373f3926";
    private Configuration m_config = null;
    private ISerializer serializer = new JsonSerializer();

    public void getSentiment(String txt)
    {
        String configId = null;
        Session session = Session.createSession(semantria_key, semantria_secret, serializer);
        ////////////////////////////////////////////////
        //session.setCallbackHandler(new CallbackHandler());
        for(Configuration config : session.getConfigurations())
        {
            if(config.getName().equals("TEST_CONFIG"))
            {
                configId = config.getId();
            }
        }

        int status = session.queueDocument(new Document("TEST_ID_1",
                txt, "tag")
                , configId);

        DocAnalyticData task = null;
        for ( int i = 0; i < 5; i++ )
        {
            task = session.getDocument("TEST_ID_1", configId);
            if (task != null)
            {
                out.println(task.getSentimentPolarity()+" - "+task.getSentimentScore());

                break;
            }

            try
            {
                Thread.sleep(1000L);
            } catch (InterruptedException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
    public void analizeText()
    {

    }
}
