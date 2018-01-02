package xml.file.generator;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DuplicateNode {
	
	public void processFile(String path) {
		try {
			String filepath = path;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// Get the root element
			 Node enrollment = doc.getElementsByTagName("Enrollment").item(0);
			 
			// update EnrollemntList attribute
			 for (int i = 0 ; i<1000; i++) {
				 Node newNode = enrollment.cloneNode(true);
				 doc.getDocumentElement().appendChild(newNode); 
			 }

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);

			System.out.println("Done");

		   } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		   } catch (TransformerException tfe) {
			tfe.printStackTrace();
		   } catch (IOException ioe) {
			ioe.printStackTrace();
		   } catch (SAXException sae) {
			sae.printStackTrace();
		   }
		}
	

	public static void main(String[] args) {
		
		DuplicateNode dupNode = new DuplicateNode();
		NameLookUp nameLookUp = new NameLookUp();
		dupNode.processFile(nameLookUp.rootDirectory()+"/data/inputData/" + "PecosBatchIndividual.xml");
		
		
	}
}
