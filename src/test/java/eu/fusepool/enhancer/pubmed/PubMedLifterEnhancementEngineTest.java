/**
 * 
 */
package eu.fusepool.enhancer.pubmed;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.apache.commons.io.IOUtils;
import org.apache.stanbol.enhancer.contentitem.inmemory.InMemoryContentItemFactory;
import org.apache.stanbol.enhancer.servicesapi.ContentItem;
import org.apache.stanbol.enhancer.servicesapi.ContentItemFactory;
import org.apache.stanbol.enhancer.servicesapi.EngineException;
import org.apache.stanbol.enhancer.servicesapi.EnhancementEngine;
import org.apache.stanbol.enhancer.servicesapi.impl.StringSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.fusepool.enhancer.pubmed.testutil.MockComponentContext;
import eu.fusepool.enhancer.pubmed.testutil.MockLogService;

/**
 * @author giorgio
 *
 */
public class PubMedLifterEnhancementEngineTest {


	static PubMedLifterEnhancementEngine engine ;
	static MockComponentContext ctx ;

	private static final ContentItemFactory ciFactory = InMemoryContentItemFactory.getInstance();



	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		//properties.put("CLEAN_ON_STARTUP", false) ;
		properties.put(EnhancementEngine.PROPERTY_NAME, "PubMedEngine") ;
		ctx = new MockComponentContext(properties) ;

		engine = new PubMedLifterEnhancementEngine() ;
		engine.logService = new MockLogService() ;
		engine.parser = Parser.getInstance() ;
		//Set<String> supportedFormats = engine.parser.getSupportedFormats() ;
		engine.activate(ctx) ;
	}

	@AfterClass 
	public static void clean() {
		engine.deactivate(ctx) ;
	}



	@Test
	public void testFile01() {
		ContentItem ci01 = null ;
		try {
			ci01 = createContentItemFromFile("505-520.xml");
		} catch (IOException e) {
			fail("No data. Exception  thrown: "+e.getMessage());
		}
		try {
			engine.computeEnhancements(ci01) ;
		} catch (EngineException e) {

			fail("Engine should not throw exception: "+e.getMessage());
		}	

		assertFalse("Metadata should not be empty ",ci01.getMetadata().isEmpty()) ;

	}


	@Test
	public void testFile02() {
		ContentItem ci02 = null ;
		try {
			ci02 = createContentItemFromFile("505-520.xml");
		} catch (IOException e) {
			fail("No data. Exception  thrown: "+e.getMessage());
		}
		try {
			engine.computeEnhancements(ci02) ;
		} catch (EngineException e) {

			fail("Engine should not throw exception: "+e.getMessage());
		}	

		assertFalse("Metadata should not be empty ",ci02.getMetadata().isEmpty()) ;

	}


	@Test
	public void testFile03() {
		ContentItem ci02 = null ;
		try {
			ci02 = createContentItemFromFile("wrong-data.rdf");
		} catch (IOException e) {
			fail("No data. Exception  thrown: "+e.getMessage());
		}
		try {
			engine.computeEnhancements(ci02) ;
		} catch (EngineException e) {

			fail("Engine should not throw exception: "+e.getMessage());
		}	
		assertTrue("Metadata should be empty ",ci02.getMetadata().isEmpty()) ;
		//assertTrue(true) ;

	}


	private ContentItem createContentItemFromFile(String fileName) throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/test/data/"+fileName) ;
		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer);
		String theString = writer.toString();
		//System.out.println(theString);
		ContentItem ci = ciFactory.createContentItem(new UriRef("urn:test:content-item:")+fileName, new StringSource(theString)) ;
		return ci ;
	}

}
