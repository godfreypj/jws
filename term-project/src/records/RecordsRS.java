package src.records;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.servlet.ServletContext;

@Path("/")
public class RecordsRS {
    @Context
    private ServletContext sctx; // dependency injection
    private static Records rlist; // set in populate()

    public RecordsRS() {
    }

    @GET
    @Path("/xml")
    @Produces({ MediaType.APPLICATION_XML })
    public Response getXml() {
        checkContext();
        return Response.ok(rlist, "application/xml").build();
    }

    @GET
    @Path("/xml/{id: \\d+}")
    @Produces({ MediaType.APPLICATION_XML }) // could use "application/xml" instead
    public Response getXml(@PathParam("id") int id) {
        checkContext();
        return toRequestedType(id, "application/xml");
    }

    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/plain/{id: \\d+}")
    public Response getPlain(@PathParam("id") int id) {
        checkContext();
        return toRequestedType(id, "application/plain");
    }

    @GET
    @Path("/plain")
    @Produces({ MediaType.TEXT_PLAIN })
    public String getPlain() {
        checkContext();
        return rlist.toString();
    }

    @POST
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/create")
    public Response create(@FormParam("id") int id,
            @FormParam("doctor") String doctor, // Doctorname!ID
            @FormParam("patients") String patients) { // PatientName!INSUM,PatientName!INSUM,...
        checkContext();

        // Check that sufficient data are present to create a new record.
        String msg = null;
        if (doctor == null || patients == null)
            msg = "Missing doctor or patients.\n";

        if (msg != null)
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();

        // Create a new Doctor
        Doctor d = new Doctor();
        String[] doctorRecord = doctor.split("!");
        d.setName(doctorRecord[0]);
        d.setId(Integer.parseInt(doctorRecord[1]));
        // Create the doctors patients list
        ArrayList<Patient> patientsList = new ArrayList<Patient>();
        // Create each patient
        String[] eachPatient = patients.split(",");
        for (String patient : eachPatient) {
            Patient p = new Patient();
            String[] patientRecord = patient.split("!");
            p.setName(patientRecord[0]);
            p.setInsnum(patientRecord[1]);
            p.setId(Integer.parseInt(patientRecord[2]));
            // Add the patient to the doctors list
            patientsList.add(p);
        }
        // Now we have a Doctor and his Patients list, create a record
        Record r = new Record();
        r.setDoctor(d);
        r.setPatients(patientsList);
        // Add the record to Records
        rlist.setRecords(r);

        // TODO: handle failure
        msg = "Record " + r.toString() + " has been created.\n";
        return Response.ok(msg, "text/plain").build();
    }

    @PUT
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/update")
    public Response update(@FormParam("id") int id,
            @FormParam("name") String name) {
        checkContext();

        // Check that sufficient data are present to do an edit.
        String msg = null;
        if (name == null) {
            msg = "No name is given: nothing to edit.\n";
        }
        // Create a doctor
        Doctor d = new Doctor();
        d.setId(id);
        d.setName(name);
        // See if this Doctor exists already
        List<Record> records = rlist.getRecords();
        for (Record r : records) {
            Doctor doc = r.getDoctor();
            // If our doctor is found, update the name
            if (doc.compareTo(d) == 0) {
                doc.setName(name);
            } else {
                msg = "There is no doctor with ID " + id + "\n";
            }
        }
        if (msg != null)
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();

        msg = "Doctor with " + id + " has been updated with " + name + ".\n";
        return Response.ok(msg, "text/plain").build();
    }

    @DELETE
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/delete/{id: \\d+}")
    public Response delete(@PathParam("id") int id) {
        checkContext();
        String msg = null;
        String idStr = Integer.toString(id);
        if (idStr.length() != 4) {
            msg = "Id must have exactly 4 digits.\n";
        }
        // Create a doctor and see if this Doctor exists already
        List<Record> records = rlist.getRecords();
        for (Record r : records) {
            Doctor doc = r.getDoctor();
            // If we find the Doctors record, remove it
            if (id == doc.getId()) {
                Record foundRecord = rlist.getRecord(id);
                rlist.removeRecord(foundRecord);
                // Otherwise, return failure
            } else {
                msg += "No Doctor with " + id + " found.";
                return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
            }
        }
        msg = "Doctor and patients with " + id + " deleted.\n";
        return Response.ok(msg, "text/plain").build();
    }

    // ** utilities
    private void checkContext() {
        if (rlist == null)
            populate();
    }

    private void populate() {
        rlist = new Records();

        // doctorname!ID!PatientName!INSUM,PatientName!INSUM,...
        String patientsFile = "/WEB-INF/data/patients.db";
        InputStream inPatients = sctx.getResourceAsStream(patientsFile);
        String doctorsFile = "/WEB-INF/data/doctors.db";
        InputStream inDoctor = sctx.getResourceAsStream(doctorsFile);

        // Every one line of doctors.db is a Doctor and their ID separated by "!":
        // Dr.Name!1234
        // For every line of doctors.db, split by "!"
        // where the first part is a name and the second part is an ID
        // Create a new Doctor, set parts
        // Read in 3 lines of patients DB (every doctor has 3 patients to start)
        // Every line is a Patient, their Insurance Num, and their Doctor's ID:
        // PatientName!I123!1234
        // For every line of patients.db, split by "!"
        // where the first part is a name, 2nd is an insnum, and 3rd is an ID
        // Create a new Patient, set parts
        // Create a new List of Patients, add Patient to Patients
        // Do for every 3 lines of patients.db
        // Once Doctor and Patients List are created, create a new Record, set parts
        // Once Record is created, add to Records list
        if (inDoctor != null || inPatients != null) {
            try {
                BufferedReader readerDoctor = new BufferedReader(new InputStreamReader(inDoctor));
                BufferedReader readerPatients = new BufferedReader(new InputStreamReader(inPatients));
                String recordDoctor = null;
                while ((recordDoctor = readerDoctor.readLine()) != null) {
                    Doctor d = new Doctor();
                    ArrayList<Patient> patients = new ArrayList<Patient>();
                    String[] doctorParts = recordDoctor.split("!");
                    d.setName(doctorParts[0]);
                    d.setId(Integer.parseInt(doctorParts[1]));
                    for (int i = 0; i < 3; i++) {
                        Patient p = new Patient();
                        String recordPatient = readerPatients.readLine();
                        if (recordPatient != null) {
                            String[] patientParts = recordPatient.split("!");
                            p.setName(patientParts[0]);
                            p.setInsnum(patientParts[1]);
                            p.setId(Integer.parseInt(patientParts[2]));
                            patients.add(p);
                        }
                    }
                    Record r = new Record();
                    r.setDoctor(d);
                    r.setPatients(patients);
                    rlist.setRecords(r);
                }
            } catch (Exception e) {
                throw new RuntimeException("I/O failed!");
            }
        }
    }

    // Generate an HTTP error response or typed OK response.
    private Response toRequestedType(int id, String type) {
        String result = rlist.toString();
        if (result == null) {
            String msg = id + " is a bad ID.\n";
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
        } else
            return Response.ok(result, type).build(); // toXml is automatic
    }
}
