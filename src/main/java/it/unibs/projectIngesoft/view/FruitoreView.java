package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.libraries.InputDatiTerminale;

public class FruitoreView implements UtenteViewableTerminal{

    public static final String TITLE_MAIN_MENU = "MENU' PRINCIPALE - SCAMBIO ORE";
    public static final String[] vociMainFruitore = new String[]{
            "Cambia Credenziali",
            //"Effettua proposta di scambio",
            "Menu Proposte",
            "Menu Categorie",
    };

    public static final String TITLE_MENU_CATEGORIE = "MENU' CATEGORIE";
    public static final String[] vociCategorieFruitore = new String[]{
            "Esplora Gerarchie"
    };

    public static final String TITLE_MENU_PROPOSTE = "MENU' PROPOSTE";
    public static final String[] vociProposteFruitore = new String[]{
            "Visualizza proposte inviate",
            "Effettua proposta di scambio",
            "Modifica stato proposta"
    };


    @Override
    public void stampaMenu() {
        System.out.println("TO IMPLEMENT");
    }

    @Override
    public int getUserSelection() {
        return InputDatiTerminale.leggiInteroConMinimo(">> Selezione (>0): ", 0);
    }

    @Override
    public String getUserInput() {
        return InputDatiTerminale.leggiStringaNonVuota(">> Input stringa: ");
    }
}
