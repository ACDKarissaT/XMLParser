package xml.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xml.exceptions.InvalidXMLFormatException;

/**
 * <p>
 * 	This is a simple XML Parser. It parses an XML string or file.
 * </p>
 * 
 * @author Karissa Tuason
 * @since 1.0
 */

public class XMLParser {
	
	/**
	 * <p>
	 * This class is a helper class of XMLParser. 
	 * Its a container start and end index.
	 * </p>
	 * @author Karissa Tuason
	 * @since 1.0
	 */
	private class ParserNode implements Comparable<ParserNode>{
		int start;
		int end;
		
		public ParserNode(int start, int end) {
			this.start = start;
			this.end = end;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o != null && o instanceof ParserNode) {
				ParserNode pn = (ParserNode) o;
				return this.start == pn.start && this.end == pn.end;
			}
			return false;
		}
		
		@Override
		public String toString() {
			return "ParserNode[" + this.start + " " + this.end+ "]";
		}

		@Override
		public int compareTo(ParserNode o) {
			// TODO Auto-generated method stub
			if (o != null) {
				return Integer.compare(this.start, o.start);
			}
			else return 0;
		}
	}
	
	/**
	 * This class is a helper class for XMLParser.
	 * It is a container for tags and where they appear in the xml string;
	 * 
	 * @author Karissa Tuason
	 *
	 */
	private class TagNode{
		String tag;
		int index;
		int length;
		
		public TagNode(String tag, int index, int length) {
			this.tag = tag;
			this.index = index;
			this.length = length;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o != null && o instanceof TagNode) {
				TagNode tn = (TagNode) o;
				return getTagName(this.tag).equals(getTagName(tn.tag));
			}
			return false;
		}
		
		@Override
		public String toString() {
			return "TagNode[" + this.tag + " " + this.index+ " " + this.length + "]";
		}
	}
	
	/**
	 * The string representation of the xml
	 */
	private String xml;
	
	/**
	 * A hashmap of elements with their start and end index of their value on the xml string.
	 */
	private HashMap<String, ArrayList<ParserNode>> xmlHash;
	
	/**
	 * List of start tags.
	 */
	private LinkedList<TagNode> sTag;
	
	/**
	 * List of end tags
	 */
	private ArrayList<TagNode> eTag; 
	
	/**
	 * List of other tags (prolog, comments, etc)
	 */
	private LinkedList<TagNode> oTag;
	
	/**
	 * <p>
	 * Constructs a parser of the given xml string representation.
	 * </p>
	 * 
	 * @author Karissa Tuason
	 * @since 1.0
	 * @param xml the string representation of the xml
	 * @throws InvalidXMLFormatException 
	 */
	public XMLParser(String xml) throws InvalidXMLFormatException {
		this(xml, false);
	}
	
	/**
	 * <p>
	 * Constructs a parser of the given xml file or xml string representation.
	 * </p>
	 * 
	 * @author Karissa Tuason
	 * @since 1.0
	 * @param str string either a filename or the string representation of the xml
	 * @param isFile boolean if str is a filename.
	 * @throws InvalidXMLFormatException 
	 */
	public XMLParser(String str, boolean isFile) throws InvalidXMLFormatException {
		sTag = new LinkedList<TagNode>();
		eTag = new ArrayList<TagNode>();
		oTag = new LinkedList<TagNode>();
		if (isFile) {
			//get xml string block from file
		} else {
			this.xml = str;
		}
		this.xmlHash = new HashMap<String, ArrayList<ParserNode>>();
		//validate xml
		validateXML();
		//parse xml
		parseXML();
		
		
//		System.out.println(oTag);
//		System.out.println(sTag);
//		System.out.println(eTag);
		
//		System.out.println(xmlHash);
	}
	
	/**
	 * <p>
	 * Validates if xml string is in xml format.
	 * </p>
	 * 
	 * @return void
	 * @throws InvalidXMLFormatException 
	 */
	private void validateXML() throws InvalidXMLFormatException {
		getTags();
		//fixETags();
		//check for prolog as first entry
		//check for proper closing of tags
		//check for root tag
		//throw an exception if xml is not valid
	}
	
	
	/**
	 * <p>
	 * Parses the xml string by populating the element Hashmap
	 * </p>
	 * 
	 * @author Karissa Tuason
	 * @since 1.0
	 * 
	 * @return void
	 * @throws InvalidXMLFormatException 
	 */
	private void parseXML() throws InvalidXMLFormatException {
		//parse the xml
		//go through tags and populate hashmap
		TagNode start;
		while (sTag.peek() != null) {
			start = sTag.poll();
			putTag(start);
		}
		
		if (!eTag.isEmpty()) {
			throw new InvalidXMLFormatException();
		}
		Set<String> keys = xmlHash.keySet();
		for (String string : keys) {
			Collections.sort(xmlHash.get(string));
		}
		
		
	}
	
	/**
	 * Iterates through the start tag nodes and finds the end tag node pair. Then it populates the xmlHash with
	 * a ParserNode corresponding to the tag in tagNode
	 * 
	 * @param tagNode the tagNode with its tag string and index
	 * @throws InvalidXMLFormatException throws exception with an ending node is not found
	 */
	private void putTag(TagNode tagNode) throws InvalidXMLFormatException {
		String tag = tagNode.tag;
		int startIndex = -1;
		int endIndex = -1;
		int i;
		startIndex = tagNode.index;
		if(tag.endsWith("/")) {
			endIndex = startIndex;
		} else {
			//find endTag
			i = eTag.indexOf(tagNode);
			if (i != -1) {
				endIndex = eTag.get(i).index;
			}
			while (sTag.peek() != null && endIndex > sTag.peek().index) {
				putTag(sTag.poll());
			}
			i = eTag.indexOf(tagNode);
			if (i != -1) {
				endIndex = eTag.get(i).index;
				eTag.remove(i);
			}
			if (endIndex == -1) {
				throw new InvalidXMLFormatException();
			}
		}
		placeToHash(getTagName(tag), new ParserNode(startIndex, endIndex));
		
	}
	
	/**
	 * Places given tag and parser node to the xml hashmap.
	 * @param tag the element name
	 * @param pn the parser node (start and end index of value)
	 */
	private void placeToHash(String tag, ParserNode pn) {
		if (xmlHash.containsKey(tag)) {
			xmlHash.get(tag).add(pn);
		} else {
			ArrayList<ParserNode> al = new ArrayList<ParserNode>();
			al.add(pn);
			xmlHash.put(tag, al);
		}
	}
	
	
	
	
	/**
	 * <p>Gets the first word of a given tag</p>
	 * 
	 * @author Karissa Tuason
	 * @since 1.0
	 * @param String tag the string between "<" and ">"
	 * @return the first word in the tag before " " or ">"
	 */
	private String getTagName(String tag) {
		int i = tag.indexOf(" ");
		if (i > -1) {
			return tag.substring(0, i);
		} else {
			return tag;
		}
	}
	
	/**
	 * <p>
	 * This method finds all xml tags (Substrings between "<" and ">") including prologs and comments
	 * and sorts them into start tags, end tags, and other tags.
	 * </p>
	 * 
	 * @author Karissa Tuason
	 * @since 1.0
	 * 
	 * @return void
	 */
	private void getTags() {
		//match and pattern
		ArrayList<String> start = new ArrayList<String>();
		ArrayList<String> end = new ArrayList<String>();
		ArrayList<String> other = new ArrayList<String>();
		String regex = "<[^<]+>";
		Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(xml);
		String tag = "";
		while (m.find()) {
			tag = m.group(1);
			//validate tag
			if (tag.charAt(1) == '?' || tag.charAt(1) == '!') {
				other.add(tag.substring(1, tag.length()-1));
			} else {
				if (tag.charAt(1) != '/') {
					start.add(tag.substring(1, tag.length()-1));
				} else {
					end.add(tag.substring(2, tag.length()-1));
				}
			}
		}
		
//		System.out.println(other);
//		System.out.println(start);
//		System.out.println(end);
		
		int indexOffset = 0;
		int index = -1;
		StringBuilder s = new StringBuilder(xml);
		for (String string : other) {
			index =s.indexOf("<" + string + ">");
			oTag.add(new TagNode(string, index + indexOffset + string.length()+2, string.length()+2));
			s.delete(index, index + string.length()+2);		
			indexOffset += string.length() + 1;
		}
		s = new StringBuilder(xml);
		indexOffset = 0;
		for (String string : start) {
			index =s.indexOf("<" + string + ">");
			sTag.add(new TagNode(string, index + indexOffset + string.length()+2, string.length()+2));
			s.delete(index, index + string.length()+ 2);			
			indexOffset += string.length() + 1;
		}
		s= new StringBuilder(xml);
		indexOffset = 0;
		for (String string : end) {
			index =s.indexOf("</" + string + ">");
			eTag.add(new TagNode(string, index + indexOffset + string.length()+2, string.length()+2));
			s.delete(index, index + string.length()+2);
			indexOffset += string.length() + 1;
		}
		
//		System.out.println(oTag);
//		System.out.println(sTag);
//		System.out.println(eTag);
	}
	
	public static void main(String[] args) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<note>\r\n" + 
				"<close />" +
				"<to>Tove</to>\r\n" + 
				"<from><from>Jani</from></from>\r\n" + 
				"<heading>Reminder</heading>\r\n" + 
				"<body   >Don't forget me this weekend!</body     >\r\n" + 
				"<from> hello </from>\r\n"+
				"</note>";
		XMLParser xp;
		try {
			xp = new XMLParser(xml);
		} catch (InvalidXMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
