package hierarchy.tree.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Objects of this class represent the e-learning platfom's objects So they keep
 * data as url and courseId.
 * 
 * 
 * @author marigianna
 * 
 */
public class HierarchyUtilities {

	private static final String TAG_CHILDREN = "children";
	private static final String TAG_TARGET = "target";
	private static final String CATALINA_BASE = "catalina.home";
	private static final String TAG_LIB = "lib";
	private static final String TAG_JSON = "json";

	private String pathUrl; // is kept as string in a normalized way, it is used
							// for savning data into the relevand folder
	private int courseId; // courseId
	JSONObject childObj;

	public HierarchyUtilities(String pathUrl, int courseId) {
		this.pathUrl = pathUrl;
		this.courseId = courseId;
		this.childObj = null;
	}

	public String getPathUrl() {
		return this.pathUrl;
	}

	public int getCourseId() {
		return this.courseId;
	}

	/**
	 * 
	 * @return pathStr : it is the normalized path of the resource which is
	 */
	/*
	 * public String normalizePath() { String path = getPathUrl(); String
	 * pathStr = path.toString(); // split string accodring to / String[] tokens
	 * = pathStr.split("/");
	 * 
	 * List<String> list = new ArrayList<String>(Arrays.asList(tokens)); //
	 * remove last two elements for (int i = 0; i < 2; i++)
	 * list.remove(list.size() - 1); pathStr = list.toString(); pathStr =
	 * pathStr.replaceAll("\\W", ""); // just leave alphanumerical // characters
	 * 
	 * // pathStr = path.getScheme(); System.out.println("pathStr is:" +
	 * pathStr); return pathStr; }
	 */

	/**
	 * Creates a JSON file with the given parameter Calculated the path on the
	 * server
	 * 
	 * @param jsonObj
	 * @throws IOException
	 */
	public void writeJSONtoFile(JSONObject jsonObj) {

		String fileName = TAG_JSON + "_" + getCourseId() + "." + TAG_JSON;
		String path = createPathFiles(TAG_JSON);
		try {
			// Create file
			FileWriter fstream = new FileWriter(path + fileName);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("[" + jsonObj.toString() + "]");
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		// WriteToFile(path, jsonObj.toString(), "tmpJsonFile.json");

	}

	/**
	 * Create the path in catalina home. It makes the relevant path for link -
	 * courseID in order to organize the resources
	 * 
	 * @param file
	 *            --> this case RDF or JSON
	 * @return path
	 */
	public String createPathFiles(String fileType) {
		String path = System.getProperty(CATALINA_BASE);

		path += File.separator + TAG_LIB + File.separator + fileType
				+ File.separator + getPathUrl() + File.separator
				+ getCourseId() + File.separator;

		File file = new File(path);

		if (!file.exists())
			file.mkdirs();

		try {
			Process p = Runtime.getRuntime().exec(
					"chmod -R 777 " + getPathUrl());
			p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}

	public JSONObject normalizeJSONObj(JSONObject jsonObj) {

		JSONArray childrenArr;

		Iterator<?> jsonObjItr = jsonObj.keys();
		/* variables declaration - end */

		// if it is not a leaf: we need to traverse to the leaf
		if (jsonObjItr.hasNext()) {
			try {

				if (jsonObj.has(TAG_TARGET))
					jsonObj.remove(TAG_TARGET);
				if (jsonObj.has(TAG_CHILDREN)) {
					childrenArr = (JSONArray) jsonObj.get(TAG_CHILDREN);
					for (int i = 0; i < childrenArr.length(); i++) {
						childObj = childrenArr.getJSONObject(i);
						normalizeJSONObj(childObj);
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonObj;

	}

}
