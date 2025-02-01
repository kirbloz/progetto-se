package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Utilitas;

import java.util.ArrayList;
import java.util.List;
import it.unibs.projectIngesoft.libraries.Menu;
import it.unibs.projectIngesoft.model.CategorieModel;

import static it.unibs.projectIngesoft.libraries.Utilitas.MAX_FATTORE;
import static it.unibs.projectIngesoft.libraries.Utilitas.MIN_FATTORE;

public class ConfiguratoreView implements UtenteViewableTerminal {

    public static final String TITLE_MAIN_MENU = "MENU' PRINCIPALE - SCAMBIO ORE";
    public static final String[] vociMainConfiguratore = new String[]{
            "Cambia Credenziali",
            "Menu Comprensorio ",
            "Menu Categorie",
            "Menu Fattori",
            "Menu Proposte"
    };

    public static final String TITLE_MENU_COMPRENSORIO = "MENU' COMPRENSORI GEOGRAFICI";
    public static final String[] VOCI_COMPRENSORIO_GEOGRAFICO = new String[]{
            "Aggiungi Comprensorio Geografico",
            "Visualizza Comprensorio Geografico"
    };
    public static final String TITLE_MENU_CATEGORIE = "MENU' CATEGORIE";
    public static final String[] vociCategorieConfiguratore = new String[]{
            "Aggiungi Gerarchia",
            "Visualizza Gerarchie"
    };

    public static final String TITLE_MENU_PROPOSTE = "MENU' PROPOSTE";
    public static final String[] vociProposteConfiguratore = new String[]{
            "Visualizza proposte da categoria",
            "Visualizza proposte pronte"
    };

    public static final String TITLE_MENU_FATTORI = "MENU' FATTORI";
    public static final String[] vociFattori = new String[]{
            "Visualizza Fattori di Conversione"
    };

    /// Copiato da FattoriModel
    public static final String INSERISCI_IL_FATTORE_TRA = ">> Inserisci il fattore tra [%s] e [%s]:\n> ";

    public static final String MSG_INSERISCI_NOME_FOGLIA = ">> Inserisci il nome della categoria FOGLIA:\n> ";
    public static final String MSG_INSERISCI_NOME_RADICE = ">> Inserisci il nome della categoria RADICE:\n> ";

    public static final String MSG_INSERISCI_FOGLIA_ESTERNA = ">> Inserire la foglia (da una gerarchia) ESTERNA con cui fare il confronto tra queste";
    public static final String MSG_INSERISCI_FOGLIA_INTERNA = ">> Inserire la foglia (dalla gerarchia) INTERNA con cui fare il confronto tra queste";
    public static final String MSG_INSERISCI_CATEGORIA_VISUALIZZA_FATTORI = ">> Inserisci la categoria di cui vuoi controllare i fattori di conversione";

    public static final String WARNING_CATEGORIA_NON_ESISTE = ">> (!!) La foglia richiesta non esiste o non esistono fattori relativi a questa foglia";
    public static final String WARNING_NO_FATTORI_MEMORIZZATI = ">> (!!) Non ci sono Fattori di Conversione memorizzati";
    /// fine roba da FattoriModel


    // STRINGHE PER CATEGORIE

    public static final String HEADER_VISUALIZZA_GERARCHIE = ">> Visualizza gerarchie di categorie <<\n";
    public static final String HEADER_VISUALIZZA_RADICE = ">> Visualizza gerarchia di %s <<\n";

    public static final String TITLE_SUBMENU_AGGIUNGI_GERARCHIA = "AGGIUNGI GERARCHIA";
    public static final String[] VOCI_SUBMENU_AGGIUNGI_GERARCHIA = new String[]{
            "Aggiungi Categoria",
    };


    public static final String ASK_INSERISCI_DESCRIZIONE_VALORE_DOMINIO = ">> Vuoi inserire una descrizione per questo valore?";
    public static final String MSG_INSERIMENTO_NUOVO_DOMINIO = ">> Inserisci il nome del dominio della nuova categoria:\n> ";
    public static final String MSG_INSERIMENTO_VALORE_DOMINIO = ">> Inserisci il valore di %s nel dominio di {%s}\n> ";
    public static final String MSG_INSERIMENTO_DOMINIO_PER_FIGLIE = ">> Inserisci il nome del dominio per eventuali figlie della nuova categoria:\n> ";

    public static final String ASK_CATEGORIA_IS_FOGLIA = ">> Questa Categoria è Foglia?";

    public static final String MSG_INSERIMENTO_NOME_CATEGORIA_MADRE = ">> Inserisci il nome della CATEGORIA MADRE per %s:\n> ";

    public static final String MSG_INSERIMENTO_RADICE = ">> Scegli un nome per il nuovo albero di categorie che non esista già.\n> ";
    public static final String MSG_PRINT_LISTA_RADICI = ">> Sopra trovi tutte le categorie radice.\n";

    public static final String MSG_SELEZIONE_RADICE = ">> Inserisci il nome di una categoria radice\n";
    public static final String MSG_INPUT_NOME_RADICE = ">> Inserisci il nome della categoria radice\n> ";
    public static final String MSG_INPUT_SCELTA_CAMPO = ">> Scegli un campo tra quelli delle categorie non foglia\n";

    public static final String MSG_INPUT_NUOVO_NOME_RADICE = ">> Inserisci il nome della NUOVA CATEGORIA RADICE:\n> ";
    public static final String MSG_INSERIMENTO_NUOVA_CATEGORIA = ">> Inserisci il nome della NUOVA CATEGORIA:\n> ";

    public static final String MSG_INPUT_DESCRIZIONE_VALORE_DOMINIO = ">> Inserisci la descrizione (da 0 a 100 caratteri):\n> ";
    public static final String CONFIRM_DESCRIZIONE_AGGIUNTA = ">> Descrizione aggiunta <<";

    public static final String WARNING_RADICE_ESISTE = ">> (!!) Per favore indica una categoria radice che non esiste già\n";
    public static final String WARNING_RADICE_NON_ESISTE = ">> (!!) Per favore indica una categoria radice che esiste";
    public static final String WARNING_CATEGORIA_ESISTE = ">> (!!) Per favore indica una categoria che non esista già in questo albero gerarchico.\n";
    public static final String WARNING_CATEGORIA_NF_NON_ESISTE = ">> (!!) Per favore indica una categoria non foglia dell'albero gerarchico selezionato.\n";
    public static final String WARNING_NO_GERARCHIE_MEMORIZZATE = ">> (!!) Nessuna gerarchia memorizzata.";
    private static final String MSG_RICHIESTA_USERNAME = "Inserisci un username non ancora utilizzato:";
    private static final String MSG_RICHIESTA_PASSWORD = "Inserisci la password:";
    private static final String MSG_INSERISCI_FOGLIA_TRA_QUESTE = "Inserisci una tra le seguenti Categorie:";
    public static final String MSG_INSERISCI_CATEGORIA = ">> Prego, Inserisci la categoria desiderata:";
    private static final String MSG_INPUT_NOME = "Inserisci un nome non già in uso:";


    // STRINGHE PER PROPOSTE


    // STRINGHE PER COMPRENSORI


    public void stampaMenu() {
        System.out.println("TO IMPLEMENT");
    }


    public int getUserSelection() {
        return InputDatiTerminale.leggiInteroConMinimo(">> Selezione (>0): ", 0);
    }


    public String getUserInput(String prompt) {
        return InputDatiTerminale.leggiStringaNonVuota(prompt);
    }

    @Override
    public String getUserInput() {
        return "";
    }

    public String getUserInputMinMaxLength(String prompt, int minLength, int maxLength) {
        return InputDatiTerminale.stringReaderSpecificLength(prompt, minLength, maxLength);
    }

    public boolean getUserChoiceYoN(String prompt){
        return InputDatiTerminale.yesOrNo(prompt);
    }


    public int visualizzaMenuPrincipale() {
        Menu menu = new Menu(ConfiguratoreView.TITLE_MAIN_MENU,
                vociMainConfiguratore);
        return menu.scegli();
    }



    // CATEGORIE

    public int visualizzaMenuCategorie() {
        Menu menuCategorie = new Menu(TITLE_MENU_CATEGORIE,
                vociCategorieConfiguratore);
        return menuCategorie.scegli();

    }

    public int visualizzaMenuAggiungiGerarchia() {
        Menu subMenu = new Menu(TITLE_SUBMENU_AGGIUNGI_GERARCHIA, VOCI_SUBMENU_AGGIUNGI_GERARCHIA);
        return subMenu.scegli();
    }

    public String visualizzaInserimentoNomeCategoriaRadice(CategorieModel model) {
        visualizzaListaRadici(model.getRadici());
        return getUserInput(MSG_PRINT_LISTA_RADICI+MSG_INSERIMENTO_RADICE );
    }

    public String visualizzaInserimentoCampoCategoria() {
        return getUserInput(MSG_INSERIMENTO_NUOVO_DOMINIO);
    }


    public void uscitaMenu(String menu) {
        switch (menu) {
            case "aggiungiGerarchia":
                System.out.println(">> USCITA ZIOPERA");
            case "programma":
                System.out.println(">> USCITA PROGRAMA");
            case "submenu":
                System.out.println(">> USCITA submenu");
        }
    }


    /**
     * Visualizza una stringa di descrizione delle categorie radici,
     * senza eventuali figlie.
     *
     * @param radici lista radici.
     */
    public void visualizzaListaRadici(List<Categoria> radici) {
        System.out.println(HEADER_VISUALIZZA_GERARCHIE);
        if (radici == null || radici.isEmpty()){
            System.out.println(">> Nessuna radice presente.");
            return;
        }

        for (Categoria radice : radici) {
            System.out.println("> RADICE");
            visualizzaCategoria(radice);
        }
        System.out.println();
    }


    public void visualizzaCategoria(Categoria categoria) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ").append(categoria.getNome()).append(" ]\n");

        if (!categoria.isRadice()) {  // se non è una radice, allora si stampano i dati della categoria madre
            sb.append("Madre: ").append(categoria.getNomeMadre())
                    .append("\n");
            sb.append("Dominio: ").append(categoria.getCampo())
                    .append(" = ").append(categoria.getValoreDominio()).
                    append("\n");
        }
        if (!categoria.isFoglia()) { // se non è foglia, si stampa il dominio impresso alle figlie
            sb.append("Dominio Figlie: ").append(categoria.getCampoFiglie())
                    .append("\n");
        } else {
            sb.append("> Foglia\n");
        }
        System.out.println(sb);
        System.out.println();
    }

    public void visualizzaFiglieCategoria(Categoria categoria) {
        if (!categoria.hasFiglie()) {
            System.out.println("\t﹂ Nessuna figlia.");
            return;
        }

        List<Categoria> figlie = categoria.getCategorieFiglie();

        figlie.forEach(figlia -> {
            System.out.println();
            visualizzaCategoria(figlia);
        });

        System.out.println();
    }


    public double visualizzaInserimentoFdCInternoEsterno(String nomeFogliaEsternaFormattata, String nomeFogliaInternaFormattata){
        return InputDatiTerminale.leggiDoubleConRange(INSERISCI_IL_FATTORE_TRA.formatted(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata), MIN_FATTORE, MAX_FATTORE);
    }

/// ///////INSERIMENTO PER CASI D'USO CATEGORIE //////
    /**
     * Guida l'input del nome di una nuova Categoria per una gerarchia.
     *
     * @param tempRadice, radice della gerarchia a cui aggiungere la nuova Categoria
     * @return nome della nuova Categoria
     */
    public String inserimentoNomeNuovaCategoria(CategorieModel model, Categoria radice) {
        assert radice != null : "radice non può essere null";
        //assert this.esisteRadice(tempRadice) : "tempRadice non è il nome di una radice";
        // questo è garantito prima di entrare nel metodo

        String tempNome;
        do {
            visualizzaCategoria(radice); // stampa la gerarchia
            tempNome = InputDatiTerminale.leggiStringaNonVuota(MSG_INSERIMENTO_NUOVA_CATEGORIA);
            if (model.esisteCategoriaNellaGerarchia(tempNome, radice.getNome())) {
                System.out.print(WARNING_CATEGORIA_ESISTE);
            }
        } while (model.esisteCategoriaNellaGerarchia(tempNome, radice.getNome()));
        return tempNome;
    }

    /**
     * Guida l'immissione del nome della Categoria a cui assegnare nomeCategoria come figlia.
     * Controlla che si inserisca il nome di una Categoria che può avere figlie.
     *
     * @param nomeCategoriaRadice, Categoria radice della gerarchia di riferimento
     * @param nomeCategoria,       Categoria figlia da assegnare alla madre
     * @return Categoria a cui la Categoria figlia sarà assegnata
     */
    public String inserimentoNomeCategoriaMadre(String nomeCategoria, List<String> possibiliMadri) {
        String nomeMadre;
        do{
            nomeMadre = getUserInput(String.format(MSG_INSERIMENTO_NOME_CATEGORIA_MADRE, nomeCategoria));
            if(!possibiliMadri.contains(nomeMadre))
                System.out.println(WARNING_CATEGORIA_NF_NON_ESISTE);
        }while (!possibiliMadri.contains(nomeMadre));

        return nomeMadre;
    }

    /**
     * Guida l'immissione di una stringa imponendo un vincolo di univocit&agrave; tra i valori del dominio che le Categorie "sorelle"
     * assumono.
     *
     * @param tempNome,       Categoria a cui assegnare il nome del ValoreDominio
     * @param categoriaMadre, Categoria madre da cui si eredita il dominio e da cui ottenere i valori delle "sorelle"
     * @return nome del ValoreDominio
     */
    public String inserimentoValoreDominio(String tempNome, Categoria categoriaMadre) {
        String nomeValore;
        List<String> valoriSorelle = categoriaMadre.getValoriDominioFiglie();
        do {
            nomeValore = getUserInput(String.format(MSG_INSERIMENTO_VALORE_DOMINIO, tempNome, categoriaMadre.getCampoFiglie()));
        } while (valoriSorelle.contains(nomeValore));
        return nomeValore;
    }



    /////////////////////////////////////////////// VIEW per i fattori /////////////////////////////////////////////////

    public void stampaOpzioni(String[] opzioni) {
        for (String opt : opzioni) {
            System.out.println(opt);
        }
    }

    // TODO rivedere

    /**
     * Permette di scegliere una categoria foglia tra quelle appena create.
     * Guida l'immissione del nome e radice della categoria.
     *
     * @param nomeRadice, nome della radice delle foglie
     * @param foglie,     lista di foglie appena create
     * @return stringa formattata come "radice:foglia"
     */
	public String selezioneFogliaDaLista(String nomeRadice, List<Categoria> foglie) {
        // stampa categorie interne (foglie della nuova radice)
        String[] foglieArray = new String[foglie.size()];
        for (int i = 0; i < foglie.size(); i++) {
            foglieArray[i] = Utilitas.factorNameBuilder(nomeRadice, foglie.get(i).getNome());
        }
        return selezioneFogliaDaLista(foglieArray);
    }

    public String selezioneFogliaDaLista(String[] foglie) {
        // stampa categorie interne (foglie della nuova radice)
        System.out.println(MSG_INSERISCI_FOGLIA_TRA_QUESTE);
        for (String foglia : foglie) {
            System.out.println(foglia);
        }
        // immissione della foglia e verifica che sia corretto [New:A in (Old:A New:A x)]
        String fogliaSelezionata;
        boolean fogliaEsiste = false;
        do {
            fogliaSelezionata = inserimentoFogliaFormattata(MSG_INSERISCI_CATEGORIA);
            for (String foglia : foglie) {
                if (foglia.equals(fogliaSelezionata)) {
                    fogliaEsiste = true;
                    break;
                }
            }
        } while (!fogliaEsiste);

        return fogliaSelezionata;
    }

    /**
     * Permette di scegliere una categoria foglia, chiedendo prima una radice e poi una foglia
     *
     * @return stringa formattata come "radice:foglia"
     */
    public String inserimentoFogliaFormattata(String messaggio) {
        // inserimento guidato e controllo [Old:A in (Old:A New:A x)]
        //return inserimentoNomeFogliaFormattato(messaggio);
        System.out.println(messaggio);
		return Utilitas.factorNameBuilder(
				InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_RADICE),
				InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_FOGLIA)
		);
    }

    public double ottieniFattoreDiConversione(String nomeFogliaEsternaFormattata, String nomeFogliaInternaFormattata) {
        return InputDatiTerminale.leggiDoubleConRange(INSERISCI_IL_FATTORE_TRA.formatted(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata), Utilitas.MIN_FATTORE, Utilitas.MAX_FATTORE);
    }

    public String richiestaUsername() {
        String username = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        return username;
    }

    public String richiestaPassword() {
        String username = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);
        return username;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////// Comprensorio View /////////////////////////////////////////////////

    public int visualizzaMenuComprensorio() {
        Menu menuCategorie = new Menu(TITLE_MENU_COMPRENSORIO, VOCI_COMPRENSORIO_GEOGRAFICO);
        return menuCategorie.scegli();
    }

    public String selezionaNomeDaLista(List<String> lista){
        System.out.println("Scegli uno tra i seguenti nomi:");
        for (String nome : lista) {
            System.out.println(nome);
        }
        // immissione della foglia e verifica che sia corretto [New:A in (Old:A New:A x)]
        String nomeInserito;
        boolean esisteNome = false;
        do {
            nomeInserito = InputDatiTerminale.leggiStringaNonVuota(MSG_INPUT_NOME);
            for (String nome : lista) {
                if (nome.equals(nomeInserito)) {
                    esisteNome = true;
                    break;
                }
            }
        } while (!esisteNome);

        return nomeInserito;
    }

    public String selezionaNonGiaInUso(List<String> array) {
        // immissione della foglia e verifica che sia corretto [New:A in (Old:A New:A x)]
        String scelto;
        boolean giaInUso = false;
        do {
            scelto = InputDatiTerminale.leggiStringaNonVuota(MSG_INPUT_NOME);
            for (String s : array) {
                if (s.equalsIgnoreCase(scelto)) {
                    giaInUso = true;
                    break;
                }
            }
        } while (giaInUso);

        return scelto;
    }

    public List<String> inserimentoComuni() {
        return InputDatiTerminale.inserisciListaStringheUnivoche("Inserisci Comune", true);
    }

    /**
     * visualizza un comprensorio
     * @param nome
     * @param comuni
     */
    public void visualizzaComprensorio(String nome, List<String> comuni) {
        System.out.println(nome.toUpperCase());
        for (String s : comuni) {
            System.out.println(s);
        }
    }

    public void stampaErroreComprensoriVuoto() {
        System.out.println("Errore: Non esistono Comprensori da Stampare");
    }
}
