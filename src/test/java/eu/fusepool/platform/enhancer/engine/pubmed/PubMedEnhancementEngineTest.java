/**
 * 
 */
package eu.fusepool.platform.enhancer.engine.pubmed;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.Triple;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.impl.TripleImpl;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.apache.clerezza.rdf.ontologies.DCTERMS;
import org.apache.clerezza.rdf.ontologies.FOAF;
import org.apache.clerezza.rdf.ontologies.RDF;
import org.apache.commons.io.IOUtils;
import org.apache.stanbol.enhancer.contentitem.inmemory.InMemoryBlob;
import org.apache.stanbol.enhancer.contentitem.inmemory.InMemoryContentItemFactory;
import org.apache.stanbol.enhancer.servicesapi.Blob;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.ContentItemFactory;
import org.apache.stanbol.enhancer.servicesapi.ContentSource;
import org.apache.stanbol.enhancer.servicesapi.EngineException;
import org.apache.stanbol.enhancer.servicesapi.EnhancementEngine;
import org.apache.stanbol.enhancer.servicesapi.helper.ContentItemHelper;
import org.apache.stanbol.enhancer.servicesapi.impl.ByteArraySource;
import org.apache.stanbol.enhancer.servicesapi.impl.StringSource;
import org.apache.stanbol.enhancer.servicesapi.rdf.TechnicalClasses;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.fusepool.platform.enhancer.engine.pubmed.OntologiesTerms;
import eu.fusepool.platform.enhancer.engine.pubmed.PubMedEnhancementEngine;
import eu.fusepool.platform.enhancer.engine.pubmed.testutil.MockComponentContext;

/**
 * @author Giorgio Costa
 * @author Luigi Selmi
 *
 */
public class PubMedEnhancementEngineTest {


	static PubMedEnhancementEngine engine ;
	static MockComponentContext ctx ;
	
	private static ContentItem ci = null ;

	private static final ContentItemFactory ciFactory = InMemoryContentItemFactory.getInstance();
	
	// The file used for these tests must not be changed. Results, such as number of entities and enhancements, depend on this file.
	// If another file is used the following values must be updated accordingly
	private static final String TEST_FOLDER = "/test/data/";
	//private static final String TEST_FILE = "505-520.xml";
	private static final String TEST_FILE = "AAPS_J_2008_Apr_2_10(1)_193-199.nxml";
	private final int PERSONS_NUMBER = 3; // number of entities of type foaf:Person extracted from the test file.



	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		//properties.put("CLEAN_ON_STARTUP", false) ;
		properties.put(EnhancementEngine.PROPERTY_NAME, "PubMedEngine") ;
		ctx = new MockComponentContext(properties) ;

		engine = new PubMedEnhancementEngine() ;
		engine.parser = Parser.getInstance() ;
		//Set<String> supportedFormats = engine.parser.getSupportedFormats() ;
		engine.activate(ctx) ;
		
		// creates a content item from the document and compute the enhancements
		createContentItemFromFile(TEST_FILE);
	}

	@AfterClass 
	public static void clean() {
		engine.deactivate(ctx) ;
		File test_data_folder = new File(MockComponentContext.BUNDLE_TEST_DATA_FOLDER) ;
		if(test_data_folder.exists() && test_data_folder.isDirectory())
			test_data_folder.delete() ;
	}
	
	/*
	 * Test if subjects of type person are found after the transformation and prints them
	 */
	/*
	@Test
	public void testTransformXML() {
		
		MGraph graph = null;
		
		try {
			
			graph = engine.transformXML(ci);
			
		} catch (EngineException e) {
			 
			System.out.println("Error while transforming the XML file into RDF");
		}
		
		
		
		if (! graph.isEmpty()) {
			
			// Filter triples for persons
			Iterator<Triple> ipersons = graph.filter(null, RDF.type, FOAF.Person) ;
			
			int personsNumber = 0;
			while (ipersons.hasNext()){
				personsNumber += 1;
				Triple triple = ipersons.next();
				System.out.println(triple.toString());
				//String subjectUri = triple.getSubject().toString();
				//System.out.println("Filtered subject of type foaf:Person = " + subjectUri);
				
			}
			
			
		}
		else {
			System.out.println("Enhancement graph empty !");
		}
		
		//assertTrue("Subjects of type foaf:Person found in the document " + personsNumber, personsNumber == PERSONS_NUMBER);
		
	}
	*/
	
	/*
	 * Test if entity-reference annotations have been added for each entity found after the transformation of the input XML file into RDF.
	 */
	/*
	@Test
	public void testAddEnhancements() {
		
		MGraph xml2rdf = null;
		
		try {
			
			xml2rdf = engine.transformXML(ci);
			
		} catch (EngineException e) {
			 
			System.out.println("Error while transforming the XML file into RDF");
		}
		
		MGraph graph = engine.addEnhancements(ci, xml2rdf);
		
		int entityReferences = 0;
		
		if (! graph.isEmpty()) {
			
			// Filter triples for entities annotations
			Iterator<Triple> ireferences = graph.filter(null, TechnicalClasses.ENHANCER_ENHANCEMENT, null);
			while (ireferences.hasNext()) {
				entityReferences += 1;
				Triple triple = ireferences.next();
				String enhancement = triple.getSubject().toString();
				String entity = triple.getObject().toString();
				System.out.println("Entity references " + entityReferences + ") "  + enhancement + " entity reference: " + entity);
			}
			
		}
		else {
			System.out.println("Enhancement graph empty !");
		}
		
		//assertTrue("Entities found in the document " + entityReferences, entityReferences == ENTITIES_REFERENCED);
	
	
	}
	*/
	
	/*
	 * Test whether a plain text representation part of document has been added to the content item. 
	 */
	@Test
	public void testAddPartToContentItem() {
		
		String plainTextPart = "";
		
		engine.addPartToContentItem(ci);
		
		// Reading part with index 1 of the content item that contain an object of type ContentSource
		ByteArraySource source = ci.getPart(1, ByteArraySource.class);
		
		// Get the MIME TYPE of the part
		String partMimeType = source.getMediaType();
		
		
		try {
			
			plainTextPart = IOUtils.toString(source.getStream());
			
			//System.out.println(plainTextPart);
		}
		catch(IOException e) {
			System.out.println("Error while reading plain text part");
		}
		
		Iterator<Triple> iparts = ci.getMetadata().filter(ci.getUri(), DCTERMS.hasPart, null);
		while(iparts.hasNext()) {
			System.out.println("Content Item parts: " + iparts.next().getObject().toString());
		}
		
		assertTrue("text/plain".equals(partMimeType));
		
		
	}



	private static void createContentItemFromFile(String fileName) {
		
		String filePath = TEST_FOLDER + fileName;
		try {
			InputStream in = PubMedEnhancementEngineTest.class.getResourceAsStream(filePath) ;
			StringWriter writer = new StringWriter();
			IOUtils.copy(in, writer);
			String content = writer.toString();
			//System.out.println(theString);
			ci = ciFactory.createContentItem(new UriRef("urn:test:content-item:") + fileName, new StringSource(content)) ;
			
		}
		catch (IOException e) {
			System.out.println("Error while creating content item from file " + filePath);
		} 
		
	}

}
