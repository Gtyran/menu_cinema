package l3m.Servlets;

import l3m.Gestionnaires.GestionnaireCommande;
import l3m.Gestionnaires.GestionnaireFacture;
import l3m.Gestionnaires.GestionnaireMenu;
import l3m.Gestionnaires.GestionnaireClient;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bindingClasse.Commande;
import bindingClasse.Plats;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;

public class CommandeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String champIdCommande = "id";
    private final String champIdclient = "idclient";
    private final String champAdresse = "adresseLivraison";
    private final String champDate = "date";
    private final String champIdsFilm = "idsFilm";
    private final String champIdsPlat = "idsPlat";
    private final String champUtilisePoint = "point_utilise";
    private final double prixFilm = 3.75;

    /**
     * Methode des requetes get sur /api/commande/ Renvoie en response les
     * commandes du client donné dans le corps de la requete
     *
     * Recupère les commandes d'un client donné par appel de la méthode
     * getCommande(String id): Commande de la classe commande
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");

        if (request.getParameter("bestsellerplats") != null && request.getParameter("idclient") != null) {
            try {
                String res = "";
                List<Integer[]> list_plats = new ArrayList<>();

                GestionnaireCommande gestCom = new GestionnaireCommande(request.getParameter("idclient"), null, null, null);
                gestCom.logs("../exec/extras/./sequenceDiagram_7.sh");
                Thread.sleep(1000);

                list_plats = gestCom.getListBestSellerPlat();

                if (list_plats.size() > 0) {
                    res += "[\n";
                    ListIterator<Integer[]> it = list_plats.listIterator();
                    while (it.hasNext()) {
                        Integer[] str = it.next();
                        if (it.hasNext()) {
                            res += "{\n"
                                    + "    \"id\": \"" + str[0] + "\",\n"
                                    + "    \"nombre_vente\": \"" + str[1] + "\"\n"
                                    + "},\n";
                        } else {
                            res += "{\n"
                                    + "    \"id\": \"" + str[0] + "\",\n"
                                    + "    \"nombre_vente\": \"" + str[1] + "\"\n"
                                    + "}";
                        }

                    }
                    res += "]\n";
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println(res);

                    gestCom.logs("../exec/extras/./sequenceDiagram_9.sh");
                    Thread.sleep(1000);

                } else {
                    res = "{\"error\" : \"Pas de meilleur vente de plat pour cette requête !\"}";
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(CommandeServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            String res = "";
            HashMap<String, String> parametres = new HashMap();
            GestionnaireCommande gestCom;
            Commande commande = new Commande();
            GestionnaireClient gestCl;
            List<String> lstIdsCmdes;
            List<Commande> listeCmdes = new ArrayList<>();

            /**
             * Recherche des commandes d'un client recup idclient de url et id
             * command pour une commande specifique recup idclient pour
             * retourner toutes les commandes chercher dans la base la commande
             * et la retourner
             */
            parametres.put(champIdclient, request.getParameter("idclient"));
            parametres.put(champIdCommande, request.getParameter("id"));
            response.setContentType("application/json");

            //System.out.println("idclient " + parametres.get(champIdclient));
            //System.out.println("id commande " + parametres.get(champIdCommande));
            if (getAllCommandeValide(parametres)) {
                if (getSpecCommandeValide(parametres)) {
                    // on retourne une commande specifique
                    gestCom = new GestionnaireCommande(parametres.get(champIdclient), null, null, null);
                    commande = gestCom.getCommande(parametres.get(champIdCommande));
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println(commande.toString());
                } else {
                    /*
                    on doit retourner toutes les commandes
                
                    Comme on a deja les ids des commandes on peut aller les chercher dans la base
                     */

                    gestCl = new GestionnaireClient(parametres.get(champIdclient), null, null);
                    try {
                        lstIdsCmdes = gestCl.getListeCommande();
                        if (lstIdsCmdes.size() > 0) {
                            gestCom = new GestionnaireCommande(parametres.get(champIdclient), null, null, null);
                            res += "[";
                            for (String idCom : lstIdsCmdes) {
                                res += gestCom.getCommande(idCom).toString();
                                if (lstIdsCmdes.indexOf(idCom) != lstIdsCmdes.size() - 1) {
                                    res += " ,";
                                }
                            }
                            res += "]";
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().println(res);
                        } else {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().println("[]");
                        }
                    } catch (SQLException e) {
                        System.out.println("erreur > CommandeServlet: doGet");
                        System.out.println("Message: " + e.getMessage());

                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().println("Une erreur interne empêche le serveur de repondre à votre demande");
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("Champ client non specifié");
            }
        }
    }

    /**
     * Methode qui va etre appelée par les requetes post à partir de l'url
     * /api/commande/
     *
     * Enregistre la commande en appelant la méthode enregistrerCommandeBD():
     * void de GestionnaireCommande
     *
     * @param request requete envoyée par le client
     * @param response reponse de la requete
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");

        // Extract userId from HTTP parameters
        Enumeration<String> P = request.getParameterNames();
        HashMap<String, String> parametres = new HashMap();
        GestionnaireCommande gestCom;
        Commande commande = new Commande();
        String res = "";
        String idclient = "";
        String adresse_livraison = "";
        List<String> idFilms;
        List<String> idPlats;
        double prix = 0.0;

        while (P.hasMoreElements()) {
            String p = P.nextElement();
            parametres.put(p, request.getParameter(p));
        }

        Boolean utilise_point = Boolean.parseBoolean(parametres.get(champUtilisePoint));

        // on decoupe la liste d'id reçue du client en List d'id
        if (parametres.get(champIdsFilm) != null) {
            idFilms = Arrays.asList(parametres.get(champIdsFilm).split("\\s*,\\s*"));
        } else {
            idFilms = new ArrayList<>();
        }

        if (parametres.get(champIdsPlat) != null) {
            idPlats = Arrays.asList(parametres.get(champIdsPlat).split("\\s*,\\s*"));
        } else {
            idPlats = new ArrayList<>();
        }

        if (commandeValide(parametres, idFilms, idPlats)) {
            idclient = parametres.get(champIdclient);
            adresse_livraison = parametres.get(champAdresse);
            try {
                //GestionnaireCommande(String idClient, List<String> idPlats, List<String> idFilms, String adresseLivraison)
                gestCom = new GestionnaireCommande(idclient, idPlats, idFilms, adresse_livraison);

                gestCom.logs("../exec/commande/./sequenceDiagram_1.sh");
                Thread.sleep(1000);

                /**
                 * Le serveur métier se charge d’enregistrer la commande et
                 * d’obtenir les détails des plats (prix, nom…) à partir de
                 * leurs identifiants en lisant la base XML des plats. Une
                 * facture est générée par une Servlet (NB: les prix des films
                 * sont forfaitaires, donc constants à 3€75) et renvoyée au
                 * client Angular qui se charge de l’afficher pour le client.
                 */
                commande = gestCom.getCommande();
                prix = calculPrixCommande(commande);
                gestCom.logs("../exec/commande/./sequenceDiagram_2.sh");
                Thread.sleep(1000);

                commande.setPrix(prix);
                gestCom.logs("../exec/commande/./sequenceDiagram_3.sh");
                Thread.sleep(1000);

                gestCom.setCommande(commande);
                gestCom.logs("../exec/commande/./sequenceDiagram_4.sh");
                Thread.sleep(1000);
                gestCom.enregistrerCommandeDB(utilise_point);

                response.setStatus(HttpServletResponse.SC_OK);

                response.getWriter().println(etablirFacture(commande));
                gestCom.logs("../exec/commande/./sequenceDiagram_6.sh");
                Thread.sleep(1000);
            } catch (SQLException se) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println(se.getMessage());
            } catch (InterruptedException ex) {
                Logger.getLogger(CommandeServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Requete non valide");
        }
    }

    private String etablirFacture(Commande commande) {
        String res = "";

        try {
            GestionnaireFacture gestFacture = new GestionnaireFacture(commande);
            Document facture = gestFacture.createFactureOnDOM();
            res = GestionnaireFacture.prettyPrint(facture);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return res;
    }

    /**
     * Calcule le prix total d'une commande en faisant la somme du total du prix
     * des films et des plats
     *
     * @param commande commande dont le prix doit etre calculé
     * @return Le prix total de la commande
     */
    private double calculPrixCommande(Commande commande) {
        double prix = 0.0;
        GestionnaireMenu gestionnaireMenu;
        Plats plats;

        prix += prixFilm * commande.getIdFilms().size();
        gestionnaireMenu = new GestionnaireMenu(commande.getIdPlats());

        plats = gestionnaireMenu.getCarteDB();

        for (String id : commande.getIdPlats()) {
            prix += plats.getPrixPlat(id);
        }
        return prix;
    }

    /**
     * Fais une sorte de JSON avec les parametres de la requete et leur valeur
     *
     * @param request
     * @return
     */
    private String processQueryTest(HttpServletRequest request) {
        String res = "{";
        Enumeration<String> P = request.getParameterNames();
        while (P.hasMoreElements()) {
            String p = P.nextElement();
            res += "\"" + p + "\": \"" + request.getParameter(p) + "\"";
            if (!P.hasMoreElements()) {
                res += ", ";
            }
        }
        return res + "}";
    }

    /**
     * Verfie qu'une commande reçue possède bien les parametres attendus et ne
     * sont pas null
     *
     * @param params
     * @param idF
     * @param idP
     * @return Vrai si Si les paramètres contiennent les champs idclient (pas
     * vide), addresse et idsFilm (pas vide)
     *
     * Ou alors les paramètres contiennent les champs idclient (pas vide),
     * addresse et idsPlats (pas vide)
     *
     * Ou bien les paramètres contiennent les champs idclient (pas vide),
     * addresse et idsFilm (pas vide) et idsPlats (pas vide)
     */
    private boolean commandeValide(HashMap<String, String> params, List<String> idF, List<String> idP) {
        boolean valide = false;

        if (params.containsKey(champIdclient) && params.containsKey(champAdresse)
                && params.containsKey(champIdsFilm) && params.containsKey(champUtilisePoint)) {
            if (params.get(champIdclient) != null && !idF.get(0).equals("")) {
                valide = true;
            }
        } else if (params.containsKey(champIdclient) && params.containsKey(champAdresse)
                && params.containsKey(champIdsPlat) && params.containsKey(champUtilisePoint)) {
            if (params.get(champIdclient) != null && !idP.get(0).equals("")) {
                valide = true;
            }
        } else if (params.containsKey(champIdclient) && params.containsKey(champAdresse)
                && params.containsKey(champIdsPlat) && params.containsKey(champIdsFilm) && params.containsKey(champUtilisePoint)) {
            if (params.get(champIdclient) != null && !idP.get(0).equals("") && !idF.get(0).equals("")) {
                valide = true;
            }
        }

        return valide;
    }

    /**
     * Verfie si une requette est pour avoir toutes les commandes
     *
     * @param params Paramètres de la requete
     * @return Renvoie vrai si la requete poss
     */
    private boolean getAllCommandeValide(HashMap<String, String> params) {
        boolean valide = false;

        if (params.containsKey(champIdclient) && params.get(champIdclient) != null) {
            valide = true;
        }
        return valide;
    }

    /**
     * Verfie si une requette est pour avoir toutes une commande specifique
     *
     * @param params Paramètres de la requete
     * @return Renvoie vrai si la requete poss
     */
    private boolean getSpecCommandeValide(HashMap<String, String> params) {
        boolean valide = false;

        if (params.containsKey(champIdclient) && params.containsKey(champIdCommande)) {
            if (params.get(champIdclient) != null && params.get(champIdCommande) != null) {
                valide = true;
            }
        }
        return valide;
    }
}
