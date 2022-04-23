public class City {
    private int id;
    private String cityName; 
    private String countryName; 
    private String latitude; 
    private String longitude;
    
    public int getId() { return id; }

    public String getCityName() { return cityName; }
    public void setCityName(String cn) {
        this.cityName = cn;
    }

    public String getCountryName() { return countryName; }
    public void setCountryName(String cn) {
        this.countryName = cn;
    }

    public double getLatitude() { 
        String[] latitude = this.latitude.split(" ");

        double value = Double.parseDouble(latitude[0]); 
        String direction = latitude[1].toLowerCase();

        if ("s".equals(direction)){
            return value * (-1);
        } else {
            return value;
        }
    }

    public double getLongitude() { 
        String[] longitude = this.longitude.split(" ");

        double value = Double.parseDouble(longitude[0]); 
        String direction = longitude[1].toLowerCase();

        if ("w".equals(direction)){
            return value * (-1);
        } else {
            return value;
        }
    }
 
    public City(int i, String city, String country, String lat, String lon){
        id = i;
        cityName = city; 
        countryName = country; 
        latitude = lat; 
        longitude = lon;
    }
}
