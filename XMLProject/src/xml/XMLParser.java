package xml;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>
 * 	This is and XML Parser. It parses an XML string or file.
 * </p>
 * @author Karissa Tuason
 * @since 1.0
 */

public class XMLParser {
	
	private class ParserNode{
		int start;
		int end;
		
		public ParserNode(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}
	
	String xml;
	HashMap<String, ArrayList<ParserNode>> xmlHash;
	
	public XMLParser(String xml) {
		this(xml, false);
	}
	
	public XMLParser(String str, boolean isFile) {
		if (isFile) {
			//get xml string block from file
		} else {
			this.xml = str;
		}
		this.xmlHash = new HashMap<String, ArrayList<ParserNode>>();
	}
	
	
	

}
