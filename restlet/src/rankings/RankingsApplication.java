package rankings;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.routing.Router;

import org.restlet.data.Status;
import org.restlet.data.MediaType;

public class RankingsApplication extends Application {
	@Override
	public synchronized Restlet createInboundRoot() {
		// To illlustrate the different API possibilities, implement the
		// DELETE operation as an anonymous Restlet class. For the
		// remaining operations, follow Restlet best practices and
		// implement each as a Java class.

		// DELETE handler
		Restlet janitor = new Restlet(getContext()) {
			public void handle(Request request, Response response) {
				String msg = null;

				String localRanking = (String) request.getAttributes().get("rankings");
				System.out.println("PHIL" + localRanking);

				if (localRanking == null)
					System.out.println("TOMATO");
					msg = badRequest("No rank given.\n");

				Integer rank = null;
				try {
					rank = Integer.parseInt(localRanking.trim());
				} catch (Exception e) {
					msg = badRequest("Ill-formed ID.\n");
				}

				Ranking band = Rankings.find(rank);


				if (band == null)
					msg = badRequest("No band found with rank " + rank + "\n");
				else {
					Rankings.getList().remove(band);
					msg = "Band " + rank + " removed.\n";
				}

				// Update rankings
				Rankings.updateRankings();

				// Generate HTTP response.
				response.setEntity(msg, MediaType.TEXT_PLAIN);
			}
		};

		// Create the routing table.
		Router router = new Router(getContext());
		router.attach("/", PlainResource.class);
		router.attach("/xml", XmlAllResource.class);
		router.attach("/xml/{id}", XmlOneResource.class);
		router.attach("/json", JsonAllResource.class);
		router.attach("/create", CreateResource.class);
		router.attach("/update", UpdateResource.class);
		router.attach("/delete/{rankings}", janitor); // instance of anonymous class

		return router;
	}

	private String badRequest(String msg) {
		Status error = new Status(Status.CLIENT_ERROR_BAD_REQUEST, msg);
		return error.toString();
	}

}
