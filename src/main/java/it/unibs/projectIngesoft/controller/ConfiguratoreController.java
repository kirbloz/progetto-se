package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
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


    private final ConfiguratoreView view;

    private final CategorieModel categorieModel;
    private final FattoriModel fattoriModel;
    private final ProposteModel proposteModel;
    private final ComprensorioGeograficoModel compGeoModel;
    private final UtentiModel utentiModel;
    private final Configuratore utenteAttivo;

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
            utenteAttivo.setFirstAccess(false);
            cambioCredenziali();
        }

        int scelta;

        do {
            scelta = view.visualizzaMenuPrincipale();

            switch (scelta) {
                case 0 -> view.uscitaMenu("programma");
                case 1 -> cambioCredenziali(); // cambio credenziali
                case 2 -> runControllerComprensoriGeografici(); // menu comprensorio
                case 3 -> runControllerCategorie(); // menu categorie
                case 4 -> runControllerFattori(); // menu fattori
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
        int scelta;
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
        int scelta;
        do {
            scelta = view.visualizzaMenuFattori();
            switch (scelta) {
                case 1 -> visualizzaFattori();
                default -> {
                }
            }
        } while (scelta != 0);
    }

    public void runControllerProposte() {
        int scelta;
        do {
            scelta = view.visualizzaMenuProposte();
            switch (scelta) {
                case 1 -> visualizzaPropostePerCategoria();
                case 2 -> visualizzaProposteDaNotificare();
                //case 3 -> cambiaStatoProposta(); // questo non è nei casi d'uso del configuratore. o sbaglio?
                case 0 -> view.uscitaMenu("submenu");
            }
        } while (scelta != 0);
    }

    //todo test - funziona?
    public void runControllerComprensoriGeografici() {
        int scelta;
        do {
            scelta = view.visualizzaMenuComprensorio();
            switch (scelta) {
                case 1 -> aggiungiComprensorio();
                case 2 -> scegliComprensorioDaVisualizzare();
                case 0 -> view.uscitaMenu("submenu");
            }
        } while (scelta != 0);
    }


    /// //////////////////////////// CATEGORIE /////////////////////////////////////////////////


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
        // se esiste almeno una foglia, allora calcola i fattori di conversione
        ArrayList<FattoreDiConversione> nuoviDaNuovaRadice = new ArrayList<>();
        for (int i = 0; i < foglie.size(); i++) {
            String nomeFogliai = Utilitas.factorNameBuilder(nomeRadice, foglie.get(i).getNome());
            for (int j = i + 1; j < foglie.size(); j++) {
                String nomeFogliaj = Utilitas.factorNameBuilder(nomeRadice, foglie.get(j).getNome());
                double fattore_ij = view.getUserInputMinMaxDouble(INSERISCI_IL_FATTORE_TRA.formatted(nomeFogliai, nomeFogliaj), MIN_FATTORE, MAX_FATTORE);

                FattoreDiConversione fattoreIJ = new FattoreDiConversione(nomeFogliai, nomeFogliaj, fattore_ij);

                nuoviDaNuovaRadice.add(fattoreIJ);
            }
        }
        return nuoviDaNuovaRadice;
    }

    private void generaEMemorizzaNuoviFattori(String nomeRadice, List<Categoria> foglie) {
        if(foglie.isEmpty()) return;

        //1. chiedi i fattori nuovi all'utente sulla base delle categorie appena inserite
        ArrayList<FattoreDiConversione> nuoviDaNuovaRadice = ottieniFattoriDelleNuoveCategorie(nomeRadice, foglie);
        //è vuoto se esiste una sola foglia

        //se esistono fattori già presenti vanno calcolati i rapporti tra i nuovi (o la singola nuova foglia) e i vecchi
        if (!fattoriModel.isEmpty()) {
            //2.1. chiedi le 2 foglie (una nuova(interna) e una preesistene(esterna)) per fare iol confronto
            String nomeFogliaEsternaFormattata = view.selezioneFogliaDaLista(fattoriModel.getKeysets());
            // 2. scegliere una categoria delle nuove, da utilizzare per il primo fattore di conversione (se non esistono nuovi fattori la scelta è tra una sola foglia)
            String nomeFogliaInternaFormattata = view.selezioneFogliaDaLista(nomeRadice, foglie);
            //2.2. chiedi il fattore di conversione EsternoInterno [x in (Old:A, New:A, x)]
            double fattoreDiConversioneEsternoInterno = view.ottieniFattoreDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata);

            //todo controlla se worka quando c'è una sola nuova categoria (anche gli inversi)
            fattoriModel.inserisciFattoriDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneEsternoInterno, nuoviDaNuovaRadice);
        } else {
            fattoriModel.inserisciSingolaFogliaNellaHashmap(nomeRadice, foglie);
        }
    }

    public void visualizzaFattori( ){
        view.visualizzaFattori(fattoriModel.getHashMapFattori(), view.selezioneFogliaDaLista(fattoriModel.getKeysets()));
    }

    public void aggiungiRadice() {
        ConfiguratoreView configView = view;

        String nomeRadice;
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
        int scelta;

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
                ? new ValoreDominio(nomeValoreDominio,
                view.getUserInputMinMaxLength(MSG_INPUT_DESCRIZIONE_VALORE_DOMINIO, 0, 100))
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
        for (Categoria radice : categorieModel.getRadici())
            view.visualizzaGerarchia(radice);
    }


    /// /////////////////////////////////////////////// COMPRENSORIO ////////////////////////////////////////////////////

    public void aggiungiComprensorio() {
        // metodo spostato nella libreria InputDatiTerminale come "stringReaderNotInAvailable"
        //String nomeComprensorio = view.selezionaNonGiaInUso(compGeoModel.getListaNomiComprensoriGeografici());
        String nomeComprensorio = view.inserimentoNomeComprensorio(compGeoModel.getListaNomiComprensoriGeografici().toArray(String[]::new));

        List<String> comuniDaInserire = view.inserimentoComuni(); //fai in inputdati un inserimento ArraydiStringhe univoche

        // Memorizzazione del nuovo comprensorio
        compGeoModel.aggiungiComprensorio(nomeComprensorio, comuniDaInserire);
    }

    public void scegliComprensorioDaVisualizzare() {
        if (compGeoModel.isEmpty()) {
            view.stampaErroreComprensoriVuoto();
            return;
        }
        //todo per qualche motivo, a video si legge "inserisci un nome non già in uso".. che è sbagliato
        String comprensorioDaStampare = view.selezionaNomeDaLista(compGeoModel.getListaNomiComprensoriGeografici());

        view.visualizzaComprensorio(comprensorioDaStampare, compGeoModel.getStringComuniByComprensorioName(comprensorioDaStampare));
    }

    /// ///////////////////////////////////////////// PROPOSTE ///////////////////////////////////////////////////////

    /**
     * todo testare
     * Duplicato da ProposteModel
     */
    public void visualizzaPropostePerCategoria() {
        String categoria = view.selezioneFogliaDaLista(fattoriModel.getKeysets());
        Predicate<Proposta> filtro = p -> p.getOfferta().equals(categoria) || p.getRichiesta().equals(categoria);
        view.visualizzaProposteCategoriaHeader(categoria);
        view.visualizzaProposte(proposteModel.getFilteredProposte(filtro).toList());
    }

    /**
     * todo testare
     * Duplicato da ProposteModel
     */
    public void visualizzaProposteDaNotificare() {
        view.visualizzaPropostePronteHeader();
        view.visualizzaProposteDaNotificare(proposteModel.getFilteredProposte(Proposta::isDaNotificare).toList());
        proposteModel.save();
    }
}
