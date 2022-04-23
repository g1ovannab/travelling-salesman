public class CoordinatesHandle {
    private static final CoordinatesHandle ch = new CoordinatesHandle();
    
    public static CoordinatesHandle getCH(){ return ch; }
    private CoordinatesHandle(){}
    
    public final static double earthRadius = 6371;
    
    /**
     * 
     * @param latitudeFrom 
     * @param longitudeFrom
     * @param latitudeTo
     * @param longitudeTo
     * @return
     */
    public static double CalculateDistances(double latitudeFrom, double longitudeFrom, double latitudeTo, double longitudeTo){
        double distanceLatitude = Math.toRadians(latitudeTo-latitudeFrom);
        double distanceLongitude = Math.toRadians(longitudeTo-longitudeFrom);

        latitudeFrom = Math.toRadians(latitudeFrom);
        latitudeTo = Math.toRadians(latitudeTo);

        // square of half the chord length between the points
        double a = Math.sin(distanceLatitude/2) * Math.sin(distanceLatitude/2) + 
        Math.sin(distanceLongitude/2) * Math.sin(distanceLongitude/2) * Math.cos(latitudeFrom) * Math.cos(latitudeTo);
        
        // angular distance in radians
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (earthRadius * c);
    }

}
