package l3m;

import l3m.Gestionnaires.GestionnaireMenu;
import l3m.Gestionnaires.GestionnaireClient;
import bindingClasse.Client;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Je suis passé par l'itération 0 de la BD...
public class BdAccess {

    /**
     * Verifie si un client existe dans la base sinon l'enregistre et le
     * retourne sous la forme JSON + Data plats
     *
     * @param userId Identifiant fourni par firebase
     * @return Une chaine de caractères en forme de JSON
     * @throws SQLException
     */
    public static String authentifyUser(Client client) throws SQLException {
        String res = "";

        GestionnaireClient clt = new GestionnaireClient(client.getId(), client.getNom(), client.getPrenom());
        
        clt.logs("../exec/client/./sequenceDiagram_1.sh");

        //nouveau client
        if (clt.enregistrerClientDB()) {
            res = "{\n"
                    + "    \"id\": \"" + client.getId() + "\",\n"
                    + "    \"nom\": \"" + clt.getNom() + "\",\n"
                    + "    \"prenom\": \"" + clt.getPrenom() + "\",\n"
                    + "    \"email\": \"" + clt.getEmail() + "\",\n"
                    + "    \"adresse\": \"" + clt.getAdresse() + "\",\n"
                    + "    \"photo\": \"" + clt.getPhoto() + "\",\n"
                    + "    \"tel\": \"" + clt.getTel() + "\",\n"
                    + "    \"points\": \"" + clt.getPoints() + "\"\n"
                    + "}";
        } else {
            res = "{\"error\" : \"L'enregistremenet du client n'a pas pu se faire !\"}";
        }

        return res;
    }

    static String listeAllPlats() throws SQLException {
        String res = "";
        boolean request = false;

        GestionnaireMenu plats = new GestionnaireMenu();

        return res;
    }

}
