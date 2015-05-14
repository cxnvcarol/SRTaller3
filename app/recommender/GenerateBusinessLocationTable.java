package recommender;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GenerateBusinessLocationTable {

	public static final String rutaNegocios = "./data/yelp_academic_dataset_business.json";
	public static final String business_loc_db = "./data/business_geoloc.csv";

	public static void main(String[] args) {
		cargarNegocios();
	}

	private static void cargarNegocios() {
		try {

			BufferedReader br = new BufferedReader(new FileReader(rutaNegocios));
			PrintWriter pw = new PrintWriter(business_loc_db, "UTF-8");
			pw.println("business_id,name,full_address,city,state,stars,latitude,longitude");

			System.out.println("Lee el archivo");
			String line = "";
			while ((line = br.readLine()) != null) {
				try {

					line = line.replace("\\", "");

					// System.out.println("Nueva linea");
					// System.out.println(line);

					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObject = (JSONObject) jsonParser.parse(line);

					// get a String from the JSON object
					String business_id = (String) jsonObject.get("business_id");
					String name = (String) jsonObject.get("name");
					name = name.replaceAll(",", "");
					String full_address = (String) jsonObject
							.get("full_address");
					full_address = full_address.replaceAll(",", "").replaceAll("//n", "").replaceAll("//t", "").replaceAll("//t", "");
					String city = (String) jsonObject.get("city");
					String state = (String) jsonObject.get("state");
					double latitude = (Double) jsonObject.get("latitude");
					double longitude = (Double) jsonObject.get("longitude");
					double stars = (Double) jsonObject.get("stars");

					pw.println(business_id + "," + name + "," + full_address
							+ "," + city + "," + state + "," + stars + ","
							+ latitude + "," + longitude);

				} catch (Exception e) {
					System.out.println(e.getClass() + " :: " + e.getMessage());
				}
			}
			pw.close();
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error loading users: file not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
