public class CoordinatesHandle {
    private static final CoordinatesHandle ch = new CoordinatesHandle();
    
    public static CoordinatesHandle getCH(){ return ch; }
    private CoordinatesHandle(){}
        
    public static double CalculateDistances(double xFrom, double yFrom, double xTo, double yTo){
        return Math.sqrt((Math.pow((xFrom - xTo), 2)) + (Math.pow((yFrom - yTo), 2)));
    }

    /**
     * Calculates the distance between an entire permutation of cities. If the permutation is 0123,
     * this function calculates the distance between city 0 and 1, added with the distance between city 1 and 2,
     * added with the distance between city 2 and 3, added with the distance between city 3 and 0.
     * 
     * @param permutation The permutation of cities;
     * @param graph The "graph" representation in which we obtain the distances through it;
     * @returns The distance between a permutation of cities.
     */
    public static double GetDistanceBetweenPermutation(int[] permutation, double[][] graph){
        int startCity = permutation[0];

        double distance = 0;

        int fromID, toID;
        for (int i = 0; i < permutation.length; i++){
            fromID = permutation[i];

            if (i == permutation.length - 1){
                toID = startCity;
            } else {
                toID = permutation[i + 1];
            }
            distance += graph[fromID][toID];
        }
        return distance;
    }
}
