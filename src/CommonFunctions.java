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

        if (k == ids.size() -1 && ids.get(0) == 0) permutations.add(ids.stream().mapToInt(i->i).toArray());
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
    
    public static Map.Entry<int[], Double> GetShortestByDynamicProgramming(List<City> cities, double[][] graph, List<Integer> pathDP){
        
        
        int size = cities.size();
        int sizePow = (int) Math.pow(2, cities.size());
        
        int[][] graphPath = new int[size][sizePow]; 
        double[][] graphDP = new double[size][sizePow];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < sizePow; j++) {
				graphPath[i][j] = -1;
                graphDP[i][j] = -1;
			}
		}

        for (int i = 0; i < size; i++) {
			graphDP[i][0] = graph[i][0];
		}

        pathDP.add(0);
        Double pathValue = Heuristic(0, sizePow - 2, graph, graphDP, graphPath, size, sizePow);
        GetPathByValue(0, sizePow - 2, graphPath, pathValue, sizePow, pathDP);
        int[] shortestPath = pathDP.stream().mapToInt(Integer::intValue).toArray(); 
        
        return new AbstractMap.SimpleEntry<int[], Double>(shortestPath, pathValue);
    }

    public static void GetPathByValue(int start, int set, int[][] graphPath, double pathValue, int sizePow, List<Integer> pathDP){
        if(graphPath[start][set] == -1)
			return;
		int x = graphPath[start][set];
		int mascara = sizePow -1 - (int)Math.pow(2, x);
		int marcado = set & mascara;
		
		pathDP.add(x);
		GetPathByValue(x, marcado, graphPath, pathValue, sizePow, pathDP);
    }

    public static Double Heuristic(int init, int set, double[][] graph, double[][] graphDP, int[][] graphPath, int size, int sizePow){
        int mascara, marcado;
        double resultado = -1, temp;
		
        if(graphDP[init][set] != -1)
			return graphDP[init][set];
		else {
            for (int i = 0; i < size; i++) {
                mascara = sizePow - 1 - (int) Math.pow(2, i);
                marcado = set & mascara;
                if(marcado != set) {
                    temp = (graph[init][i] + Heuristic(i, marcado, graph, graphDP, graphPath, size, sizePow));
                    if(resultado == -1 || resultado > temp) {
                        resultado = temp;
                        graphPath[init][set] = i;
                    }
                }
            }
            graphDP[init][set] = resultado;
            return resultado;
        }
    }


    public static Map.Entry<int[], Double> GetShortestByGreedy(List<City> cities, double[][] graph){
        
        List<Integer> path = new ArrayList<>();
        int size = cities.size();
        
		double totalDistance = 0;
		int currentCity = 0;
		path.add(currentCity);
		int nearestCity = currentCity;
		double minimumDistance = Double.MAX_VALUE;

		while(path.size() < size) {
			
            for(int i = 0; i < size; i++) {
				if(i != currentCity && !path.contains(i)) {//se nao e a cidade atual, nao adiciona no path
					if(graph[currentCity][i] < minimumDistance) {//pega a cidade mais proxima
						minimumDistance = graph[currentCity][i];
						nearestCity = i;
					}
				}
			}

			path.add(nearestCity);
			totalDistance += minimumDistance;
			currentCity = nearestCity;
			minimumDistance = Double.MAX_VALUE;
		}

		totalDistance += graph[path.get(path.size() - 1)][0];
        int[] shortestPath = path.stream().mapToInt(Integer::intValue).toArray(); 

        return new AbstractMap.SimpleEntry<int[], Double>(shortestPath, totalDistance);

    }
}
