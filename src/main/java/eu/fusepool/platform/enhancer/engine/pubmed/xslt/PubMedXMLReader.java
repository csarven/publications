/**
 * 
 */
package eu.fusepool.platform.enhancer.engine.pubmed.xslt;

import java.io.IOException;

import org.apache.xerces.util.XMLCatalogResolver;
import org.apache.xml.resolver.tools.ResolvingXMLReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

/**
 * @author giorgio
 *
 */
public class PubMedXMLReader extends ResolvingXMLReader {

	final Logger logger = LoggerFactory.getLogger(this.getClass()) ;
	
	XMLCatalogResolver resolver ;
	
	/**
	 * @throws Exception 
	 * @throws SAXNotRecognizedException 
	 * 
	 */
//	public PubMedXMLReader(boolean test) throws SAXNotRecognizedException, Exception {
//		super();
//		String [] catalogs =  
//			  {"test/catalog.xml"};
//		// Create catalog resolver and set a catalog list.
//		resolver = new XMLCatalogResolver();
//		resolver.setPreferPublic(true);
//		resolver.setCatalogList(catalogs);
//		
//		// Set the resolver on the parser.
//		//EntityResolver2 resolver = new DTDResolver() ;
////		this.setProperty(
////		  "http://apache.org/xml/properties/internal/entity-resolver", 
////		  this);
//		
//	}

	
	public PubMedXMLReader() throws SAXNotRecognizedException, Exception {
		super();
		String [] catalogs =  //{"test/catalog.xml"}; 
			  {"file:///"+CatalogBuilder.getCatalogPath()};
		// Create catalog resolver and set a catalog list.
		resolver = new XMLCatalogResolver();
		//resolver.setPreferPublic(true);
		resolver.setCatalogList(catalogs);
		
		
	}
	
	/* (non-Javadoc)
	 * @see org.apache.xml.resolver.tools.ResolvingXMLFilter#resolveEntity(java.lang.String, java.lang.String)
	 */
	@Override
	public InputSource resolveEntity(String publicId, String systemId) {
		try {
			logger.debug("<public publicId=\""+publicId+"\"" + " uri=\""+systemId+"\" />");
			InputSource is = resolver.resolveEntity(publicId, systemId);
			if(is==null) {
				logger.warn("# NOT FOUND #<public publicId=\""+publicId+"\"" + " uri=\""+systemId+"\" />");
			}
			return is ;
		} catch (SAXException e) {
			logger.error("Error resolving entity", e);
			return null ;
		} catch (IOException e) {
			logger.error("Error resolving entity", e);
			return null ;
		}
	}


	
	
	
}
