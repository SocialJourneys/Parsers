package org.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParseStreetNamesToNTforKIM extends DefaultHandler {
	
	
 

	List ways;
	int count =0;
	
	private String tempVal;
	
	//to maintain context
	private Way tempWay;
	private boolean isRoad = false;
	 BufferedWriter bufferWritter;
	 String originalOSMfile;
	 String kimMappingResultXMLfile;
	 HashMap <String,String[]> abbreviations;
	
	
	public ParseStreetNamesToNTforKIM(String originalOSMfile, String kimXMLfile) throws IOException{
		ways = new ArrayList();
		this.originalOSMfile = originalOSMfile;
		this.kimMappingResultXMLfile = kimXMLfile;
		
		//init and load the list of abbreviations
        this.abbreviations = new HashMap <String,String[]> ();
        this.abbreviations.put("Avenue", new String [] {"Ave","Av"});
        this.abbreviations.put("Boulevard",new String [] { "Blvd"});
        this.abbreviations.put("Broadway", new String [] {"Bdwy"});
        this.abbreviations.put("Circus", new String [] {"Cir"});
        this.abbreviations.put("Close", new String [] {"Cl"});
        this.abbreviations.put("Court", new String [] {"Ct"});
        this.abbreviations.put("Crescent", new String [] {"Cr"});
        this.abbreviations.put("Drive", new String [] {"Dr"});
        this.abbreviations.put("Garden", new String [] {"Gdn"});
        this.abbreviations.put("Gardens", new String [] {"Gdns"});
        this.abbreviations.put("Green", new String [] {"Gn"});
        this.abbreviations.put("Grove", new String [] {"Gr"});
        this.abbreviations.put("Junction", new String [] {"J"});
        this.abbreviations.put("Lane", new String [] {"Ln"});
        this.abbreviations.put("Mount", new String [] {"Mt"});
        this.abbreviations.put("Place", new String [] {"Pl"});
        this.abbreviations.put("Park", new String [] {"Pk"});
        this.abbreviations.put("Ridge", new String [] {"Rdg"});
        this.abbreviations.put("Road", new String [] {"Rd"});
        this.abbreviations.put("Square", new String [] {"Sq"});
        this.abbreviations.put("Street", new String [] {"St"});
        this.abbreviations.put("Terrace", new String [] {"Ter"});
        this.abbreviations.put("Valley", new String [] {"Val"});
       
	}
	
	public void runExample() throws IOException {
		
		

		//true = append file
		
		FileWriter fileWritter = new FileWriter(kimMappingResultXMLfile);
		
		  bufferWritter = new BufferedWriter(fileWritter);
		  /* String data= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
         bufferWritter.write(new String(data.getBytes(),"UTF-8"));
         data="<document>\n";
        	 bufferWritter.write(new String(data.getBytes(),"UTF-8"));
		
		
	//	printData();
		  data="</document>";
		  bufferWritter.write(new String(data.getBytes(),"UTF-8"));
		 bufferWritter.close();*/
		parseDocument();
         System.out.println("converted ways: "+ count);
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
		 
			
		//	System.out.println("ID: "+ way.getID() );
/*	String data = "<way id=\""+way.getID()+"\">\n";
	
	bufferWritter.write(new String(data.getBytes(),"UTF-8") );*/
		     
			 
		/*	 Iterator it2 = way.getNodes().iterator();
			 while(it2.hasNext()) {
					String node = (String) it2.next();
					data = "<nd ref=\""+node+"\"/>\n";
					bufferWritter.write(new String(data.getBytes(),"UTF-8") );
				}*/
			
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
				
				
			// data = "<tag k=\""+touple[0]+"\" v=\""+touple[1]+"\"/>\n";
				
				if (touple[0].equalsIgnoreCase("NAME")) {
				
			    data = " <http://sj.abdn.ac.uk/resource/"+way.getID()+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://sj.abdn.ac.uk/ontology/Highway> .\n ";
				
			    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
				
			    data = "<http://sj.abdn.ac.uk/resource/"+way.getID()+"> <http://www.w3.org/2000/01/rdf-schema#label> \""+touple[1]+"\" .\n";
			    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
			    
			    data = "<http://sj.abdn.ac.uk/resource/"+way.getID()+"> <http://proton.semanticweb.org/2006/05/protons#mainLabel> \""+touple[1]+"\" .\n";
			    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
				
			    data = "<http://sj.abdn.ac.uk/resource/"+way.getID()+"> <http://proton.semanticweb.org/2006/05/protons#generatedBy> <http://www.ontotext.com/kim/2006/05/wkb#Gazetteer> .\n";
			    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
			    
			   
			    ArrayList<String> aliases = generateAliases  (touple[1]); 
			    
			    System.out.println (aliases);
			    
			    	for (int i=0;i<aliases.size();i++) {
			    		writeAlias (aliases.get(i), way.getID(), i);
			    	}
			    
				}
			}
			
			//System.out.println("Nodes: "+ way.getNodes() );
		//	data ="</way>\n";
		//	bufferWritter.write(new String(data.getBytes(),"UTF-8"));
		
	}

    private ArrayList <String> generateAliases (String label) {
    	
    	ArrayList <String> aliases = new ArrayList <String> ();
    	
    
    	
    	//add the obvious
    	aliases.add(label);
    	aliases.add(label.toLowerCase());
    	aliases.add(label.toUpperCase());
    	
    	if (label.contains(" ")) {
    	String[] parts = label.split(" ");
    	String lastPart = parts[parts.length - 1];
    	
    		if (this.abbreviations.containsKey(lastPart)) {
    			
    			String labelBase = "";
        		
    			for (int i=0;i<parts.length -1;i++) {
    				labelBase = labelBase + parts [i] + " ";
    			}
    			
    			for (int i=0;i<abbreviations.get(lastPart).length;i++) {
    		
    			
    			label ="";
    			//add abbreviation
    			label = labelBase + this.abbreviations.get(lastPart)[i];
    		
    			aliases.add(label);
    			aliases.add(label.toLowerCase());
    			aliases.add(label.toUpperCase());
    			}
    		}
    	
    	
    	}
    	
    	
    	return aliases;
    	
    }

    private void writeAlias (String alias, String resourceIDbase, int aliasNumb) throws UnsupportedEncodingException, IOException {
    	
     String data = "<http://sj.abdn.ac.uk/resource/"+resourceIDbase+"."+aliasNumb+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://proton.semanticweb.org/2006/05/protons#Alias> .\n";
		    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
		    
		    data = "<http://sj.abdn.ac.uk/resource/"+resourceIDbase+"."+aliasNumb+"> <http://www.w3.org/2000/01/rdf-schema#label> \""+alias+"\" .\n";
		    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
		    
		    if  (aliasNumb==1) {
		    data = "<http://sj.abdn.ac.uk/resource/"+resourceIDbase+"> <http://proton.semanticweb.org/2006/05/protons#hasMainAlias> <http://sj.abdn.ac.uk/resource/"+resourceIDbase+"."+aliasNumb+"> .\n";
		    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
		    }
		    
		    else {
		    data = "<http://sj.abdn.ac.uk/resource/"+resourceIDbase+"> <http://proton.semanticweb.org/2006/05/protons#hasAlias> <http://sj.abdn.ac.uk/resource/"+resourceIDbase+"."+aliasNumb+"> .\n";
		    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
		    }
    	
    }


	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		tempVal = "";
		if(qName.equalsIgnoreCase("WAY")) {
			//create a new instance of employee
			tempWay = new Way();
			tempWay.setID(attributes.getValue("id"));
		}
		else if(qName.equalsIgnoreCase("TAG")&&(tempWay!=null)) {
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
			
			if (touple [0].equalsIgnoreCase("HIGHWAY"))
				isRoad =true;
			
			tempWay.addTag(touple);
			}
		}
		else if(qName.equalsIgnoreCase("ND")&&(tempWay!=null)) {
			//create a new instance of employee
			tempWay.addNodes(attributes.getValue("ref"));
		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(qName.equalsIgnoreCase("WAY")) {
			//add it to the list
			ArrayList tempList = tempWay.getTags();
			if (isRoad ) {
				//ways.add(tempWay);
				try {
					writeData(tempWay);
					count++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isRoad =false;
			}
			
		}
		
	}
	
	
	
}
