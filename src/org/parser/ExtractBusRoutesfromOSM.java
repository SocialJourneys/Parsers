package org.parser;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class ExtractBusRoutesfromOSM extends DefaultHandler {
	
	
 

	List routes;
	int count =0;
	
	private String tempVal;
	
	//to maintain context
	private Way tempRoute;
	private boolean isBusRoute = false;
	 BufferedWriter bufferWritter;
	 String originalOSMfile;
	 String roadsXMLfile;
	
	
	public ExtractBusRoutesfromOSM(String originalOSMfile, String roadsXMLfile) throws IOException{
		routes = new ArrayList();
		this.originalOSMfile = originalOSMfile;
		this.roadsXMLfile = roadsXMLfile;
		
       
       
	}
	
	public void runExample() throws IOException {
		
		

		//true = append file
		
		FileWriter fileWritter = new FileWriter(roadsXMLfile);
		
         bufferWritter = new BufferedWriter(fileWritter);
        /* String data= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
         bufferWritter.write(new String(data.getBytes(),"UTF-8"));
         data="<document>\n";
        	 bufferWritter.write(new String(data.getBytes(),"UTF-8"));*/
		parseDocument();
		System.out.println("converted routes: "+ count);
	//	printData();
		//String  data="</document>";
		//  bufferWritter.write(new String(data.getBytes(),"UTF-8"));
		 bufferWritter.close();
		 
	}

	private void parseDocument() {
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse the file and also register this class for call backs
			sp.parse(originalOSMfile, this);
			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}

		
private void writeData(Way way) throws IOException{
		
	String data ="";
	
	data = " <http://sj.abdn.ac.uk/resource/"+way.getID()+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://sj.abdn.ac.uk/ontology/BusRoute> .\n ";
	
	bufferWritter.write(new String(data.getBytes(),"UTF-8"));
			
	/*	//	System.out.println("ID: "+ way.getID() );
	String data = "<relation id=\""+way.getID()+"\">\n";
	
	bufferWritter.write(new String(data.getBytes(),"UTF-8") );
		     
		*/	 
			 Iterator it2 = way.getNodes().iterator();
			 while(it2.hasNext()) {
					String node = (String) it2.next();
					//data = "<member ref=\""+node+"\"/>\n";
					data = " <http://sj.abdn.ac.uk/resource/"+way.getID()+"> <http://sj.abdn.ac.uk/ontology/BusRoute#includesWay> <http://sj.abdn.ac.uk/resource/"+node+"> .\n ";
					bufferWritter.write(new String(data.getBytes(),"UTF-8") );
				}
			
		//	System.out.println("Type: " );
			
			Iterator it3 = way.getTags().iterator();
			while(it3.hasNext()) {
				String [] touple = (String[]) it3.next();
				//String key = new String(touple[0].getBytes(),"UTF-8");//force to convert UTF-8 standard will address this issue Invalid byte 1 of 1-byte UTF-8 sequence ;
				//String value = new String(touple[1].getBytes(),"UTF-8");//force to convert UTF-8 standard will address this issue Invalid byte 1 of 1-byte UTF-8 sequence ;
		//	System.out.println("Key : " + touple[0] + "  Value : " + touple [1]  );
				
				
				touple[0] = touple[0].replaceAll("'","\u0027");
				touple[1] = touple[1].replaceAll("'","\u0027");
				touple[0] = touple[0].replaceAll("\"","\u0027");
				touple[1] = touple[1].replaceAll("\"","\u0027");
				touple[0] = touple[0].replaceAll("&","&amp;");
				touple[1] = touple[1].replaceAll("&","&amp;");
				
				//modifying dataset a littl ebit ;) NEEED TO CHANGE
				touple[0] = touple[0].replaceAll("<","_");
				touple[1] = touple[1].replaceAll("<","_");
				
				
		//	 data = "<tag k=\""+touple[0]+"\" v=\""+touple[1]+"\"/>\n";
				
				
				if (touple[0].equalsIgnoreCase("REF")) {
					data = " <http://sj.abdn.ac.uk/resource/"+way.getID()+"> <http://www.w3.org/2000/01/rdf-schema#label> \""+touple[1]+"\" .\n";
					bufferWritter.write(new String(data.getBytes(),"UTF-8") );
				}
			}
			
			//System.out.println("Nodes: "+ way.getNodes() );
		//	data ="</relation>\n";
		//	bufferWritter.write(new String(data.getBytes(),"UTF-8"));
		
	}

	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		tempVal = "";
		if(qName.equalsIgnoreCase("RELATION")) {
			//create a new instance of employee
			tempRoute = new Way();
			tempRoute.setID(attributes.getValue("id"));
		}
		else if(qName.equalsIgnoreCase("TAG")&&(tempRoute!=null)) {
			//create a new instance of employee
			if ((!attributes.getValue("k").equals("note"))&&(!attributes.getValue("k").equals("width"))&&(!attributes.getValue("k").equals("incline"))) {
			String [] touple = new String [2];
			touple [0] = attributes.getValue("k");
			touple [1] = attributes.getValue("v");
			
			if(touple [0]==null) {
				touple [0]="";
			}
			
			if(touple [1]==null) {
				touple [1]="";
			}
			
			if ((touple [0].equalsIgnoreCase("ROUTE"))&&(touple [1].equalsIgnoreCase("BUS")))
				isBusRoute =true;
			
			tempRoute.addTag(touple);
			}
		}
		else if(qName.equalsIgnoreCase("MEMBER")&&(tempRoute!=null)) {
			//create a new instance of employee
			tempRoute.addNodes(attributes.getValue("ref"));
		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(qName.equalsIgnoreCase("RELATION")) {
			//add it to the list
			ArrayList tempList = tempRoute.getTags();
			if (isBusRoute ) {
				//ways.add(tempWay);
				try {
					writeData(tempRoute);
					count++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isBusRoute =false;
			}
			
		}
		
	}
	
	
	
}
