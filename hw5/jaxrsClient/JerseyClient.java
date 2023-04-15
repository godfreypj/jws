import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.representation.Form;

public class JerseyClient {
    private static final String baseUrl = "http://localhost:8080/";
    private static final String placesUrl = "places3/resourcesC/";
    private static final String predictionsUrl = "predictions3/resourcesP/";

    public static void main(String[] args) {
        new JerseyClient().demo();
    }

    private void demo() {
        Client client = Client.create();
        client.setFollowRedirects(true); // in case the service redirects

        // Demo for places
        WebResource placesResource = client.resource(baseUrl + placesUrl);
        getPlaceById(placesResource);
        postPlace(placesResource);
        deletePlace(placesResource);
        // Demo for predictions
        WebResource predictionsResource = client.resource(baseUrl + predictionsUrl);
        getPredictionById(predictionsResource);
        postPrediction(predictionsResource);
        deletePrediction(predictionsResource);
    }

    private void getPlaceById(WebResource resource) {
        String id = "xml/3";
        // GET all XML by ID
        String response = resource.path(id)
                .accept(MediaType.APPLICATION_XML_TYPE)
                .get(String.class);
        report("GET place id=3 in XML:\n", response);

    }

    private void getPredictionById(WebResource resource) {
        String id = "xml/32";
        // GET all XML by ID
        String response = resource.path(id)
                .accept(MediaType.APPLICATION_XML_TYPE)
                .get(String.class);
        report("GET prediction id=32 in XML:\n", response);

    }

    private void postPlace(WebResource resource) {

        String create = "create";
        Form form = new Form(); // HTTP body, a simple hash
        form.add("place", "Woodstock");
        form.add("points", "Home of Woodstock69!A farm to this day");

        String response = resource.path(create)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.TEXT_PLAIN_TYPE)
                .post(String.class, form);
        report("POST a new place:\n", response);
    }

    private void postPrediction(WebResource resource) {

        String create = "create";
        Form form = new Form(); // HTTP body, a simple hash
        form.add("who", "Philip Godfrey");
        form.add("what", "I will pass this class");

        String response = resource.path(create)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .accept(MediaType.TEXT_PLAIN_TYPE)
                .post(String.class, form);
        report("POST a new prediction:\n", response);
    }

    private void deletePlace(WebResource resource) {
        String delete = "delete/3";
        // GET all XML by ID
        String response = resource.path(delete)
                .accept(MediaType.TEXT_PLAIN_TYPE)
                .delete(String.class);
        report("DELETE:\n", response);
    }

    private void deletePrediction(WebResource resource) {
        String delete = "delete/31";
        // GET all XML by ID
        String response = resource.path(delete)
                .accept(MediaType.TEXT_PLAIN_TYPE)
                .delete(String.class);
        report("DELETE:\n", response);
    }

    private void report(String msg, String response) {
        System.out.println("\n" + msg + response);
    }
}
