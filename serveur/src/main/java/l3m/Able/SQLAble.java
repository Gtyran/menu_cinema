/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l3m.Able;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author hanh1
 */
public abstract class SQLAble implements DataBaseAble {

    protected Connection dbConnection;

    @Override
    public void connectToDataBase() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            File fileXML = new File("../bd/xml_connexion/config.xml");

            Document document = builder.parse(fileXML);
            XPath xpath = XPathFactory.newInstance().newXPath();
            String hostname = (String) xpath.compile("//config//jdbc//serveur").evaluate(document, XPathConstants.STRING);
            String port = (String) xpath.compile("//config//jdbc//port").evaluate(document, XPathConstants.STRING);
            String dbname = (String) xpath.compile("//config//jdbc//db").evaluate(document, XPathConstants.STRING);
            String identidiant = (String) xpath.compile("//config//jdbc//username").evaluate(document, XPathConstants.STRING);
            String mot_de_passe = (String) xpath.compile("//config//jdbc//password").evaluate(document, XPathConstants.STRING);

            // chaine de connexion à modifier si bd differente de MySql
            String conn_url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbname + "?useLegacyDatetimeCode=false&serverTimezone=Europe/Paris";

            dbConnection = null;

            try {
                // Enregistrement du driver Mysql
                dbConnection = DriverManager.getConnection(conn_url, identidiant, mot_de_passe);

                dbConnection.setAutoCommit(true);

            } catch (SQLException e) {
                System.err.println("Erreur paramètres de connexion Mysql :");
                System.err.println(e.getMessage());

            }

            if (dbConnection != null) {
                //.println("You made it, take control your database now!");
            } else {
                //System.out.println("Failed to make connection!");
            }

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Where is your MySQL JDBC Driver?");
                e.printStackTrace();
                return;
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SQLAble.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(SQLAble.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SQLAble.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(SQLAble.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void disconnect() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            System.err.println("Erreur fermeture connexion Mysql :");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public java.sql.ResultSet request(String request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public abstract void logs(String request);

}
