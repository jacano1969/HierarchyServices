package hierarchy.tree.services;

import java.net.URI;

import gr.forth.ics.swkm.model2.vocabulary.RdfSchema;
import hierarchy.tree.utilities.HierarchyUtilities;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
//import org.json.JSONException;
//import org.json.JSONObject;
import skwm.ontologies.utilities.OntologyUtilities;

//maps this resource to the URL orders

@Path("/createinstances/{url}")
public class CreateInstances {

	// Allows to insert contextual objects into the class
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	//@Produces("application/json")
	@Produces("text/plain")
	// public List<Order> getOrders(String hello) {
	public String createInstances(@PathParam("url") String url) {
	
			return "true";	//everything went well - ontology created
		
	}
}
