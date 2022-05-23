import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class App {

    public static ArrayList<City> cities;
    public static double[][] graph;
    public static Pattern regexCities;

    public static List<int[]> permutations;
    public static Map<int[],Double> Paths;

    public enum Methods{
        BruteForce, DynamicProgramming, DivideAndConquer
    }
    
    public static void main(String[] args) throws IOException,FileNotFoundException  {

        try {
            StartApplication();
            
            File citiesFile = new File("files/cities.txt");

            FileReader fr = new FileReader(citiesFile);
            BufferedReader br = new BufferedReader(fr);

            //Read header;
            String line = br.readLine();

            // Read first value;
            line = br.readLine();

            int id = 1;
            int index = 0;
            while (line != null){

                String[] coordinates = line.split(" ");

                Double lat = Double.parseDouble(coordinates[0]);
                Double lon = Double.parseDouble(coordinates[1]);

                cities.add(new City(id, index, lat, lon));
                line = br.readLine();
                id++;
                index++;
            }

            br.close();

            graph = GetAdjacencyMatrix();
        
            CalculateShortestPath();

        } catch (Exception e) {
            File citiesFile = new File("error.txt");

            FileWriter fr = new FileWriter(citiesFile);
            BufferedWriter bw = new BufferedWriter(fr);

            bw.write("ERROR on the program logic's. Message: " + e.getMessage());
            bw.close();
        }

    }

    private static void StartApplication(){
        permutations = new ArrayList<int[]>();
        Paths = new HashMap<int[],Double>();
        cities = new ArrayList<City>();
    }

    /** 
     * @returns The adjacency matrix equivalent to the "graph".
     */
    public static double[][] GetAdjacencyMatrix(){
        int vertices = cities.size();

        double[][] matrix = new double[vertices][vertices];

        for (int from = 0; from < vertices; from++){
            for (int to = 0; to < vertices; to++){

                if (from == to){ matrix[from][to] = 0; } 
                else {
                    matrix[from][to] = CoordinatesHandle.CalculateDistances(
                        cities.get(from).getX(), 
                        cities.get(from).getY(), 
                        cities.get(to).getX(), 
                        cities.get(to).getY()
                    );
                }
            }
        }
        return matrix;
    }

    /**
     * Calculates the shortest Path for all cities, using different methods.
     * @throws IOException
     */
    public static void CalculateShortestPath() throws IOException{

        Map.Entry<int[], Double> shortestPath = CommonFunctions.GetShortestByBruteForce(cities, graph, permutations);
        ShowShortestPath(shortestPath, Methods.BruteForce);

        // shortestPath = CommonFunctions.GetShortestByDynamicProgramming(cities, graph, permutations);
        // ShowShortestPath(shortestPath, Methods.DynamicProgramming);

        // shortestPath = CommonFunctions.GetShortestByDivideAndConquer(cities, graph, permutations); 
        // ShowShortestPath(shortestPath, Methods.DivideAndConquer);

    }

    /**
     * Prints the shortest path between cities with different methods.
     * 
     * @param shortest The <Key, Value> pair. They represent the permutation with the shortest path, 
     * and the distance in Km;
     * @param method The method used fo get the shortest distance.
     * @throws IOException
     */
    public static void ShowShortestPath(Map.Entry<int[], Double> shortest, Methods method) throws IOException{
        String shortestPath = "";

        File statisticsFile = new File("statistics.txt");
        FileWriter fr = new FileWriter(statisticsFile);
        BufferedWriter bw = new BufferedWriter(fr);

        if (method.name() == "BruteForce"){
            bw.write("Checking statistics for using 'Brute Force':\n");
        } else if (method.name() == "BruteForce"){
            bw.write("Checking statistics for using 'Dynamic Programming':\n");
        } else if (method.name() == "BruteForce"){
            bw.write("Checking statistics for using 'Divide and Conquer':\n");
        }

        String path = shortest.getValue().toString();
        path = path.substring(0, (path.indexOf(".")) + 3);

        bw.write("The shortest path between the cities is " + path + "\n");

        for (int i = 0; i < cities.size(); i++){
            shortestPath += cities.get(shortest.getKey()[i]).getId() + " -> ";
        }
        bw.write("The order of cities you'll need to pass is: " + shortestPath);
        bw.write("\n\n");
                
        bw.close();
    }

}
