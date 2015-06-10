package org.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class ExtractIndividualsIntoKIMInstances {
    
	private Model  model;
	private String ontPath;
	private String outputFilePath;
	private HashMap<String,String[]> individualAttributes;
	private ArrayList <String> types;
	
	BufferedWriter bufferWritter;
	int count;
	
	
	public ExtractIndividualsIntoKIMInstances (String pathToOntologyFile, String outputFilePath) {
		
		this.ontPath = pathToOntologyFile;
		this.outputFilePath = outputFilePath;
	    individualAttributes = new HashMap<String,String[]>();
	    types = new ArrayList <String> ();
	}
		

		
	
	public void readOntologyInstanceLabels () {
			model= FileManager.get().loadModel( ontPath );
			
			//Stores URI and label of the individual that is loaded from the ontology
		    
			
			OntModel ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
			
			//find all individuals deffined in the ontology
			ExtendedIterator individuals = ontology.listIndividuals();
			
			
			while (individuals.hasNext()) {
				count++;
				
				String type = "";
				
			    Resource individual = (Resource) individuals.next();
			    String URI = individual.getURI();
			    Property prop = model.createProperty ("skos:altLabel");
			    String label = individual.getProperty(prop).asTriple().getObject().getLiteralLexicalForm();
			    
			    String [] attributtes = new String[2];
			    
			    attributtes[0] = label;
			    
			    Property prop2 = model.createProperty ("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			   
			   
			    int counter = 0;
			  
			    for (StmtIterator i = individual.listProperties(prop2); i.hasNext(); ) {
			        Statement st = (Statement) i.next();
			        if (!st.asTriple().getObject().hasURI("http://www.w3.org/2002/07/owl#NamedIndividual")) {
			        	type = st.asTriple().getObject().getURI();
			        	//System.out.println(type);
			        	
			        	if (!types.contains(type)) {
			        		types.add(type);
			        	}
			        }
			        		
			    }
			    
			    
			       // System.out.println( individual.getProperty(prop2).asTriple() );
			   
			  
			    attributtes[1] = type;
			    
			    individualAttributes.put(URI,attributtes);
			}
			
			model.close();
		}
		 
		
	
	public void runExample() throws IOException {
			
		    readOntologyInstanceLabels ();

			//true = append file
			
			FileWriter fileWritter = new FileWriter(outputFilePath);
			
			
			bufferWritter = new BufferedWriter(fileWritter);
			
			writeData ();
	        System.out.println("processed instances: "+ count);
	        System.out.println(types);
	        bufferWritter.close();
		}

		
		
		
	private void writeData() throws IOException{
		String data ="";
			 
				
		Iterator<String> labelsIt = individualAttributes.keySet().iterator();
				
				
				while(labelsIt.hasNext()) {
					
					
					
					String uri = labelsIt.next();
					String [] attributes = individualAttributes.get(uri);
					String label = attributes[0];
					String type = attributes[1];
					
					data = "<"+uri+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <"+type+"> .\n ";
					
					bufferWritter.write(new String(data.getBytes(),"UTF-8")); 
						
					data = "<"+type+"> <http://www.w3.org/2000/01/rdf-schema#subClassOf> <http://datex2.eu/schema/2/2_0/Enumeration> .\n";
					bufferWritter.write(new String(data.getBytes(),"UTF-8"));
					
				    data = "<"+uri+"> <http://www.w3.org/2000/01/rdf-schema#label> \""+label+"\" .\n";
				    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
				    
				    data = "<"+uri+"> <http://proton.semanticweb.org/2006/05/protons#mainLabel> \""+label+"\" .\n";
				    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
					
				    data = "<"+uri+"> <http://proton.semanticweb.org/2006/05/protons#generatedBy> <http://www.ontotext.com/kim/2006/05/wkb#Gazetteer> .\n";
				    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
				    
				   
				    ArrayList<String> aliases = generateAliases  (label); 
				    
				  //  System.out.println (aliases);
				    
				    	for (int i=0;i<aliases.size();i++) {
				    		writeAlias (aliases.get(i), uri, i);
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
	    	
	    	return aliases;
	    	
	    }

	    private void writeAlias (String alias, String uri, int aliasNumb) throws UnsupportedEncodingException, IOException {
	    	
	     String data = "<"+uri+"."+aliasNumb+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://proton.semanticweb.org/2006/05/protons#Alias> .\n";
			    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
			    
			    data = "<"+uri+"."+aliasNumb+"> <http://www.w3.org/2000/01/rdf-schema#label> \""+alias+"\" .\n";
			    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
			    
			    if  (aliasNumb==1) {
			    data = "<"+uri+"> <http://proton.semanticweb.org/2006/05/protons#hasMainAlias> <"+uri+"."+aliasNumb+"> .\n";
			    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
			    }
			    
			    else {
			    data = "<"+uri+"> <http://proton.semanticweb.org/2006/05/protons#hasAlias> <"+uri+"."+aliasNumb+"> .\n";
			    bufferWritter.write(new String(data.getBytes(),"UTF-8"));
			    }
	    	
	    }
		
		
		
	}
