package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.model.*;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.view.FruitoreView;

import java.util.List;

import static it.unibs.projectIngesoft.view.ConfiguratoreView.*;
import static it.unibs.projectIngesoft.view.FruitoreView.WARNING_NO_RAMI_DA_ESPLORARE;

public class FruitoreController {

    private FruitoreView view;

    private CategorieModel categorieModel;
    private FattoriModel fattoriModel;
    private ProposteModel proposteModel;
    private ComprensorioGeograficoModel compGeoModel;
    private UtentiModel utentiModel;
    private Fruitore utenteAttivo;

    public FruitoreController(FruitoreView view,
                              CategorieModel categorieModel,
                              FattoriModel fattoriModel,
                              ProposteModel proposteModel,
                              ComprensorioGeograficoModel compGeoModel,
                              UtentiModel utentiModel,
                              Fruitore utenteAttivo) {
        this.view = view;
        this.categorieModel = categorieModel;
        this.fattoriModel = fattoriModel;
        this.proposteModel = proposteModel;
        this.compGeoModel = compGeoModel;
        this.utentiModel = utentiModel;
        this.utenteAttivo = utenteAttivo;
    }


    public void run(){
        int scelta = 0;

        do {
            scelta = view.visualizzaMenuPrincipale();

            switch (scelta) {
                case 0 -> view.uscitaMenu("programma");//System.out.println(AccessoView.MSG_PROGRAM_EXIT);
                case 1 -> cambioCredenziali();
                //case 1 -> userHandler.cambioCredenziali(utenteAttivo);
                case 2 -> runControllerProposte();
                //case 2 -> loopProposte(menuProposte, utenteAttivo);
                case 3 -> runControllerCategorie();
                //case 3 -> loopCategorie(menuCategorie, isConfiguratore);
                default -> {
                } // già gestito dalla classe Menu
            }

        } while (scelta != 0);

    }

    /**
     * Duplicato da configuratore controller.
     */
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
                case 1 -> this.esploraGerarchie();
                case 0 -> view.uscitaMenu("submenu");
            }
        } while (scelta != 0);
    }


    public void runControllerProposte() {
        int scelta;
        do {
            scelta = view.visualizzaMenuProposte();
            switch (scelta) { // switch con un solo case per ampliamento futuro
                case 1 -> view.visualizzaProposte(proposteModel.getProposteModificabiliPerAutore(utenteAttivo));
                case 2 -> this.effettuaProposta();
                case 3 -> this.cambiaStatoProposta();
                case 0 -> view.uscitaMenu("submenu");
            }
        } while (scelta != 0);
    }


    ///////////////////////// CATEGORIE //////////////////////////

    /**
     * Duplicato da CategorieModel
     * todo implementare/rifattorizzare
     */
    public void esploraGerarchie() {
        List<String> nomiRadici = categorieModel.getRadici().stream().map(Categoria::getNome).toList();

        if (nomiRadici.isEmpty()) {
            //System.out.println(WARNING_NO_GERARCHIE_MEMORIZZATE);
            view.print(WARNING_NO_GERARCHIE_MEMORIZZATE);
        }
        //int scelta = view.visualizzaMenuEsploraGerarchia();
        int scelta;

        // 0. seleziono la radice della gerarchia da esplorare
        Categoria radice = inserimentoNomeCategoria(categorieModel.getRadici());
        Categoria madreCorrente = radice; // categoria madre del livello che si sta visualizzando al momento
        //List<Categoria> livello = madreCorrente.getCategorieFiglie();

        do {
            // 1. print del livello corrente
            view.visualizzaLivello(madreCorrente.getCampoFiglie(), madreCorrente);
            // 2. print menu
            Categoria nuovaMadre = madreCorrente;
            scelta = view.visualizzaMenuEsploraGerarchia();
            switch (scelta) {
                case 1 -> { // esplora
                    String nuovoCampo = inserimentoValoreCampo(madreCorrente.getCategorieFiglie());
                    nuovaMadre = nuovoCampo == null ?
                            madreCorrente : selezionaCategoriaDaValoreCampo(nuovoCampo, madreCorrente.getCategorieFiglie());
                }
                // torna indietro di un livello
                case 2 -> nuovaMadre = madreCorrente.isRadice() ?
                        madreCorrente : radice.cercaCategoria(madreCorrente.getNomeMadre());
                default -> view.uscitaMenu("esplora");//System.out.println(MSG_USCITA_SUBMENU);
            }
            // aggiorno i valori
            madreCorrente = nuovaMadre == null ? madreCorrente : nuovaMadre;
            //livello = madreCorrente.getCategorieFiglie();
        } while (scelta != 0);
    }


    /**
     * Duplicato parzialmente da ConfiguratoreView::inserimentoNomeCategoriaMadre
     *
     * @param categorie
     * @return
     */
    public Categoria inserimentoNomeCategoria(List<Categoria> categorie) {
        /*String nomeMadre;
        do{
            view.visualizzaRadici(categorie);
            nomeMadre = getUserInput(String.format(MSG_INSERIMENTO_NOME_CATEGORIA_MADRE, nomeCategoria));
            if(!possibiliMadri.contains(nomeMadre))
                System.out.println(WARNING_CATEGORIA_NF_NON_ESISTE);
        }while (!possibiliMadri.contains(nomeMadre));

        return nomeMadre;*/

        String tempNomeRadice;
        do {
            view.visualizzaRadici(categorie);
            tempNomeRadice = view.getUserInput(MSG_SELEZIONE_RADICE /*+ MSG_INPUT_NOME_RADICE*/);

            if (!categorieModel.esisteRadice(tempNomeRadice))
                view.print(WARNING_RADICE_NON_ESISTE);
        } while (!categorieModel.esisteRadice(tempNomeRadice));
        return categorieModel.getRadice(tempNomeRadice);
    }

    public String inserimentoValoreCampo(List<Categoria> livello) {
        String[] valoriFiglie = livello.stream()
                .filter(categoria -> !categoria.isFoglia())
                //.map(Categoria::getCampoFiglie)
                .map(categoria -> categoria.getValoreDominio().getNome())
                .toArray(String[]::new);

        if (valoriFiglie.length == 0) {
            view.print(WARNING_NO_RAMI_DA_ESPLORARE);
            return null;
        }

        return view.getUserInput(MSG_INPUT_SCELTA_CAMPO, valoriFiglie);
    }

    private Categoria selezionaCategoriaDaValoreCampo(String nuovoValore, List<Categoria> livello) {
        return livello.stream()
                .filter(categoria -> categoria.getValoreDominio().getNome().equals(nuovoValore))
                .findFirst()
                .orElse(null);
    }

    ///////////////////////// PROPOSTE //////////////////////////
    /**todo finire
     * Guida la creazione di una nuova proposta di scambio di prestazioni d'opera
     */
    public void effettuaProposta() {
        String categoriaRichiesta;
        String categoriaOfferta;
        int oreRichiesta;
        int oreOfferta;

        // 1. inserimento categoria richiesta, ore, e categoria offerta
        boolean esisteCategoriaRichiesta = false;
        do{
            categoriaRichiesta = view.inserimentoFogliaFormattata("richiesta"/*">> Inserisci Categoria Richiesta: "*/);
            if(fattoriModel.existsKeyInHashmapFattori(categoriaRichiesta)){
               esisteCategoriaRichiesta = true;
            }else {
                view.visualizzaErroreInserimentoCategoria();
            }
        }while(!esisteCategoriaRichiesta);

        oreRichiesta = view.inserimentoOre();

        boolean esisteCategoriaOfferta = false;
        do{
            categoriaOfferta = view.inserimentoFogliaFormattata("offerta"/*">> Inserisci Categoria Offerta: "*/);
            if(fattoriModel.existsKeyInHashmapFattori(categoriaOfferta)){
                esisteCategoriaRichiesta = true;
            }else {
                view.visualizzaErroreInserimentoCategoria();
            }
        }while(!esisteCategoriaOfferta);

        // 2. calcolo ore per l'offerta
        oreOfferta = fattoriModel.calcolaRapportoOre(categoriaRichiesta, categoriaOfferta, oreRichiesta);
        if (oreOfferta == -1) {
            view.visualizzaErroreCalcoloOre();
            return;
        }

        // 3. conferma e memorizza la proposta
        if (!view.confermaInserimento(categoriaRichiesta, categoriaOfferta, oreRichiesta, oreOfferta)) {
            view.visualizzaMessaggioAnnulla();
            return;
        }

        Proposta tempProposta = new Proposta(categoriaRichiesta, categoriaOfferta, oreRichiesta, oreOfferta, (Fruitore) utenteAttivo);

        // 3.1 se confermi ma è duplicata, segnala e non aggiunge
        if (proposteModel.controllaPropostaDuplicata(tempProposta)) {
            view.visualizzaMessaggioErroreDuplicato();
            return;
        }

        proposteModel.addProposta(tempProposta);
        proposteModel.cercaProposteDaChiudere(tempProposta);
    }

    /**
     * todo da implementare/rifattorizzare
     * Codice duplicato da ProposteModel
     * L'autore di una proposta può cambiare il suo stato tra RITIRATA e APERTA.
     * Guida la selezione della proposta.
     * L'autore è sempre un Fruitore.
     */
    private void cambiaStatoProposta() {
        //assert utenteAttivo instanceof Fruitore;

        if (!proposteModel.esisteAlmenoUnaPropostaPerAutore(utenteAttivo)) {
            //System.out.println(MSG_NON_HAI_PROPOSTE_NON_CHIUSE);
			view.visualizzaErroreProposteInesistenti();
            return;
        }

        String categoriaRichiesta;
        String categoriaOfferta;
        int oreRichiesta;
		
        Proposta daCambiare = null;

        // 1. inserimento categoria richiesta, ore, e categoria offerta
        boolean found = false;
        do {
            view.visualizzaProposte(proposteModel.getProposteModificabiliPerAutore(utenteAttivo));

            categoriaRichiesta = view.inserimentoFogliaFormattato(MSG_SELEZIONE_CATEGORIA_RICHIESTA);
            oreRichiesta = view.inserimentoOre();
            categoriaOfferta = view.inserimentoFogliaFormattato(MSG_SELEZIONE_CATEGORIA_OFFERTA);

            daCambiare = cercaProposta(comprensorio, categoriaOfferta, categoriaRichiesta, oreRichiesta, utenteAttivo); //ma il wadelo ha fallato questo o sto delirando
			
            if (daCambiare != null ) {
                found = true;
            }
			
        } while (!found);

        // 2. cambio stato guidato e conferma
		boolean conferma = view.viualizzaConfermaCambioStatoProposta(daCambiare);
		
        if(conferma){
			proposteModel.cambiaStato(daCambiare);
		}
    }

    /**
     * Mostra le proposte filtrando per categoria, che appaia come offerta o richiesta.
     * Guida l'immissione della categoria.
     */
    public void visualizzaPropostePerCategoria() {
        /*
        assert hashListaProposte != null;
        String categoria = gestFatt.selezioneFoglia(MSG_INSERISCI_CATEGORIA);
        Predicate<Proposta> filtro = p -> p.getOfferta().equals(categoria) || p.getRichiesta().equals(categoria);
        visualizzaProposte(HEADER_PROPOSTE_CATEGORIA.formatted(categoria), filtro);
        */
    }
}
