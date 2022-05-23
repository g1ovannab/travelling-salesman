import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommonFunctions {
    private static final CommonFunctions cf = new CommonFunctions();
    
    public static CommonFunctions getCF(){ return cf; }
    private CommonFunctions(){}
    
    /**
     * Set up a permutation of cities.
     * 
     * @param ids The values to permute, or the id of the cities;
     * @param k Index of element to permute;
     * @param permutations List of permutations that we need to get the shortest path.
    */
    public static void PermuteCities(List<Integer> ids, int k, List<int[]> permutations){
        
        for(int i = k; i < ids.size(); i++){
            Collections.swap(ids, i, k);
            PermuteCities(ids, k+1, permutations);
            Collections.swap(ids, k, i);
        }

        if (k == ids.size() -1){ permutations.add(ids.stream().mapToInt(i->i).toArray()); }
    } 

    /**
     * Get the sortest path using the 'Brute Force' algorithm/logic/method.
     * 
     * @param cities The list of cities;
     * @param graph The "graph" representation in which we obtain the distances through it;
     * @param permutations All the permutations between cities;
     * @returns The shortest path.
     */
    public static Map.Entry<int[], Double> GetShortestByBruteForce(List<City> cities, double[][] graph, List<int[]> permutations){
        
        int[] shortestPathPermutation = new int[cities.size()];
        double shortestPath = Double.MAX_VALUE;
        
        List<Integer> indexOfCitiesToPermute = new ArrayList<Integer>();
        cities.forEach(city -> indexOfCitiesToPermute.add(city.getIndex()));
        
        CommonFunctions.PermuteCities(indexOfCitiesToPermute,0, permutations);
        
        for (int i = 0; i < permutations.size(); i++){
            double distanceOfPermutation = CoordinatesHandle.GetDistanceBetweenPermutation(permutations.get(i), graph);
            
            /* If the distance of this specific permutation is lower than the value of the shortest path, 
            the new shortest path will be the distance of this permutation;*/
            if (distanceOfPermutation < shortestPath){
                shortestPath = distanceOfPermutation;
                shortestPathPermutation = permutations.get(i);
            }
        }
        return new AbstractMap.SimpleEntry<int[], Double>(shortestPathPermutation, shortestPath);
    }    
    
}
