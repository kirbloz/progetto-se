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

    Utente utenteAttivo;
    UtentiModel modelUtenti;

    public ErmesController() {
        UtentiMapper utentiMapper = new UtentiMapper("users.json",
                "defaultCredentials.json",
                new SerializerJSON<>(),
                new SerializerJSON<>());
        modelUtenti = new UtentiModel(utentiMapper);

    }

    public void mainLoop() {
        UtenteViewableTerminal view;

        ComprensorioGeograficoModel compGeoModel = new ComprensorioGeograficoModel(
                new CompGeoMapper("comprensoriGeograficiTest.json",
                        new SerializerJSON<>()));

        AccessoController controllerAccesso = new AccessoController(modelUtenti, compGeoModel);
        Utente utenteAttivo = controllerAccesso.run();

        CategorieModel categorieModel = new CategorieModel(
                new CategorieMapper("categorieTest.json",
                        new SerializerJSON<>()));

        FattoriModel fattoriModel = new FattoriModel(
                new FattoriMapper("fattoriTest.json",
                        new SerializerJSON<>()));

        ProposteModel proposteModel = new ProposteModel(utenteAttivo,
                new ProposteMapper("proposteTest.json",
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



        //controllerAccesso.events.subscribe("utenteOttenuto", this);
        //viewAccesso.menuIniziale();

            /*this.utenteAttivo = utentiController.effettuaAccesso();

            if(this.utenteAttivo instanceof Configuratore){
                view = new ConfiguratoreView();
            }else{
                view = new FruitoreView();
            }*/


        //}while(true);
        /*boolean exit = false;
        while (!exit) {
            view.stampaMenu();
            int choice = view.getUserSelection();
            switch (choice) {
                case 1:
                    //fai qualcosa
                    break;
                case 2:
                    //fai altro
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    //view.printMessage("Opzione non valida. Riprova.");
            }
        }*/

    }

}
