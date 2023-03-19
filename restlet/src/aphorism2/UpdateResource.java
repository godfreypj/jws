package aphorism2;

import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.data.Status;
import org.restlet.data.MediaType;
import org.restlet.data.Form;

public class UpdateResource extends ServerResource {
	public UpdateResource() {
	}

	@Put
	public Representation update(Representation data) {
		Status status = null;
		String msg = null;

		// Extract the data from the POST body.
		Form form = new Form(data);
		String ranking = form.getFirstValue("ranking");
		String band = form.getFirstValue("band");

		if (ranking == null || band == null) {
			msg = "A Ranking and new band must be provided.\n";
			status = Status.CLIENT_ERROR_BAD_REQUEST;
		} else {
			int localRank = Integer.parseInt(ranking.trim());
			Ranking localBand = Rankings.find(localRank);
			if (localBand == null) {
				msg = "There is no band with Rank " + localRank + "\n";
				status = Status.CLIENT_ERROR_BAD_REQUEST;
			} else {
				localBand.setBandName(band);
				msg = "Ranking " + localRank + " has been updated to '" + band + "'.\n";
				status = Status.SUCCESS_OK;
			}
		}

		setStatus(status);
		return new StringRepresentation(msg, MediaType.TEXT_PLAIN);
	}
}
