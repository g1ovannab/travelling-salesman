import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class App {

    public static ArrayList<City> cities;
    public static double[][] graph;

    /* Brute Force */
    public static List<int[]> permutations;
    /* Dynamic Programming */
    public static List<Integer> pathDP;

    public enum Methods{
        BruteForce, DynamicProgramming, Greedy
    }
    
    public static void main(String[] args) throws IOException,FileNotFoundException  {

        try {
            DeleteFiles();
            StartApplication();

            
            File citiesFile;

            /* This part of code must be commented. The code is registered for 
            reasons of internal tests for the accomplishment of the report. */
            Boolean runTest = true;

            if (runTest) {
                int numberOfCities = 12;
                citiesFile = new File("files/citiesTest.txt");
                RunTests(citiesFile, numberOfCities);

            } else {
                citiesFile = new File("files/cities.txt");
            }
            /* End of test. */


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

            if (cities.size() <= 2){
                throw new IllegalArgumentException("Number of cities cannot be equal or under 2.");
            } else {
                System.out.println("Getting adjacency matrix...");
                graph = GetAdjacencyMatrix();

                System.out.println("Calculating shortest path...");
                CalculateShortestPath();
            }

        } catch (Exception e) {
            File citiesFile = new File("files/error.txt");

            FileWriter fr = new FileWriter(citiesFile, true);
            BufferedWriter bw = new BufferedWriter(fr);
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println(dtf.format(now));
            bw.write("[" + dtf.format(now) + "]: ");


            bw.write("ERROR on the program logic's. Message: " + e.getMessage() + "\n");
            bw.close();
        }

    }

    private static void RunTests(File cities, int n) throws IOException{
        Random random = new Random();
        
        double x, y;

        int min = 0, max = 10000;

        FileWriter fw = new FileWriter(cities, true);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(n + "\n");
        
        for (int i = 1; i <= n; i++){
            // pra cada cidade gerar dois numeros de 0 a 1000
            x = (random.nextInt(max - min + 1) + min) / 10.0;
            y = (random.nextInt(max - min + 1) + min) / 10.0;

            bw.write(x + " " + y);
            if (i != n) bw.write("\n");
        }

        bw.close();
    }

    private static void StartApplication(){
        permutations = new ArrayList<int[]>();
        cities = new ArrayList<City>();
        pathDP = new ArrayList<Integer>();
    }

    private static void DeleteFiles(){
        try {
            File statisticsFile = new File("files/statistics.txt");
            if (statisticsFile.exists()) statisticsFile.delete();

            File testsFile = new File("files/citiesTest.txt");
            if (testsFile.exists()) testsFile.delete();

        } catch (Exception e) {
            throw e;
        }
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
        long start, finish;        
        Map.Entry<int[], Double> shortestPath = new AbstractMap.SimpleEntry<int[], Double>(null, null);

        /* Brute Force */
        System.out.println("Getting shortest by BF...");
        start = System.currentTimeMillis();
        shortestPath = CommonFunctions.GetShortestByBruteForce(cities, graph, permutations);
        finish = System.currentTimeMillis();
        ShowShortestPath(shortestPath, Methods.BruteForce, finish - start);

        /* Dynamic Programming */
        System.out.println("Getting shortest by DP...");
        start = System.currentTimeMillis();
        shortestPath = CommonFunctions.GetShortestByDynamicProgramming(cities, graph, pathDP);
        finish = System.currentTimeMillis();
        ShowShortestPath(shortestPath, Methods.DynamicProgramming, finish - start);

        /* Divide And Conquer */
        System.out.println("Getting shortest by G...");
        start = System.currentTimeMillis();
        shortestPath = CommonFunctions.GetShortestByGreedy(cities, graph); 
        finish = System.currentTimeMillis();
        ShowShortestPath(shortestPath, Methods.Greedy, finish - start);
    }

    /**
     * Prints the shortest path between cities with different methods.
     * 
     * @param shortest The <Key, Value> pair. They represent the permutation with the shortest path, 
     * and the distance in Km;
     * @param method The method used fo get the shortest distance.
     * @throws IOException
     */
    public static void ShowShortestPath(Map.Entry<int[], Double> shortest, Methods method, long time) throws IOException{
        String shortestPath = "";

        File statisticsFile = new File("files/statistics.txt");
        FileWriter fr = new FileWriter(statisticsFile, true);
        BufferedWriter bw = new BufferedWriter(fr);

        bw.write("Checking statistics for using '");

        if (method.name() == "BruteForce"){
            bw.write("Brute Force':\n");
        } else if (method.name() == "DynamicProgramming"){
            bw.write("Dynamic Programming':\n");
        } else if (method.name() == "Greedy"){
            bw.write("Greedy':\n");
        }

        bw.write("\tTime spent for " + cities.size()+ " cities: " + time + " milliseconds.\n\n");

        String path = shortest.getValue().toString();
        path = path.substring(0, (path.indexOf(".")) + 3);

        bw.write("The shortest path between the cities is " + path + "\n");

        for (int i = 0; i < cities.size(); i++){
            shortestPath += cities.get(shortest.getKey()[i]).getId() + " -> ";
        }
        shortestPath += cities.get(shortest.getKey()[0]).getId();

        bw.write("The order of cities you'll need to pass is: " + shortestPath + "\n");
        bw.write("\n\n");
                
        bw.close();
    }

}
