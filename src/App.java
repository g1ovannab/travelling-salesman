import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class App {

    public static ArrayList<City> cities;
    public static double[][] graph;
    
    public static void main(String[] args) throws IOException,FileNotFoundException  {

        cities = new ArrayList<City>();

        File citiesFile = new File("files/cities.txt");

        FileReader fr = new FileReader(citiesFile);
        BufferedReader br = new BufferedReader(fr);

        //Read header
        String line = br.readLine();
        // Read first value
        line = br.readLine();

        while (line != null){
            
            String[] values = line.split(",");

            if (values.length != 4 & values.length > 0){
                System.out.println("Not able to register city " + values[0]);
            } else {
                cities.add(new City(values[0], values[1], values[2], values[3]));
            }

            line = br.readLine();
        }

        br.close();

        graph = GetAdjacencyMatrix();
        
        CalculateShortestDistance();

    }

    /** 
     * @returns the adjacency matrix equivalent to the "graph".
     */
    public static double[][] GetAdjacencyMatrix(){
        int vertices = cities.size();

        double[][] matrix = new double[vertices][vertices];

        for (int from = 0; from < vertices; from++){
            for (int to = 0; to < vertices; to++){

                if (from == to){
                    matrix[from][to] = 0;
                } else {
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

    public static void CalculateShortestDistance(){
        // int[] shortestDistance = new int[cities.size()];

        // shortestDistance = GetShortestByBruteForce();
        // shortestDistance = GetShortestByDynamicProgramming();

        // shortestDistance = GetShortestByDivideAndConquer(); 
        // or
        // shortestDistance = GetShortestByGuloso(); 

    }

}
