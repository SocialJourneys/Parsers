package org.kmlFromXml;

import java.io.IOException;
import java.util.ArrayList;

public class RunKmlCreate {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String listOfwayIDs = "/Users/milan/OSM/wayId.csv";
		String startNodeID = "283038461";
		String endNodeID = "";
		String xmlWays = "/Users/milan/OSM/bordersRoads.xml";
		String xmlNodes = "/Users/milan/OSM/bordersNodes.xml";
		String kmlPath ="/Users/milan/OSM/routeInKML.kml";
		
		
	
		
		System.out.println("Filtering nodes");
		FilterNodes nodes =  new FilterNodes (listOfwayIDs,xmlWays,xmlNodes, startNodeID,endNodeID,kmlPath);
		ArrayList <String> nodesList = nodes.getNodesList ();
		System.out.println("Finished filteringNodes");
/*		System.out.println ("Nodes filtered:" + nodes.size());
		
		System.out.println("Extracting Coordinates");
		ArraiList <String []> coordinates = new ExtractCoordinates (nodes,scotlandNodes,englandNodes);
		
		System.out.println ("Extracted coordinates:" + coordinates.size());
		System.out.println("Finished looking for id's");
		
		
		System.out.println("Start writing in KMLL file");
		
		System.out.println("Finished writing in XML file");*/

	}

	

}
