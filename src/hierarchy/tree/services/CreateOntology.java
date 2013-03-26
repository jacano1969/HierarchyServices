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

@Path("/createontology/{url}/{courseId}/{jsonStr}")
public class CreateOntology {

	// Allows to insert contextual objects into the class
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	//@Produces("application/json")
	@Produces("text/plain")
	// public List<Order> getOrders(String hello) {
	public String createOntology(@PathParam("url") String url,@PathParam("courseId") int courseId,@PathParam("jsonStr") String jsonStr) {
		// List<Order> orders = new ArrayList<Order>();
		// orders.addAll(OrderDao.instance.getModel().values());
		URI uri =  uriInfo.getAbsolutePath();
		System.out.println(uri.toString());
		jsonStr = "{\"id\": 1,\"text\": \"Item 1\",\"iconCls\": \"icon-ok\",\"target\": "
				+ "{ \"jQuery180016273543753015074\": 16 },\"checked\": false,\"state\": \"open\", "
				+ "\"children\": [{ \"id\": 2, \"text\": \"Item 1_1\", \"target\": { \"jQuery180016273543753015074\": 15 "
				+ "}, \"checked\": false, \"state\": \"open\", \"children\": [{ \"id\": 3, \"text\": \"Item 1_1_1\", "
				+ "\"target\": { \"jQuery180016273543753015074\": 14 }, \"checked\": false,\"state\": \"open\",\"children\": [{ "
				+ "\"id\": 7,\"text\": \"Item 1_1_1_1\", \"target\": { \"jQuery180016273543753015074\": 13},\"checked\": false}, "
				+ "{\"id\": 6,\"text\": \"Item 1_1_1_2\",\"target\": {\"jQuery180016273543753015074\": 12},"
				+ " \"checked\": false }] }]},{ \"id\": 8, \"text\": \"Item 1_2\",\"target\": {\"jQuery180016273543753015074\": 11"
				+ "}, \"checked\": false,\"state\": \"open\",\"children\": [{\"id\": 4,\"text\": \"Item 1_2_1\", "
				+ "\"target\": { \"jQuery180016273543753015074\": 10 },\"checked\": false },{ "
				+ "\"id\": 5,\"text\": \"Item 1_2_2\",\"target\": {\"jQuery180016273543753015074\": 9},\"checked\": false}]"
				+ "},{\"id\": 9,\"text\": \"Item 1_3\",\"target\": { \"jQuery180016273543753015074\": 17 }, \"checked\": false }] }";
		System.out.println(jsonStr);
		try {
			
			
			JSONObject jsonObj = new JSONObject(jsonStr);	//will throw exception if JSON is not well formed
			
			HierarchyUtilities hierarchyUt = new HierarchyUtilities(url,courseId);
			jsonObj  = hierarchyUt.normalizeJSONObj(jsonObj);
			
			hierarchyUt.writeJSONtoFile(jsonObj);
			OntologyUtilities ontologyUt = new OntologyUtilities(jsonObj);

			ontologyUt.createTreeHierarchyOntology(jsonObj,RdfSchema.RESOURCE);
			
			ontologyUt.saveModel(hierarchyUt);
			// TODO: in php: make an alert to the user when web service responses
			return "true";	//everything went well - ontology created
		} catch (JSONException e) {
			e.printStackTrace();
			return "false"; //exception  - ontology was not created
		}
	}
}
