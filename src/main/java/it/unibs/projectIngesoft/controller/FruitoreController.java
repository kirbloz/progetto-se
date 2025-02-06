package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.model.*;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.view.FruitoreView;

import java.util.List;



import static it.unibs.projectIngesoft.view.FruitoreView.*;

public class FruitoreController extends BaseController <Fruitore> {

    public static final String WARNING_RADICE_NON_ESISTE = ">> (!!) Per favore indica una categoria radice che esiste";
    public static final String MSG_SELEZIONE_RADICE = ">> Inserisci il nome di una categoria radice\n";
    public static final String MSG_INPUT_SCELTA_CAMPO = ">> Scegli un campo tra quelli delle categorie non foglia\n";


    private FruitoreView view;

    /*private CategorieModel categorieModel;
    private FattoriModel fattoriModel;
    private ProposteModel proposteModel;
    private ComprensorioGeograficoModel compGeoModel;
    private UtentiModel utentiModel;
    private Fruitore utenteAttivo;*/

    public FruitoreController(FruitoreView view,
                              CategorieModel categorieModel,
                              FattoriModel fattoriModel,
                              ProposteModel proposteModel,
                              ComprensorioGeograficoModel compGeoModel,
                              UtentiModel utentiModel,
                              Fruitore utenteAttivo) {
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


    public void run(){
        int scelta = 0;

        do {
            scelta = view.visualizzaMenuPrincipale();

            switch (scelta) {
                case 0 -> view.uscitaMenu("programma");
                case 1 -> cambioCredenziali();
                case 2 -> runControllerProposte();
                case 3 -> runControllerCategorie();
                default -> {
                } // già gestito dalla classe Menu
            }

        } while (scelta != 0);

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
                case 1 -> this.visualizzaProposteEffettuate();
                case 2 -> this.effettuaProposta();
                case 3 -> this.cambiaStatoProposta();
                case 0 -> view.uscitaMenu("submenu");
            }
        } while (scelta != 0);
    }


    ///////////////////////// CATEGORIE //////////////////////////

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

    /**
     * Duplicato da CategorieModel
     */
    public void esploraGerarchie() {
        List<String> nomiRadici = categorieModel.getRadici().stream().map(Categoria::getNome).toList();

        if (nomiRadici.isEmpty()) {
            view.print(WARNING_NO_GERARCHIE_MEMORIZZATE);
            return;
        }
        int scelta;

        // 0. seleziono la radice della gerarchia da esplorare
        List<Categoria> listaRadiciNonFoglia = categorieModel.getRadici()
                .stream().filter(c -> !c.isFoglia()).toList();
        Categoria radice = inserimentoNomeCategoria(listaRadiciNonFoglia);
        Categoria madreCorrente = radice; // categoria madre del livello che si sta visualizzando al momento

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
        } while (scelta != 0);
    }


    /**
     * Duplicato parzialmente da ConfiguratoreView::inserimentoNomeCategoriaMadre
     *
     * @param categorie
     * @return
     */
    public Categoria inserimentoNomeCategoria(List<Categoria> categorie) {

        String tempNomeRadice;
        do {
            view.visualizzaListaRadici(categorie);
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

        return view.getUserInputFromAvailable(MSG_INPUT_SCELTA_CAMPO, valoriFiglie);
    }

    private Categoria selezionaCategoriaDaValoreCampo(String nuovoValore, List<Categoria> livello) {
        return livello.stream()
                .filter(categoria -> categoria.getValoreDominio().getNome().equals(nuovoValore))
                .findFirst()
                .orElse(null);
    }

    ///////////////////////// PROPOSTE //////////////////////////
    /**
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
            categoriaRichiesta = view.inserimentoFogliaFormattata(MSG_INSERISCI_RICHIESTA/*">> Inserisci Categoria Richiesta: "*/);
            if(fattoriModel.existsKeyInHashmapFattori(categoriaRichiesta)){
               esisteCategoriaRichiesta = true;
            }else {
                view.visualizzaErroreInserimentoCategoria();
            }
        }while(!esisteCategoriaRichiesta);

        oreRichiesta = view.inserimentoOre();

        boolean esisteCategoriaOfferta = false;
        do{
            categoriaOfferta = view.inserimentoFogliaFormattata(MSG_INSERISCI_OFFERTA/*">> Inserisci Categoria Offerta: "*/);
            if(fattoriModel.existsKeyInHashmapFattori(categoriaOfferta)){
                esisteCategoriaOfferta = true;
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

        Proposta tempProposta = new Proposta(categoriaRichiesta, categoriaOfferta, oreRichiesta, oreOfferta, utenteAttivo);

        // 3.1 se confermi ma è duplicata, segnala e non aggiunge
        if (proposteModel.controllaPropostaDuplicata(tempProposta)) {
            view.visualizzaMessaggioErroreDuplicato();
            return;
        }

        proposteModel.addProposta(tempProposta);
        proposteModel.cercaProposteDaChiudere(tempProposta);
    }

    /**
     * Codice duplicato da ProposteModel
     * L'autore di una proposta può cambiare il suo stato tra RITIRATA e APERTA.
     * Guida la selezione della proposta.
     * L'autore è sempre un Fruitore.
     */
    private void cambiaStatoProposta() {

        if (!proposteModel.esisteAlmenoUnaPropostaPerAutore(utenteAttivo)) {
			view.visualizzaErroreProposteInesistenti();
            return;
        }

        view.visualizzaProposteModificabiliHeader();

        String categoriaRichiesta;
        String categoriaOfferta;
        int oreRichiesta;
		
        Proposta daCambiare;

        // 1. inserimento categoria richiesta, ore, e categoria offerta
        boolean found = false;
        do {
            view.visualizzaProposte(proposteModel.getProposteModificabiliPerAutore(utenteAttivo));

            categoriaRichiesta = view.inserimentoFogliaFormattata(MSG_SELEZIONE_CATEGORIA_RICHIESTA);
            oreRichiesta = view.inserimentoOre();
            categoriaOfferta = view.inserimentoFogliaFormattata(MSG_SELEZIONE_CATEGORIA_OFFERTA);

            daCambiare = proposteModel.cercaPropostaCambiabile(categoriaOfferta, categoriaRichiesta, oreRichiesta, utenteAttivo);
            if (daCambiare != null )
                found = true;
			
        } while (!found);

        // 2. cambio stato guidato e conferma
		boolean conferma = view.visualizzaInserimentoConfermaCambioStatoProposta(daCambiare);
        if(conferma){
			proposteModel.cambiaStato(daCambiare);
            view.visualizzaConfermaCambioStatoProposta();
		}
    }

    private void visualizzaProposteEffettuate(){
        view.visualizzaProposteAutoreHeader(utenteAttivo.getUsername());
        view.visualizzaProposte(proposteModel.getPropostePerAutore(utenteAttivo));
    }
}
