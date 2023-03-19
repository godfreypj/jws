package rankings;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.data.Status;
import org.restlet.data.MediaType;
import org.restlet.data.Form;

public class CreateResource extends ServerResource {
	public CreateResource() {
	}

	@Post
	public Representation create(Representation data) {
		Status status = null;
		String msg = null;

		// Extract the data from the POST body.
		Form form = new Form(data);
		String band = form.getValues("band");
		int ranking = Integer.parseInt(form.getFirstValue("ranking"));

		if (band == null || ranking < 1) {
			msg = "No band or invalid ranking was given.\n";
			status = Status.CLIENT_ERROR_BAD_REQUEST;
		} else {
			Rankings.add(band, ranking);
			msg = "The band '" + band + "' has been added with ranking " + ranking + ".\n";
			status = Status.SUCCESS_OK;
		}

		setStatus(status);
		return new StringRepresentation(msg, MediaType.TEXT_PLAIN);
	}
}
