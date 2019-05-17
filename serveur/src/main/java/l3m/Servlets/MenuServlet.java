package l3m.Servlets;

import l3m.Gestionnaires.GestionnaireMenu;
import l3m.Gestionnaires.GestionnaireClient;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bindingClasse.Ingredient;
import bindingClasse.Plat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String champIdclient  = "id";
    private final String champAdresse   = "adresse_livraison";
    private final String champDate      = "date";
    private final String champIdsFilm   = "idsFilm";
    private final String champIdsPlat   = "idsPlat";

    /**
     * Methode des requetes get sur /api/menu/ Renvoie en response les Menus du
     * client donné dans le corps de la requete
     *
     * Recupère les Menus disponible dans le fichier XML getMenu(String id):
     * Menu de la classe Menu
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

        HashMap<String, String> parametres = new HashMap();
        GestionnaireMenu menu = new GestionnaireMenu();
        GestionnaireClient gestionClient;
        
        boolean res = false;
        String requette = "[";

        /**
         * Recherche des Menus d'un client recup id de url et id command
         * pour une Menu specifique recup id pour retourner toutes les
         * Menus chercher dans la base la Menu et la retourner
         */
        parametres.put(champIdclient, request.getParameter("id"));
        //System.out.println("Get plats pour le client " + parametres.get(champIdclient));
        gestionClient = new GestionnaireClient(parametres.get(champIdclient), null, null);
        //System.out.println("id " + parametres.get(champIdclient));

        if (menuValide(parametres)) {

            // on retourne un Menu specifique
            try {
                menu.logs("../exec/client/./sequenceDiagram_9.sh");
                Thread.sleep(1000);
                
                Iterator<Plat> listPlats = menu.getCarteDB().getPlats().iterator();

                while (listPlats.hasNext()) {
                    Plat value = listPlats.next();

                    requette += "{";

                    requette += "\"id\": \"" + value.getId() + "\",";
                    requette += "\"nom\": \"" + value.getNom() + "\",";
                    requette += "\"type\": \"" + value.getType() + "\",";
                    requette += "\"prix\": \"" + value.getPrix() + "\",";

                    requette += "\"ingredients\": [";
                    Iterator<Ingredient> listIngredients = value.getIngredients().iterator();
                    while (listIngredients.hasNext()) {
                        Ingredient valueIngredient = listIngredients.next();
                        if (listIngredients.hasNext()) {
                            requette += "\"" + valueIngredient.value() + "\",";
                        } else {
                            requette += "\"" + valueIngredient.value() + "\"";
                        }
                    }
                    requette += "]";

                    if (listPlats.hasNext()) {
                        requette += "},";
                    } else {
                        requette += "}";
                    }

                }

                requette += "]";

                //transformation du contenu en JSON
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(requette);
                
                menu.logs("../exec/client/./sequenceDiagram_10.sh");
                Thread.sleep(1000);
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("{\"error\" : \"Erreur d'interrogation de la BD\"}");
                System.out.println(e.getMessage());
            } catch (InterruptedException ex) {
                Logger.getLogger(MenuServlet.class.getName()).log(Level.SEVERE, null, ex);
            }   //on retourne tous les Menus
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("{\"error\" : \"Paramètre(s) attendu absent(s) de la requete ou client inexistant\"}");
        }

    }

    /**
     * Methode qui va etre appelée par les requetes post à partir de l'url
     * /api/Menu/
     *
     * Enregistre la Menu en appelant la méthode enregistrerMenuBD(): void de
     * GestionnaireMenu
     *
     * @param request requete envoyée par le client
     * @param response reponse de la requete
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");

        // Extract userId from HTTP parameters
        Enumeration<String> P = request.getParameterNames();
        HashMap<String, String> parametres = new HashMap();

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
     * Verfie qu'une Menu reçue possède bien les parametres attendus et ne sont
     * pas null
     *
     * @param params
     * @return
     */
    private boolean menuValide(HashMap<String, String> params) {
        boolean valide = false;

        if (params.containsKey(champIdclient)) {
            if (params.get(champIdclient) != null) {
                valide = true;
            }
        }
        return valide;
    }
}
