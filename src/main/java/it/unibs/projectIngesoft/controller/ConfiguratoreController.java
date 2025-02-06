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

public class ConfiguratoreController extends BaseController<Configuratore>{

    public static final String MSG_INPUT_DESCRIZIONE_VALORE_DOMINIO = ">> Inserisci la descrizione (da 0 a 100 caratteri):\n> ";
    public static final String CONFIRM_DESCRIZIONE_AGGIUNTA = ">> Descrizione aggiunta <<";
    public static final String ASK_INSERISCI_DESCRIZIONE_VALORE_DOMINIO = ">> Vuoi inserire una descrizione per questo valore?";
    public static final String MSG_INSERIMENTO_DOMINIO_PER_FIGLIE = ">> Inserisci il nome del dominio per eventuali figlie della nuova categoria:\n> ";

    public static final String ASK_CATEGORIA_IS_FOGLIA = ">> Questa Categoria è Foglia?";


    private final ConfiguratoreView view;

    /*private final CategorieModel categorieModel;
    private final FattoriModel fattoriModel;
    private final ProposteModel proposteModel;
    private final ComprensorioGeograficoModel compGeoModel;
    private final UtentiModel utentiModel;
    private final Configuratore utenteAttivo;*/

    public ConfiguratoreController(ConfiguratoreView view,
                                   CategorieModel categorieModel,
                                   FattoriModel fattoriModel,
                                   ProposteModel proposteModel,
                                   ComprensorioGeograficoModel compGeoModel,
                                   UtentiModel utentiModel, Configuratore utenteAttivo) {
        /*this.view = view;
        this.categorieModel = categorieModel;
        this.fattoriModel = fattoriModel;
        this.proposteModel = proposteModel;
        this.compGeoModel = compGeoModel;
        this.utentiModel = utentiModel;
        this.utenteAttivo = utenteAttivo;*/
        super(view, categorieModel, fattoriModel, proposteModel, compGeoModel, utentiModel, utenteAttivo);
        this.view = view;
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
                case 1 -> cambioCredenziali();
                case 2 -> runControllerComprensoriGeografici();
                case 3 -> runControllerCategorie();
                case 4 -> runControllerFattori();
                case 5 -> runControllerProposte();
                default -> {
                }
            }
        } while (scelta != 0);
    }


    public void runControllerCategorie() {
        int scelta;
        do {
            scelta = view.visualizzaMenuCategorie();
            switch (scelta) { // switch con un solo case per ampliamento futuro
                case 1 -> this.aggiungiGerarchia();
                case 2 -> view.visualizzaGerarchie(categorieModel.getRadici());
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
                case 0 -> view.uscitaMenu("submenu");
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
                case 0 -> view.uscitaMenu("submenu");
            }
        } while (scelta != 0);
    }

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

    private void cambioCredenziali() {
        String username;
        do {
            username = view.richiestaUsername();
        } while (utentiModel.existsUsername(username));
        String password = view.richiestaPassword();
        utentiModel.cambioCredenziali(utenteAttivo, username, password);
    }

    /////////////////////////////// CATEGORIE /////////////////////////////////////////////////

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
               double fattoreIJ = view.ottieniFattoreDiConversione(nomeFogliai, nomeFogliaj);
                nuoviDaNuovaRadice.add(new FattoreDiConversione(nomeFogliai, nomeFogliaj, fattoreIJ));
            }
        }
        return nuoviDaNuovaRadice;
    }

    private void generaEMemorizzaNuoviFattori(String nomeRadice, List<Categoria> foglie) {
        if (foglie.isEmpty()) return;

        //1. chiedi i fattori nuovi all'utente sulla base delle categorie appena inserite
        ArrayList<FattoreDiConversione> nuoviDaNuovaRadice = ottieniFattoriDelleNuoveCategorie(nomeRadice, foglie);
        //è vuoto se esiste una sola foglia
        //se esistono fattori già presenti vanno calcolati i rapporti tra i nuovi (o la singola nuova foglia) e i vecchi
        if (!fattoriModel.isEmpty()) {
            //2.1. chiedi le 2 foglie (una nuova(interna) e una preesistene(esterna)) per fare il confronto
            String nomeFogliaEsternaFormattata = view.selezioneFogliaDaLista(fattoriModel.getKeysets());
            // 2. scegliere una categoria delle nuove, da utilizzare per il primo fattore di conversione (se non esistono nuovi fattori la scelta è tra una sola foglia)
            String nomeFogliaInternaFormattata = view.selezioneFogliaDaLista(nomeRadice, foglie);
            //2.2. chiedi il fattore di conversione EsternoInterno [x in (Old:A, New:A, x)]
            double fattoreDiConversioneEsternoInterno = view.ottieniFattoreDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata);
            fattoriModel.inserisciFattoriDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneEsternoInterno, nuoviDaNuovaRadice);
        }else if (!nuoviDaNuovaRadice.isEmpty()) {
            nuoviDaNuovaRadice.addAll(fattoriModel.calcolaInversi(nuoviDaNuovaRadice));
            // 1. aggiungi i nuovi fattori (sono gli unici)
            fattoriModel.aggiungiArrayListDiFattori(nuoviDaNuovaRadice);
            //todo just added, i thouth it was there already but idk
        } else {
            fattoriModel.inserisciSingolaFogliaNellaHashmap(nomeRadice, foglie);
        }
    }

    public void visualizzaFattori() {
        view.visualizzaFattori(fattoriModel.getHashMapFattori(), view.selezioneFogliaDaLista(fattoriModel.getKeysets()));
    }

    public void aggiungiRadice() {
        String nomeRadice;
        do {
            nomeRadice = view.visualizzaInserimentoNomeCategoriaRadice(categorieModel);
        } while (categorieModel.esisteRadice(nomeRadice));
        String nomeCampo = view.visualizzaInserimentoCampoCategoria();
        categorieModel.aggiungiCategoriaRadice(nomeRadice, nomeCampo);
    }

    /**
     * Guida l'inserimento di una gerarchia. Permette di inserire categorie e configurarle.
     * Poi richiama la procedura per inserire i fattori di conversione.
     */
    public void aggiungiGerarchia() {
        // 0. predispone una radice e la salva localmente
        this.aggiungiRadice();
        Categoria radice = categorieModel.getRadici().getLast();

        // 1. loop per l'inserimento di categorie nella gerarchia della radice appena creata
        int scelta;
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
        // 1. chiede nome
        String nomeCategoria = view.inserimentoNomeNuovaCategoria(categorieModel, radice);

        // 1.1 chiede madre per nuova radice, verificando che esista
        String nomeCategoriaMadre = view.inserimentoNomeCategoriaMadre(nomeCategoria,
                getListaNomiCategorieGerarchiaFiltrata(radice, Categoria::isNotFoglia));
        Categoria categoriaMadre = radice.cercaCategoria(nomeCategoriaMadre);

        // 2. chiede valore del dominio ereditato + descrizione
        String nomeValoreDominio = view.inserimentoValoreDominio(nomeCategoria, categoriaMadre);
        ValoreDominio valoreDominio = creaValoreDominio(nomeValoreDominio);

        // 3. chiede se è foglia
        boolean isFoglia = view.getUserChoiceYoN(ASK_CATEGORIA_IS_FOGLIA);
        // 3.1 se non è foglia, inserisce il dominio che imprime alle figlie
        String nomeCampoFiglie = isFoglia ?
                "" : view.getUserInput(MSG_INSERIMENTO_DOMINIO_PER_FIGLIE);

        // 4. creazione dell'oggetto Categoria
        Categoria tempCategoria = creaCategoriaNonRadice(nomeCategoria, categoriaMadre, valoreDominio, nomeCampoFiglie, isFoglia);
        // 5. aggiunta della categoria figlia alla madre
        categoriaMadre.aggiungiCategoriaFiglia(tempCategoria);
    }

    public ValoreDominio creaValoreDominio(String domainValueName) {
        boolean insertDescription = view.getUserChoiceYoN(ASK_INSERISCI_DESCRIZIONE_VALORE_DOMINIO);
        if (insertDescription) {
            String description = view.getUserInputMinMaxLength(MSG_INPUT_DESCRIZIONE_VALORE_DOMINIO, 0, 100);
            view.print(CONFIRM_DESCRIZIONE_AGGIUNTA);
            return new ValoreDominio(domainValueName, description);
        }
        return new ValoreDominio(domainValueName);
    }

    public Categoria creaCategoriaNonRadice(String nome, Categoria madre, ValoreDominio valoreDominio, String campoFiglie, boolean isFoglia) {
        if(isFoglia)
                return new Categoria(nome, madre, valoreDominio);
        else
            return new Categoria(nome, campoFiglie, madre, valoreDominio);

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


    /// /////////////////////////////////////////////// COMPRENSORIO ////////////////////////////////////////////////////

    public void aggiungiComprensorio() {
        String nomeComprensorio = view.inserimentoNomeComprensorio(compGeoModel.getListaNomiComprensoriGeografici().toArray(String[]::new));
        List<String> comuniDaInserire = view.inserimentoComuni(); //fai in inputdati un inserimento ArraydiStringhe univoche
        compGeoModel.aggiungiComprensorio(nomeComprensorio, comuniDaInserire);
    }

    public void scegliComprensorioDaVisualizzare() {
        if (compGeoModel.isEmpty()) {
            view.stampaErroreComprensoriVuoto();
            return;
        }
        String comprensorioDaStampare = view.selezionaNomeDaLista(compGeoModel.getListaNomiComprensoriGeografici());
        view.visualizzaComprensorio(comprensorioDaStampare, compGeoModel.getStringComuniByComprensorioName(comprensorioDaStampare));
    }

    /// ///////////////////////////////////////////// PROPOSTE ///////////////////////////////////////////////////////

    public void visualizzaPropostePerCategoria() {
        String categoria = view.selezioneFogliaDaLista(fattoriModel.getKeysets());
        Predicate<Proposta> filtro = p -> p.getOfferta().equals(categoria) || p.getRichiesta().equals(categoria);
        view.visualizzaProposteCategoriaHeader(categoria);
        view.visualizzaProposte(proposteModel.getFilteredProposte(filtro).toList());
    }

    public void visualizzaProposteDaNotificare() {
        view.visualizzaPropostePronteHeader();
        view.visualizzaProposteDaNotificare(proposteModel.getFilteredProposte(Proposta::isDaNotificare).toList());
        proposteModel.save();
    }
}
