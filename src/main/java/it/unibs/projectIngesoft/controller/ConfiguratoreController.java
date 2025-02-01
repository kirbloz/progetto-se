package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Utilitas;
import it.unibs.projectIngesoft.model.*;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.view.ConfiguratoreView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static it.unibs.projectIngesoft.libraries.Utilitas.MAX_FATTORE;
import static it.unibs.projectIngesoft.libraries.Utilitas.MIN_FATTORE;
import static it.unibs.projectIngesoft.view.ConfiguratoreView.*;

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
        if (utenteAttivo.isFirstAccess()) {
            cambioCredenziali();
        }

        int scelta = 0;

        do {
            scelta = view.visualizzaMenuPrincipale();

            switch (scelta) {
                case 0 -> view.uscitaMenu("programma");
                //System.out.println(AccessoView.MSG_PROGRAM_EXIT);
                //case 1 ->
                // userHandler.cambioCredenziali(utenteAttivo); // cambio credenziali
                case 2 -> runControllerComprensoriGeografici();
                //loopComprensoriGeografici(menuComprensoriGeografici); // menu comprensorio
                case 3 -> runControllerCategorie();
                //loopCategorie(menuCategorie, isConfiguratore); // menu categorie
                case 4 -> runControllerFattori();
                // loopFattori(menuFattori); // menu fattori
                case 5 -> runControllerProposte();
                // loopProposte(menuProposte, utenteAttivo);
                default -> {
                } // già gestito dalla classe Menu
            }

        } while (scelta != 0);
    }

    private void cambioCredenziali() {
        String username;
        do {
            username = view.richiestaUsername();
        } while (utentiModel.existsUsername(username));
        String password = view.richiestaPassword();
        utentiModel.cambioCredenziali(utenteAttivo, username, password);
    }


    public void runControllerCategorie() {
        int scelta = 0;
        do {
            scelta = view.visualizzaMenuCategorie();
            switch (scelta) { // switch con un solo case per ampliamento futuro
                case 1 -> this.aggiungiGerarchia();
                case 2 -> this.visualizzaGerarchie();
                case 0 -> view.uscitaMenu("submenu");
            }
        } while (scelta != 0);
    }

    public void runControllerFattori() {

    }

    public void runControllerProposte() {

    }

    public void runControllerComprensoriGeografici() {

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
        // se esiste almeno una foglia, allora calcola i fattori di conversione
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
            return; //todo a seconda di come avverrà il lancio di questo metodo questo controllo potrebbe essere inutile (forse si può usare assert btw)
        }

        //se esistono fattori già presenti vanno calcolati i rapporti tra i nuovi (o la singola nuova foglia) e i vecchi
        if (!fattoriModel.isEmpty()) {//todo wade porco dio attacca il cervello se devi modificare il codice altrui
            //2.1. chiedi le 2 foglie (una nuova(interna) e una preesistene(esterna)) per fare iol confronto
            String nomeFogliaEsternaFormattata = view.selezioneFogliaDaLista(fattoriModel.getKeysets());
            // 2. scegliere una categoria delle nuove, da utilizzare per il primo fattore di conversione
            String nomeFogliaInternaFormattata = view.selezioneFogliaDaLista(nomeRadice, foglie);
            //2.2. chiedi il fattore di conversione EsternoInterno [x in (Old:A, New:A, x)]
            double fattoreDiConversioneEsternoInterno = view.ottieniFattoreDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata);

            //todo controlla se worka quando c'è una sola nuova categoria (anche gli inversi)
            fattoriModel.inserisciFattoriDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneEsternoInterno, nuoviDaNuovaRadice);
        } else {
            fattoriModel.inserisciSingolaFogliaNellaHashmap(nomeRadice, foglie);
        }
    }

    public void aggiungiRadice() {
        ConfiguratoreView configView = (ConfiguratoreView) view;

        String nomeRadice = "";
        do {
            nomeRadice = configView.visualizzaInserimentoNomeCategoriaRadice(categorieModel);
        } while (categorieModel.esisteRadice(nomeRadice));

        String nomeCampo = configView.visualizzaInserimentoCampoCategoria();

        categorieModel.aggiungiCategoriaRadice(nomeRadice, nomeCampo);
    }

    /**
     * Guida l'inserimento di una gerarchia. Permette di inserire categorie e configurarle.
     * Poi richiama la procedura per inserire i fattori di conversione.
     */
    public void aggiungiGerarchia() {
        int scelta = 0;

        // 0. predispone una radice e la salva localmente
        this.aggiungiRadice();
        Categoria radice = categorieModel.getRadici().getLast();

        // 1. loop per l'inserimento di categorie nella gerarchia della radice appena creata
        do {
            scelta = view.visualizzaMenuAggiungiGerarchia();
            switch (scelta) { // switch con un solo case per ampliamento futuro
                case 1 -> this.aggiungiCategoria(radice);
                default -> view.uscitaMenu("submenu");
            }
        } while (scelta != 0);

        //1.5 imposto a foglie tutte le categorie che non hanno figlie
        radice.impostaCategorieFoglia();

        // 2. procedura per i fattori
        generaEMemorizzaNuoviFattori(radice.getNome(), radice.getFoglie());

        //3. salvataggio dei dati
        categorieModel.save();
    }


    /**
     * Metodo per l'inserimento di una Categoria generica NON RADICE.
     * Non modificabile.
     *
     * @param radice, Categoria radice di riferimento
     */
    private void aggiungiCategoria(Categoria radice) {
        assert radice != null : "la radice non deve essere null";
        assert this.categorieModel.esisteRadice(radice.getNome()) : "non è il nome di una radice";



        // 1. chiede nome
        String nomeCategoria = view.inserimentoNomeNuovaCategoria(categorieModel, radice);
        assert nomeCategoria != null
                && !nomeCategoria.isEmpty() : "Il nome della categoria non deve essere null o vuoto";

        // 1.1 chiede madre per nuova radice, verificando che esista
        // todo metodo da testare
        String nomeCategoriaMadre = view.inserimentoNomeCategoriaMadre(nomeCategoria,
                getListaNomiCategorieGerarchiaFiltrata(radice, Categoria::isNotFoglia));
        Categoria categoriaMadre = radice.cercaCategoria(nomeCategoriaMadre);


        // 2. chiede valore del dominio ereditato + descrizione
        String nomeValoreDominio = view.inserimentoValoreDominio(nomeCategoria, categoriaMadre);
        ValoreDominio valoreDominio;
        boolean insertDescription = view.getUserChoiceYoN(ASK_INSERISCI_DESCRIZIONE_VALORE_DOMINIO);
        valoreDominio = insertDescription
                ? new ValoreDominio(nomeValoreDominio, view.getUserInputMinMaxLength(MSG_INPUT_DESCRIZIONE_VALORE_DOMINIO, 0, 100))
                : new ValoreDominio(nomeValoreDominio);
        if (insertDescription)
            System.out.println(CONFIRM_DESCRIZIONE_AGGIUNTA);

        // 3. chiede se è foglia
        boolean isFoglia = view.getUserChoiceYoN(ASK_CATEGORIA_IS_FOGLIA);

        // 3.1 se non è foglia, inserisce il dominio che imprime alle figlie
        String nomeCampoFiglie = isFoglia ?
                "" : view.getUserInput(MSG_INSERIMENTO_DOMINIO_PER_FIGLIE);

        // 4. creazione dell'oggetto Categoria
        Categoria tempCategoria = isFoglia
                ? new Categoria(nomeCategoria, categoriaMadre, valoreDominio)
                : new Categoria(nomeCategoria, nomeCampoFiglie, categoriaMadre, valoreDominio);

        // 5. aggiunta della categoria figlia alla madre
        categoriaMadre.aggiungiCategoriaFiglia(tempCategoria);
        //assert categoriaMadre.getCategorieFiglie().contains(tempCategoria) : "La categoria madre deve contenere la nuova categoria figlia";

    }

    public List<Categoria> getListaCategorieGerarchiaFiltrata(Categoria radice, Predicate<Categoria> filtro) {
        List<Categoria> lista = Categoria.appiatisciGerarchiaSuLista(radice, new ArrayList<>());
        return lista.stream().filter(filtro).toList();

    }

    public List<String> getListaNomiCategorieGerarchiaFiltrata(Categoria radice, Predicate<Categoria> filtro) {
        return getListaCategorieGerarchiaFiltrata(radice, filtro)
                .stream()
                .map(Categoria::getNome)
                .toList();

    }

    public void visualizzaGerarchie() {
        view.visualizzaListaRadici(categorieModel.getRadici());
    }


}
