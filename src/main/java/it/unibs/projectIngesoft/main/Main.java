package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.gestori.GestoreCategorie;
import it.unibs.projectIngesoft.gestori.GestoreComprensorioGeografico;
import it.unibs.projectIngesoft.gestori.GestoreFattori;
import it.unibs.projectIngesoft.gestori.GestoreUtenti;
import it.unibs.projectIngesoft.libraries.Menu;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.HashMap;

/* Classe main.Main.

 */
public class Main {

    protected static final String FATTORI_DI_CONVERSIONE_XML_FILEPATH = "fattori.xml"; ///DA RINOMINARE
    protected static final String UTENTI_XML_FILEPATH = "users.xml";
    public static final String CATEGORIE_XML_FILEPATH = "categorie.xml";
    public static final String COMPRENSORI_GEOGRAFICI_XML_FILEPATH = "comprensoriGeografici.xml";

    public static final String MSG_PROGRAM_EXIT = "> ARRIVEDERCI <";
    public static final String MSG_DEFAULT_NO_VALID_MENU_SELECTION = "Niente da mostrare.";

    protected static final String TITLE_MAIN_MENU = "MENU' PRINCIPALE - SCAMBIO ORE";
    protected static final String[] vociMain = new String[]{
            "Cambia Credenziali",
            "Menu Comprensorio ",
            "Menu Categorie",
            "Menu Fattori"
    };

    protected static final String TITLE_MENU_COMPRENSORIO = "MENU' COMPRENSORI GEOGRAFICI";
    protected static final String[] vociComprensorioGeografico = new String[]{
            "Aggiungi Comprensorio Geografico",
            "Visualizza Comprensorio Geografico"
    };

    protected static final String TITLE_MENU_CATEGORIE = "MENU' CATEGORIE";
    protected static final String[] vociCategorie = new String[]{
            /*"Aggiungi Categoria Non Foglia",
            "Aggiungi Categoria Foglia",
            "Aggiungi Descrizione",*/
            "Aggiungi Gerarchia",
            "Visualizza Gerarchie"
    };

    protected static final String TITLE_MENU_FATTORI = "MENU' FATTORI";
    protected static final String[] vociFattori = new String[]{
            "Visualizza Fattori di Conversione"
    };

    // TODO remove this test data
    protected static final ArrayList<FattoreDiConversione> listaFattori = new ArrayList<>() {{
        add(new FattoreDiConversione("c1", "c2", 4.5));
        add(new FattoreDiConversione("c1", "c3", 0.2));
    }};

    protected static final ArrayList<FattoreDiConversione> listaFattori2 = new ArrayList<>() {{
        add(new FattoreDiConversione("c4", "c2", 0.6));
        add(new FattoreDiConversione("c4", "c3", 1));
    }};

    protected static final HashMap<String, ArrayList<FattoreDiConversione>> mapFattori = new HashMap<>() {{
        put("c1", listaFattori);
        put("c4", listaFattori2);
    }};

    protected static Utente utenteLoggato = new Utente();

    public static void main(String[] args) {
        /*
            Creazione di uno pseudo menu fittizio per testare i casi d'uso durante l'implementazione.
         */

        Menu menu =                      new Menu(TITLE_MAIN_MENU, vociMain);
        Menu menuCategorie =             new Menu(TITLE_MENU_CATEGORIE, vociCategorie);
        Menu menuFattori =               new Menu(TITLE_MENU_FATTORI, vociFattori);
        Menu menuComprensoriGeografici = new Menu(TITLE_MENU_COMPRENSORIO, vociComprensorioGeografico);

        GestoreUtenti userHandler =      new GestoreUtenti(UTENTI_XML_FILEPATH);
        utenteLoggato = userHandler.login();
        //idea: per la distinzione tra configuratore e utente finale nel login possiamo mettere:    // -m
        /*
        if(utenteLoggato.getClass() == Configuratore.class){
            loopMainConfiguratore(...);
        } else if (utenteLoggato.getClass() != Configuratore.class) {
            loopMainUtenteFinale(...)
        }
         */
        loopMain(menu, menuCategorie, menuFattori, menuComprensoriGeografici, userHandler);
    }

    /**
     * Metodo che cicla nelle scelte per il menu principale
     *
     * @param menu,          menu principale
     * @param menuCategorie, sotto-menu per le categorie
     * @param menuFattori,   sotto-menu per i fattori
     */
    private static void loopMain(Menu menu, Menu menuCategorie, Menu menuFattori, Menu menuComprensoriGeografici, GestoreUtenti userHandler) {

        int scelta = 0;
        do {
            scelta = menu.scegli();

            switch (scelta) {
                case 0:
                    System.out.println(MSG_PROGRAM_EXIT);
                    break;
                case 1:
                    // cambio credenziali
                    userHandler.cambioCredenziali(utenteLoggato);
                    break;
                case 2:
                    //menu comprensorio
                    loopComprensoriGeografici(menuComprensoriGeografici);
                    // @martino
                    break;
                case 3:
                    //menu categorie
                    loopCategorie(menuCategorie);
                    break;
                case 4:
                    //menu fattori
                    loopFattori(menuFattori);
                    break;
                default:
                    System.out.println(MSG_DEFAULT_NO_VALID_MENU_SELECTION);
            }

        } while (scelta != 0);
    }

    /**
     *
     * @param menuComprensoriGeografici
     */
    private static void loopComprensoriGeografici(Menu menuComprensoriGeografici) {
        int scelta = 0;
        GestoreComprensorioGeografico gestoreComprensori = new GestoreComprensorioGeografico(COMPRENSORI_GEOGRAFICI_XML_FILEPATH);

        do {
            scelta = menuComprensoriGeografici.scegli();
            gestoreComprensori.entryPoint(scelta);
        } while (scelta != 0);
    }


    /**
     * Metodo che cicla nelle scelte per il menu delle categorie
     *
     * @param menuCategorie, autoesplicativo
     */
    private static void loopCategorie(Menu menuCategorie) {
        int scelta = 0;
        GestoreCategorie gestCat = new GestoreCategorie(CATEGORIE_XML_FILEPATH, FATTORI_DI_CONVERSIONE_XML_FILEPATH);

        do {
            scelta = menuCategorie.scegli();
            gestCat.entryPoint(scelta);
        } while (scelta != 0);
    }

    /**
     * Metodo che cicla nelle scelte per il menu dei fattori
     *
     * @param menuFattori, autoesplicativo
     */
    private static void loopFattori(Menu menuFattori) {
        int scelta = 0;
        GestoreFattori gestFatt = new GestoreFattori(FATTORI_DI_CONVERSIONE_XML_FILEPATH);

        //per hot testing
        //gestFatt.setFattori(mapFattori);
        //gestFatt.visualizzaFattori();

        do {
            scelta = menuFattori.scegli();
            gestFatt.entryPoint(scelta);
        } while (scelta != 0);

    }
}
