/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l3m.Servlets;

import bindingClasse.Plats;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import l3m.Gestionnaires.GestionnaireCommande;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import l3m.Able.XMLAble;
import l3m.Gestionnaires.GestionnaireClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import l3m.Gestionnaires.GestionnaireMenu;

/**
 *
 * @author hp
 */
public class SuggestionPlatServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String champId = "id";
    private final String champid_film = "id_film";
    GestionnaireCommande gCommande;
    GestionnaireMenu gMenu;

    List<String> plats = new ArrayList<>();

    /**
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

        Enumeration<String> P = request.getParameterNames();//pour stocker les noms des attributs dans ce cas ifFilm
        HashMap<String, String> parametres = new HashMap();//pour stocker la valeur de l'attribut idFilm

        while (P.hasMoreElements()) {
            String p = P.nextElement();
            parametres.put(p, request.getParameter((String) p));
        }

        if (filmValideValues(parametres)) {
            try {
                gCommande = new GestionnaireCommande(parametres.get(champId), null, null, null);
                gCommande.logs("../exec/extras/./sequenceDiagram_4.sh");
                Thread.sleep(1000);
                
                plats = gCommande.getListPlatForFilm(parametres.get(champid_film));
                gMenu = new GestionnaireMenu(plats);
                Plats ps = gMenu.getMenu();
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(plats);
                
                gCommande.logs("../exec/extras/./sequenceDiagram_6.sh");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SuggestionPlatServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("{\"error\" : \"Paramètres non complets\"}");
        }

    }

    /**
     * Verfie qu'un film reçu possède bien les parametres attendus et ne sont
     * pas null
     *
     * @param params HashMap representant les <cle-valeur> du paramètre d'appel
     * @return true si les paramètres obligatoires sont présents false sinon
     */
    private boolean filmValideValues(HashMap<String, String> params) {
        boolean valide = false;

        if (params.containsKey(champId) && params.containsKey(champid_film)
                && params.get(champid_film) != null && params.get(champId) != null) {
            valide = true;
        }
        return valide;
    }

}
