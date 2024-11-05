package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.gestori.GestoreCategorie;
import it.unibs.projectIngesoft.gestori.GestoreComprensorioGeografico;
import it.unibs.projectIngesoft.gestori.GestoreFattori;
import it.unibs.projectIngesoft.gestori.GestoreUtenti;
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
            "Menu Categorie",
    };

    protected static final String TITLE_MENU_COMPRENSORIO = "MENU' COMPRENSORI GEOGRAFICI";
    protected static final String[] vociComprensorioGeografico = new String[]{
            "Aggiungi Comprensorio Geografico",
            "Visualizza Comprensorio Geografico"
    };

    protected static final String TITLE_MENU_CATEGORIE = "MENU' CATEGORIE";
    protected static final String[] vociCategorie = new String[]{
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

    protected static Utente utenteLoggato = new Utente();
    protected static boolean isConfiguratore = false;

    public static void main(String[] args) {

        Menu menuIniziale = new Menu(TITLE_STARTING_MENU, vociMenuIniziale);
        Menu menu = new Menu(TITLE_MAIN_MENU, isConfiguratore ? vociMainConfiguratore : vociMainFruitore);
        Menu menuCategorie = new Menu(TITLE_MENU_CATEGORIE, isConfiguratore ? vociCategorie : vociCategorieFruitore);
        Menu menuFattori = new Menu(TITLE_MENU_FATTORI, vociFattori);
        Menu menuComprensoriGeografici = new Menu(TITLE_MENU_COMPRENSORIO, vociComprensorioGeografico);

        GestoreUtenti userHandler = new GestoreUtenti(UTENTI_XML_FILEPATH, UTENTI_DEF_CREDS_XML_FILEPATH);
        GestoreComprensorioGeografico comprensorioHandler = new GestoreComprensorioGeografico(COMPRENSORI_GEOGRAFICI_XML_FILEPATH);


        Utente utenteAttivo = loopMenuIniziale(menuIniziale, userHandler, comprensorioHandler.listaNomiComprensoriGeografici());
        if(utenteAttivo != null)
            loopMain(menu, menuCategorie, menuFattori, menuComprensoriGeografici, userHandler, utenteAttivo);
    }

    /**
     * Metodo che gestisce il menu principale
     *
     * @param menu,          menu principale
     * @param menuCategorie, sotto-menu per le categorie
     * @param menuFattori,   sotto-menu per i fattori
     */
    private static void loopMain(Menu menu, Menu menuCategorie, Menu menuFattori, Menu menuComprensoriGeografici, GestoreUtenti userHandler, Utente utenteAttivo) {
        int scelta;
        if (isConfiguratore)  //TODO utenteAttivo.getClass() == Configuratore.class
            do {
                scelta = menu.scegli();

                switch (scelta) {
                    case 0 -> System.out.println(MSG_PROGRAM_EXIT);
                    case 1 -> userHandler.cambioCredenziali(utenteLoggato); // cambio credenziali
                    case 2 -> loopComprensoriGeografici(menuComprensoriGeografici); //menu comprensorio
                    case 3 -> loopCategorie(menuCategorie); //menu categorie
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
                    case 1 -> userHandler.cambioCredenziali(utenteLoggato);
                    case 2 -> loopCategorie(menuCategorie);
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
            case 0:
                System.out.println(MSG_PROGRAM_EXIT);
                return utenteLoggato = null;
            case 1:
                utenteLoggato = userHandler.login();
                break;
            case 2:
                utenteLoggato = userHandler.register(listaNomiComprensorio);
                break;
            default:
                utenteLoggato = null;
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
    private static void loopCategorie(Menu menuCategorie) {
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
