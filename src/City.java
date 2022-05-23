public class City {
    private int id;
    private int index;
    private double x; 
    private double y;
    
    public int getId() { return id; }
    public int getIndex() { return index; }

    public double getX() { return x; }
    public void setX(double x){
        this.x = x;
    }

    public double getY() { return y; }
    public void setY(double y){
        this.y = y;
    }
    
    public City(int i, int in, double lat, double lon){
        id = i;
        index = in;
        x = lat; 
        y = lon; 
    }
}
