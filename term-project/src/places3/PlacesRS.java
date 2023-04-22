package src.places3;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/")
public class PlacesRS {
    @Context
    private ServletContext sctx; // dependency injection
    private static PlacesList plist; // set in populate()

    public PlacesRS() {
    }

    @GET
    @Path("/xml")
    @Produces({ MediaType.APPLICATION_XML })
    public Response getXml() {
        checkContext();
        return Response.ok(plist, "application/xml").build();
    }

    @GET
    @Path("/xml/{id: \\d+}")
    @Produces({ MediaType.APPLICATION_XML }) // could use "application/xml" instead
    public Response getXml(@PathParam("id") int id) {
        checkContext();
        return toRequestedType(id, "application/xml");
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/json")
    public Response getJson() {
        checkContext();
        return Response.ok(toJson(plist), "application/json").build();
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/json/{id: \\d+}")
    public Response getJson(@PathParam("id") int id) {
        checkContext();
        return toRequestedType(id, "application/json");
    }

    @GET
    @Path("/plain")
    @Produces({ MediaType.TEXT_PLAIN })
    public String getPlain() {
        checkContext();
        return plist.toString();
    }

    @POST
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/create")
    public Response create(@FormParam("id") int id,
            @FormParam("place") String place,
            @FormParam("points") String points) {
        checkContext();

        // Check that sufficient data are present to create a new place.
        String msg = null;
        if (place == null || points == null)
            msg = "Missing place or points.\n";

        if (msg != null)
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();

        // Create the new place.
        String record = place + "!" + points;
        int newId = plist.add(record);

        msg = "Place " + newId + " has been created.\n";
        return Response.ok(msg, "text/plain").build();
    }

    @PUT
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/update")
    public Response update(@FormParam("id") int id,
            @FormParam("place") String place,
            @FormParam("points") String points) {
        checkContext();

        // Check that sufficient data are present to do an edit.
        String msg = null;
        if (place == null && points == null) {
            msg = "Neither place nor points is given: nothing to edit.\n";
        }
        Places p = plist.find(id);
        if (p == null)
            msg = "There is no place with ID " + id + "\n";

        if (msg != null)
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
        // Update.
        if (place != null)
            p.setPlace(place);
        if (points != null)
            if (points.contains("!")) {
                String[] parts = points.split("!");
                for (int i = 1; i < parts.length; i++) {
                    p.setPoint(parts[i], i);
                }
            } else {
                p.setPoint(points, 1);
            }

        msg = "Place " + id + " has been updated.\n";
        return Response.ok(msg, "text/plain").build();
    }

    @DELETE
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/delete/{id: \\d+}")
    public Response delete(@PathParam("id") int id) {
        checkContext();
        String msg = null;
        Places p = plist.find(id);
        if (p == null) {
            msg = "There is no place with ID " + id + ". Cannot delete.\n";
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
        }
        plist.getPlcs().remove(p);
        msg = "Place " + id + " deleted.\n";

        return Response.ok(msg, "text/plain").build();
    }

    // ** utilities
    private void checkContext() {
        if (plist == null)
            populate();
    }

    private void populate() {
        plist = new PlacesList();

        String filename = "/WEB-INF/data/places.db";
        InputStream in = sctx.getResourceAsStream(filename);

        // Read the data into the array of Places.
        if (in != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String record = null;
                while ((record = reader.readLine()) != null) {
                    addPlace(record);
                }
            } catch (Exception e) {
                throw new RuntimeException("I/O failed!");
            }
        }
    }

    private int addPlace(String record) {
        int id = plist.add(record);
        return id;
    }

    // Place --> JSON document
    private String toJson(Places place) {
        String json = "If you see this, there's a problem.";
        try {
            json = new ObjectMapper().writeValueAsString(place);
        } catch (Exception e) {
        }
        return json;
    }

    // PlacesList --> JSON document
    private String toJson(PlacesList plist) {
        String json = "If you see this, there's a problem.";
        try {
            json = new ObjectMapper().writeValueAsString(plist);
        } catch (Exception e) {
        }
        return json;
    }

    // Generate an HTTP error response or typed OK response.
    private Response toRequestedType(int id, String type) {
        Places plc = plist.find(id);
        if (plc == null) {
            String msg = id + " is a bad ID.\n";
            return Response.status(Response.Status.BAD_REQUEST).entity(msg).type(MediaType.TEXT_PLAIN).build();
        } else if (type.contains("json"))
            return Response.ok(toJson(plc), type).build();
        else
            return Response.ok(plc, type).build(); // toXml is automatic
    }
}
