public class City {
    public String cityName; 
    public String countryName; 
    public String latitude; 
    public String longitude;
    
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
    public void setlatitude(String d) {
        this.latitude = d;
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
    public void setLongitude(String d) {
        this.longitude = d;
    }
 
    public City(){
        cityName = ""; 
        countryName = ""; 
        latitude = ""; 
        longitude = "";
    }

    public City(String city, String country, String lat, String lon){
        cityName = city; 
        countryName = country; 
        latitude = lat; 
        longitude = lon;
    }
}
