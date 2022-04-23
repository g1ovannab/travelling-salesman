import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    
    public static void main(String[] args) throws IOException,FileNotFoundException  {

        permutations = new ArrayList<int[]>();
        cities = new ArrayList<City>();
        regexCities = Pattern.compile("(?<cityName>[\\w\\s]*)\\s*,\\s*(?<countryName>[\\w\\s]*)\\s*,\\s*(?<latitude>[\\d]*.[\\d]*\\s\\w)\\s*,\\s*(?<longitude>[\\d]*.[\\d]*\\s\\w)\\s*\\n*", Pattern.CASE_INSENSITIVE);

        File citiesFile = new File("files/citiesTest.txt");

        FileReader fr = new FileReader(citiesFile);
        BufferedReader br = new BufferedReader(fr);

        //Read header
        String line = br.readLine();
        // Read first value
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
        int[] shortestDistance = new int[cities.size()];

        shortestDistance = GetShortestByBruteForce();
        // shortestDistance = GetShortestByDynamicProgramming();

        // shortestDistance = GetShortestByDivideAndConquer(); 
        // or
        // shortestDistance = GetShortestByGuloso(); 

    }

    public static int[] GetShortestByBruteForce(){
        int[] shortestDistance = new int[cities.size()];

        List<Integer> a = new ArrayList<Integer>();
        cities.forEach(city -> a.add(city.getId()));
        

        permute(a,0);

        Map<int[],Double> distances = new HashMap<int[],Double>();
        
        for (int i = 0; i < permutations.size(); i++){
            double distance = CoordinatesHandle.GetDistanceBetweenPermutation(permutations.get(i), graph);
            distances.put(permutations.get(i), distance);
        }

        // shortest will be min of distances
        return shortestDistance;
    }    

    static void permute(List<Integer> arr, int k){
        for(int i = k; i < arr.size(); i++){
            Collections.swap(arr, i, k);
            permute(arr, k+1);
            Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            permutations.add(arr.stream().mapToInt(i->i).toArray());
            System.out.println(Arrays.toString(arr.toArray()));
        }
    } 
}
