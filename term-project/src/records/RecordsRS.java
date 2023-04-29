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
    private ServletContext sctx; // For reading in the db
    private static Records rlist; // Initial read in/creation in populate()

    // Constructor
    public RecordsRS() {
    }

    @GET
    @Path("/xml")
    @Produces({ MediaType.APPLICATION_XML })
    public Response getXml() {
        checkContext();
        // Return all in XML
        return Response.ok(rlist, "application/xml").build();
    }

    @GET
    @Path("/xml/{id: \\d+}")
    @Produces({ MediaType.APPLICATION_XML })
    public Response getXml(@PathParam("id") int id) {
        checkContext();
        // Return record by ID in XML
        return toRequestedType(id, "application/xml");
    }

    @GET
    @Path("/plain")
    @Produces({ MediaType.TEXT_PLAIN })
    public String getPlain() {
        checkContext();
        // Return all records in plain text
        return rlist.toString();
    }

    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/plain/{id: \\d+}")
    public Response getPlain(@PathParam("id") int id) {
        checkContext();
        // Return record by ID in plain text
        return toRequestedType(id, "text/plain");
    }

    @POST
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/create")
    public Response create(@FormParam("id") int id,
            @FormParam("doctor") String doctor, // Doctorname!ID
            @FormParam("patients") String patients) { // PatientName!INSUM,PatientName!INSUM,...
        checkContext();

        // Check that sufficient data is present to create a new record.
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

        // Create eachPatient list
        String[] eachPatient = patients.split(",");
        // Iterate through all given patients, creating a Patient for each one
        for (String patient : eachPatient) {
            Patient p = new Patient();
            String[] patientRecord = patient.split("!");
            p.setName(patientRecord[0]);
            p.setInsnum(patientRecord[1]);
            p.setId(Integer.parseInt(doctorRecord[1]));
            // Add the patient to the doctors list
            patientsList.add(p);
        }
        // Now we have a Doctor and his Patients list, create a record
        Record r = new Record();
        r.setDoctor(d);
        r.setPatients(patientsList);
        // Add the record to Records
        rlist.setRecords(r);

        // Return success!
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
        // Check if we got a good id
        String idStr = Integer.toString(id);
        if (idStr.length() != 4) {
            msg = "Id must have exactly 4 digits.\n";
        }

        // Create a doctor
        Doctor d = new Doctor();
        d.setId(id);
        d.setName(name);

        // See if this Doctor exists already
        boolean doctorFound = false;
        List<Record> records = rlist.getRecords();
        // Iterate over the existing records
        for (Record r : records) {
            Doctor doc = r.getDoctor();
            // If our doctor is found, update the name
            if (doc.compareTo(d) == 0) {
                doc.setName(name);
                doctorFound = true;
                break;
            }
        }
        // Otherwise, return failure
        if (!doctorFound) {
            msg = "There is no doctor with ID " + id + "\n";
        }
        if (msg != null)
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();

        // Return success!
        msg = "Doctor with " + id + " has been updated with " + name + ".\n";
        return Response.ok(msg, "text/plain").build();
    }

    @DELETE
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/delete/{id: \\d+}")
    public Response delete(@PathParam("id") int id) {
        checkContext();

        // Check that sufficient data is present to do a delete
        String msg = null;
        // Check if we got a good id
        String idStr = Integer.toString(id);
        if (idStr.length() != 4) {
            msg = "Id must have exactly 4 digits.\n";
        }

        // Create a doctor, who cares about the name!
        Doctor d = new Doctor();
        d.setId(id);
        // See if this Doctor exists already
        boolean doctorFound = false;
        List<Record> records = rlist.getRecords();
        for (Record r : records) {
            Doctor doc = r.getDoctor();
            // If our doctor is found, delete the record
            if (doc.compareTo(d) == 0) {
                rlist.removeRecord(r);
                doctorFound = true;
                break;
            }
        }
        // Otherwise, return failure
        if (!doctorFound) {
            msg = "There is no doctor with ID " + id + "\n";
        }
        if (msg != null)
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();

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

        // The db is guranteed to have 3 patients per Doctor
        String patientsFile = "/WEB-INF/data/patients.db";
        InputStream inPatients = sctx.getResourceAsStream(patientsFile);
        String doctorsFile = "/WEB-INF/data/doctors.db";
        InputStream inDoctor = sctx.getResourceAsStream(doctorsFile);

        // Check if the dbs were read in correctly
        if (inDoctor != null || inPatients != null) {
            try {
                BufferedReader readerDoctor = new BufferedReader(new InputStreamReader(inDoctor));
                BufferedReader readerPatients = new BufferedReader(new InputStreamReader(inPatients));
                String recordDoctor = null;
                // For every line, create a new Doctor and patient list
                while ((recordDoctor = readerDoctor.readLine()) != null) {
                    Doctor d = new Doctor();
                    ArrayList<Patient> patients = new ArrayList<Patient>();
                    String[] doctorParts = recordDoctor.split("!");
                    d.setName(doctorParts[0]);
                    d.setId(Integer.parseInt(doctorParts[1]));
                    // For every 3 lines, create new Patients and add them to the list
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
                    // Now we have the objects we need to create a record
                    Record r = new Record();
                    r.setDoctor(d);
                    r.setPatients(patients);
                    // Add the record to Records
                    rlist.setRecords(r);
                }
                // Return failure
            } catch (Exception e) {
                throw new RuntimeException("I/O failed!");
            }
        }
    }

    // Generate an HTTP error response or typed OK response.
    private Response toRequestedType(int id, String type) {
        // If we want plain text, we use toString
        Object result;
        if (type.equals("text/plain")) {
            Record r = rlist.getRecord(id);
            result = r.toString();
            // Otherwise, just return the record
        } else {
            result = rlist.getRecord(id);
        }
        // If we found nothing, return failure
        if (result == null) {
            String msg = id + " is a bad ID.\n";
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
        } else // Otherwise, return success!
            return Response.ok(result, type).build(); // toXml is automatic
    }
}
