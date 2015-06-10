package org.parser;



	import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	 
	public class ExtractBusServicesForBusStopFromBaseMap  extends DefaultHandler {
		
		
	 

		
		
		HashMap stops;
		String serviceCode ="";
		String lineName = "";
		ArrayList <String> lines;
		ArrayList <String> linesRef;
		String tempVal ; 
		int count =0;
		ArrayList <String> sessionBusKeys;
		
		
		//to maintain context
		private Way tempRoute;
		private boolean isBusRoute = false;
		
		BufferedWriter bufferWritter;
		File sourceFolder;
		String destinationFile;
		
		
		public ExtractBusServicesForBusStopFromBaseMap(File baseMapfile, String ntfile) throws IOException{
			
			stops = new HashMap <String,HashMap <String,ArrayList<String>>>();
			
			this.sourceFolder = baseMapfile;
			this.destinationFile = ntfile;
			this.linesRef = new ArrayList ();
	        System.out.println ("running");
	       
		}
		
		public void runExample() throws IOException {
			
			

			//true = append file
			
			FileWriter fileWritter = new FileWriter(destinationFile,true);
			
	        bufferWritter = new BufferedWriter(fileWritter);
	        
	       
			
	        itterateTroughAllFiles (sourceFolder);
	        writeData (stops);
	        
			System.out.println("converted routes: "+ count);
			bufferWritter.close();
			 
		}

		private void parseDocument(String fileToParse) {
			
			sessionBusKeys = new ArrayList <String> ();
			
			//get a factory
			SAXParserFactory spf = SAXParserFactory.newInstance();
			try {
			
				//get a new instance of parser
				SAXParser sp = spf.newSAXParser();
				
				//parse the file and also register this class for call backs
				sp.parse(fileToParse, this);
				
			}catch(SAXException se) {
				se.printStackTrace();
			}catch(ParserConfigurationException pce) {
				pce.printStackTrace();
			}catch (IOException ie) {
				ie.printStackTrace();
			}
		}

			
	private void writeData(HashMap stops) throws IOException{
			
		String data ="";
		
	for (int i=0;i< linesRef.size();i++){
		count++;
		data = " <http://sj.abdn.ac.uk/resource/basemap/busLines/ABDN_"+linesRef.get(i)+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://sj.abdn.ac.uk/ontology/BusRoute> .\n ";
		
		bufferWritter.write(new String(data.getBytes(),"UTF-8")); 
		
        data = " <http://sj.abdn.ac.uk/resource/basemap/busLines/ABDN_"+linesRef.get(i)+"> <http://www.w3.org/2000/01/rdf-schema#label> \""+linesRef.get(i)+"\" .\n ";
		
		bufferWritter.write(new String(data.getBytes(),"UTF-8"));
	}
		
		
		Iterator it = stops.keySet().iterator(); 
		
		while (it .hasNext()) {
			Object key = it.next();
			HashMap busServicesOnaBusStop = (HashMap) stops.get(key);
			
			Iterator it2 = busServicesOnaBusStop.keySet().iterator(); 
			while (it2 .hasNext()) {
				Object key2 = it2.next();
				ArrayList lines = (ArrayList) busServicesOnaBusStop.get(key2);
				
				for (int i=0;i<lines.size();i++) {
					
 
					  data = "<http://sj.abdn.ac.uk/resource/basemap/busLines/ABDN_"+lines.get(i)+">  <http://sj.abdn.ac.uk/ontology/BusRoute#includesStop> <http://sj.abdn.ac.uk/resource/naptan/stops/"+key+">.\n ";
						
					  bufferWritter.write(new String(data.getBytes(),"UTF-8"));
				}
			}
			
		}
		

			
		}

		//Event Handlers
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			//reset
			tempVal = "";
			//System.out.println (qName);
			
		}
		

		public void characters(char[] ch, int start, int length) throws SAXException {
			tempVal = new String(ch,start,length);
			//System.out.println (tempVal);
		}
		
		public void endElement(String uri, String localName, String qName) throws SAXException {

			if(qName.equalsIgnoreCase("StopPointRef")) {
				//create a stop entry if set does not contain it yet
				if (!stops.keySet().contains(tempVal)) {
					HashMap <String,ArrayList<String>> services = new HashMap <String,ArrayList<String>>();
					stops.put(tempVal, services);
				}
				
				if (!sessionBusKeys.contains(tempVal)) {
				sessionBusKeys.add(tempVal);
				}
				
			}
			
			if(qName.equalsIgnoreCase("ServiceCode")) {
				
				serviceCode = tempVal;
				lines = new ArrayList <String>();
				
			}
			
			if(qName.equalsIgnoreCase("LineName")) {
				
				lines.add(tempVal);
				
				//remember all unique line resources for later
				if (!linesRef.contains(tempVal)) {
					 linesRef.add(tempVal);
				}
			}
			
			if(qName.equalsIgnoreCase("Service")) {
				//System.out.println (lines);
				//System.out.println (sessionBusKeys);
				for (int j=0; j<sessionBusKeys.size();j++){
					
					Object key = sessionBusKeys.get(j);
					HashMap map =  (HashMap) stops.get(key);
					
					if (!map.containsKey(serviceCode)) {
					map.put(serviceCode, lines);
					}
					else {
					//	System.out.println ("Multiple service code mentions encountered - expanding list of lines");
						ArrayList originalLineList =  (ArrayList) map.get (serviceCode);
						
						for (int i =0; i <lines.size();i++) {
							if (!originalLineList.contains(lines.get(i))) {
							originalLineList.add(lines.get(i));
							}
						}
						
						map.put(serviceCode, originalLineList);
					}
					
					stops.put(key, map);
					
					
					
				}
			}
			if(qName.equalsIgnoreCase("Services")) {
				
				//System.out.println (stops);
			}
			
		}
		
		public void itterateTroughAllFiles(File folder) {
		    for (File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		            itterateTroughAllFiles(fileEntry);
		        } else {
		            System.out.println(fileEntry.getName());
		            parseDocument(sourceFolder+"/"+fileEntry.getName());
		            
		        }
		    }
		    
		  
		    //System.out.println (stops.get("639003653"));
		    
		}
		
	}
