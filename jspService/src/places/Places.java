package places;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.beans.XMLEncoder; // simple and effecleactive
import javax.servlet.ServletContext;

public class Places {
    private int n = 32;
    private Place[ ] places;
    private ServletContext sctx;

    public Places() { }

    // The ServletContext is required to read the data from
    // a text file packaged inside the WAR file
    public void setServletContext(ServletContext sctx) {
	this.sctx = sctx;
    }
    public ServletContext getServletContext() { return this.sctx; }

    // getPlaces returns an XML representation of
    // the Places array
    public void setPlaces(String ps) { } // no-op
    public String getPlaces() {
	// Has the ServletContext been set?
	if (getServletContext() == null) 
	    return null;      

	// Have the data been read already?
	if (places == null) 
	    populate(); 

	// Convert the Places array into an XML document
	return toXML();
    }

    //** utilities
    private void populate() {
	String filename = "/WEB-INF/data/places.db";
	InputStream in = sctx.getResourceAsStream(filename);

	// Read the data into the array of Places. 
	if (in != null) {
	    try {
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader reader = new BufferedReader(isr);

		places = new Place[n];
		int i = 0;
		String record = null;
		while ((record = reader.readLine()) != null) {
			//Create an array with the strings split by !
		    String[] parts = record.split("!");
			//Instantiate a new Place object
		    Place p = new Place();
			//Add some info to the different parts and set them
			parts[0] = "CITY: " + parts[0].toString();
		    p.setWhere(parts[0]);
			//Check the output
			System.out.println(parts[0].toString());
			parts[1] = "POI #1: " + parts[1].toString();
		    p.setWhat(parts[1]);
			//Check the output
			System.out.println(parts[1].toString());
			parts[2] = "POI #2: " + parts[2].toString();
		    p.setWhat(parts[2]);
			//Check the output
			System.out.println(parts[2].toString());
		    places[i++] = p;
		}
	    }
	    catch (IOException e) { }
	}
    }

    private String toXML() {
	String xml = null;
	try {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    XMLEncoder encoder = new XMLEncoder(out);
	    encoder.writeObject(places); // serialize to XML
	    encoder.close();
	    xml = out.toString(); // stringify
	}
	catch(Exception e) { }
	return xml;
    }
}
