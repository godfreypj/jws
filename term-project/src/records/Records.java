package src.records;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "records")
public class Records {
    private List<Record> records;

    public Records() {
        records = new CopyOnWriteArrayList<Record>();
    }

    @XmlElement(name = "record")
    public List<Record> getRecords() {
        return this.records;
    }

    public Record getRecord(int id) {
        Doctor d = new Doctor();
        Record returnRecord = null;
        d.setId(id);
        for (Record r : this.records) {
            if (r.getDoctor().compareTo(d) == 0) {
                returnRecord = r;
            }
        }
        return returnRecord;
    }

    public void setRecords(Record record) {
        this.records.add(record);
    }

    public Doctor findDoctor(int id) {
        // Create a Docto obj for return
        Doctor doc = new Doctor();
        // Loop through the record
        for (Record r : records) {
            Doctor d = r.getDoctor();
            if (d.getId() == id) {
                doc = d;
            }
        }
        return doc;
    }

    public String toString() {
        String res = "";
        for (Record r : records) {
            res += r + "\n";
        }
        return res;
    }

    public void removeRecord(Record foundRecord) {
        this.records.remove(foundRecord);
    }
}