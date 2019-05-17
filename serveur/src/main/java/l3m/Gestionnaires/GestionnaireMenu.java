/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l3m.Gestionnaires;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import bindingClasse.Ingredient;
import bindingClasse.Plats;
import bindingClasse.Plat;
import bindingClasse.Plat;
import bindingClasse.Plats;
import bindingClasse.TypeDePlat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import l3m.Able.XMLAble;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Node;

/**
 *
 * @author Soraya Housbane
 */
public class GestionnaireMenu extends XMLAble {

    private Plats menu;

    public GestionnaireMenu() {
        this.menu = null;

    }

    public GestionnaireMenu(List<String> idPlats) {
        Plats pl = getCarteDB();
        List<Plat> plMenu = new ArrayList<>();
        menu = new Plats();

        for (String id : idPlats) {
            for (Plat p : pl.getPlats()) {
                if (id.equals(p.getId())) {
                    plMenu.add(p);
                }
            }
        }
        menu.setPlats(plMenu);
    }

    public Plats getMenu() {
        return menu;
    }

    public Plats getCarteDB() {
        Plats plats = new Plats();
        List<Plat> listPlats = new ArrayList<Plat>();

        String XML_request = "//plat";
        String cartes = null;

        if (!request(XML_request).isEmpty()) {
            cartes = request(XML_request);

            try {
                JSONParser jsonParser = new JSONParser();

                //les plats
                JSONObject jsonObject = (JSONObject) jsonParser.parse(cartes);

                JSONArray jsonArray = (JSONArray) jsonObject.get("plats");

                Iterator platIterator = jsonArray.iterator();

                while (platIterator.hasNext()) {
                    JSONObject OnePlat = (JSONObject) platIterator.next();

                    Plat plat = new Plat();
                    
                    String IdPlat = (String) OnePlat.get("id");
                    plat.setId(IdPlat);

                    String nom = (String) OnePlat.get("nom");
                    plat.setNom(nom);

                    String typePlat = (String) OnePlat.get("type");
                    plat.setType(TypeDePlat.fromValue(typePlat));
                    
                    double prixPlat = Double.parseDouble((String) OnePlat.get("prix"));
                    plat.setPrix(prixPlat);
                    
                    
                    JSONArray JsonIngredients = (JSONArray) OnePlat.get("ingredients");
                    Iterator ingredientIterator = JsonIngredients.iterator();
                    List<Ingredient> ingredients = new ArrayList<Ingredient>();
                    while (ingredientIterator.hasNext()) {
                        ingredients.add(Ingredient.fromValue((String) ingredientIterator.next()));
                    }
                    plat.setIngredients(ingredients);
                    
                    listPlats.add(plat);
                    
                }
                
                plats.setPlats(listPlats);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return plats;
        } else {
            plats = null;
            return plats;
        }

    }

    public void ajouterAuMenu(String id) {
        Plat p = null;
        p.setId(id);

        //this.menu=(Plats) id;
        menu.getPlats().add(p);
    }

    public void enleverDuMenu(String id) {
        Plat p = null;
        p.setId(id);
        menu.getPlats().remove(p);

    }

    /**
     * Recherche de l'existance d'un plat par son id A partir d'une requête
     * XPath sur le fichier plats.xml
     *
     * @param id
     * @return
     */
    protected boolean existPlatDB(String id) {
        String XML_request = "/plats/plat/id[text() =\"" + id + "\"]/text()";

        if (!request(XML_request).isEmpty()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void connectToDataBase() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
