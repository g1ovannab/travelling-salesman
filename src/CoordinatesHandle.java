public class CoordinatesHandle {
    private static final CoordinatesHandle ch = new CoordinatesHandle();
    
    public static CoordinatesHandle getCH(){ return ch; }
    private CoordinatesHandle(){}
    
    public final static double earthRadius = 6371; // Kilometers.
    
    /**
     * Calculates the distance from one city to other, based on the coordinates from each city.
     * 
     * @param latitudeFrom Latitude of the city we are leaving from;
     * @param longitudeFrom Longitude of || ;
     * 
     * @param latitudeTo Latitude of the city we are going to;
     * @param longitudeTo Longitude of || ;
     * @returns The distance in kilometers.
     */
    public static double CalculateDistances(double latitudeFrom, double longitudeFrom, double latitudeTo, double longitudeTo){
        double distanceLatitude = Math.toRadians(latitudeTo-latitudeFrom);
        double distanceLongitude = Math.toRadians(longitudeTo-longitudeFrom);

        latitudeFrom = Math.toRadians(latitudeFrom);
        latitudeTo = Math.toRadians(latitudeTo);

        // Square of half the chord length between the points;
        double a = Math.sin(distanceLatitude/2) * Math.sin(distanceLatitude/2) + 
        Math.sin(distanceLongitude/2) * Math.sin(distanceLongitude/2) * Math.cos(latitudeFrom) * Math.cos(latitudeTo);
        
        // Angular distance in radians;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (earthRadius * c);
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
