package skwm.ontologies.utilities;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import gr.forth.ics.swkm.model2.*;
import gr.forth.ics.swkm.model2.io.Format;
import gr.forth.ics.swkm.model2.io.RdfIO;
import gr.forth.ics.swkm.model2.vocabulary.RdfSchema;
import hierarchy.tree.utilities.HierarchyUtilities;

public class OntologyUtilities {

	private static final String TAG_CHILDREN = "children";
	private static final String TAG_TEXT = "text";
	private static final String TAG_ID = "id";
	private static final String BASE_URI = "http://www.ics.forth.gr/ConceptOntologies#";
	private static final String TAG_RDF = "rdf";
	private Model model;
	JSONObject childObj, rootObj, remainObj;

	public OntologyUtilities(JSONObject jsonObj) {
		model = ModelBuilder.newHorizontal().build();
		rootObj = jsonObj;
		remainObj = null;
		childObj = null;

	}

	/**
	 * Takes the JSON Object and returns the RDF Ontology file on /RDF/server
	 * TODO: also needs adding data about Moodle link & course that identify
	 * uniquely the RDF file
	 * 
	 * @param jsonObj
	 *            - is the jsonObj that we will be traversing
	 * @param concept
	 *            - is the parent class of the json object. If object is root ,
	 *            the URI is RdfSchema.RESOURCE
	 * @throws IOException
	 */
	public void createTreeHierarchyOntology(JSONObject jsonObj, Uri parent) {
		/* variables declaration */
		String text = null;
		Integer id = null;
		Uri concept = null;
		JSONArray childrenArr;
		String textUri = null;
		Iterator<?> jsonObjItr = jsonObj.keys();
		/* variables declaration - end */

		// if it is not a leaf: we need to traverse to the leaf
		if (jsonObjItr.hasNext()) {
			try {
				text = (String) jsonObj.get(TAG_TEXT);
				text = text.replaceAll("\\s", ""); // remove spaces
				id = (Integer) jsonObj.get(TAG_ID);
				concept = formOntologyUri(Integer.toString(id));
				// System.out.println(text);
				if (jsonObj.has(TAG_CHILDREN)) {
					// model.add().newClass(concept);
					// TODO:do I need to add new class? :Answer : omitted -
					// protege does not create classes
					childrenArr = (JSONArray) jsonObj.get(TAG_CHILDREN);
					for (int i = 0; i < childrenArr.length(); i++) {
						childObj = childrenArr.getJSONObject(i);
						createTreeHierarchyOntology(childObj, concept);
					}
				}
				// add the class in correspodent hierarchy
				model.add().s(concept).p(RdfSchema.SUBCLASSOF).o(parent);
				if (parent.equals(RdfSchema.RESOURCE)) {
					// <rdf:Property rdf:about="&moodle;text"
					// a:maxCardinality="1"
					// rdfs:label="text">
					// <rdfs:domain rdf:resource="&moodle;Item_1"/>
					// <rdfs:range rdf:resource="&rdfs;Literal"/>
					// </rdf:Property>
					// add the text property for all classes
					textUri = BASE_URI + "text";
					model.add().newProperty(textUri);
					model.add().s(textUri).p(RdfSchema.DOMAIN).o(concept);
					model.add().s(textUri).p(RdfSchema.RANGE)
							.o(RdfSchema.LITERAL);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void printModel() {
		for (Triple t : model.triples().fetch()) {
			System.out.print(t.toSimpleString());
		}

	}

	public void saveModel(HierarchyUtilities ut) {

		String path = ut.createPathFiles(TAG_RDF);
		try {
			// turtle format as it is nearest to JSON
			RdfIO.write(model, Format.TURTLE).toFile(
					new File(path + "conceptsRDFSchema.ttl"));
			System.out.println(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// TODO: add information about the learning object (LOM)
	private void createLOMOntology() {
		;
	}

	private Uri formOntologyUri(String text) {
		Uri concept;
		String conceptStr = BASE_URI + text;
		concept = Uri.parse(conceptStr);
		return concept;
	}

}
