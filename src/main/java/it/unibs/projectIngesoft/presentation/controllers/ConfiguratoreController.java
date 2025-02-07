package it.unibs.projectIngesoft.presentation.controllers;

import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.FattoreDiConversione;
import it.unibs.projectIngesoft.core.domain.entities.Proposta;
import it.unibs.projectIngesoft.core.domain.entities.ValoreDominio;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Fruitore;
import it.unibs.projectIngesoft.core.domain.model.*;
import it.unibs.projectIngesoft.libraries.Utilitas;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Configuratore;
import it.unibs.projectIngesoft.presentation.view.ConfiguratoreView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConfiguratoreController extends UtenteController<Configuratore> {

    public static final String MSG_INPUT_DESCRIZIONE_VALORE_DOMINIO = ">> Inserisci la descrizione (da 0 a 100 caratteri):\n> ";
    public static final String CONFIRM_DESCRIZIONE_AGGIUNTA = ">> Descrizione aggiunta <<";
    public static final String ASK_INSERISCI_DESCRIZIONE_VALORE_DOMINIO = ">> Vuoi inserire una descrizione per questo valore?";
    public static final String MSG_INSERIMENTO_DOMINIO_PER_FIGLIE = ">> Inserisci il nome del dominio per eventuali figlie della nuova categoria:\n> ";

    public static final String ASK_CATEGORIA_IS_FOGLIA = ">> Questa Categoria è Foglia?";


    private final ConfiguratoreView view;

    public ConfiguratoreController(ConfiguratoreView view,
                                   CategorieModel categorieModel,
                                   FattoriModel fattoriModel,
                                   ProposteModel proposteModel,
                                   ComprensorioGeograficoModel compGeoModel,
                                   UtentiModel utentiModel, Configuratore utenteAttivo) {
        super(categorieModel, fattoriModel, proposteModel, compGeoModel, utentiModel, utenteAttivo);
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

    protected void cambioCredenziali() {
        String username;
        do {
            username = view.richiestaUsername();
        } while (utentiModel.existsUsername(username));
        String password = view.richiestaPassword();
        utentiModel.cambioCredenziali(utenteAttivo, username, password);
    }

    /////////////////////////////// FATTORI /////////////////////////////////////////////////

    /**
     * Cicla le foglie dalla prima all'ultima per generare tutte le coppie di valori possibili
     * (combinazione senza ripetizione) e per ogni coppia generata chiede il fattore all'utente.
     * Genera il FattoreDiConversione + il suo inverso e li memorizza.
     *
     * @param nomeRadice, nome della radice delle foglie
     * @param foglie,     lista di foglie
     * @return lista di fattori di conversione
     */
    private List<FattoreDiConversione> ottieniFattoriDelleNuoveCategorie(String nomeRadice, List<Categoria> foglie) {
        // se esiste almeno una foglia, allora calcola i fattori di conversione
        List<FattoreDiConversione> nuoviDaNuovaRadice = new ArrayList<>();
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

    public void generaEMemorizzaNuoviFattori(String nomeRadice, List<Categoria> foglie) {
        if (foglie.isEmpty()) return;
        List<FattoreDiConversione> nuoviFattoriTraTutteLeFoglieDellaNuovaRadice = ottieniFattoriDelleNuoveCategorie(nomeRadice, foglie);

        if (!fattoriModel.isEmpty()) {
            String nomeFogliaEsternaFormattata = view.selezioneFogliaDaLista(fattoriModel.getKeysets());
            String nomeFogliaInternaFormattata = view.selezioneFogliaDaLista(nomeRadice, foglie);
            double fattoreDiConversioneTraEsternaEInterna = view.ottieniFattoreDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata);
            fattoriModel.calcolaEInserisciFattoriDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneTraEsternaEInterna, nuoviFattoriTraTutteLeFoglieDellaNuovaRadice);
        }else if (!nuoviFattoriTraTutteLeFoglieDellaNuovaRadice.isEmpty()) {
            nuoviFattoriTraTutteLeFoglieDellaNuovaRadice.addAll(fattoriModel.calcolaInversi(nuoviFattoriTraTutteLeFoglieDellaNuovaRadice));
            fattoriModel.aggiungiListDiFattori(nuoviFattoriTraTutteLeFoglieDellaNuovaRadice);
        } else {
            fattoriModel.inserisciSingolaFogliaNellaHashmap(nomeRadice, foglie);
        }
    }

    public void visualizzaFattori() {
        String categoriaFormattata = view.selezioneFogliaDaLista(fattoriModel.getKeysets());
        if (!fattoriModel.esisteCategoria(categoriaFormattata)) {
            view.visualizzaErroreCategoriaNonEsiste();
            return;
        }
        if (!fattoriModel.esistonoFattoriPerCategoria(categoriaFormattata)) {
            view.visualizzaErroreNoFattoriMemorizzati();
            return;
        }
        view.visualizzaFattori(fattoriModel.getFattoriFromFoglia(categoriaFormattata));
    }

    /// /////////////////////////////////////////////// CATEGORIA ////////////////////////////////////////////////////

    public void aggiungiRadice() {
        String nomeRadice;
        boolean valid;
        do {
            nomeRadice = view.visualizzaInserimentoNomeCategoriaRadice(categorieModel);
            valid = !nomeRadice.contains(":");
        } while (categorieModel.esisteRadice(nomeRadice) || !valid);
        String nomeCampo = view.visualizzaInserimentoCampoCategoria();
        categorieModel.aggiungiCategoriaRadice(nomeRadice, nomeCampo);
    }


    public void aggiungiGerarchia() {
        this.aggiungiRadice();
        Categoria radice = categorieModel.getRadici().getLast();

        int scelta;
        do {
            scelta = view.visualizzaMenuAggiungiGerarchia();
            switch (scelta) { // switch con un solo case per ampliamento futuro
                case 1 -> this.aggiungiCategoria(radice);
                default -> view.uscitaMenu("submenu");
            }
        } while (scelta != 0);

        radice.impostaCategorieFoglia();
        generaEMemorizzaNuoviFattori(radice.getNome(), radice.getFoglie());
        categorieModel.save();
    }


    private void aggiungiCategoria(Categoria radice) {
        String nomeCategoria = view.inserimentoNomeNuovaCategoria(categorieModel, radice);

        String nomeCategoriaMadre = view.inserimentoNomeCategoriaMadre(nomeCategoria,
                categorieModel.getListaNomiCategorieGerarchiaFiltrata(radice, Categoria::isNotFoglia));
        Categoria categoriaMadre = radice.cercaCategoria(nomeCategoriaMadre);

        String nomeValoreDominio = view.inserimentoValoreDominio(nomeCategoria, categoriaMadre);
        ValoreDominio valoreDominio = creaValoreDominio(nomeValoreDominio);

        boolean isFoglia = view.getUserChoiceYoN(ASK_CATEGORIA_IS_FOGLIA);
        String nomeCampoFiglie = isFoglia ?
                "" : view.getUserInput(MSG_INSERIMENTO_DOMINIO_PER_FIGLIE);

        categoriaMadre.aggiungiCategoriaFiglia(creaCategoriaNonRadice(nomeCategoria, categoriaMadre, valoreDominio, nomeCampoFiglie, isFoglia));
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


    /// /////////////////////////////////////////////// COMPRENSORIO ////////////////////////////////////////////////////

    public void aggiungiComprensorio() {
        String nomeComprensorio = view.inserimentoNomeComprensorio(compGeoModel.getListaNomiComprensoriGeografici().toArray(String[]::new));
        List<String> comuniDaInserire = view.inserimentoComuni();
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

        Map<Fruitore, List<Proposta>> mapDaNotificare = new HashMap<>();


        Map<String, List<Proposta>> propostePerAutore = proposteModel.getFilteredProposte(Proposta::isDaNotificare)
                .collect(Collectors.groupingBy(Proposta::getAutoreUsername));

        for (Map.Entry<String, List<Proposta>> entry : propostePerAutore.entrySet()) {
            String autore = entry.getKey();
            List<Proposta> listaPropostaPerNome = entry.getValue();

            utentiModel.getFruitoreDaUsername(autore).ifPresent(fruitore ->
                    mapDaNotificare.put(fruitore, listaPropostaPerNome)
            );
        }

        view.visualizzaProposteDaNotificare(mapDaNotificare);
        proposteModel.save();
    }


}
