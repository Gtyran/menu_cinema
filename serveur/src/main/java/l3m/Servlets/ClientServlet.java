/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l3m.Servlets;

import l3m.Gestionnaires.GestionnaireClient;
import bindingClasse.Client;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cash
 */
public class ClientServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String champId = "userId";
    private final String champNom = "nom";
    private final String champPrenom = "prenom";
    private final String champPhoto = "photo";
    private final String champEmail = "email";
    private final String champTel = "tel";
    private final String champAdresse = "adresse";
    private final String champPoints = "points";

    private final String id = "id";
    private final String nom = "nom";
    private final String prenom = "prenom";
    private final String email = "email";
    private final String tel = "tel";
    private final String photo = "photo";
    private final String points = "points";
    private final String adresse = "adresse";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");

        Enumeration<String> P = request.getParameterNames();
        HashMap<String, String> parametres = new HashMap();
        Client client = new Client();

        while (P.hasMoreElements()) {
            String p = P.nextElement();
            parametres.put(p, request.getParameter((String) p));
        }
        client.setId(parametres.get(id));
        client.setNom(parametres.get(nom));
        client.setPrenom(parametres.get(prenom));
        client.setEmail(parametres.get(email));
        client.setPhoto(parametres.get(photo));
        client.setPoints((int) Integer.parseInt(parametres.get(points)));
        client.setTel(parametres.get(tel));
        client.setAdresse(parametres.get(adresse));

        //System.out.println("\nclient à modifier: \n " + client.toString() + "\n");
        GestionnaireClient gestionClient = new GestionnaireClient(client.getId(), client.getNom(), client.getPrenom());
        
        if (putValide(parametres)) {
            try {
                gestionClient.logs("../exec/client/./sequenceDiagram_6.sh");
                Thread.sleep(1000);
                
                gestionClient.setClient(client);
                //System.out.println("\nclient à modifier: \n " + gestionClient.getClient().toString() + "\n");
                gestionClient.editClientDB();

                client = gestionClient.getClient();

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(client.toString());  
                
                gestionClient.logs("../exec/client/./sequenceDiagram_8.sh");
                Thread.sleep(1000);

            } catch (SQLException ex) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println(ex.getMessage());
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("{\"error\" : \"Paramètres non complets\"}");
        }

    }

    /*@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");

        Enumeration<String> P = request.getParameterNames();
        HashMap<String, String> parametres = new HashMap();
        Client client = new Client();
        GestionnaireClient gestionClient;

        while (P.hasMoreElements()) {
            String p = P.nextElement();
            parametres.put(p, request.getParameter((String) p));
        }

        client.setId(parametres.get(id));

        if (getClientValide(parametres)) {

            try {
                gestionClient = new GestionnaireClient(client.getId(), null, null);
                if (gestionClient.existsClientDB()) {
                    client = gestionClient.getClient();
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println(client.toString());
                }
            } catch (SQLException se) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("{\"error\" : \"" + se.getMessage() + "\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("{\"error\" : \"Le client demandé n'est pas dans la base\"}");
        }
    }*/

    /**
     * renvoie vraie si les clefs valeur sont correct
     *
     * @param parametres
     * @return
     */
    private boolean putValide(HashMap<String, String> parametres) {
        boolean res = false;

        if (parametres.containsKey(id) && parametres.containsKey(nom) && parametres.containsKey(prenom)
                && parametres.containsKey(email) && parametres.containsKey(tel) && parametres.containsKey(photo)
                && parametres.containsKey(adresse)) {
            if (parametres.get(id) != null) {
                res = true;
            }
        }

        return res;
    }

    private boolean getClientValide(HashMap<String, String> parametres) {
        boolean res = false;

        if (parametres.containsKey(id) && (parametres.get(id) != null)) {
            res = true;
        }

        return res;
    }
}
