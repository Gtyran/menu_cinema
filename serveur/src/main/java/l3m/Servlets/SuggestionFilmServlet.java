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
import java.util.logging.Level;
import java.util.logging.Logger;
import l3m.Able.XMLAble;
import l3m.Gestionnaires.GestionnaireClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import l3m.Gestionnaires.GestionnaireMenu;

/**
 *
 * @author hp
 */
public class SuggestionFilmServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String champId = "id";
    private final String champid_menu = "id_menu";
    GestionnaireCommande gCommande;

    String films_json;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");

        Enumeration<String> P = request.getParameterNames();
        HashMap<String, String> parametres = new HashMap();

        while (P.hasMoreElements()) {
            String p = P.nextElement();
            parametres.put(p, request.getParameter((String) p));
        }

        if (platValideValues(parametres)) {

            try {
                gCommande = new GestionnaireCommande(champId, null, null, null);
                gCommande.logs("../exec/extras/./sequenceDiagram_1.sh");
                Thread.sleep(1000);
                
                List<String> f = gCommande.getListFilmsForPlat(parametres.get(champid_menu));
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(f);
                
                gCommande.logs("../exec/extras/./sequenceDiagram_3.sh");
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SuggestionFilmServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("{\"error\" : \"Paramètres non complets\"}");
        }

    }

    private boolean platValideValues(HashMap<String, String> params) {
        boolean valide = false;

        if (params.containsKey(champId)
                && params.containsKey(champid_menu)) {
            if (params.get(champId) != null
                    && params.get(champid_menu) != null) {
                valide = true;
            }
        }
        return valide;
    }
}
