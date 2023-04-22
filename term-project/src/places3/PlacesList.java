package src.places3;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "placesList")
public class PlacesList {
    private List<Places> plcs;
    private AtomicInteger plcsId;

    public PlacesList() {
        plcs = new CopyOnWriteArrayList<Places>();
        plcsId = new AtomicInteger();
    }

    @XmlElement
    @XmlElementWrapper(name = "places")
    public List<Places> getPlcs() {
        return this.plcs;
    }

    public void setPlcs(List<Places> plcs) {
        this.plcs = plcs;
    }

    @Override
    public String toString() {
        String s = "";
        for (Places p : plcs)
            s += p.toString();
        return s;
    }

    public Places find(int id) {
        Places plc = null;
        // Search the list -- for now, the list is short enough that
        // a linear search is ok but binary search would be better if the
        // list got to be an order-of-magnitude larger in size.
        for (Places p : plcs) {
            if (p.getId() == id) {
                plc = p;
                break;
            }
        }
        return plc;
    }

    public Places findByPlace(String place) {
        Places plc = null;
        for (Places p : plcs) {
            if (p.getPlace() == place) {
                plc = p;
                break;
            }
        }
        return plc;
    }

    public int add(String record) {

        // Instantiate ID
        int id = plcsId.incrementAndGet();

        // points can be as many entries as needed
        String[] parts = record.split("!");
        // check if it exists
        Places existing = findByPlace(parts[0]);

        // it doesn't exist
        if (existing == null) {
            Places plc = new Places();
            plc.setPlace(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                plc.setPoint(parts[i], i);
                plc.setId(id);
            }
            plcs.add(plc);
        } else {
            for (int i = 1; i < parts.length; i++) {
                existing.setPoint(parts[i], i);
                // overwrite ID
                id = existing.getId();
            }
        }
        return id;
    }
}