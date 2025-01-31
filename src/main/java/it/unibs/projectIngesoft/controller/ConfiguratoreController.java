package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Utilitas;
import it.unibs.projectIngesoft.model.*;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.view.ConfiguratoreView;

import java.util.ArrayList;
import java.util.List;

import static it.unibs.projectIngesoft.libraries.Utilitas.MAX_FATTORE;
import static it.unibs.projectIngesoft.libraries.Utilitas.MIN_FATTORE;
import static it.unibs.projectIngesoft.view.ConfiguratoreView.INSERISCI_IL_FATTORE_TRA;
import static it.unibs.projectIngesoft.view.ConfiguratoreView.MSG_INSERISCI_FOGLIA_ESTERNA;

public class ConfiguratoreController {


    private ConfiguratoreView view;

    private CategorieModel categorieModel;
    private FattoriModel fattoriModel;
    private ProposteModel proposteModel;
    private ComprensorioGeograficoModel compGeoModel;
    private UtentiModel utentiModel;
    private Configuratore utenteAttivo;

    public ConfiguratoreController(ConfiguratoreView view,
                                   CategorieModel categorieModel,
                                   FattoriModel fattoriModel,
                                   ProposteModel proposteModel,
                                   ComprensorioGeograficoModel compGeoModel,
                                   UtentiModel utentiModel, Configuratore utenteAttivo) {
        this.view = view;
        this.categorieModel = categorieModel;
        this.fattoriModel = fattoriModel;
        this.proposteModel = proposteModel;
        this.compGeoModel = compGeoModel;
        this.utentiModel = utentiModel;
        this.utenteAttivo = utenteAttivo;
    }

    /**
     * Richiama il metodo necessario in base alla selezione dal menu principale.
     */
    public void run() {
        if(utenteAttivo.isFirstAccess()){
            cambioCredenziali();
        }

        int scelta = 0;
        do {
            scelta = view.visualizzaMenuPrincipale();

            switch (scelta) {
                case 0 -> view.uscitaMenu("programma");//System.out.println(AccessoView.MSG_PROGRAM_EXIT);
                //case 1 -> userHandler.cambioCredenziali(utenteAttivo); // cambio credenziali
                //case 2 -> loopComprensoriGeografici(menuComprensoriGeografici); //menu comprensorio
                //case 3 -> loopCategorie(menuCategorie, isConfiguratore); //menu categorie
                //case 4 -> loopFattori(menuFattori); //menu fattori
                //case 5 -> loopProposte(menuProposte, utenteAttivo);
                default -> {
                } // già gestito dalla classe Menu
            }

        } while (scelta != 0);
    }

    private void cambioCredenziali() {
        String username;
        do {
            username = view.richiestaUsername();
        }while(utentiModel.existsUsername(username));
        String password = view.richiestaPassword();
        utentiModel.cambioCredenziali(utenteAttivo, username, password);
    }


    //todo da rifattorizzare per l'utilizzo nel controller

    /**
     * Cicla le foglie dalla prima all'ultima per generare tutte le coppie di valori possibili
     * (combinazione senza ripetizione) e per ogni coppia generata chiede il fattore all'utente.
     * Genera il FattoreDiConversione + il suo inverso e li memorizza.
     *
     * @param nomeRadice, nome della radice delle foglie
     * @param foglie,     lista di foglie
     * @return lista di fattori di conversione
     */
    private ArrayList<FattoreDiConversione> ottieniFattoriDelleNuoveCategorie(String nomeRadice, List<Categoria> foglie) {
        //todo levare questo controllo se non serve a nulla
        //se non esistono foglie non serve a nulla fare i fattori
        if (foglie.isEmpty())
            return null;
        // se esiste almeno una foglia, allora calcola i fattori //todo se esiste esattamente una foglia Ritorna un new ArrayList vuoto
        ArrayList<FattoreDiConversione> nuoviDaNuovaRadice = new ArrayList<>();
        for (int i = 0; i < foglie.size(); i++) {
            String nomeFogliai = Utilitas.factorNameBuilder(nomeRadice, foglie.get(i).getNome());
            for (int j = i + 1; j < foglie.size(); j++) {
                String nomeFogliaj = Utilitas.factorNameBuilder(nomeRadice, foglie.get(j).getNome());
                // TODO levare user interaction
                double fattore_ij = InputDatiTerminale.leggiDoubleConRange(INSERISCI_IL_FATTORE_TRA.formatted(nomeFogliai, nomeFogliaj), MIN_FATTORE, MAX_FATTORE);

                FattoreDiConversione fattoreIJ = new FattoreDiConversione(nomeFogliai, nomeFogliaj, fattore_ij);


                nuoviDaNuovaRadice.add(fattoreIJ);
            }
        }
        return nuoviDaNuovaRadice;
    }

    //TODO chiama questa funzione quando hai finito l'inserimento di una nuova radice con tutte le sue categorie dimmerda
    private void generaEMemorizzaNuoviFattori(String nomeRadice, List<Categoria> foglie) {
        //1. chiedi i fattori nuovi all'utente sulla base delle categorie appena inserite
        ArrayList<FattoreDiConversione> nuoviDaNuovaRadice = ottieniFattoriDelleNuoveCategorie(nomeRadice, foglie);
        //è null se non esistono foglie, è vuoto se esiste una sola foglia
        if (nuoviDaNuovaRadice == null) { //se non esistono foglie non hanno senso i fattori
            return;
        }

        //se esistono fattori già presenti vanno calcolati i rapporti tra i nuovi (o la singola nuova foglia) e i vecchi
        if (!fattoriModel.isEmpty()) {
            //2.1. chiedi le 2 foglie (una nuova(interna) e una preesistene(esterna)) per fare iol confronto
            //2.2. chiedi il fattore di conversione EsternoInterno
            view.stampaOpzioni(fattoriModel.getKeysets());
            String nomeFogliaEsternaFormattata = view.selezioneFoglia(MSG_INSERISCI_FOGLIA_ESTERNA);
            // 2. scegliere una categoria delle nuove, da utilizzare per il primo fattore di conversione
            String nomeFogliaInternaFormattata = view.selezioneFogliaDaLista(nomeRadice, foglie);

            //3. chiedi il fattore di conversione tra le 2 [x in (Old:A New:A x)]
            double fattoreDiConversioneEsternoInterno = view.ottieniFattoreDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata);
            //todo controlla se worka quando c'è una sola nuova categoria (anche gli inversi)
            fattoriModel.inserisciFattoriDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneEsternoInterno, nuoviDaNuovaRadice);
        } else {
            //Todo assumo che non si chiami questo metodo se non sono state create categorie foglia (spero wade non sia un idiota e non lo chiami)
            fattoriModel.inserisciSingolaFogliaNellaHashmap(nomeRadice, foglie);
        }
    }

    public void aggiungiRadice() {
        ConfiguratoreView configView = (ConfiguratoreView) view;

        String nomeRadice = "";
        do {
            nomeRadice = configView.visualizzaInserimentoCategoriaRadice(categorieModel);
        } while (categorieModel.esisteRadice(nomeRadice));

        String nomeCampo = configView.visualizzaInserimentoCampoCategoria();

        categorieModel.aggiungiCategoriaRadice(nomeRadice, nomeCampo);

    }

    public void visualizzaGerarchie() {
        view.visualizzaListaRadici(categorieModel.getRadici());
    }


}
