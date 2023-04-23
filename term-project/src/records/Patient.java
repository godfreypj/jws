package src.records;

import javax.xml.bind.annotation.XmlElement;

public class Patient implements Comparable<Patient> {
    // Patient is made up of a name, insurance number and their doctors ID
    private String name; // patients name
    private String insnum = ""; // insurance number
    private int id; // patients doctors ID

    public Patient() {
    }

    @Override
    public String toString() {
        return String.format(name + " ==> " + insnum + "\n");
    }

    // ** properties
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getName() {
        return this.name;
    }

    public void setInsnum(String insum) {
        this.insnum = insum;
    }

    @XmlElement
    public String getInsnum() {
        return this.insnum;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public int getId() {
        return this.id;
    }

    // implementation of Comparable interface
    public int compareTo(Patient other) {
        return this.id - other.id;
    }
}