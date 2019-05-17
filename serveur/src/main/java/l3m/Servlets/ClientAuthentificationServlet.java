package l3m.Servlets;

import bindingClasse.Client;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import l3m.BdAccess;

// Je suis passé par l'itération 0 du serveur...
public class ClientAuthentificationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String champId        = "id";
    private final String champNom       = "nom";
    private final String champPrenom    = "prenom";
    private final String champPhoto     = "photo";
    private final String champEmail     = "email";
    private final String champTel       = "tel";
    private final String champAdresse   = "adresse";
    private final String champPoints    = "points";

    /*____________________________________________________________________________________________________________________
	 * doPost is expecting a HTTP parameter userId
	 * It sends back a XML serialization of the previous command with HTTP code 200 if a userId is specifyed
	 * It sends back a HTTP code 401 error if the userId is not specified or empty
	 * It sends back a HTTP code 500 error if a problem occured when accessing to the database
     */
    /**
     * Modifiez les méthodes doPost de la classe ClientAuthentificationServlet
     * de sorte à vérifier que la requête reçut contient un paramètre
     * clientToken. Si ça n’est pas le cas, renvoyez une erreur HTTP 401
     * (HttpServletResponse.SC_UNAUTHORIZED). Si c’est le cas, alors renvoyez un
     * code HTTP 200 (HttpServletResponse.SC_OK) avec le contenu produit par la
     * méthode BdAccess.authentifyUser.
     *
     * @param request
     * @param response
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
        String res = "";
        Enumeration<String> P = request.getParameterNames();
        HashMap<String, String> parametres = new HashMap();
        Client client = new Client();

        while (P.hasMoreElements()) {
            String p = P.nextElement();
            parametres.put(p, request.getParameter((String) p));
        }

        // Call the database and return result
        if (clientValideLoging(parametres)) {
            try {
                client.setId(parametres.get(champId));
                client.setNom(parametres.get(champNom));
                client.setPrenom(parametres.get(champPrenom));

                res = BdAccess.authentifyUser(client);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(res);

            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println(e.toString());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("{\"error\" : \"Paramètre(s) attendu absent(s) de la requete\"}");
        }

    }


    /**
     * Verfie qu'un client reçu possède bien les parametres attendus et ne sont
     * pas null
     *
     * @param params HashMap representant les <cle-valeur> du paramètre d'appel
     * @return true si les paramètres obligatoires sont présents false sinon
     */
    private boolean clientValideLoging(HashMap<String, String> params) {
        boolean valide = false;

        if (params.containsKey(champId) && params.containsKey(champNom)
                && params.containsKey(champPrenom) && params.containsKey(champPhoto)
                && params.containsKey(champEmail) && params.containsKey(champTel)
                && params.containsKey(champAdresse) && params.containsKey(champPoints)) {
            if (params.get(champId) != null
                    && params.get(champNom) != null
                    && params.get(champPrenom) != null) {
                valide = true;
            }
        }
        return valide;
    }

}
