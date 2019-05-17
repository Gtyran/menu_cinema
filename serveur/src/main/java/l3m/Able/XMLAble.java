/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l3m.Able;

import l3m.Able.DataBaseAble;

/**
 * ******* XML Work biblio import ********
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * ******* XML Work biblio import ********
 */
/**
 *
 * @author Mehdi
 */
public abstract class XMLAble implements DataBaseAble {

    protected Document xml;

    /**
     * OUverture du fichier XML
     */
    public void connectToDatabase() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            File fileXML = new File("../bd/xml_menu/plats.xml");

            xml = builder.parse(fileXML);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    @Override
    /**
     * Fermeture du fichier XML
     */
    public void disconnect() {

    }

    @Override
    /**
     * Node recherche dans le fichier XML
     */
    public String request(String request) {
        String requette = "{\"plats\" :[";
        String expression = "";

        try {

            connectToDatabase();

            Element root = xml.getDocumentElement();
            XPathFactory xpf = XPathFactory.newInstance();
            XPath path = xpf.newXPath();

            NodeList list = (NodeList) path.evaluate(request, root, XPathConstants.NODESET);

            int nodesLength = list.getLength();

            //Parcours de la boucle
            for (int i = 0; i < nodesLength; i++) {
                Node n = list.item(i);

                requette += "{";

                expression = "id";
                Node nodeID = (Node) path.evaluate(expression, n, XPathConstants.NODE);
                requette += "\"" + nodeID.getNodeName() + "\": \"" + nodeID.getTextContent() + "\",";
                
                expression = "nom";
                Node nodenom = (Node) path.evaluate(expression, n, XPathConstants.NODE);
                requette += "\"" + nodenom.getNodeName() + "\": \"" + nodenom.getTextContent() + "\",";
                
                expression = "type";
                Node nodetype = (Node) path.evaluate(expression, n, XPathConstants.NODE);
                requette += "\"" + nodetype.getNodeName() + "\": \"" + nodetype.getTextContent() + "\",";

                expression = "prix";
                Node nodeprix = (Node) path.evaluate(expression, n, XPathConstants.NODE);
                requette += "\"" + nodeprix.getNodeName() + "\": \"" + nodeprix.getTextContent() + "\",";

                expression = "ingredients";
                path.compile(expression);

                NodeList ingredients = (NodeList) path.evaluate(expression, n, XPathConstants.NODESET);

                requette += "\"ingredients\": [";
                for (int j = 0; j < ingredients.getLength(); j++) {
                    Node ingredient = ingredients.item(j);
                    if( j != ingredients.getLength() - 1 ){
                        requette += "\"" + ingredient.getTextContent() + "\",";
                    }else{
                        requette += "\"" + ingredient.getTextContent() + "\"";
                    }
                }
                requette += "]";

                if(i != nodesLength - 1){
                    requette += "},";
                }else{
                    requette += "}";
                }
            }

            return requette + "]}";

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return requette;
    }

    @Override
    public abstract void logs(String request);
    
    

}
