package it.unibs.projectIngesoft.gestori;


import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.Albero;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
import it.unibs.projectIngesoft.libraries.InputDati;
import it.unibs.projectIngesoft.libraries.Menu;
import it.unibs.projectIngesoft.libraries.Serializer;

import java.util.List;

public class GestoreCategorie {

    public static final String HEADER_VISUALIZZA_GERARCHIE = ">> Visualizza gerarchie di categorie <<\n";
    public static final String HEADER_VISUALIZZA_RADICE = ">> Visualizza gerarchia di %s <<\n";

    public static final String MSG_INSERIMENTO_NUOVO_DOMINIO = ">> Inserisci il nome del dominio della nuova categoria:\n> ";
    public static final String MSG_INSERIMENTO_VALORE_DOMINIO = ">> Inserisci il valore di %s nel dominio di {%s}\n> ";
    public static final String MSG_INSERIMENTO_DOMINIO_PER_FIGLIE = ">> Inserisci il nome del dominio per eventuali figlie della nuova categoria:\n> ";
    public static final String[] VOCI_SUBMENU_GERARCHIA = new String[]{
            "Aggiungi Categoria",
    };
    public static final String TITLE_SUBMENU_AGGIUNGI_GERARCHIA = "AGGIUNGI GERARCHIA";

    public static final String MSG_INSERIMENTO_NOME_CATEGORIA_MADRE = ">> Inserisci il nome della CATEGORIA MADRE per %s:\n> ";

    public static final String MSG_INSERIMENTO_RADICE = ">> Scegli un nome per il nuovo albero di categorie che non esista già.\n";
    public static final String MSG_PRINT_LISTA_RADICI = ">> Di seguito tutte le categorie radice.\n";

    public static final String MSG_INPUT_NUOVO_NOME_RADICE = ">> Inserisci il nome della NUOVA CATEGORIA RADICE:\n> ";
    public static final String MSG_INSERIMENTO_NUOVA_CATEGORIA = ">> Inserisci il nome della NUOVA CATEGORIA:\n> ";

    public static final String MSG_INPUT_DESCRIZIONE_VALORE_DOMINIO = ">> Inserisci la descrizione (da 0 a 100 caratteri):\n> ";
    public static final String CONFIRM_DESCRIZIONE_AGGIUNTA = ">> Descrizione aggiunta <<";

    public static final String WARNING_RADICE_ESISTE = ">> (!!) Per favore indica una categoria radice che non esiste già\n";
    public static final String WARNING_CATEGORIA_ESISTE = ">> (!!) Per favore indica una categoria che non esista già in questo albero gerarchico.\n";
    public static final String WARNING_CATEGORIA_NF_NON_ESISTE = ">> (!!) Per favore indica una categoria non foglia dell'albero gerarchico selezionato.\n";

    public static final boolean DEBUG_DATA = false;


    // non sono sicuro di quale struttura dati utilizzare
    private Albero tree;
    private final String filePath;
    private GestoreFattori gestFatt;


    public GestoreCategorie(String categorieFilePath, String fattoriFilePath) {

        this.filePath = categorieFilePath;
        this.tree = new Albero();
        this.gestFatt = new GestoreFattori(fattoriFilePath);

        //deserializeXML(); // load dati

        if (DEBUG_DATA) {

            Categoria radice1 = new Categoria("cat1 radice", "materia");
            Categoria radice2 = new Categoria("cat2 radice", "lezione");

            tree.getRadici().add(radice1);
            tree.getRadici().add(radice2);
            radice1.addCategoriaFiglia(new Categoria("figlia1", radice1, new ValoreDominio("matematica")));
            radice1.addCategoriaFiglia(new Categoria("figlia2", radice1, new ValoreDominio("italiano")));
            radice2.addCategoriaFiglia(new Categoria("figlia3", radice2, new ValoreDominio("online")));
            radice2.addCategoriaFiglia(new Categoria("figliaNF1", "gradoScuola", radice2, new ValoreDominio("in presenza")));


            serializeXML();
        } else {
            deserializeXML();
        }

        //serializeXML();
    }


    /**
     * Sfrutto l'implementazione statica della classe Serializer.
     * Questo metodo esiste da prima di Serializer, per non cambiare ogni call a questo ho semplicemente sostituito il corpo.
     */
    public void serializeXML() {
        assert this.tree != null;
        assert this.filePath != null;

        Serializer.serialize(this.filePath, this.tree);
    }

    /**
     * Sfrutto l'implementazione statica della classe Serializer.
     * Questo metodo esiste da prima di Serializer, per non cambiare ogni call a questo ho semplicemente sostituito il corpo.
     */
    public void deserializeXML() {
        assert this.tree != null;

        List<Categoria> tempCat = Serializer.deserialize(new TypeReference<>() {
        }, this.filePath);

        tree.getRadici().clear();
        if (tempCat != null)
            tree.getRadici().addAll(tempCat);

    }


    /**
     * Richiama il metodo necessario in base alla selezione dal menu principale.
     *
     * @param scelta, selezione dal menu
     */
    public void entryPoint(int scelta) {

        switch (scelta) {
            case 1:
                subMenuAggiungiGerarchia();
                break;
            case 2:
                visualizzaGerarchie();
                break;
            default:
                break;
        }
    }

    /**
     * Metodo per la creazione di un'intera gerarchia.
     * Guida l'inserimento di una radice ed eventuali Categorie, con tutte le loro informazioni.
     * Terminato l'inserimento, salva su file XML i dati.
     */
    public void subMenuAggiungiGerarchia() {

        Menu subMenu = new Menu(TITLE_SUBMENU_AGGIUNGI_GERARCHIA, VOCI_SUBMENU_GERARCHIA);
        int scelta;

        // 0. predispone una radice
        this.aggiungiCategoriaRadice();
        Categoria radice = this.tree.getRadici().getLast();

        // 1. loop per l'inserimento di categorie nella gerarchia della radice appena creata
        do {
            scelta = subMenu.scegli();
            // switch ad un solo case per ampliamento futuro
            switch (scelta) {
                case 1:
                    this.aggiungiCategoria(radice);
                    break;
                default:
                    System.out.println("> Uscita dal submenu e salvataggio.. ");
                    break;
            }
        } while (scelta != 0);

        if (radice.getNumCategorieFiglie() == 0)
            radice.setFoglia();

        // 2. procedura per i fattori TODO
        List<Categoria> foglie = tree.getFoglie(radice.getNome());
        gestFatt.inserisciFattoriDiConversione(radice.getNome(), foglie);

        //3. salvataggio dei dati
        serializeXML();
    }

    /**
     * Metodo per l'inserimento di una Categoria generica NON RADICE.
     * Non modificabile.
     *
     * @param radice, Categoria radice di riferimento
     */
    private void aggiungiCategoria(Categoria radice) {
        assert radice != null : "la radice non deve essere null";

        String descrizioneValoreDominio = "";
        String nomeCampoFiglie = "";
        ValoreDominio valoreDominio;

        // 1. chiede nome
        String nomeCategoria = this.inserimentoNomeNuovaCategoria(radice.getNome());
        assert nomeCategoria != null && !nomeCategoria.isEmpty() : "Il nome della categoria non deve essere null o vuoto";
        // 1.1 chiede madre per nuova radice, verificando che esista
        Categoria categoriaMadre = this.inserimentoNomeCategoriaMadre(radice.getNome(), nomeCategoria);
        assert categoriaMadre != null : "La madre della categoria non deve essere null";

        // 2. chiede valore del dominio ereditato + descrizione
        String nomeValoreDominio = this.inserimentoValoreDominio(nomeCategoria, categoriaMadre);
        assert nomeValoreDominio != null && !nomeValoreDominio.isEmpty() : "Il nome del valore del dominio non deve essere null o vuoto";

        boolean insertDescription = InputDati.yesOrNo(">> Vuoi inserire una descrizione per questo valore?");
        if (insertDescription) {
            descrizioneValoreDominio = InputDati.stringReaderSpecificLength(MSG_INPUT_DESCRIZIONE_VALORE_DOMINIO, 0, 100);
            assert descrizioneValoreDominio != null : "La descrizione del valore del dominio non deve essere null";
            valoreDominio = new ValoreDominio(nomeValoreDominio, descrizioneValoreDominio);
            System.out.println(CONFIRM_DESCRIZIONE_AGGIUNTA);
        } else {
            valoreDominio = new ValoreDominio(nomeValoreDominio);
        }

        // 3. chiede se è foglia
        boolean isFoglia = InputDati.yesOrNo(">> Questa Categoria è Foglia?");

        // 3.1 se non è foglia, inserisce il dominio che imprime alle figlie
        if (!isFoglia) {
            nomeCampoFiglie = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_DOMINIO_PER_FIGLIE);
            assert nomeCampoFiglie != null && !nomeCampoFiglie.isEmpty() : "Il nome del campo delle figlie non deve essere null o vuoto";
        }

        // 4. creazione dell'oggetto Categoria
        Categoria tempCategoria = isFoglia
                ? new Categoria(nomeCategoria, categoriaMadre, valoreDominio)
                : new Categoria(nomeCategoria, nomeCampoFiglie, categoriaMadre, valoreDominio);

        // 5. aggiunta della categoria figlia alla madre
        categoriaMadre.addCategoriaFiglia(tempCategoria);
        assert categoriaMadre.getCategorieFiglie().contains(tempCategoria) : "La categoria madre deve contenere la nuova categoria figlia";

    }


    private String inserimentoNomeCategoriaRadice() {
        assert this.tree != null;
        assert this.tree.getRadici() != null;

        String tempNomeRadice;
        do {
            System.out.println(MSG_INSERIMENTO_RADICE + MSG_PRINT_LISTA_RADICI + radiciToString());
            tempNomeRadice = InputDati.leggiStringaNonVuota(MSG_INPUT_NUOVO_NOME_RADICE);
            if (this.esisteRadice(tempNomeRadice)) {
                System.out.println(WARNING_RADICE_ESISTE);
            }
        } while (this.esisteRadice(tempNomeRadice));
        return tempNomeRadice;

    }

    /**
     * Guida l'input del nome di una nuova Categoria per una gerarchia.
     *
     * @param tempRadice, radice della gerarchia a cui aggiungere la nuova Categoria
     * @return nome della nuova Categoria
     */
    private String inserimentoNomeNuovaCategoria(String tempRadice) {
        assert tempRadice != null : "il nome della radice non può essere null";
        assert this.esisteRadice(tempRadice) : "tempRadice non è il nome di una radice";

        String tempNome;
        do {
            visualizzaGerarchia(tempRadice); // stampa la gerarchia
            tempNome = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_NUOVA_CATEGORIA);
            if (esisteCategoriaNellaGerarchia(tempNome, tempRadice)) {
                System.out.print(WARNING_CATEGORIA_ESISTE);
            }
        } while (esisteCategoriaNellaGerarchia(tempNome, tempRadice));
        return tempNome;
    }

    /**
     * Verifica che la stringa passata non sia già il nome di un'altra Categoria della stessa gerarchia
     *
     * @param tempNome    nome da controllare
     * @param tempRadice, radice della gerarchia
     * @return true se esiste una Categoria con quel nome
     */
    private boolean esisteCategoriaNellaGerarchia(String tempNome, String tempRadice) {
        return this.tree.getRadice(tempRadice).cercaCategoria(tempNome) == null;
    }


    /**
     * Guida l'input del nome della Categoria a cui assegnare nomeCategoria come figlia.
     * Controlla che si inserisca il nome di una Categoria che può avere figlie.
     *
     * @param nomeCategoriaRadice, Categoria radice della gerarchia di riferimento
     * @param nomeCategoria,       Categoria figlia da assegnare alla madre
     * @return Categoria a cui la Categoria figlia sarà assegnata
     */
    private Categoria inserimentoNomeCategoriaMadre(String nomeCategoriaRadice, String nomeCategoria) {
        assert nomeCategoriaRadice != null : "il nome della radice non può essere null";
        assert nomeCategoria != null : "il nome della categoria figlia non può essere null";

        String nomeCategoriaMadre;
        boolean esisteMadre; // si setta a true quando (e se) la si trova
        Categoria categoriaMadre;

        do { //chiedi temp nome madre
            visualizzaGerarchia(nomeCategoriaRadice); // stampa la gerarchia
            nomeCategoriaMadre = InputDati.leggiStringaNonVuota(String.format(MSG_INSERIMENTO_NOME_CATEGORIA_MADRE, nomeCategoria));

            // TODO potrebbe essere più bello questo check
            // search for tempmadre e salva l'oggetto
            categoriaMadre = this.tree.getRadice(nomeCategoriaRadice).cercaCategoria(nomeCategoriaMadre);
            esisteMadre = categoriaMadre != null
                    && !categoriaMadre.isFoglia()
                    && !nomeCategoriaMadre.equals(nomeCategoria); // se non è assegnato null, allora l'ha trovata!

            if (!esisteMadre) System.out.print(WARNING_CATEGORIA_NF_NON_ESISTE);
        } while (!esisteMadre);

        return categoriaMadre;
    }


    /**
     * Guida l'input di una stringa imponendo un vincolo di univocit&agrave; tra i valori del dominio che le Categorie "sorelle"
     * assumono.
     *
     * @param tempNome,       Categoria a cui assegnare il nome del ValoreDominio
     * @param categoriaMadre, Categoria madre da cui si eredita il dominio e da cui ottenere i valori delle "sorelle"
     * @return nome del ValoreDominio
     */
    private String inserimentoValoreDominio(String tempNome, Categoria categoriaMadre) {
        String nomeValore;
        List<String> valoriSorelle = categoriaMadre.getValoriDominioFiglie();
        do {
            nomeValore = InputDati.leggiStringaNonVuota(String.format(MSG_INSERIMENTO_VALORE_DOMINIO, tempNome, categoriaMadre.getCampoFiglie()));
        } while (valoriSorelle.contains(nomeValore));
        return nomeValore;
    }


    /**
     * Permette la creazione di una categoria radice.
     */
    public void aggiungiCategoriaRadice() {

        // 1. chiede un nome univoco tra le radici
        String tempNome = inserimentoNomeCategoriaRadice();
        // 2. chiedi nome del dominio (campo) che questa categoria imporrà alle sue figlie
        String tempCampo = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_NUOVO_DOMINIO);
        // non chiedo subito di inserire i valori del dominio -> da inserire quando si aggiunge una figlia
        // 3. creazione oggetto e aggiunta all'albero
        Categoria tempRadice = new Categoria(tempNome, tempCampo);
        this.tree.aggiungiRadice(tempRadice);
        serializeXML();
    }

    /**
     * Verifica che una categoria radice esista
     *
     * @param tempNome, nome della categoria da cercare
     * @return boolean, risultato ricerca
     */
    private boolean esisteRadice(String tempNome) {
        return this.tree.contains(tempNome);
    }

    /**
     * Stampa a video una struttura pseudo-tree-like delle gerarchie di radici presenti nel programma.
     */
    public void visualizzaGerarchie() {
        System.out.println(HEADER_VISUALIZZA_GERARCHIE);
        System.out.println(this);
    }

    /**
     * Stampa a video le info di un singolo albero gerarchico.
     *
     * @param nomeRadice, nome della categoria radice dell'albero da mostrare
     */
    public void visualizzaGerarchia(String nomeRadice) {
        System.out.println(String.format(HEADER_VISUALIZZA_RADICE, nomeRadice));
        System.out.println(this.tree.getRadice(nomeRadice) + " \n");

    }

    /**
     * Produce una stringa con le info delle radici.
     * Si limita al nome delle radici e al nome del loro dominio (che danno alle figlie).
     *
     * @return stringa formattata
     */
    public String radiciToString() {
        StringBuilder sb = new StringBuilder();
        for (Categoria radice : tree.getRadici())
            sb.append(radice.simpleToString()).append("\n");
        return sb.toString();
    }

    /**
     * Produce una stringa con le info degli alberi gerarchici.
     *
     * @return stringa formattata
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Categoria radice : tree.getRadici())
            sb.append("> RADICE\n").append(radice).append("\n\n");
        return sb.toString();
    }

}
