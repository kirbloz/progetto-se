package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.controller.AccessoController;
import it.unibs.projectIngesoft.controller.ConfiguratoreController;
import it.unibs.projectIngesoft.controller.FruitoreController;
import it.unibs.projectIngesoft.mappers.*;
import it.unibs.projectIngesoft.model.*;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.utente.Utente;
import it.unibs.projectIngesoft.view.ConfiguratoreView;
import it.unibs.projectIngesoft.view.FruitoreView;
import it.unibs.projectIngesoft.view.UtenteViewableTerminal;

public class ErmesController {

    private static final String FATTORI_DI_CONVERSIONE_JSON_FILEPATH = "fattoriTest.json";
    private static final String UTENTI_JSON_FILEPATH = "usersTest.json";
    private static final String UTENTI_DEF_CREDS_JSON_FILEPATH = "defaultCredentials.json";
    private static final String CATEGORIE_JSON_FILEPATH = "categorieTest.json";
    private static final String COMPRENSORI_GEOGRAFICI_JSON_FILEPATH = "comprensoriGeograficiTest.json";
    private static final String PROPOSTE_JSON_FILEPATH = "proposteTest.json";


    //Utente utenteAttivo;
    UtentiModel modelUtenti;

    public ErmesController() {
        UtentiMapper utentiMapper = new UtentiMapper(UTENTI_JSON_FILEPATH,
                UTENTI_DEF_CREDS_JSON_FILEPATH,
                new SerializerJSON<>(),
                new SerializerJSON<>());
        modelUtenti = new UtentiModel(utentiMapper);

    }

    public void mainLoop() {
        ComprensorioGeograficoModel compGeoModel = new ComprensorioGeograficoModel(
                new CompGeoMapper(COMPRENSORI_GEOGRAFICI_JSON_FILEPATH,
                        new SerializerJSON<>()));

        AccessoController controllerAccesso = new AccessoController(modelUtenti, compGeoModel);
        Utente utenteAttivo = controllerAccesso.run();

        CategorieModel categorieModel = new CategorieModel(
                new CategorieMapper(CATEGORIE_JSON_FILEPATH,
                        new SerializerJSON<>()));

        FattoriModel fattoriModel = new FattoriModel(
                new FattoriMapper(FATTORI_DI_CONVERSIONE_JSON_FILEPATH,
                        new SerializerJSON<>()));

        ProposteModel proposteModel = new ProposteModel(utenteAttivo,
                new ProposteMapper(PROPOSTE_JSON_FILEPATH,
                        new SerializerJSON<>()));


        if (utenteAttivo instanceof Configuratore) {
            ConfiguratoreController controller = new ConfiguratoreController(
                    new ConfiguratoreView(),
                    categorieModel,
                    fattoriModel,
                    proposteModel,
                    compGeoModel,
                    modelUtenti,
                    (Configuratore) utenteAttivo
            );
            controller.run();

        }else{
            FruitoreController controller = new FruitoreController(
                    new FruitoreView(),
                    categorieModel,
                    fattoriModel,
                    proposteModel,
                    compGeoModel,
                    modelUtenti,
                    (Fruitore) utenteAttivo
            );
            controller.run();
        }


    }

}
