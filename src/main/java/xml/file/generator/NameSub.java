package xml.file.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NameSub implements Runnable{
	private  String file;
	private String preNodeName;
	private String nodeName;
	private String rootNode;
	Set<String> refSet = new HashSet<String>();
	public NameSub(String file, String preNodeName, String nodeName, String rootNode) {
		super();
		this.file = file;
		this.preNodeName = preNodeName;
		this.nodeName = nodeName;
		this.rootNode = rootNode;
	}
	
	
	@Override
	public void run() {
		File fileName = new File(rootDirectory()+"/data/inputData/" + file);
		boolean updated = false;
		Set<String> preNodesSet = new HashSet<String>();
		Set<String> nodeNameSet = new HashSet<String>();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document document = docBuilder.parse(fileName);
			Node root = document.getFirstChild();
			NodeList enrollmentList = root.getChildNodes();
			preNodesSet = lookUpId(enrollmentList,rootNode,preNodesSet,preNodeName);
			nodeNameSet = lookUpId(enrollmentList,rootNode,nodeNameSet,nodeName);
			
			for (int i = 0; i<enrollmentList.getLength();i++) {
				Node childNode = enrollmentList.item(i);
				if (rootNode.equals(childNode.getNodeName())) {
					NodeList providersList = childNode.getChildNodes();
					for (int j = 0; j < providersList.getLength(); j++) {
						// get the current recordIdentifier and enrollmentId
						Node innerNode = providersList.item(j);
							if (innerNode.getNodeName().equals(preNodeName) && innerNode.getTextContent()!=null) {
								String recordIdentifier = innerNode.getTextContent();
								String preUpdate = innerNode.getTextContent();
								StringBuilder sb = new StringBuilder(recordIdentifier);
								//generate random values while the value is not distinct
								uniqueSet(preNodesSet,9,2,sb);
								updated = updateWrite(preNodesSet,document,innerNode,preUpdate,fileName);
							}
							if (innerNode.getNodeName().equals(nodeName) && innerNode.getTextContent()!=null) {
								String enrollmentId = innerNode.getTextContent();
								String preUpdate = innerNode.getTextContent();
								StringBuilder sb = new StringBuilder(enrollmentId);
								// generate random values while values are not distinct
								uniqueSet(nodeNameSet,9,2,sb);
								updated = updateWrite(nodeNameSet,document,innerNode,preUpdate,fileName);
								break;
							}
					}
					
				}	
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public int randomNumgeneration(int max, int min) {
		Random rand = new Random();
		int num = rand.nextInt(max) + min;
		return num;
		
	}
	public void uniqueSet(Set<String> initSet, int max , int min, StringBuilder sb) {
	
		do {
			 int n = randomNumgeneration(max,min);
		     int m = randomNumgeneration(max,min);
		     sb.setCharAt(n, String.valueOf(m).charAt(0));
		     sb.setCharAt(m, String.valueOf(n).charAt(0));
		}while(initSet.contains(sb.toString())||refSet.contains(sb.toString()));
		initSet.add(sb.toString());
		refSet.add(sb.toString());
		
	}
	public  String rootDirectory(){
		   String workingDir = System.getProperty("user.dir");
		 return workingDir;
    }
	
	public boolean updateWrite(Set<String> idsSet, Document doc, Node node,String baseVal,File fileName) {
		boolean success = false;
		for (String element:idsSet) {
			if(!(element.equals(baseVal))) {
				node.setTextContent(element);
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				try {
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(fileName);
					transformer.transform(source, result);
					success = true;
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}	
		}
		idsSet.clear();
		return success;	
	}
	
	public Set<String> lookUpId(NodeList list, String rootNode,Set<String> refrncSet,String refVal){
		for (int i = 0; i<list.getLength();i++) {
			Node childNode = list.item(i);
			String nodeName = childNode.getNodeName();
			if (rootNode.equals(nodeName)) {
				NodeList providersList = childNode.getChildNodes();
				for (int j = 0; j < providersList.getLength(); j++) {
					// get the current recordIdentifier and enrollmentId
					Node innerNode = providersList.item(j);
						if (innerNode.getNodeName().equals(refVal)&& innerNode.getTextContent()!="") {
							refrncSet.add(innerNode.getTextContent());
						}
				}
			}
		}
		
		return refrncSet;
		
	}
	public static void main(String[] args) {
		// creating a thread pool to check the status of each thread
		List<Thread> threads = new ArrayList<Thread>();
		Runnable task = new NameSub("PecosBatchIndividual.xml", "TrkngId", "EnrollmentId", "Enrollment");
		
		/**
		 * creating 10 threads
		 */
		for (int i = 0; i<20; i++) {
			Thread worker = new Thread(task);
			/**
			 * set the name for each thread
			 */
			worker.setName(String.valueOf(i));
			/**
			 * start each thread and add it to the thread pool
			 */
			worker.start();
			threads.add(worker);
		}
		int running = 0;
		do {
			running = 0;
			for (Thread thread : threads) {
				if (thread.isAlive()) {
					running++;
				}
			}
//            System.out.println("We have " + running + " running threads. ");
		} while (running > 0);

		
		
	}


}
