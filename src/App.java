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
import java.util.regex.Matcher;
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
            
            File citiesFile = new File("files/citiesTest2.txt");

            FileReader fr = new FileReader(citiesFile);
            BufferedReader br = new BufferedReader(fr);

            //Read header;
            String line = br.readLine();
            // Read first value;
            line = br.readLine();

            int id = 0;
            while (line != null){
                Matcher m = regexCities.matcher(line);

                if (m.matches()){
                    cities.add(new City(id, m.group("cityName"), m.group("countryName"), m.group("latitude"), m.group("longitude")));
                } else {
                    String[] values = line.split(",");
                    System.out.println("Not able to register city " + values[0]);
                }
            
                line = br.readLine();
                id++;
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
        regexCities = Pattern.compile("(?<cityName>[\\w\\s]*)\\s*,\\s*(?<countryName>[\\w\\s]*)\\s*,\\s*(?<latitude>[\\d]*.[\\d]*\\s\\w)\\s*,\\s*(?<longitude>[\\d]*.[\\d]*\\s\\w)\\s*\\n*", Pattern.CASE_INSENSITIVE);
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
                        cities.get(from).getLatitude(), 
                        cities.get(from).getLongitude(), 
                        cities.get(to).getLatitude(), 
                        cities.get(to).getLongitude()
                    );
                }
            }
        }
        return matrix;
    }

    /**
     * Calculates the shortest Path for all cities, using different methods.
     */
    public static void CalculateShortestPath(){

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
     */
    public static void ShowShortestPath(Map.Entry<int[], Double> shortest, Methods method){
        switch (method) {
            case BruteForce:
                System.out.println("Checking statistics for using 'Brute Force':");
            case DynamicProgramming:
                System.out.println("Checking statistics for using 'Dynamic Programming':");
            case DivideAndConquer:
                System.out.println("Checking statistics for using 'Divide and Conquer':");
        }
        System.out.println();
        System.out.println("The shortest path between the cities is " + Double.valueOf(new DecimalFormat("#.##").format(shortest.getValue())) + " km.");
        System.out.println("The order of cities you'll need to pass is: ");
        for (int i = 0; i < cities.size(); i++){
            System.out.print(cities.get(shortest.getKey()[i]).getCityName() + " -> ");
        }
        System.out.println();

        System.out.println("ATENTION: You don't need to start with the same city as told. Just follow the SEQUENCE of cities (ordered or mirrored). ");
    }

}
