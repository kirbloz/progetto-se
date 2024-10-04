package gestori;

import attivita.CategoriaNonFoglia;

import java.util.ArrayList;

public class GestoreCategorie {
    // non sono sicuro di quale struttura dati utilizzare
    ArrayList<CategoriaNonFoglia> categorie;

    public GestoreCategorie() {
        /* legge da file memorizza le categorie già presenti
        prepara tutto apposto */
        //serializeXML();
        //this.deserializeXML();
    }


    /**
     * Da richiamare prima di fare qualsiasi cosa.
     * Per leggere le categorie salvate.
     */
    public void leggiCategorie() {
        //TODO
    }

    /**
     * Richiama il metodo necessario in base alla selezione dal menu.
     *
     * @param scelta, selezione dal menu
     */
    public void entryPoint(int scelta) {
        //TODO
        switch (scelta) {
            case 1:
                //aggiungi cat nf
                aggiungiCategoriaNF();
                break;
            case 2:
                //aggiungi cat f
                aggiungiCategoriaF();
                break;
            case 3:
                //aggiungi gerarchia
                aggiungiGerarchia();
                break;
            case 4:
                //descrizione
                aggiungiDescrizioneValoreDominio();
                break;
            case 5:
                //visualizza
                visualizzaGerarchia();
                break;
            default:
                System.out.println("Nulla da mostrare");
        }
    }

    public void aggiungiCategoriaNF() {
        //TODO
    }

    public void aggiungiCategoriaF() {
        //TODO
    }

    /**
     * Permette la creazione di una categoria radice.
     */
    public void aggiungiGerarchia() {
        //TODO
    }

    /**
     * Aggiunge la descrizione al valore di dominio di una qualche categoria.
     */
    public void aggiungiDescrizioneValoreDominio() {
        //TODO
    }

    /**
     * Stampa a video la struttura tree-like delle gerarchie di categorie presenti nel programma.
     */
    public void visualizzaGerarchia() {
        //TODO
    }
}
