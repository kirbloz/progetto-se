package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.gestori.*;
import it.unibs.projectIngesoft.libraries.Menu;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;

public class Main {

    protected static final String FATTORI_DI_CONVERSIONE_XML_FILEPATH = "fattori.xml";
    protected static final String UTENTI_XML_FILEPATH = "users.xml";
    protected static final String UTENTI_DEF_CREDS_XML_FILEPATH = "defaultCredentials.xml";
    public static final String CATEGORIE_XML_FILEPATH = "categorie.xml";
    public static final String COMPRENSORI_GEOGRAFICI_XML_FILEPATH = "comprensoriGeografici.xml";
    private static final String PROPOSTE_XML_FILEPATH = "proposte.xml";

    public static final String MSG_PROGRAM_EXIT = "> ARRIVEDERCI <";

    protected static final String TITLE_STARTING_MENU = "BENVENUTO";
    protected static final String[] vociMenuIniziale = new String[]{
            "Login",
            "Registrazione"
    };

    protected static final String TITLE_MAIN_MENU = "MENU' PRINCIPALE - SCAMBIO ORE";
    protected static final String[] vociMainConfiguratore = new String[]{
            "Cambia Credenziali",
            "Menu Comprensorio ",
            "Menu Categorie",
            "Menu Fattori"
    };
    protected static final String[] vociMainFruitore = new String[]{
            "Cambia Credenziali",
            "Effettua proposta di scambio",
            "Menu Categorie",
    };

    protected static final String TITLE_MENU_COMPRENSORIO = "MENU' COMPRENSORI GEOGRAFICI";
    protected static final String[] vociComprensorioGeografico = new String[]{
            "Aggiungi Comprensorio Geografico",
            "Visualizza Comprensorio Geografico"
    };

    protected static final String TITLE_MENU_CATEGORIE = "MENU' CATEGORIE";
    protected static final String[] vociCategorieConfiguratore = new String[]{
            "Aggiungi Gerarchia",
            "Visualizza Gerarchie"
    };

    protected static final String[] vociCategorieFruitore = new String[]{
            "Esplora Gerarchie"
    };

    protected static final String TITLE_MENU_FATTORI = "MENU' FATTORI";
    protected static final String[] vociFattori = new String[]{
            "Visualizza Fattori di Conversione"
    };


    public static void main(String[] args) {

        Menu menuIniziale = new Menu(TITLE_STARTING_MENU, vociMenuIniziale);

        GestoreUtenti userHandler = new GestoreUtenti(UTENTI_XML_FILEPATH, UTENTI_DEF_CREDS_XML_FILEPATH);
        GestoreComprensorioGeografico comprensorioHandler = new GestoreComprensorioGeografico(COMPRENSORI_GEOGRAFICI_XML_FILEPATH);
        GestoreProposte proposeHandler = new GestoreProposte(PROPOSTE_XML_FILEPATH, FATTORI_DI_CONVERSIONE_XML_FILEPATH);

        Utente utenteAttivo = loopMenuIniziale(menuIniziale, userHandler, comprensorioHandler.listaNomiComprensoriGeografici());
        if (utenteAttivo != null) {
            boolean isConfiguratore = utenteAttivo instanceof Configuratore;
            // menu con diverse voci tra fruitore e configuratore
            Menu menu = new Menu(TITLE_MAIN_MENU, isConfiguratore ? vociMainConfiguratore : vociMainFruitore);
            Menu menuCategorie = new Menu(TITLE_MENU_CATEGORIE, isConfiguratore ? vociCategorieConfiguratore : vociCategorieFruitore);
            // menu per soli configuratori
            Menu menuFattori = isConfiguratore ?
                    new Menu(TITLE_MENU_FATTORI, vociFattori) : null;
            Menu menuComprensoriGeografici = isConfiguratore ?
                    new Menu(TITLE_MENU_COMPRENSORIO, vociComprensorioGeografico) : null;

            loopMain(menu, menuCategorie, menuFattori, menuComprensoriGeografici, userHandler, proposeHandler, utenteAttivo);
        } else {
            System.out.println(MSG_PROGRAM_EXIT);
        }
    }

    /**
     * Metodo che gestisce il menu principale
     *
     * @param menu,          menu principale
     * @param menuCategorie, sotto-menu per le categorie
     * @param menuFattori,   sotto-menu per i fattori
     */
    private static void loopMain(Menu menu, Menu menuCategorie, Menu menuFattori, Menu menuComprensoriGeografici, GestoreUtenti userHandler, GestoreProposte proposeHandler, Utente utenteAttivo) {
        int scelta;
        boolean isConfiguratore = utenteAttivo instanceof Configuratore;

        if (isConfiguratore)
            do {
                scelta = menu.scegli();

                switch (scelta) {
                    case 0 -> System.out.println(MSG_PROGRAM_EXIT);
                    case 1 -> userHandler.cambioCredenziali(utenteAttivo); // cambio credenziali
                    case 2 -> loopComprensoriGeografici(menuComprensoriGeografici); //menu comprensorio
                    case 3 -> loopCategorie(menuCategorie, isConfiguratore); //menu categorie
                    case 4 -> loopFattori(menuFattori); //menu fattori
                    default -> {
                    } // già gestito dalla classe Menu
                }

            } while (scelta != 0);
        else
            do {
                scelta = menu.scegli();

                switch (scelta) {
                    case 0 -> System.out.println(MSG_PROGRAM_EXIT);
                    case 1 -> userHandler.cambioCredenziali(utenteAttivo);
                    case 2 -> proposeHandler.effettuaProposta();
                    case 3 -> loopCategorie(menuCategorie, isConfiguratore);
                    default -> {
                    } // già gestito dalla classe Menu
                }

            } while (scelta != 0);
    }

    private static Utente loopMenuIniziale(Menu menu, GestoreUtenti userHandler, ArrayList<String> listaNomiComprensorio) {
        int scelta;
        Utente utenteLoggato;
        scelta = menu.scegli();
        switch (scelta) {
            case 1 -> utenteLoggato = userHandler.login();
            case 2 -> utenteLoggato = userHandler.register(listaNomiComprensorio);
            default -> utenteLoggato = null;
        }
        return utenteLoggato;
    }


    /**
     * Gestisce il menu per i comprensori geografici.
     *
     * @param menuComprensoriGeografici, menu gestito
     */
    private static void loopComprensoriGeografici(Menu menuComprensoriGeografici) {
        int scelta;
        GestoreComprensorioGeografico gestoreComprensori = new GestoreComprensorioGeografico(COMPRENSORI_GEOGRAFICI_XML_FILEPATH);
        do {
            scelta = menuComprensoriGeografici.scegli();
            gestoreComprensori.entryPoint(scelta);
        } while (scelta != 0);
    }


    /**
     * Gestisce il menu per le categorie.
     *
     * @param menuCategorie, menu gestito
     */
    private static void loopCategorie(Menu menuCategorie, boolean isConfiguratore) {
        int scelta;
        GestoreCategorie gestoreCategorieCat = new GestoreCategorie(CATEGORIE_XML_FILEPATH, FATTORI_DI_CONVERSIONE_XML_FILEPATH);
        do {
            scelta = menuCategorie.scegli();
            gestoreCategorieCat.entryPoint(scelta, isConfiguratore);
        } while (scelta != 0);
    }

    /**
     * Gestisce il menu per i fattori di conversione.
     *
     * @param menuFattori, menu gestito
     */
    private static void loopFattori(Menu menuFattori) {
        int scelta;
        GestoreFattori gestoreFattori = new GestoreFattori(FATTORI_DI_CONVERSIONE_XML_FILEPATH);
        do {
            scelta = menuFattori.scegli();
            gestoreFattori.entryPoint(scelta);
        } while (scelta != 0);

    }
}
