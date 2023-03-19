package rankings;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.ext.xml.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.restlet.data.Status;
import org.restlet.data.MediaType;

public class XmlOneResource extends ServerResource {
	public XmlOneResource() {
	}

	@Get
	public Representation toXml() {
		// Extract the bands rank.
		String rank = (String) getRequest().getAttributes().get("rank");
		if (rank == null)
			return badRequest("No Rank provided\n");

		int localRank;
		try {
			localRank = Integer.parseInt(rank.trim());
		} catch (Exception e) {
			return badRequest("No such band\n");
		}

		// Search for the Band.
		Ranking localRanking = Rankings.find(localRank);
		if (localRanking == null)
			return badRequest("No band with rank " + localRanking + "\n");

		// Generate the XML response.
		DomRepresentation dom = null;
		try {
			dom = new DomRepresentation(MediaType.TEXT_XML);
			dom.setIndenting(true);
			Document doc = dom.getDocument();

			Element root = doc.createElement("band");
			root.appendChild(doc.createTextNode(localRanking.toString()));
			doc.appendChild(root);
		} catch (Exception e) {
		}
		return dom;
	}

	private StringRepresentation badRequest(String msg) {
		Status error = new Status(Status.CLIENT_ERROR_BAD_REQUEST, msg);
		return new StringRepresentation(error.toString());
	}
}
