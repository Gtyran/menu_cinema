package l3m.Gestionnaires;

import bindingClasse.Client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import l3m.Able.SQLAble;

public class GestionnaireClient extends SQLAble {

    private Client client;

    public GestionnaireClient(String id, String nom, String prenom) {
        this.client = new Client();
        client.setId(id);
        client.setNom(nom);
        client.setPrenom(prenom);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getNom() {
        return client.getNom();
    }

    public String getPrenom() {
        return client.getPrenom();
    }

    public String getPhoto() {
        return client.getPhoto();
    }

    public String getAdresse() {
        return client.getAdresse();
    }

    public String getEmail() {
        return client.getEmail();
    }

    public String getTel() {
        return client.getTel();
    }

    public int getPoints() {
        return client.getPoints();
    }

    public void editEmail(String email) {
        if (email != null) {
            client.setEmail(email);
        } else {
            System.err.println("Le paramètre de la méthode editEmail est de valeur NULL");
        }
    }

    public void editAdresse(String adresse) {
        if (adresse != null) {
            client.setAdresse(adresse);
        } else {
            System.err.println("Le paramètre de la méthode editAdresse est de valeur NULL");
        }
    }

    public void editTel(String tel) {
        if (tel != null && tel.length() != 10) {
            client.setTel(tel);
        } else {
            System.err.println("Le paramètre de la méthode editTel est de valeur NULL ou la taille n'est pas égale à 10");
        }
    }

    public void editPhoto(String photo) {
        if (photo != null) {
            client.setPhoto(photo);
        } else {
            System.err.println("Le paramètre de la méthode editPhoto est de valeur NULL");
        }
    }

    public boolean enregistrerClientDB() throws SQLException {
        boolean reponse = false;
        int rowAffected = 0;

        String insertTableSQL = "INSERT INTO clients "
                + "(ID, NOM, PRENOM, PHOTO, EMAIL, TEL, ADRESSE, POINTS) VALUES "
                + "(?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = null;
        
        logs("../exec/client/./sequenceDiagram_2.sh");
        
        if (!existsClientDB()) {
            logs("../exec/client/./sequenceDiagram_3.sh");
            try {
                connectToDataBase();
                preparedStatement = dbConnection.prepareStatement(insertTableSQL,
                        PreparedStatement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, client.getId());
                preparedStatement.setString(2, client.getNom());
                preparedStatement.setString(3, client.getPrenom());
                preparedStatement.setString(4, client.getPhoto());
                preparedStatement.setString(5, client.getEmail());
                preparedStatement.setString(6, client.getTel());
                preparedStatement.setString(7, client.getAdresse());
                preparedStatement.setDouble(8, client.getPoints());

                rowAffected = preparedStatement.executeUpdate();

                if (rowAffected > 0) {
                    reponse = true;
                    logs("../exec/client/./sequenceDiagram_4.sh");
                }

            } catch (SQLException e) {
                System.err.println(" > enregistrerClientDB : CLIENTS");
                System.out.println(e.getMessage());

            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

                if (dbConnection != null) {
                    disconnect();
                }
                
                logs("../exec/client/./sequenceDiagram_5.sh");
            }
        } else {
            // si le client existe on le select dans l'instance client
            reponse = true;
            logs("../exec/client/./sequenceDiagram_5.sh");
        }

        return reponse;
    }

    protected boolean existsClientDB() throws SQLException {
        boolean existe = false;
        PreparedStatement preparedStatement = null;
        String selectTableSQL = "SELECT * from clients WHERE ID = ?";
        ResultSet rs = null;

        try {
            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(selectTableSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, client.getId());

            // execute select SQL stetement
            rs = preparedStatement.executeQuery();
            //get response count
            rs.last();
            if (rs.getRow() > 0) {
                existe = true;
            }
            //replacer le curseur au debut du resultSet
            rs.beforeFirst();

            while (rs.next()) {
                String id = rs.getString("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String photo = rs.getString("photo");
                String email = rs.getString("email");
                String tel = rs.getString("tel");
                String adresse = rs.getString("adresse");
                int points = rs.getInt("points");

                client.setId(id);
                client.setNom(nom);
                client.setPrenom(prenom);
                client.setPhoto(photo);
                client.setEmail(email);
                client.setTel(tel);
                client.setAdresse(adresse);
                client.setPoints(points);
            }

        } catch (SQLException e) {
            System.err.println(" > existsClientDB : CLIENTS");
            System.out.println(e.getMessage());

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
                rs = null;
            }

            if (dbConnection != null) {
                disconnect();
            }
        }

        return existe;
    }

    public void editClientDB() throws SQLException, InterruptedException {
        PreparedStatement preparedStatement = null;
        String updateTableSQL = "update clients set nom = ?, prenom = ?, tel = ?, adresse = ?, email = ?, photo = ? "
                + " where id = ?";
        
        try {
            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(updateTableSQL);
            preparedStatement.setString(1, client.getNom());
            preparedStatement.setString(2, client.getPrenom());
            preparedStatement.setString(3, client.getTel());
            preparedStatement.setString(4, client.getAdresse());
            preparedStatement.setString(5, client.getEmail());
            preparedStatement.setString(6, client.getPhoto());
            preparedStatement.setString(7, client.getId());

            // execute select SQL stetement
            preparedStatement.executeUpdate();
            
            logs("../exec/client/./sequenceDiagram_7.sh");
            Thread.sleep(1000);

        } catch (SQLException e) {
            System.err.println(" > editClientDB : CLIENTS");
            System.out.println(e.getMessage());

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (dbConnection != null) {
                disconnect();
            }
        }

    }

    public String getClientIdDB(String nom, String prenom) throws SQLException {
        String reponse = null;
        PreparedStatement preparedStatement = null;
        String selectTableSQL = "SELECT ID from CLIENTS WHERE NOM = ? AND PRENOM = ?";
        ResultSet rs = null;

        try {
            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(selectTableSQL);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);

            // execute select SQL stetement
            rs = preparedStatement.executeQuery();
            reponse = rs.getString("ID");

        } catch (SQLException e) {
            System.err.println(" > getClientIdDB : CLIENTS");
            System.out.println(e.getMessage());

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
                rs = null;
            }

            if (dbConnection != null) {
                disconnect();
            }
        }
        return reponse;
    }

    public boolean deleteClientDB(String id) throws SQLException {
        boolean reponse = true;
        PreparedStatement preparedStatement = null;
        String deleteSQL = "DELETE CLIENT WHERE ID = ?";
        ResultSet rs = null;

        try {
            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, id);

            // execute select SQL stetement
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(" > deleteClietDB : CLIENTS");
            System.out.println(e.getMessage());
            reponse = false;

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (dbConnection != null) {
                disconnect();
            }
        }
        return reponse;

    }

    public List<String> getListeCommande() throws SQLException {
        List<String> commandes = new ArrayList<>();
        String query = "select * from commandes where id_client = ?";
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, client.getId());

            // execute select SQL stetement
            rs = preparedStatement.executeQuery();
            //get response count
            rs.last();
            //System.out.println("Nombre de lignes de commande " + rs.getRow());
            //replacer le curseur au debut du resultSet
            rs.beforeFirst();
            while (rs.next()) {
                String id = "" + rs.getInt("id");
                commandes.add(id);
            }
        } catch (SQLException e) {
            System.out.println("erreur > getListeCommande: GESTIONNAIRECLIENT");
            System.out.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
                rs = null;
            }

            if (dbConnection != null) {
                disconnect();
            }
        }
        return commandes;
    }

    public Client getClient() {
        return client;
    }
    
    public void updatePoint(int value) throws SQLException {
        PreparedStatement preparedStatement = null;
        String updateTableSQL = "update clients set points = ? "
                + " where id = ?";
        
        try {
            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(updateTableSQL);
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, client.getId());

            // execute select SQL stetement
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println(" > editClientDB : CLIENTS");
            System.out.println(e.getMessage());

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (dbConnection != null) {
                disconnect();
            }
        }

    }
    
    public Integer getPoint() throws SQLException {
        Integer reponse = 0;
        
        PreparedStatement preparedStatement = null;
        String selectTableSQL = "SELECT * from clients WHERE ID = ?";
        ResultSet rs = null;

        try {
            connectToDataBase();
            preparedStatement = dbConnection.prepareStatement(selectTableSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, client.getId());

            // execute select SQL stetement
            rs = preparedStatement.executeQuery();

            //replacer le curseur au debut du resultSet
            rs.beforeFirst();

            while (rs.next()) {
                reponse = rs.getInt("points");
            }

        } catch (SQLException e) {
            System.err.println(" > existsClientDB : CLIENTS");
            System.out.println(e.getMessage());

        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
                rs = null;
            }

            if (dbConnection != null) {
                disconnect();
            }
        }

        
        return reponse;
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
