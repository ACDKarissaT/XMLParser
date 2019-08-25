package xml.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xml.exceptions.InvalidXMLFormatException;

/**
 * <p>
 * 	This is a simple XML Parser. It parses an XML string or file. Once parsed can get the element values using getValue(element).
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
	 * A hashmap of elements with the start and end index of their value on the xml string.
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
	 * @throws InvalidXMLFormatException thros if xml is not valid
	 * @throws FileNotFoundException 
	 */
	public XMLParser(String xml) throws InvalidXMLFormatException, FileNotFoundException{
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
	 * @throws InvalidXMLFormatException throws if xml is not valid
	 * @throws FileNotFoundException 
	 */
	public XMLParser(String str, boolean isFile) throws InvalidXMLFormatException, FileNotFoundException {
		sTag = new LinkedList<TagNode>();
		eTag = new ArrayList<TagNode>();
		oTag = new LinkedList<TagNode>();
		if (isFile) {
			getFileString(str);
		} else {
			this.xml = str;
		}
		xml = xml.replaceAll("\r\n", "");
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
	
	private void getFileString(String fileName) throws FileNotFoundException {
		File f = new File(fileName);
		StringBuilder str = new StringBuilder();
		if (f.exists()) {
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) {
				str.append(sc.nextLine());
			}
			sc.close();
			xml = str.toString();
			System.out.println(xml);
		} else {
			throw new FileNotFoundException();
		}
	}
	
	/**
	 * <p>
	 * Validates if xml string contains the xml prolog.
	 * </p>
	 * 
	 * @return void
	 * @throws InvalidXMLFormatException throws if xml is not valid
	 */
	private void validateXML() throws InvalidXMLFormatException {
		getTags();
		if (!oTag.isEmpty()) {
			TagNode tn = oTag.get(0);
			String[] prolog_elements = tn.tag.split(" +");
			
			
		} else {
			throw new InvalidXMLFormatException();
		}
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
			indexOffset += string.length() + 2;
		}
		s = new StringBuilder(xml);
		indexOffset = 0;
		for (String string : start) {
			index =s.indexOf("<" + string + ">");
			sTag.add(new TagNode(string, index + indexOffset + string.length()+2, string.length()+2));
			s.delete(index, index + string.length()+ 2);			
			indexOffset += string.length() + 2;
		}
		s= new StringBuilder(xml);
		indexOffset = 0;
		for (String string : end) {
			index =s.indexOf("</" + string + ">");
			eTag.add(new TagNode(string, index + indexOffset, string.length()+3));
			s.delete(index, index + string.length()+3);
			indexOffset += string.length() + 3;
		}
		
//		System.out.println(oTag);
//		System.out.println(sTag);
//		System.out.println(eTag);
	}
	
	/**
	 * Returns the values found for the xml element.
	 * @author Karissa Tuason
	 * @since 1.0 
	 * @param tag the element name
	 * @return The values of the element or null if element isn't found.
	 */
	public String[] getValues(String tag) {
		ArrayList<ParserNode> nodes = xmlHash.get(tag);
		String[] values = null;
		if (nodes != null) {
			values = new String[nodes.size()];
			int count = 0;
			for (ParserNode parserNode : nodes) {
				values[count] = xml.substring(parserNode.start, parserNode.end);
				count++;
			}
			
		}
		return values;
	}
	
	/**
	 * Matches the tag with the elements stored and returns the first value.
	 * @param tag The element name.
	 * @return the value of the element or null if element isn't found;
	 */
	public String getValue(String tag) {
		ArrayList<ParserNode> nodes = xmlHash.get(tag);
		String value = null;
		if (nodes != null && !nodes.isEmpty()) {
			ParserNode first = nodes.get(0);
			value = xml.substring(first.start, first.end);
		}
		return value;
	}

}
