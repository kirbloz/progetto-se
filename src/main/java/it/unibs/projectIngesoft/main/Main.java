package main;

import gestori.GestoreCategorie;
import gestori.GestoreFattori;
import menu.Menu;

/* Classe main.Main.

 */
public class Main {

    protected final static String[] voci = new String[]{
            "Accedi",
            "Cambia Credenziali",
            "Menu Comprensorio TODO",
            "Menu Categorie WIP",
            "Menu Fattori WIP"
    };

    protected final static String[] vociCategorie = new String[]{
            "Aggiungi Categoria Non Foglia",
            "Aggiungi Categoria Foglia",
            "Aggiungi Gerarchia",
            "Aggiungi Descrizione",
            "Visualizza Gerarchia"
    };

    protected final static String[] vociFattori = new String[]{
            "Visualizza Fattori di Conversione"
            // TODO
    };

    public static void main(String[] args) {

        /*
            Creazione di uno pseudo menu fittizio per testare i casi d'uso durante l'implementazione.
         */

        Menu menu = new Menu("SCAMBIO ORE ATTIVITA'", voci);

        Menu menuCategorie = new Menu("MENU CATEGORIE", vociCategorie);

        Menu menuFattori = new Menu("MENU FATTORI", vociFattori);

        loopMain(menu, menuCategorie, menuFattori);


    }

    /**
     * Metodo che cicla nelle scelte per il menu principale
     *
     * @param menu,          menu principale
     * @param menuCategorie, sotto-menu per le categorie
     * @param menuFattori,   sotto-menu per i fattori
     */
    private static void loopMain(Menu menu, Menu menuCategorie, Menu menuFattori) {

        int scelta = 0;
        do {
            scelta = menu.scegli();

            switch (scelta) {
                //TODO
                case 4:
                    //menu categorie
                    loopCategorie(menuCategorie);
                    break;
                case 5:
                    //menu fattori
                    loopFattori(menuFattori);
                    break;
                default:
                    System.out.println("Niente da mostrare.");
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
        GestoreCategorie gestCat = new GestoreCategorie();

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
        GestoreFattori gestFatt = new GestoreFattori("serialized.xml");

        do {
            scelta = menuFattori.scegli();
            gestFatt.entryPoint(scelta);
        } while (scelta != 0);

    }
}
