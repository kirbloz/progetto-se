package main;

import menu.Menu;

/* Classe main.Main.

 */
public class Main {
    public static void main(String[] args) {

        /*
            Creazione di uno pseudo menu fittizio per testare i casi d'uso durante l'implementazione.
         */
        String[] voci = new String[] {
                "Accedi",
                "Cambia Credenziali",
                "Menu Comprensorio TODO",
                "Menu Categorie WIP",
                "Menu Fattori WIP"
        };

        String[] vociCategorie = new String[] {
                "Aggiungi Categoria Non Foglia",
                "Aggiungi Categoria Foglia",
                "Aggiungi Gerarchia",
                "Aggiungi Descrizione",
                "Visualizza Gerarchia"
        };

        String[] vociFattori = new String[] {
                "Visualizza Fattori di Conversione"
                // TODO
        };



        Menu menu = new Menu("SCAMBIO ORE ATTIVITA'", voci);

        Menu menuCategorie = new Menu("MENU CATEGORIE", vociCategorie);

        Menu menuFattori = new Menu("MENU FATTORI", vociFattori);

        loopMain(menu, menuCategorie, menuFattori);



    }

    private static void loopMain(Menu menu, Menu menuCategorie, Menu menuFattori){

        int scelta = 0;
        do{
            scelta = menu.scegli();

            switch (scelta){
                case 4:
                    //menu categorie
                    loopCategorie(menuCategorie);
                case 5:
                    //menu fattori
                    loopFattori(menuFattori);
                default:
                    System.out.println("Niente da mostrare.");
            }

        }while(scelta!=0);
    }

    private static void loopCategorie(Menu menuCategorie){
        int scelta = 0;
        do{
            scelta = menuCategorie.scegli();

            switch (scelta){

                default:
                    System.out.println("Niente da mostrare.");
            }

        }while(scelta!=0);
    }

    private static void loopFattori(Menu menuFattori){
        int scelta = 0;
        do{
            scelta = menuFattori.scegli();

            switch (scelta){

                default:
                    System.out.println("Niente da mostrare.");
            }

        }while(scelta!=0);

    }
}
