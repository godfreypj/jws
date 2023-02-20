package places;

public class Place {
    private String where;   // city
    private String what;  // points of interest
    
    public Place() { }

    public void setWhere(String where) {
	this.where = where;
    }
    public String getWhere() {
	return this.where;
    }

    public void setWhat(String what) {
	this.what = what;
    }
    public String getWhat() {
	return this.what;
    }
}
