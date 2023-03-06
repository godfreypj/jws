package places3;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "places")
public class Places implements Comparable<Places> {
    private String place; // place
    private String points = ""; // points of interest
    private int id; // identifier used as lookup-key

    public Places() {
    }

    @Override
    public String toString() {
        return String.format("%2d: ", id) + place + " ==> " + points + "\n";
    }

    // ** properties
    public void setPlace(String place) {
        this.place = place;
    }

    @XmlElement
    public String getPlace() {
        return this.place;
    }

    public void setPoint(String point, int num){
        this.points += " " + num + ". " + point;
    }

    @XmlElement
    public String getPoints() {
        return this.points;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public int getId() {
        return this.id;
    }

    // implementation of Comparable interface
    public int compareTo(Places other) {
        return this.id - other.id;
    }
}