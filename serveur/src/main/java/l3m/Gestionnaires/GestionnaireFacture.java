/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l3m.Gestionnaires;

import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import bindingClasse.Commande;
import bindingClasse.Plat;
import bindingClasse.Plats;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author cash
 */
public class GestionnaireFacture {

    public Document _doc;
    private Commande commande;

    public GestionnaireFacture() {
        commande = new Commande();
    }

    public GestionnaireFacture(Commande commande) {
        this.commande = commande;
    }

    /**
     * Construit un gestionnaire pour une commande particulière deja stockée
     *
     * @param idCommande la commande dont la facture doit etre generée
     */
    /*public GestionnaireFacture(String idCommande) {
        try {
            GestionnaireCommande gestCom = new GestionnaireCommande(idCommande);
            commande = gestCom.getCommande();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }*/
    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    /**
     * Méthode qui crée une partie dans le DOM et retourne cet element
     * https://examples.javacodegeeks.com/core-java/xml/dom/create-dom-document-from-scratch/
     *
     * @return Element
     */
    public Document createFactureOnDOM() {
        Document doc = null;
        Element factureDom;
        Element eltIdCommande;
        Element eltClient;
        Element eltFilmS;
        Element eltPlatS;
        Element eltPoints;
        Element eltReduction;
        Element eltSousTotal;
        Element eltTotal;
        Element eltDate;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            doc = builder.newDocument();

            //balise partie avec attribut date et trouvé        
            factureDom = (Element) doc.createElement("facture");

            eltIdCommande = (Element) doc.createElement("idcommande");
            eltIdCommande.appendChild(doc.createTextNode(commande.getId()));

            eltClient = (Element) doc.createElement("client");
            //eltClient.setAttribute("id", commande.getIdclient());
            //!\on devrait rechercher le client possedant l'id et mettre son nom là/!\
            eltClient.appendChild(doc.createTextNode(commande.getIdclient()));

            eltFilmS = (Element) doc.createElement("films");
            for (String id : commande.getIdFilms()) {
                Element eltfilm = (Element) doc.createElement("film");
                //eltfilm.setAttribute("id", id);

                eltfilm.appendChild(doc.createTextNode(id));

                //Element eltQte = (Element) doc.createElement("quantite");
                //eltQte.appendChild(doc.createTextNode());
                //eltfilm.appendChild(eltQte);
                //Element eltPrix = (Element) doc.createElement("prix");
                //eltPrix.appendChild(doc.createTextNode("3.75"));
                //eltfilm.appendChild(eltPrix);
                eltFilmS.appendChild(eltfilm);
            }

            eltPlatS = (Element) doc.createElement("plats");
            //System.out.println(commande.getIdPlats().toString());

            List<String> getIdPlats_tmp = commande.getIdPlats().stream().distinct().collect(Collectors.toList());
            Map<Integer, Integer> count_id_plats = new HashMap<Integer, Integer>();

            /**
             * Remplissage du tabeau Des id plats
             */
            for (int i = 0; i < getIdPlats_tmp.size(); i++) {
                count_id_plats.put(Integer.parseInt(getIdPlats_tmp.get(i)), 0);
            }

            //System.out.println(">>>>  1 >>>>>>>>>>>" + count_id_plats.toString());
            /**
             * Count chaque valeur
             */
            for (int i = 0; i < commande.getIdPlats().size(); i++) {
                Set<Map.Entry<Integer, Integer>> setHm = count_id_plats.entrySet();
                Iterator<Map.Entry<Integer, Integer>> it = setHm.iterator();
                while (it.hasNext()) {
                    Map.Entry<Integer, Integer> e = it.next();
                    if (Integer.parseInt(commande.getIdPlats().get(i)) == e.getKey()) {
                        count_id_plats.replace(e.getKey(), (e.getValue() + 1));
                    }
                }
            }

            /**
             * Inserts values in plats table
             */
            //System.out.println(">>>>  2 >>>>>>>>>>>" + count_id_plats.toString());
            Set<Map.Entry<Integer, Integer>> setHm = count_id_plats.entrySet();
            Iterator<Map.Entry<Integer, Integer>> it = setHm.iterator();

            while (it.hasNext()) {
                Map.Entry<Integer, Integer> e = it.next();

                Element eltPlat = (Element) doc.createElement("plat");

                eltPlat.setAttribute("id", e.getKey().toString());
                //Element eltNom = (Element) doc.createElement("nom");
                //rechercher le nom du plat et le mettre
                //eltNom.appendChild(doc.createTextNode("3.75"));
                //eltPlat.appendChild(eltNom);

                Element eltQte = (Element) doc.createElement("quantite");
                eltQte.appendChild(doc.createTextNode(e.getValue().toString()));
                eltPlat.appendChild(eltQte);

                Element eltPrix = (Element) doc.createElement("prix");
                //recherche du prix du plat
                GestionnaireMenu prix_plat = new GestionnaireMenu();
                Plats les_plats = prix_plat.getCarteDB();

                Double prix = les_plats.getPrixPlat(e.getKey().toString());

                eltPrix.appendChild(doc.createTextNode(prix.toString()));
                eltPlat.appendChild(eltPrix);

                eltPlatS.appendChild(eltPlat);
            }

            eltDate = (Element) doc.createElement("date");
            eltDate.appendChild(doc.createTextNode(commande.getDate()));

            GestionnaireClient client = new GestionnaireClient(commande.getIdclient(), null, null);
            Integer points_client = client.getPoint();
            eltPoints = (Element) doc.createElement("points");
            eltPoints.appendChild(doc.createTextNode("" + points_client));

            String prix_reduction = new DecimalFormat("##.##").format(commande.getReduction());
            prix_reduction = prix_reduction.replace(",", ".");

            eltReduction = (Element) doc.createElement("reduction");
            eltReduction.appendChild(doc.createTextNode("" + prix_reduction));
            
            String prix_s = new DecimalFormat("##.##").format(commande.getPrix());
            prix_s = prix_s.replace(",", ".");
            
            eltSousTotal = (Element) doc.createElement("sous-total");
            eltSousTotal.appendChild(doc.createTextNode("" + prix_s));
            
            
            String prix_toatl = new DecimalFormat("##.##").format((commande.getPrix() - commande.getReduction()));
            prix_toatl = prix_toatl.replace(",", ".");
            
            eltTotal = (Element) doc.createElement("total");
            eltTotal.appendChild(doc.createTextNode("" + prix_toatl));

            //relie 
            factureDom.appendChild(eltIdCommande);
            factureDom.appendChild(eltClient);
            factureDom.appendChild(eltDate);
            factureDom.appendChild(eltFilmS);
            factureDom.appendChild(eltPlatS);
            factureDom.appendChild(eltPoints);
            factureDom.appendChild(eltReduction);
            factureDom.appendChild(eltSousTotal);
            factureDom.appendChild(eltTotal);
            doc.appendChild(factureDom);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }

        return doc;
    }

    public static final String prettyPrint(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));

        return out.toString();
    }
}
