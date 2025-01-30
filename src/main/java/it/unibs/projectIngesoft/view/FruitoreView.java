package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.libraries.InputDatiTerminale;

public class FruitoreView implements UtenteViewableTerminal {

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


    // CATEGORIE

    public static final String HEADER_ESPLORA_GERARCHIE = "\n>> ESPLORA GERARCHIE <<\n";
    public static final String HEADER_ESPLORAZIONE_LIVELLO = "\n>> LIVELLO CORRENTE [ %s ] <<\n";

    public static final String TITLE_SUBMENU_ESPLORA_GERARCHIA = "ESPLORA GERARCHIA";
    public static final String[] VOCI_SUBMENU_ESPLORA_GERARCHIA = new String[]{
            "Esplora un nuovo livello",
            "Torna indietro di un livello"
    };

    public static final String WARNING_NO_RAMI_DA_ESPLORARE = ">> (!!) Non ci sono nuovi rami da esplorare";


    @Override
    public int visualizzaMenuCategorie() {
        return 0;
    }

    @Override
    public void uscitaMenu(String menu) {

    }

    @Override
    public void stampaMenu() {
        System.out.println("TO IMPLEMENT");
    }

    @Override
    public int getUserSelection() {
        return InputDatiTerminale.leggiInteroConMinimo(">> Selezione (>0): ", 0);
    }

    @Override
    public String getUserInput(String prompt) {
        return "";
    }

    @Override
    public String getUserInput() {
        return InputDatiTerminale.leggiStringaNonVuota(">> Input stringa: ");
    }
}
