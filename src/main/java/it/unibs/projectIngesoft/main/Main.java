package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.gestori.GestoreCategorie;
import it.unibs.projectIngesoft.gestori.GestoreFattori;
import it.unibs.projectIngesoft.gestori.GestoreUtenti;
import it.unibs.projectIngesoft.menu.Menu;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.HashMap;

/* Classe main.Main.

 */
public class Main {

    protected static final String FATTORI_DI_CONVERSIONE_XML_FILEPATH = "serialized.xml";
    protected static final String UTENTI_XML_FILEPATH = "users.xml";
    public static final String CATEGORIE_XML_FILEPATH = "categorie.xml";

    public static final String MSG_PROGRAM_EXIT = "> ARRIVEDERCI <";
    public static final String MSG_DEFAULT_NO_VALID_MENU_SELECTION = "Niente da mostrare.";

    protected static final String TITLE_MAIN_MENU = "MENU' PRINCIPALE - SCAMBIO ORE";
    protected static final String[] vociMain = new String[]{
            //"Accedi",
            "Cambia Credenziali",
            "Menu Comprensorio TODO",
            "Menu Categorie WIP",
            "Menu Fattori WIP"
    };

    protected static final String TITLE_MENU_CATEGORIE = "MENU' CATEGORIE";
    protected static final String[] vociCategorie = new String[]{
            "Aggiungi Categoria Non Foglia",
            "Aggiungi Categoria Foglia",
            "Aggiungi Gerarchia",
            "Aggiungi Descrizione",
            "Visualizza Gerarchia"
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

    protected static final HashMap<String, ArrayList<FattoreDiConversione>> mapFattori = new HashMap<>() {{
        put("c1", listaFattori);
    }};


    protected static Utente utenteLoggato = new Utente();


    public static void main(String[] args) {
        /*
            Creazione di uno pseudo menu fittizio per testare i casi d'uso durante l'implementazione.
         */
        GestoreUtenti userHandler = new GestoreUtenti(UTENTI_XML_FILEPATH);
        Menu menu = new Menu(TITLE_MAIN_MENU, vociMain);
        Menu menuCategorie = new Menu(TITLE_MENU_CATEGORIE, vociCategorie);
        Menu menuFattori = new Menu(TITLE_MENU_CATEGORIE, vociFattori);


        utenteLoggato = userHandler.login();
        //idea: per la distinzione tra configuratore e utente finale nel login possiamo mettere:    // -m
        /*
        if(utenteLoggato.getClass() == Configuratore.class){
            loopMainConfiguratore(...);
        } else if (utenteLoggato.getClass() != Configuratore.class) {
            loopMainUtenteFinale(...)
        }
         */


        loopMain(menu, menuCategorie, menuFattori, userHandler);

    }

    /**
     * Metodo che cicla nelle scelte per il menu principale
     *
     * @param menu,          menu principale
     * @param menuCategorie, sotto-menu per le categorie
     * @param menuFattori,   sotto-menu per i fattori
     */
    private static void loopMain(Menu menu, Menu menuCategorie, Menu menuFattori, GestoreUtenti userHandler) {

        int scelta = 0;
        do {
            scelta = menu.scegli();

            switch (scelta) {
                case 0:
                    System.out.println(MSG_PROGRAM_EXIT);
                    break;
                case 1:
                    // cambio credenzilai
                    userHandler.cambioCredenziali(utenteLoggato);
                    break;
                case 2:
                    //menu comprensorio
                    // TODO
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
     * Metodo che cicla nelle scelte per il menu delle categorie
     *
     * @param menuCategorie, autoesplicativo
     */
    private static void loopCategorie(Menu menuCategorie) {
        int scelta = 0;
        GestoreCategorie gestCat = new GestoreCategorie(CATEGORIE_XML_FILEPATH);

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
