package xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
	
	private String xml;
	private HashMap<String, ArrayList<ParserNode>> xmlHash;
	private LinkedList<String> sTag, eTag;
	
	public XMLParser(String xml) {
		this(xml, false);
	}
	
	public XMLParser(String str, boolean isFile) {
		sTag = new LinkedList<String>();
		eTag = new LinkedList<String>();
		if (isFile) {
			//get xml string block from file
		} else {
			this.xml = str;
		}
		this.xmlHash = new HashMap<String, ArrayList<ParserNode>>();
	}
	
	private void validateXML() {
		//Search through xml string and
		//check for prolog as first entry
		//check for proper closing of tags
		//check for root tag
		//throw an exception if xml is not valid
	}
	
	private void parseXML() {
		//parse the xml
		//
	}

}
