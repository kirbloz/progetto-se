package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.model.*;
import it.unibs.projectIngesoft.view.AccessoView;
import it.unibs.projectIngesoft.view.FruitoreView;

public class FruitoreController {

    private FruitoreView view;

    private CategorieModel categorieModel;
    private FattoriModel fattoriModel;
    private ProposteModel proposteModel;
    private ComprensorioGeograficoModel compGeoModel;
    private UtentiModel utentiModel;

    public FruitoreController(FruitoreView view,
                              CategorieModel categorieModel,
                              FattoriModel fattoriModel,
                              ProposteModel proposteModel,
                              ComprensorioGeograficoModel compGeoModel,
                              UtentiModel utentiModel) {
        this.view = view;
        this.categorieModel = categorieModel;
        this.fattoriModel = fattoriModel;
        this.proposteModel = proposteModel;
        this.compGeoModel = compGeoModel;
        this.utentiModel = utentiModel;
    }


    public void run(){
        int scelta = 0;

        do {
            scelta = view.visualizzaMenuPrincipale();

            switch (scelta) {
                case 0 -> view.uscitaMenu("programma");//System.out.println(AccessoView.MSG_PROGRAM_EXIT);
                //case 1 -> userHandler.cambioCredenziali(utenteAttivo);
                //case 2 -> loopProposte(menuProposte, utenteAttivo);
                //case 3 -> loopCategorie(menuCategorie, isConfiguratore);
                default -> {
                } // già gestito dalla classe Menu
            }

        } while (scelta != 0);

    }
}
