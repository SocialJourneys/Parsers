package org.parser;

import java.io.File;
import java.io.IOException;

public class Run {

	/**
	 * @param args
	 * @throws IOException 
	 * 
	 * @author milan markovic
	 * 
	 * Remove comments as necessary 
	 * 
	 */
	public static void main(String[] args) throws IOException {
		
		//open street maps extracct
		String originalOSMfile = "/Users/milan/AberdeenMap/xmlExtract/map";
		
		//open street maps parsed extract including only roads
		String roadsXMLfile = "/Users/milan/AberdeenMap/xmlExtract/aberdeenRoads.xml";
		
		//convert osm roads for KIM
		String streetsNTfile = "/Users/milan/AberdeenMap/xmlExtract/AbdnStreetNames_instances.nt";
		
		//convert osm bus routes for KIM
		String busRoutesXMLfile = "/Users/milan/AberdeenMap/xmlExtract/busroutes.nt";
		//String nodesXMLfile ="/Users/milan/OSM/bordersNodes.xml";
		
		//procerss transExchange data about bus services
		File baseMapFolder = new File ("/Users/milan/AberdeenMap/basemap/aberdeen/");
		String busServicesResult = "/Users/milan/AberdeenMap/basemap/results/aberdeenBusServices.nt";
		String busServicesForKimResult = "/Users/milan/AberdeenMap/basemap/results/aberdeenBusServicesForKim.nt";
		
		
		//process datex instances
		String ontologyPath = "././resources/DataX2Enums.ttl";
		String enumerationIndividualsKim = "/Users/milan/AberdeenMap/datex/enumerationInstances.nt";
		
		
		System.out.println("Parsing bus services from bus stops from basemap");
	    ExtractBusServicesForKIM resorces = new ExtractBusServicesForKIM(baseMapFolder,busServicesForKimResult);
		resorces.runExample();
		System.out.println("Finished parsing bus services from bus stops from basemap");
		
		
		
		//System.out.println("Parsing ontology instances");
		//ExtractIndividualsIntoKIMInstances test = new ExtractIndividualsIntoKIMInstances(ontologyPath,enumerationIndividualsKim);
		//test.runExample();
		//System.out.println("KIM instances created");
		
	    //System.out.println("Parsing bus services from bus stops from basemap");
	    //ExtractBusServicesForBusStopFromBaseMap resorces = new ExtractBusServicesForBusStopFromBaseMap(baseMapFolder,busServicesResult);
		//resorces.runExample();
		//System.out.println("Finished roads from OSM");
		
	    //System.out.println("Extracting roads from OSM");
		/*ExtractRoadsFromOSMtoXML spe = new ExtractRoadsFromOSMtoXML(originalOSMfile,roadsXMLfile);
		spe.runExample();
		System.out.println("Finished roads from OSM");*/
		
		//System.out.println("Extracting bus routes from OSM");
		//ExtractBusRoutesfromOSM routes = new ExtractBusRoutesfromOSM(originalOSMfile,busRoutesXMLfile);
		//routes.runExample();
		//System.out.println("Finished extracting bus routes from OSM");
		
		//System.out.println("Extracting street names for KIM from OSM");
		//ParseStreetNamesToNTforKIM resorces = new ParseStreetNamesToNTforKIM(originalOSMfile,streetsNTfile);
		//resorces.runExample();
		//System.out.println("Finished roads from OSM");
		
		
		//ExtractNodesRef extractNodesRef = new ExtractNodesRef (roadsXMLfile) ;
		//System.out.println("Start looking for node's ids");
		//extractNodesRef.runExtractRef();
		
		//System.out.println ("Extracted ids:" + extractNodesRef.getNodes().size());
		//System.out.println("Finished looking for id's");
		
		
		//System.out.println("Start writing in XML file");
		//ExtractNodesAndWriteToXML extractNodesAndWrite = new ExtractNodesAndWriteToXML (extractNodesRef.getNodes(),originalOSMfile,nodesXMLfile) ;
		//extractNodesAndWrite.runExtraction();
		//System.out.println("Finished writing in XML file");

	}

}
