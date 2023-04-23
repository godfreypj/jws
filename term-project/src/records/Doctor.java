package src.records;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "doctor")
public class Doctor implements Comparable<Doctor> {
    // Doctor is name and an ID
    private String name; // doctors name
    private int id; // doctors ID

    public Doctor() {
    }

    @Override
    public String toString() {
        return String.format(name + "--" + "\n");
    }

    // ** properties
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getName() {
        return this.name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public int getId() {
        return this.id;
    }

    // implementation of Comparable interface
    public int compareTo(Doctor other) {
        return this.id - other.id;
    }
}