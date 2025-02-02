package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.model.*;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.view.AccessoView;
import it.unibs.projectIngesoft.view.FruitoreView;

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
                case 1 -> view.visualizzaProposte();
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
        /*System.out.println(HEADER_ESPLORA_GERARCHIE);

        if (tree.getRadici().isEmpty()) {
            System.out.println(WARNING_NO_GERARCHIE_MEMORIZZATE);
            return;
        }
        Menu subMenu = new Menu(TITLE_SUBMENU_ESPLORA_GERARCHIA, VOCI_SUBMENU_ESPLORA_GERARCHIA);
        int scelta;

        // 0. seleziono la radice della gerarchia da esplorare
        Categoria radice = tree.getRadice(selezioneNomeCategoriaRadice());
        Categoria madreCorrente = radice; // categoria madre del livello che si sta visualizzando al momento
        List<Categoria> livello = madreCorrente.getCategorieFiglie();

        do {
            // 1. print del livello corrente
            visualizzaLivello(madreCorrente.getCampoFiglie(), livello);
            // 2. print menu
            Categoria nuovaMadre = madreCorrente;
            scelta = subMenu.scegli();
            switch (scelta) {
                case 1 -> { // esplora
                    String nuovoCampo = selezionaValoreCampo(livello);
                    nuovaMadre = nuovoCampo == null ?
                            madreCorrente : selezionaCategoriaDaValoreCampo(nuovoCampo, livello);
                }
                // torna indietro di un livello
                case 2 -> nuovaMadre = madreCorrente.isRadice() ?
                        madreCorrente : radice.cercaCategoria(madreCorrente.getNomeMadre());
                default -> System.out.println(MSG_USCITA_SUBMENU);
            }
            // aggiorno i valori
            madreCorrente = nuovaMadre == null ? madreCorrente : nuovaMadre;
            livello = madreCorrente.getCategorieFiglie();
        } while (scelta != 0);*/
    }





    ///////////////////////// PROPOSTE //////////////////////////
    /**
     * todo da implementare
     * Codice duplicato da ProposteModel
     * Guida la creazione di una nuova proposta di scambio di prestazioni d'opera
     */
    public void effettuaProposta() {
        String categoriaRichiesta;
        String categoriaOfferta;
        int oreRichiesta;
        int oreOfferta;

        // 1. inserimento categoria richiesta, ore, e categoria offerta
        ///categoriaRichiesta = gestFatt.selezioneFoglia(MSG_INSERISCI_RICHIESTA);
        //oreRichiesta = InputDatiTerminale.leggiInteroPositivo(MSG_RICHIESTA_ORE);
        //categoriaOfferta = gestFatt.selezioneFoglia(MSG_INSERISCI_OFFERTA);

        // 2. calcolo ore per l'offerta
        //oreOfferta = gestFatt.calcolaRapportoOre(categoriaRichiesta, categoriaOfferta, oreRichiesta);
       /* if (oreOfferta == -1) {
            System.out.println(WARNING_IMPOSSIBILE_CALCOLARE_ORE + WARNING_PROPOSTA_ANNULLATA);
            return;
        }*/
        /*Proposta tempProposta = new Proposta(categoriaRichiesta, categoriaOfferta, oreRichiesta, oreOfferta, (Fruitore) utenteAttivo);

        // 3. conferma e memorizza la proposta
        if (!InputDatiTerminale.yesOrNo("\n" + tempProposta + "\n" + MSG_CONFERMA_PROPOSTA.formatted(oreOfferta))) {
            System.out.println(WARNING_PROPOSTA_ANNULLATA);
            return;
        }

        // 3.1 se confermi ma è duplicata, segnala e non aggiunge
        if (controllaPropostaDuplicata(tempProposta)) {
            System.out.println(WARNING_PROPOSTA_DUPLICATA);
            return;
        }
        addProposta(tempProposta);
        cercaProposteDaChiudere(tempProposta);*/
    }

    /**
     * todo da implementare/rifattorizzare
     * Codice duplicato da ProposteModel
     * L'autore di una proposta può cambiare il suo stato tra RITIRATA e APERTA.
     * Guida la selezione della proposta.
     * L'autore è sempre un Fruitore.
     */
    private void cambiaStatoProposta() {
        /*assert utenteAttivo instanceof Fruitore;
        String comprensorio = ((Fruitore) utenteAttivo).getComprensorioDiAppartenenza();

        boolean esisteAlmenoUnaPropostaPerLUtenteLoggatoOra = false;
        if (hashListaProposte != null && hashListaProposte.get(comprensorio) != null) {
            for (Proposta proposta : hashListaProposte.get(comprensorio)) {
                if (proposta.getStato() != StatiProposta.CHIUSA && proposta.getAutoreUsername().equals(utenteAttivo.getUsername())) {
                    esisteAlmenoUnaPropostaPerLUtenteLoggatoOra = true;
                    break;
                }
            }
        }

        if (!esisteAlmenoUnaPropostaPerLUtenteLoggatoOra) {
            System.out.println(MSG_NON_HAI_PROPOSTE_NON_CHIUSE);
            return;
        }

        String categoriaRichiesta;
        String categoriaOfferta;
        int oreRichiesta;
        Proposta daCambiare = null;

        // 1. inserimento categoria richiesta, ore, e categoria offerta
        boolean found = false;
        do {
            visualizzaProposteModificabili();
            //categoriaRichiesta = gestFatt.inserimentoNomeFogliaFormattato(MSG_SELEZIONE_CATEGORIA_RICHIESTA);
            oreRichiesta = InputDatiTerminale.leggiInteroPositivo(MSG_SELEZIONE_ORE);
            //categoriaOfferta = gestFatt.inserimentoNomeFogliaFormattato(MSG_SELEZIONE_CATEGORIA_OFFERTA);

            //daCambiare = cercaProposta(comprensorio, categoriaOfferta, categoriaRichiesta, oreRichiesta);

            if (daCambiare != null && daCambiare.getAutoreUsername().equals(utenteAttivo.getUsername())) {
                found = daCambiare.getStato() != StatiProposta.CHIUSA;
            }
        } while (!found);

        // 2. cambio stato guidato e conferma
        StatiProposta statoAttuale = daCambiare.getStato();
        StatiProposta statoNuovo = (statoAttuale == StatiProposta.APERTA) ? StatiProposta.RITIRATA : StatiProposta.APERTA;

        if (!InputDatiTerminale.yesOrNo(MSG_CONFERMA_CAMBIO_STATO.formatted(statoAttuale, statoNuovo)))
            return; // non conferma
        if (statoAttuale == StatiProposta.RITIRATA) {
            daCambiare.setAperta();
            cercaProposteDaChiudere(daCambiare);
        } else {
            daCambiare.setRitirata();
        }
        System.out.println(MSG_STATO_MODIFICATO.formatted(statoNuovo));

        mapper.write(new HashMap<>(hashListaProposte));*/
    }


}
