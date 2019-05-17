/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l3m.Gestionnaires;

/**
 *
 * @author Groupe 7
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import bindingClasse.Commande;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import l3m.Able.SQLAble;

public class GestionnaireCommande extends SQLAble {

    private Commande commande;

    public GestionnaireCommande(String idClient, List<String> idPlats, List<String> idFilms, String adresseLivraison) {
        Timestamp today_date = new Timestamp(System.currentTimeMillis());

        commande = new Commande();
        commande.setIdclient(idClient);
        commande.setIdPlats(idPlats);
        commande.setIdFilms(idFilms);
        commande.setAdresseLivraison(adresseLivraison);
        //https://stackoverflow.com/questions/6777810/a-datetime-equivalent-in-java-sql-is-there-a-java-sql-datetime
        commande.setDate(today_date.toString());
    }

    /**
     * Recupère la commande dont l'id passé en paramètre données
     *
     * @param id identifiant de la commande à recuperer
     * @throws SQLException
     */
    public GestionnaireCommande(String id) throws SQLException {
        String selectCommande = "select id, id_client, `date`, reduction, prix, adresse_livraison from commandes where id = ? and id_client = ?";
        String selectFilms = "select * from films where id_commande = ?";
        String selectPlats = "select * from plats where id_commande = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connectToDataBase();
            ps = dbConnection.prepareStatement(selectCommande);

            ps.setInt(1, Integer.parseInt(id));
            /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
            ps.setString(2, commande.getIdclient());
            /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
            rs = ps.executeQuery();
            while (rs.next()) {
                commande = new Commande();

                commande.setId(rs.getString(1));
                commande.setIdclient(rs.getString(2));
                commande.setDate(rs.getDate(3).toString());
                commande.setReduction(rs.getDouble(4));
                commande.setPrix(rs.getDouble(5));
                commande.setAdresseLivraison(rs.getString(6));
            }

            ps = dbConnection.prepareStatement(selectFilms, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setInt(1, Integer.parseInt(id));
            rs = ps.executeQuery();

            rs.last();
            int rowcount = rs.getRow();
            //System.out.println("number of films " + rowcount);
            rs.beforeFirst();
            commande.setIdFilms(new ArrayList());

            while (rs.next()) {
                List<String> c = commande.getIdFilms();
                c.add("" + rs.getInt(1));
                commande.setIdFilms(c);
            }

            ps = dbConnection.prepareStatement(selectPlats);
            ps.setInt(1, Integer.parseInt(id));
            rs = ps.executeQuery();
            commande.setIdPlats(new ArrayList());

            while (rs.next()) {
                List<String> c = commande.getIdPlats();
                c.add("" + rs.getInt(1) + ":" + rs.getInt(2));
                commande.setIdPlats(c);
            }
        } catch (SQLException | NumberFormatException e) {
            System.err.println("erreur > GestionnaireCommande : constructeur de la classe");
            System.out.println(e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }

            if (dbConnection != null) {
                disconnect();
            }
        }
    }

    /**
     * Enregistre une commande dans la base de données sql
     *
     * @throws SQLException
     */
    public void enregistrerCommandeDB(Boolean utilise_point) throws SQLException, InterruptedException {

        /*System.out.println("id commande: " + commande.getId());
        System.out.println("id client: " + commande.getIdclient());
        System.out.println("date: " + commande.getDate());
        System.out.println("prix: " + commande.getPrix());
        System.out.println("adresse: " + commande.getAdresseLivraison());*/
        int rowAffected = 0;
        PreparedStatement preparedStatement = null;

        String insertCommande = "INSERT INTO commandes "
                + "(id_client, date, reduction, prix, adresse_livraison) VALUES"
                + "(?,?,?,?,?)";

        String insertPlat = "INSERT INTO plats "
                + "(id_commande, id_menu, quantite) VALUES "
                + "(?,?,?)";

        String insertfilm = "INSERT INTO films "
                + "(id_commande, id_film) VALUES "
                + "(?,?)";

        try {

            if (utilise_point == true) {

                /**
                 * Caculcule de réduction 10€ -> 1 points Si points utiliser :
                 * >>>> 10 points -> -1€ >>>> reduction >>>> sous total sans la
                 * réduction >>>> revoyer les poinst
                 */
                GestionnaireClient client = new GestionnaireClient(commande.getIdclient(), null, null);
                Integer points_client = client.getPoint();

                if (points_client >= 10) {
                    Double reduction = (Double) (points_client * 1.0) / 10.0;
                    Double nouveau_points = 0.0;
                    Integer points = 0;

                    if (reduction > commande.getPrix()) {
                        nouveau_points = reduction - commande.getPrix();
                        reduction = (Double) (commande.getPrix() * 1.0) / 10.0;

                        commande.setReduction(reduction);

                        //mise a jour des points clients après utilisation
                        points = nouveau_points.intValue() / 10;
                        client.updatePoint(points);

                        //mise a jour des points clients après commande -> Point : 10€ -> 1 points
                        Double prix_total = commande.getPrix();
                        points = prix_total.intValue() / 10;
                        client.updatePoint(points);
                    } else {
                        commande.setReduction(reduction);

                        //mise a jour des points clients après utilisation -> Point : 0
                        client.updatePoint(0);

                        //mise a jour des points clients après commande -> Point : 10€ -> 1 points
                        Double prix_total = commande.getPrix();
                        points = prix_total.intValue() / 10;
                        client.updatePoint(points);
                    }

                }

                /**
                 * Fin du calcule de réduction
                 */
            }

            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(insertCommande,
                    PreparedStatement.RETURN_GENERATED_KEYS);

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date parsedate = format.parse(commande.getDate());
            java.sql.Date sqlDate = new java.sql.Date(parsedate.getTime());

            preparedStatement.setString(1, commande.getIdclient());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(commande.getDate()));
            preparedStatement.setDouble(3, commande.getReduction());
            preparedStatement.setDouble(4, commande.getPrix());
            preparedStatement.setString(5, commande.getAdresseLivraison());

            rowAffected = preparedStatement.executeUpdate();

            if (rowAffected == 0) {
                throw new SQLException("Creating commande failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    commande.setId("" + generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating commande failed, no ID obtained.");
                }
            }

            if (rowAffected > 0 && commande.getIdPlats().size() > 0 && !commande.getIdPlats().get(0).equals("")) {
                preparedStatement = dbConnection.prepareStatement(insertPlat);

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
                    Set<Entry<Integer, Integer>> setHm = count_id_plats.entrySet();
                    Iterator<Entry<Integer, Integer>> it = setHm.iterator();
                    while (it.hasNext()) {
                        Entry<Integer, Integer> e = it.next();
                        if (Integer.parseInt(commande.getIdPlats().get(i)) == e.getKey()) {
                            count_id_plats.replace(e.getKey(), (e.getValue() + 1));
                        }
                    }
                }

                /**
                 * Inserts values in plats table
                 */
                //System.out.println(">>>>  2 >>>>>>>>>>>" + count_id_plats.toString());
                Set<Entry<Integer, Integer>> setHm = count_id_plats.entrySet();
                Iterator<Entry<Integer, Integer>> it = setHm.iterator();

                Integer commande_id = Integer.parseInt(commande.getId());

                while (it.hasNext()) {
                    Entry<Integer, Integer> e = it.next();
                    preparedStatement.setInt(1, commande_id);
                    preparedStatement.setInt(2, e.getKey());
                    preparedStatement.setInt(3, e.getValue());

                    rowAffected = preparedStatement.executeUpdate();
                }

            } else {
                System.out.println("il n'y a pas de plat à inserer");
            }

            if (commande.getIdFilms().size() > 0 && !commande.getIdFilms().get(0).equals("")) {
                preparedStatement = dbConnection.prepareStatement(insertfilm);

                for (int i = 0; i < commande.getIdFilms().size(); i++) {
                    preparedStatement.setString(1, commande.getId());
                    preparedStatement.setString(2, commande.getIdFilms().get(i));

                    rowAffected = preparedStatement.executeUpdate();
                }
            } else {
                System.out.println("il n'y a pas de film à inserer");
            }

            logs("../exec/commande/./sequenceDiagram_5.sh");
            Thread.sleep(1000);

        } catch (SQLException | ParseException e) {
            System.err.println(" > enregistrerCommandeDB : GestionnaireCOMMANDE");
            System.out.println("Message: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());

        } finally {

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (dbConnection != null) {
                disconnect();
            }
        }

    }

    /**
     * Recupère le maxid de la table FILM et convertir en int pour l'insertion
     * d'une nouvelle ligne Uitile pour les BD qui n'ont pas l'auto incremente
     *
     * @return Le dernier id de la table film
     * @throws SQLException
     */
    public int getMaxIdFilm() throws SQLException {
        String selectSQL_01 = "SELECT MAX(ID)FROM MENU";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL_01);
        ResultSet res = preparedStatement.executeQuery(selectSQL_01);
        return Integer.parseInt(res.getString("MAX(ID)"));
    }

    /**
     * Recupère le maxid de la table MENU et convertir en int pour l'insertion
     * d'une nouvelle ligne Uitile pour les BD qui n'ont pas l'auto incremente
     *
     * @return Le dernier id de la table film
     * @throws SQLException
     */
    public int getMaxIdMenu() throws SQLException {
        String selectSQL_00 = "SELECT MAX(ID)FROM FILM";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL_00);
        ResultSet rs = preparedStatement.executeQuery(selectSQL_00);
        return Integer.parseInt(rs.getString("MAX(ID)"));
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    /**
     * Doit aller chercher la commande correspondante à l'id donné en paramètre
     * et la retourner
     *
     * @param id
     * @return
     */
    public Commande getCommande(String id) {
        Commande c = new Commande();
        List<String> films = new ArrayList<>();
        List<String> plats = new ArrayList<>();

        final String roquete = "select id, id_client, `date`, reduction, prix, adresse_livraison from commandes where id = ?";
        final String roqueteFilms = "select id_film from films where id_commande = ?";
        final String roquetePlats = "select id_menu, quantite from plats where id_commande = ?";

        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(roquete, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, id);

            // execute select SQL stetement
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                c = new Commande();
                c.setId(rs.getString(1));
                c.setIdclient(rs.getString(2));
                c.setDate(rs.getString(3));
                c.setReduction(rs.getDouble(4));
                c.setPrix(rs.getDouble(5));
                c.setAdresseLivraison(rs.getString(6));
            }

            preparedStatement = dbConnection.prepareStatement(roqueteFilms, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                films.add(rs.getString(1));
            }
            c.setIdFilms(films);

            preparedStatement = dbConnection.prepareStatement(roquetePlats, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                plats.add("" + rs.getString(1) + ":" + rs.getString(2));
            }
            c.setIdPlats(plats);

        } catch (SQLException e) {
            System.out.println("erreur > getCommande: GESTIONNAIRECOMMANDE");
            System.out.println("Message: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
        } finally {
            if (preparedStatement != null) {
                preparedStatement = null;
                rs = null;
            }

            if (dbConnection != null) {
                disconnect();
            }
        }

        return c;
    }

    /**
     * Doit aller chercher la liste des id des plats les plus consommer avec un
     * film donnée pour assurer la suggestion dans la partie client
     *
     * @param idFilm
     * @return
     */
    public List<String> getListPlatForFilm(String idFilm) {

        List<String> plats = new ArrayList<>();
        //c'est une requette de test seulement qui donne le méme resultat que la future requete 
        /*final String roquetePlats = "SELECT id_film "
                + "from films "
                + "join commandes on (commandes.id = films.id_commande) "
                + "join plats on (plats.id_commande = films.id) "
                + "where id_menu = ? "
                + "GROUP BY id_film "
                + "having count(*) >= 2"
                + "LIMIT 10";*/

        final String roquetePlats = "CALL suggestion_plats(" + idFilm + ")";

        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            logs("../exec/extras/./sequenceDiagram_5.sh");
            Thread.sleep(1000);

            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(roquetePlats, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //preparedStatement.setInt(1, Integer.parseInt(idFilm));

            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                //System.out.println(rs.getString(1));
                plats.add(rs.getString(1));
            }

        } catch (SQLException e) {
            System.out.println("erreur > getListPlatForFilm: GESTIONNAIRECOMMANDE");
            System.out.println("Message: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
        } catch (InterruptedException ex) {
            Logger.getLogger(GestionnaireCommande.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement = null;
                rs = null;
            }

            if (dbConnection != null) {
                disconnect();
            }
        }

        return plats;
    }

    public List<String> getListFilmsForPlat(String idPlat) {

        List<String> films = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        //c'est une requette de test seulement qui donne le méme resultat que la future requete 
        /*final String roquetePlats = "SELECT id_film "
                + "from films "
                + "join commandes on (commandes.id = films.id_commande) "
                + "join plats on (plats.id_commande = commandes.id) "
                + "where id_menu = ? "
                + "GROUP BY id_film "
                + "having count(*) >= 2 "
                + "LIMIT 10;";*/

        final String roquetePlats = "CALL suggestion_films(" + idPlat + ")";

        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            logs("../exec/extras/./sequenceDiagram_2.sh");
            Thread.sleep(1000);

            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(roquetePlats, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //preparedStatement.setString(1, idPlat);

            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                films.add(rs.getString(1));
            }

        } catch (SQLException e) {
            System.out.println("erreur > getListFilmsForPlat: GESTIONNAIRECOMMANDE");
            System.out.println("Message: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
        } catch (InterruptedException ex) {
            Logger.getLogger(GestionnaireCommande.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement = null;
                rs = null;
            }

            if (dbConnection != null) {
                disconnect();
            }
        }

//        for (int i=0;i<films.size()-1;i++){
//            String res = "{";
//            res += "\"id\": \""         + films.get(i) + "\", ";
//            res += "}";
//            result.append(res);
//        }
        return films;
    }

    /**
     * Listes des plats les plus vendus classement par ordre croissant de ventes
     *
     * @return
     */
    public List<Integer[]> getListBestSellerPlat() {

        List<Integer[]> plats = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        //c'est une requette de test seulement qui donne le méme resultat que la future requete 
        final String roquetePlats = "SELECT id_menu, SUM(quantite) as nbr_quantite "
                + "FROM plats "
                + "GROUP BY id_menu, quantite "
                + "ORDER BY nbr_quantite DESC";
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            logs("../exec/extras/./sequenceDiagram_8.sh");
            Thread.sleep(1000);

            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(roquetePlats, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                //System.out.println(rs.getString(1) + ": " + rs.getString(2));
                Integer[] value = new Integer[2];
                value[0] = rs.getInt(1);
                value[1] = rs.getInt(2);
                plats.add(value);
            }

        } catch (SQLException e) {
            System.out.println("erreur > getListBestSellerPlat: GESTIONNAIRECOMMANDE");
            System.out.println("Message: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
        } catch (InterruptedException ex) {
            Logger.getLogger(GestionnaireCommande.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement = null;
                rs = null;
            }

            if (dbConnection != null) {
                disconnect();
            }
        }

        return plats;
    }

    @Override
    public void logs(String request) {
        String s;
        Process p;
        try {
            p = Runtime.getRuntime().exec(request);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            while ((s = br.readLine()) != null) {
                System.out.println(s);
            }
            p.waitFor();

            p.destroy();
        } catch (Exception e) {
            System.out.println("Erreur, commande exec");
        }
    }

}
