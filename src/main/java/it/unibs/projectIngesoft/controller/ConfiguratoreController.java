package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.model.*;
import it.unibs.projectIngesoft.view.ConfiguratoreView;

public class ConfiguratoreController {


    private ConfiguratoreView view;

    private CategorieModel categorieModel;
    private FattoriModel fattoriModel;
    private ProposteModel proposteModel;
    private ComprensorioGeograficoModel compGeoModel;
    private UtentiModel utentiModel;

    public ConfiguratoreController(ConfiguratoreView view,
                                   CategorieModel categorieModel,
                                   FattoriModel fattoriModel,
                                   ProposteModel proposteModel,
                                   ComprensorioGeograficoModel compGeoModel,
                                   UtentiModel utentiModel) {
        this.view = view;
        this.categorieModel = categorieModel;
        this.fattoriModel = fattoriModel;
        this.proposteModel = proposteModel;
        this.compGeoModel = compGeoModel;
        this.utentiModel = utentiModel;
    }

    /**
     * Richiama il metodo necessario in base alla selezione dal menu principale.
     */
    public void entryPoint() {
        int scelta = 0;
        view.visualizzaMenuCategorie();
        scelta = view.getUserSelection();
        switch (scelta) {
            case 1 -> subMenuAggiungiGerarchia();
            case 2 -> visualizzaGerarchie();
        }
    }


    /**
     * Metodo per la creazione di un'intera gerarchia.
     * Guida l'inserimento di una radice ed eventuali Categorie, con tutte le loro informazioni.
     * Terminato l'inserimento, salva su file XML i dati.
     */
    public void subMenuAggiungiGerarchia() {
        //int scelta = 0;

        // Menu subMenu = new Menu(TITLE_SUBMENU_AGGIUNGI_GERARCHIA, VOCI_SUBMENU_AGGIUNGI_GERARCHIA);

        // 0. predispone una radice e la salva localmente
        /*model.aggiungiCategoriaRadice();
        Categoria radice = this.tree.getRadici().getLast();*/

        // 1. loop per l'inserimento di categorie nella gerarchia della radice appena creata
        /*int scelta;
        do {
            //scelta = subMenu.scegli();
            scelta = ((ConfiguratoreView) view).visualizzaMenuAggiungiGerarchia();
            switch (scelta) { // switch con un solo case per ampliamento futuro
                case 1 -> model.aggiungiCategoria(radice);
                default -> view.uscitaMenu("aggiungiGerarchia");
            }
        } while (scelta != 0);*/

        //1.5 imposto a foglie tutte le categorie che non hanno figlie
        //impostaCategorieFoglia(radice);

        // 2. procedura per i fattori
        //List<Categoria> foglie = tree.getFoglie(radice.getNome());
        //gestFatt.inserisciFattoriDiConversione(radice.getNome(), foglie);

        //3. salvataggio dei dati
        //mapper.write(getRadici());
        //serializeXML();
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
