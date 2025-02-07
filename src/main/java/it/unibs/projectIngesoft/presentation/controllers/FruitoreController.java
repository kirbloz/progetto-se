package it.unibs.projectIngesoft.presentation.controllers;

import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.Proposta;
import it.unibs.projectIngesoft.core.domain.model.*;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Fruitore;
import it.unibs.projectIngesoft.presentation.view.FruitoreView;

import java.util.List;
import java.util.Optional;

public class FruitoreController extends BaseController <Fruitore> {

    public static final String WARNING_RADICE_NON_ESISTE = ">> (!!) Per favore indica una categoria radice che esiste";
    public static final String MSG_SELEZIONE_RADICE = ">> Inserisci il nome di una categoria radice\n";
    public static final String MSG_INPUT_SCELTA_CAMPO = ">> Scegli un campo tra quelli delle categorie non foglia\n";

    public static final String WARNING_NO_RAMI_DA_ESPLORARE = ">> (!!) Non ci sono nuovi rami da esplorare";
    public static final String MSG_INSERISCI_RICHIESTA = ">> Inserisci una categoria valida di cui vuoi effettuare la RICHIESTA.";
    public static final String MSG_INSERISCI_OFFERTA = ">> Inserisci una categoria valida che sei disposto a OFFRIRE in cambio.";

    public static final String MSG_SELEZIONE_CATEGORIA_RICHIESTA = ">> Inserisci la categoria RICHIESTA per la selezione: ";
    public static final String MSG_SELEZIONE_CATEGORIA_OFFERTA = ">> Inserisci la categoria OFFERTA per la selezione: ";

    private FruitoreView view;

    public FruitoreController(FruitoreView view,
                              CategorieModel categorieModel,
                              FattoriModel fattoriModel,
                              ProposteModel proposteModel,
                              ComprensorioGeograficoModel compGeoModel,
                              UtentiModel utentiModel,
                              Fruitore utenteAttivo) {
        super(categorieModel, fattoriModel, proposteModel, compGeoModel, utentiModel, utenteAttivo);
        this.view = view;
    }


    public void run(){
        int scelta;

        do {
            scelta = view.visualizzaMenuPrincipale();

            switch (scelta) {
                case 0 -> view.uscitaMenu("programma");
                case 1 -> cambioCredenziali();
                case 2 -> runControllerProposte();
                case 3 -> runControllerCategorie();
            }

        } while (scelta != 0);

    }

    public void runControllerCategorie() {
        int scelta;
        do {
            scelta = view.visualizzaMenuCategorie();
            switch (scelta) {
                case 1 -> this.esploraGerarchie();
                case 0 -> view.uscitaMenu("submenu");
            }
        } while (scelta != 0);
    }

    public void runControllerProposte() {
        int scelta;
        do {
            scelta = view.visualizzaMenuProposte();
            switch (scelta) {
                case 1 -> this.visualizzaProposteEffettuate();
                case 2 -> this.effettuaProposta();
                case 3 -> this.cambiaStatoProposta();
                case 0 -> view.uscitaMenu("submenu");
            }
        } while (scelta != 0);
    }


    protected void cambioCredenziali() {
        String username;
        do {
            username = view.richiestaUsername();
        } while (utentiModel.existsUsername(username));
        String password = view.richiestaPassword();
        utentiModel.cambioCredenziali(utenteAttivo, username, password);
    }

    public void esploraGerarchie() {
        List<String> nomiRadici = categorieModel.getRadici().stream().map(Categoria::getNome).toList();

        if (nomiRadici.isEmpty()) {
            view.visualizzaErroreNoGerarchieMemorizzate();
            return;
        }
        int scelta;

        List<Categoria> listaRadiciEsplorabili = categorieModel.getRadici()
                .stream().filter(c -> !c.isFoglia()).toList();
        Categoria radice = inserimentoNomeCategoria(listaRadiciEsplorabili);
        Categoria madreCorrente = radice;

        do {
            view.visualizzaLivello(madreCorrente.getCampoFiglie(), madreCorrente);
            Categoria nuovaMadre;

            scelta = view.visualizzaMenuEsploraGerarchia();
            nuovaMadre = selezionaNuovaMadreLivello(scelta, madreCorrente, radice);
            madreCorrente = nuovaMadre == null ? madreCorrente : nuovaMadre;
        } while (scelta != 0);
    }

    public Categoria selezionaNuovaMadreLivello(int scelta, Categoria madreCorrente, Categoria radice){
        switch (scelta) {
            case 1 -> {
                String nuovoCampo = inserimentoValoreCampo(madreCorrente.getCategorieFiglie());
                return nuovoCampo == null ?
                        madreCorrente : selezionaCategoriaDaValoreCampo(nuovoCampo, madreCorrente.getCategorieFiglie());
            }
            case 2 -> { return madreCorrente.isRadice() ?
                    madreCorrente : radice.cercaCategoria(madreCorrente.getNomeMadre());}

            default -> {
                view.uscitaMenu("esplora");
                return null;
            }
        }
    }


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

    /**
     * Guida la creazione di una nuova proposta di scambio di prestazioni d'opera
     */
    public void effettuaProposta() {
        String categoriaRichiesta;
        String categoriaOfferta;
        int oreRichiesta;
        Optional<Integer> oreOfferta;

        do {
            categoriaRichiesta = inserimentoFogliaFormattataFromAvailable(MSG_INSERISCI_RICHIESTA);
            oreRichiesta = view.inserimentoOre();
            categoriaOfferta = inserimentoFogliaFormattataFromAvailable(MSG_INSERISCI_OFFERTA);
            if (categoriaRichiesta.equals(categoriaOfferta))
                view.visualizzaErroreRichiestaUgualeOfferta();
        }while(categoriaRichiesta.equals(categoriaOfferta));


        oreOfferta = fattoriModel.calcolaRapportoOre(categoriaRichiesta, categoriaOfferta, oreRichiesta);
        if (oreOfferta.isEmpty()) {
            view.visualizzaErroreCalcoloOre();
            return;
        }

        if (!view.confermaInserimento(categoriaRichiesta, categoriaOfferta, oreRichiesta, oreOfferta.get())) {
            view.visualizzaMessaggioAnnulla();
            return;
        }
        Proposta tempProposta = new Proposta(categoriaRichiesta, categoriaOfferta, oreRichiesta, oreOfferta.get(), utenteAttivo);

        if (proposteModel.controllaPropostaDuplicata(tempProposta, utenteAttivo.getComprensorioDiAppartenenza())) {
            view.visualizzaMessaggioErroreDuplicato();
            return;
        }

        proposteModel.addProposta(tempProposta);
        proposteModel.cercaProposteDaChiudere(tempProposta);
    }

    public String inserimentoFogliaFormattataFromAvailable(String prompt){
        if(fattoriModel.isEmpty()) {
            view.visualizzaErroreNessunaCategoriaFoglia();
        }

        view.visualizzaListaStringheFormattate(fattoriModel.getKeysets());
        boolean esisteCategoriaRichiesta = false;
        String categoriaRichiesta;
        do {
             categoriaRichiesta = view.inserimentoFogliaFormattata(prompt);
            if (fattoriModel.existsKeyInHashmapFattori(categoriaRichiesta)) {
                esisteCategoriaRichiesta = true;
            }else {
                view.visualizzaErroreInserimentoCategoria();
            }

        }while (!esisteCategoriaRichiesta);
            return categoriaRichiesta;
    }


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

        boolean found = false;
        do {
            view.visualizzaProposte(proposteModel.getProposteModificabiliPerAutore(utenteAttivo));

            categoriaRichiesta = inserimentoFogliaFormattataFromAvailable(MSG_SELEZIONE_CATEGORIA_RICHIESTA);
            oreRichiesta = view.inserimentoOre();
            categoriaOfferta = inserimentoFogliaFormattataFromAvailable(MSG_SELEZIONE_CATEGORIA_OFFERTA);

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
