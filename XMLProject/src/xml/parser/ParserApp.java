package xml.parser;

import java.io.FileNotFoundException;

import xml.exceptions.InvalidXMLFormatException;

/**
 * Class that tests XMLParser. Creates a parser using file and string. Demonstates String getValue(String element) and String[] getValues(String element).
 * @author Karissa Tuason
 *
 */
public class ParserApp {

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
			xp = new XMLParser("test.xml", true);
			String val;
			for (int i = 0; i < 5; i++) {
				val = xp.getValue("to");
				System.out.println(val);
			}
			
			
			System.out.println("\n\n\nParser from string.\n");
			xp = new XMLParser(xml);
			String elm = "from";
			String[] values= xp.getValues(elm);
			for (String string : values) {
				System.out.println(elm + "= \n" + string);
			}
			
			
		} catch (InvalidXMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
