package recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import com.hp.hpl.jena.rdf.model.*;

import models.Feature;
import models.Movie;

public class DBPediaModule {
	public static final String ruta_archivo = "./data/MappingDBpedia2Movielens.tsv";
	public static final String archivo_directores = "./data/directores.csv";
	public static final String archivo_reparto = "./data/reparto.csv";
	public static final String archivo_subject = "./data/subjects.csv";
	public static final String rdf_file = "./data/datos.rdf";

	public static final String db_features = "./data/features.csv";
	public static final String db_features_movies = "./data/features_movies.csv";

	public static final String list_directores = "./data/list_directores.txt";
	public static final String list_reparto = "./data/list_reparto.txt";
	public static final String list_subject = "./data/list_subjects.txt";

	public static ArrayList<String> directores;
	public static ArrayList<String> reparto;
	public static ArrayList<String> temas;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		directores = new ArrayList<String>();
		reparto = new ArrayList<String>();
		temas = new ArrayList<String>();
		cargarDBPedia();
	}

	public static void cargarDBPedia() {
		try {
			PrintWriter pwDirectores = new PrintWriter(new File(
					archivo_directores), "UTF-8");
			PrintWriter pwReparto = new PrintWriter(new File(archivo_reparto),
					"UTF-8");
			PrintWriter pwTemas = new PrintWriter(new File(archivo_subject),
					"UTF-8");

			PrintWriter pwRDF = new PrintWriter(new File(rdf_file), "UTF-8");

			PrintWriter pwFeatures = new PrintWriter(new File(db_features),
					"UTF-8");
			PrintWriter pwFeaturesMovies = new PrintWriter(new File(
					db_features_movies), "UTF-8");

			Model macroModel = ModelFactory.createDefaultModel();
			BufferedReader br = new BufferedReader(new FileReader(ruta_archivo));
			String ln;
			while ((ln = br.readLine()) != null) {
				try {
					String[] partes = ln.split("	"); // id,nombre,uri

					long idMov = Long.parseLong(partes[0]);
					String name, uriM;

					pwDirectores.print(idMov);
					pwReparto.print(idMov);
					pwTemas.print(idMov);

					name = partes[1];
					uriM = partes[2];
					uriM = uriM.replace("resource", "data");
					uriM += ".rdf";

					// Movie pelicula = Movie.find.byId(idMov);
					// if(pelicula==null)
					// System.out.println("NO LA ENCONTRO!!! "+ idMov+
					// " "+name);
					System.out.println("UriM: " + uriM);

					Model model = ModelFactory.createDefaultModel();
					URL rdfData = new URL(uriM);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(rdfData.openStream()));

					model.read(in, null);
					// ArrayList<Feature> featuresMovie = new
					// ArrayList<Feature>();
					StmtIterator iter = model.listStatements();
					while (iter.hasNext()) {
						Statement stmt = iter.next();
						Resource subject = stmt.getSubject();
						Property predicate = stmt.getPredicate();
						RDFNode object = stmt.getObject();

						// Feature feat= new Feature();
						// Temas de la pelicula
						if (predicate.toString().contains("subject")) {
							String subjectst = object.toString();
							subjectst = subjectst
									.replace(
											"http://dbpedia.org/resource/Category:",
											"");
							subjectst = subjectst.replace("@en", "");
							

							pwTemas.write("," + subjectst);

							// feat.name = subjectst;
							// feat.type = "SUBJECT";
							// feat.id = Feature.find.nextId();
							// featuresMovie.add(feat);

							pwFeatures.println( subjectst.hashCode()+ ";" + subjectst + ";" + "subject");
							pwFeaturesMovies.println(idMov + ";"+ subjectst.hashCode());
							// feat.save();

							if (!temas.contains(subjectst)) {
								temas.add(subjectst);
							}
						}
						// Director
						if (predicate.toString().contains("property")
								&& predicate.toString().contains("director")
								&& !predicate.toString().contains("_film)")) {
							String directorst = object.toString();
							directorst = directorst.replace(
									"http://dbpedia.org/resource/", "");
							directorst = directorst.replace("@en", "");
							String[] dirct = directorst.split("_\\(");
							directorst = dirct[0];
							String[]  varios = directorst.split(",");
							for( int i = 0 ; i<varios.length; i ++){
								String dir = varios[i];
								pwDirectores.write("," + dir);
								
								// feat.name = directorst;
								// feat.type = "DIRECTOR";
								// feat.id = Feature.find.nextId();
								// featuresMovie.add(feat);
								// feat.save();
								
								pwFeatures.println(dir.hashCode() + ";"+ dir + ";" + "director");
								pwFeaturesMovies.println(idMov + ";"+ dir.hashCode());
								
								if (!directores.contains(dir)) {
									directores.add(dir);
								}
							}

						}
						// Reparto
						if (predicate.toString().contains("property")
								&& predicate.toString().contains("starring")) {
							String repartost = object.toString();
							repartost = repartost.replace(
									"http://dbpedia.org/resource/", "");
							String restar[] = repartost.split("_\\(");

							repartost = restar[0];
							repartost = repartost.replace("@en", "");

							pwReparto.write("," + repartost);

							// feat.name = repartost;
							// feat.type = "STARRING";
							// feat.id = Feature.find.nextId();
							// feat.save();

							pwFeatures.println(repartost.hashCode() + ";"+ repartost + ";" + "director");
							pwFeaturesMovies.println(idMov + ";"+ repartost.hashCode());

							// featuresMovie.add(feat);

							if (!reparto.contains(repartost)) {
								reparto.add(repartost);
							}
						}
						// System.out.println("STATEMENT");
						// System.out.print(subject.toString());
						// System.out.print(" " + predicate.toString() + " ");
						// if (object instanceof Resource) {
						// System.out.print(object.toString());
						// }
						// else {
						// System.out.print(" \"" + object.toString() + "\"");
						// }
						// System.out.println(" .");
						macroModel.add(model);
					}
					// pelicula.features = featuresMovie;
					// pelicula.update();
					pwDirectores.println();
					pwReparto.println();
					pwTemas.println();

				} catch (FileNotFoundException e) {

				}
			}
			macroModel.write(pwRDF);

			br.close();
			pwDirectores.close();
			pwReparto.close();
			pwRDF.close();
			pwFeatures.close();
			pwFeaturesMovies.close();

			PrintWriter pwLDirectores = new PrintWriter(new File(list_directores), "UTF-8");
			PrintWriter pwLReparto = new PrintWriter(new File(list_reparto),"UTF-8");
			PrintWriter pwLTemas = new PrintWriter(new File(list_subject),"UTF-8");

			for (String d : directores) {
				pwLDirectores.println(d);
			}
			for (String r : reparto) {
				pwLReparto.println(r);
			}
			for (String t : temas) {
				pwLTemas.println(t);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
