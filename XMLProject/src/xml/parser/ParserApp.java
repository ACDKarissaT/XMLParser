package xml.parser;

import xml.exceptions.InvalidXMLFormatException;

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
			xp = new XMLParser(xml);
			String elm = "note";
			System.out.println(elm+ "= \n" + xp.getValue(elm));
			elm = "close";
			System.out.println(elm+ "= \n" + xp.getValue(elm));
			elm = "from";
			System.out.println(elm+ "= \n" + xp.getValue(elm));
			elm = "heading";
			System.out.println(elm+ "= \n" + xp.getValue(elm));
			
//			String[] values = xp.getValues(el);
//			for (String string : values) {
//				System.out.println(el + "= \n" + string);
//			}
//			el = "close";
//			values = xp.getValues(el);
//			for (String string : values) {
//				System.out.println(el + "= \n" + string);
//			}
//			el = "to";
//			values = xp.getValues(el);
//			for (String string : values) {
//				System.out.println(el + "= \n" + string);
//			}
//			el = "from";
//			values = xp.getValues(el);
//			for (String string : values) {
//				System.out.println(el + "= \n" + string);
//			}
//			el = "heading";
//			values = xp.getValues(el);
//			for (String string : values) {
//				System.out.println(el + "= \n" + string);
//			}
			
		} catch (InvalidXMLFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
