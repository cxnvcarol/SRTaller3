package recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import models.Movie;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class MovieLoader {

	public static final String movies_file = "./data/movies.csv";

	public static final String movies_output = "./data/db_movies.csv";
	public static final String features_sql_script = "./data/feature.sql";
	public static final String features_movie_sql_script = "./data/moviefeature.sql";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// crearScriptFeature();
		crearScriptMovie();
		// crearScriptFeatureMovie();
		// crearScriptUser();
		// try {
		// PrintWriter pwMovieOutput = new PrintWriter(new File(
		// movies_output), "UTF-8");
		//
		// BufferedReader br = new BufferedReader(new FileReader(movies_file));
		// String ln;
		// while ((ln = br.readLine()) != null) {
		// String[] movieInfo = ln.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		// pwMovieOutput.println(movieInfo[0]+",0, null,"+movieInfo[1] );
		//
		// }
		// pwMovieOutput.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public static void crearScriptMovie() {
		// id, imdb, dbpedia, title, numrating, avr_rating
		try {
			//PrintWriter pw = new PrintWriter(new File("./data/movies.sql"));
			BufferedReader br = new BufferedReader(new FileReader("./data/movies.dat"));
			String ln;
			ArrayList<Movie> movies = new ArrayList<Movie>();
			while ((ln = br.readLine()) != null) {
				String[] linea = ln.split("::");
				Movie movie= new Movie();
				movie.id=Long.parseLong(linea[0]);
				movie.title = linea[1];

				movie.update();
				movies.add(movie);
				//pw.println("insert into movie values (\"" + linea[0] +"\",\""+linea[1]+ "\");");
			}
			br.close();
			br = new BufferedReader(new FileReader("./data/MappingDBpedia2Movielens.tsv"));
			String line;
			while((line = br.readLine()) != null){
				String[] partes = line.split("	"); // id,nombre,uri
				long movie_id = Long.parseLong(partes[0]);
				
				Movie searched = Movie.find.byId(movie_id);
				
				String dbpediaURI = partes[2];

				dbpediaURI = dbpediaURI.replace("resource", "data");
				dbpediaURI += ".rdf";
				
				searched.dbpedia_uri = dbpediaURI;
				searched.save();

			}
			br = new BufferedReader(new FileReader("./data/ratings.dat"));
			String linea;
			while((linea = br.readLine()) != null){
				String[] partes = linea.split("::"); // id,item, rating
				
				long movie_id = Long.parseLong(partes[1]);
				Movie searched = Movie.find.byId(movie_id);
				
				double rating = Double.parseDouble(partes[2]);
				searched.addRating(rating);
				
				searched.save();

			}
			
		} catch (FileNotFoundException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void crearScriptUser() {
		try {
			PrintWriter pw = new PrintWriter(new File("./data/users.sql"));
			BufferedReader br = new BufferedReader(new FileReader(
					"./data/ratings.dat"));
			String ln;
			ArrayList<String> users = new ArrayList<String>();
			while ((ln = br.readLine()) != null) {
				String[] linea = ln.split("::");
				if (!users.contains(linea[0])) {
					users.add(linea[0]);
				}
			}
			br.close();
			for (String u : users) {
				pw.println("insert into user values (\"" + u + "\");");
			}
			pw.close();
		} catch (FileNotFoundException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void crearScriptFeatureMovie() {
		try {
			PrintWriter pwFeatureMovieOutput = new PrintWriter(new File(
					features_movie_sql_script), "UTF-8");

			BufferedReader br = new BufferedReader(new FileReader(
					"./data/features_movies.csv"));
			String ln;
			while ((ln = br.readLine()) != null) {
				String[] info = ln.split(";");
				try {
					String sqlQuery = "insert into movie_feature (movie_id, feature_id) values (\""
							+ info[0] + "\",\"" + info[1] + "\");";
					pwFeatureMovieOutput.println(sqlQuery);
				} catch (ArrayIndexOutOfBoundsException a) {
					System.out.println("Out of Bounds!");
				}

			}
			pwFeatureMovieOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void crearScriptFeature() {
		try {
			PrintWriter pwFeatureOutput = new PrintWriter(new File(
					features_sql_script), "UTF-8");

			BufferedReader br = new BufferedReader(new FileReader(
					"./data/features.csv"));
			String ln;
			while ((ln = br.readLine()) != null) {
				String[] movieInfo = ln.split(";");
				try {
					String sqlQuery = "insert into feature (id, name, type) values (\""
							+ movieInfo[0]
							+ "\",\""
							+ movieInfo[1]
							+ "\",\""
							+ movieInfo[2] + "\");";
					pwFeatureOutput.println(sqlQuery);
				} catch (ArrayIndexOutOfBoundsException a) {
					System.out.println("Out of Bounds!");
				}

			}
			pwFeatureOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
