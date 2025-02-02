package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Menu;

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



    public int visualizzaMenuPrincipale() {
        Menu menu = new Menu(TITLE_MAIN_MENU,  vociMainFruitore);
        return menu.scegli();
    }

    public int visualizzaMenuCategorie() {
        //todo da implementare
        return 0;
    }

    public int visualizzaMenuProposte() {
        //todo da implementare
        return 0;
    }


    public void uscitaMenu(String menu) {

    }


    public String getUserInput(String prompt) {
        return InputDatiTerminale.leggiStringaNonVuota(prompt);
    }

    public String richiestaUsername() {
        //duplicato in configuratore view
        //valutare se spostare in una classe utilities
        return "";
    }

    public String richiestaPassword() {
        //duplicato in configuratore view
        //valutare se spostare in una classe utilities
        return "";
    }

    ///////////////////////// PROPOSTE //////////////////////////

    public void visualizzaProposte(){
        //todo da implementare
    }

}
