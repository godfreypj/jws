package src.records;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = "record")
public class Record {
    private Doctor doctor;
    private List<Patient> patients;

    public Record() {
        patients = new CopyOnWriteArrayList<Patient>();
        doctor = new Doctor();
    }

    @XmlElementWrapper(name = "patients")
    @XmlElement(name = "patient")
    public List<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    @XmlElement(name = "doctor")
    public Doctor getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        String r = "";
        r += doctor.toString();
        for (Patient p : patients) {
            r += p.toString();
        }
        return r;
    }

    public List<Patient> findPatient(int id) {
        List<Patient> id_patients = new ArrayList<Patient>();
        for (Patient p : patients) {
            if (p.getId() == id) {
                id_patients.add(p);
            }
        }
        return id_patients;
    }

    public Record add(String record) {
        // The record comes in looking like this:
        // Dr.Name!1234!PatientName!I123!1234!PatientName!I321!1234...PatientName!...
        String[] parts = record.split("!");
        int id = Integer.parseInt(parts[1]);
        // Create a new Doctor
        Doctor d = new Doctor();
        d.setId(id);
        d.setName(parts[0]);
        // Loop through the array of patients, every 2 elements is 1 patient
        for (int i = 2; i < parts.length; i += 3) {
            // Create a new patient
            Patient p = new Patient();
            p.setName(parts[i]);
            p.setInsnum(parts[i + 1]);
            p.setId(Integer.parseInt(parts[i + 2]));
            patients.add(p);
        }
        // Create a new record
        Record r = new Record();
        r.setDoctor(d);
        r.setPatients(patients);
        return r;
    }
}