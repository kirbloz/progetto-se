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


    public static void main(String[] args) {

        // start -> caricare controller utenti -> controller utenti login + view utenti login

        // controller utenti crea gestore utenti
        // controller utenti chiama view utenti per login
            // si attende input utente

        // loop menu -> richiama controller menu
            // menuController.start();

        // stop
            // menuController.stop(); che stampa ARRIVEDERCI




        Menu menuIniziale = new Menu(MenuView.TITLE_STARTING_MENU, MenuView.vociMenuIniziale);

        UtentiModel userHandler = new UtentiModel(UTENTI_XML_FILEPATH, UTENTI_DEF_CREDS_XML_FILEPATH);
        ComprensorioGeograficoModel comprensorioHandler = new ComprensorioGeograficoModel(COMPRENSORI_GEOGRAFICI_XML_FILEPATH);
        Utente utenteAttivo = loopMenuIniziale(menuIniziale, userHandler, comprensorioHandler.listaNomiComprensoriGeografici());

        if (utenteAttivo != null) {
            boolean isConfiguratore = utenteAttivo instanceof Configuratore;
            // menu con diverse voci tra fruitore e configuratore
            Menu menu = new Menu(MenuView.TITLE_MAIN_MENU, isConfiguratore ? MenuView.vociMainConfiguratore : MenuView.vociMainFruitore);
            Menu menuCategorie = new Menu(MenuView.TITLE_MENU_CATEGORIE, isConfiguratore ? MenuView.vociCategorieConfiguratore : MenuView.vociCategorieFruitore);
            Menu menuProposte = new Menu(MenuView.TITLE_MENU_PROPOSTE, isConfiguratore ? MenuView.vociProposteConfiguratore : MenuView.vociProposteFruitore);
            // menu per soli configuratori
            Menu menuFattori = isConfiguratore ?
                    new Menu(MenuView.TITLE_MENU_FATTORI, MenuView.vociFattori) : null;
            Menu menuComprensoriGeografici = isConfiguratore ?
                    new Menu(MenuView.TITLE_MENU_COMPRENSORIO, MenuView.vociComprensorioGeografico) : null;
            // menu principale
            loopMain(menu, menuCategorie, menuFattori, menuComprensoriGeografici, menuProposte, userHandler, utenteAttivo);
        } else {
            System.out.println(MenuView.MSG_PROGRAM_EXIT);
        }
    }

    /**
     * Metodo che gestisce il menu principale
     *
     * @param menu,          menu principale
     * @param menuCategorie, sotto-menu per le categorie
     * @param menuFattori,   sotto-menu per i fattori
     */
    private static void loopMain(Menu menu, Menu menuCategorie, Menu menuFattori, Menu menuComprensoriGeografici, Menu menuProposte, UtentiModel userHandler, Utente utenteAttivo) {
        int scelta;
        boolean isConfiguratore = utenteAttivo instanceof Configuratore;

        if (isConfiguratore)
            do {
                scelta = menu.scegli();

                switch (scelta) {
                    case 0 -> System.out.println(MenuView.MSG_PROGRAM_EXIT);
                    case 1 -> userHandler.cambioCredenziali(utenteAttivo); // cambio credenziali
                    case 2 -> loopComprensoriGeografici(menuComprensoriGeografici); //menu comprensorio
                    case 3 -> loopCategorie(menuCategorie, isConfiguratore); //menu categorie
                    case 4 -> loopFattori(menuFattori); //menu fattori
                    case 5 -> loopProposte(menuProposte, utenteAttivo);
                    default -> {
                    } // già gestito dalla classe Menu
                }

            } while (scelta != 0);
        else
            do {
                scelta = menu.scegli();

                switch (scelta) {
                    case 0 -> System.out.println(MenuView.MSG_PROGRAM_EXIT);
                    case 1 -> userHandler.cambioCredenziali(utenteAttivo);
                    case 2 -> loopProposte(menuProposte, utenteAttivo);
                    case 3 -> loopCategorie(menuCategorie, isConfiguratore);
                    default -> {
                    } // già gestito dalla classe Menu
                }

            } while (scelta != 0);
    }

    private static Utente loopMenuIniziale(Menu menu, UtentiModel userHandler, ArrayList<String> listaNomiComprensorio) {
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
        ComprensorioGeograficoModel gestoreComprensori = new ComprensorioGeograficoModel(COMPRENSORI_GEOGRAFICI_XML_FILEPATH);
        do {
            scelta = menuComprensoriGeografici.scegli();
            gestoreComprensori.entryPoint(scelta);
        } while (scelta != 0);
    }

    private static void loopProposte(Menu menuProposta, Utente utenteAttivo) {
        int scelta;
        ProposteModel proposteModel = new ProposteModel(PROPOSTE_XML_FILEPATH, FATTORI_DI_CONVERSIONE_XML_FILEPATH, utenteAttivo);
        do {
            scelta = menuProposta.scegli();
            proposteModel.entryPoint(scelta);
        } while (scelta != 0);
    }

    /**
     * Gestisce il menu per le categorie.
     *
     * @param menuCategorie, menu gestito
     */
    private static void loopCategorie(Menu menuCategorie, boolean isConfiguratore) {
        int scelta;
        CategorieModel categorieModelCat = new CategorieModel(CATEGORIE_XML_FILEPATH, FATTORI_DI_CONVERSIONE_XML_FILEPATH);
        do {
            scelta = menuCategorie.scegli();
            categorieModelCat.entryPoint(scelta, isConfiguratore);
        } while (scelta != 0);
    }

    /**
     * Gestisce il menu per i fattori di conversione.
     *
     * @param menuFattori, menu gestito
     */
    private static void loopFattori(Menu menuFattori) {
        int scelta;
        FattoriModel fattoriModel = new FattoriModel(FATTORI_DI_CONVERSIONE_XML_FILEPATH);
        do {
            scelta = menuFattori.scegli();
            fattoriModel.entryPoint(scelta);
        } while (scelta != 0);

    }
}
