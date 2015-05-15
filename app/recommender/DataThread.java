package recommender;

import java.io.IOException;

/**
 * Created by carol on 15/05/15.
 */
public class DataThread extends Thread{

        public void run() {
            System.out.println("Init loading data");
            try {
                DataLoader.loadAllDB();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("All data loaded");
        }


}
