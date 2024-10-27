package it.unibs.projectIngesoft.gestori;


import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.Albero;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
import it.unibs.projectIngesoft.libraries.InputDati;
import it.unibs.projectIngesoft.libraries.Serializer;

import java.util.ArrayList;
import java.util.List;

public class GestoreCategorie {

    public static final String HEADER_VISUALIZZA_GERARCHIE = ">> Visualizza gerarchie di categorie <<\n";
    public static final String HEADER_VISUALIZZA_RADICE = ">> Visualizza gerarchia di %s <<\n";
    //public static final String HEADER_VISUALIZZA_DOMINI = ">> Visualizza i domini ed i loro valori <<\n";

    public static final String MSG_INSERIMENTO_NUOVO_DOMINIO = ">> Inserisci il nome del dominio della nuova categoria:\n> ";
    public static final String MSG_INSERIMENTO_VALORE_DOMINIO = ">> Inserisci il valore di %s nel dominio di {%s}\n> ";
    public static final String MSG_INSERIMENTO_DOMINIO_PER_FIGLIE = ">> Inserisci il nome del dominio per eventuali figlie della nuova categoria:\n> ";

    private static final String MSG_INSERISCI_RADICE = ">> Inserisci la radice dell'albero cui appartiene la foglia che ti interessa:\n> ";
    private static final String MSG_INSERISCI_FOGLIA = ">> Inserisci la foglia che ti interessa:\n> ";

    public static final String MSG_INSERIMENTO_NOME_CATEGORIA_MADRE = ">> Inserisci il nome della CATEGORIA MADRE per %s:\n> ";

    public static final String MSG_SELEZIONE_RADICE = ">> Scegli un albero di categorie.\n";
    public static final String MSG_INSERIMENTO_RADICE = ">> Scegli un nome per il nuovo albero di categorie che non esista già.\n";
    public static final String MSG_PRINT_LISTA_RADICI = ">> Di seguito tutte le categorie radice.\n";

    public static final String MSG_INPUT_NOME_RADICE = ">> Inserisci il nome della categoria radice:\n> ";
    public static final String MSG_INPUT_NUOVO_NOME_RADICE = ">> Inserisci il nome della NUOVA CATEGORIA RADICE:\n> ";
    public static final String MSG_INSERIMENTO_NUOVA_CATEGORIA = ">> Inserisci il nome della NUOVA CATEGORIA:\n> ";


    public static final String MSG_INPUT_DESCRIZIONE_VALORE_DOMINIO = ">> Inserisci la descrizione (da 0 a 100 caratteri):\n> ";
    public static final String CONFIRM_DESCRIZIONE_AGGIUNTA = ">> Descrizione aggiunta <<";

    public static final String WARNING_RADICE_NON_ESISTE = ">> (!!) Per favore indica una categoria radice esistente";
    public static final String WARNING_RADICE_ESISTE = ">> (!!) Per favore indica una categoria radice che non esiste già";
    public static final String WARNING_CATEGORIA_ESISTE = ">> (!!) Per favore indica una categoria che non esista già in questo albero gerarchico.";
    public static final String WARNING_CATEGORIA_NF_NON_ESISTE = ">> (!!) Per favore indica una categoria non foglia dell'albero gerarchico selezionato.\n";
    public static final String WARNING_DESCRIZIONE_ESISTE = ">> (!!) Questo valore possiede già una descrizione.\n";

    public static final boolean DEBUG_DATA = true;
    public static final String MSG_SELEZIONE_VALORE_DOMINIO = ">> Ora scegli dai possibili valori, quello a cui vuoi aggiungere una descrizione.";
    public static final String MSG_INPUT_NOME_VALORE_DOMINIO = ">> Inserisci il nome del valore che ti interessa del dominio {%s}:\n> ";
    public static final String MSG_INIZIO_GESTIONE_FDC = "\n\n>> Passaggio alla procedura per l'inserimento del fattore di conversione.. <<";


    // non sono sicuro di quale struttura dati utilizzare
    private Albero tree;

    private final String filePath;

    //TENTATIVO DI COUPLLING CON FATTORI
    private GestoreFattori gestFatt;


    public GestoreCategorie(String categorieFilePath, String fattoriFilePath) {
        /* legge da file memorizza le radici già presenti
        prepara tutto apposto */
        this.filePath = categorieFilePath;
        this.tree = new Albero();
        this.gestFatt = new GestoreFattori(fattoriFilePath);

        //deserializeXML(); // load dati

        if (DEBUG_DATA) {

            Categoria radice1 = new Categoria("cat1 radice", "materia");
            Categoria radice2 = new Categoria("cat2 radice", "lezione");

            tree.getRadici().add(radice1);
            tree.getRadici().add(radice2);
            radice1.addCategoriaFiglia(new Categoria("figlia1", radice1, "matematica"));
            radice1.addCategoriaFiglia(new Categoria("figlia2", radice1, "italiano"));
            radice2.addCategoriaFiglia(new Categoria("figlia3", radice2, "online"));
            radice2.addCategoriaFiglia(new Categoria("figliaNF1", "gradoScuola", radice2, "in presenza"));


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
        Serializer.serialize(this.filePath, this.tree);
    }

    /**
     * Sfrutto l'implementazione statica della classe Serializer.
     * Questo metodo esiste da prima di Serializer, per non cambiare ogni call a questo ho semplicemente sostituito il corpo.
     */
    public void deserializeXML() {

        List<Categoria> tempCat = Serializer.deserialize(new TypeReference<>() {
        }, this.filePath);

        tree.getRadici().clear();
        if (tempCat != null)
            tree.getRadici().addAll(tempCat);
    }


    /**
     * Richiama il metodo necessario in base alla selezione dal menu.
     *
     * @param scelta, selezione dal menu
     */
    public void entryPoint(int scelta) {

        // TODO
        // ma i valori dei domini, devono avere nomi univoci?
        // in caso devo aggiungere questo check -w
        // TODO
        // mettere un check prima di aggiungini CNF e CF che esista almeno una radice -w

        switch (scelta) {
            case 1:
                aggiungiCategoriaNonFoglia();
                break;
            case 2:
                aggiungiCategoriaFoglia();
                break;
            case 3:
                aggiungiCategoriaRadice();
                break;
            case 4:
                // TODO
                aggiungiDescrizioneValoriDominio();
                break;
            case 5:
                visualizzaGerarchie();
                break;
            default:
                break;
        }
    }

    private String selezioneCategoriaRadice() {
        String tempNomeRadice;
        boolean esisteRadice;
        do {
            System.out.println(MSG_SELEZIONE_RADICE + MSG_PRINT_LISTA_RADICI + radiciToString());
            tempNomeRadice = InputDati.leggiStringaNonVuota(MSG_INPUT_NOME_RADICE);
            esisteRadice = this.esisteRadice(tempNomeRadice);
            if (!esisteRadice)
                System.out.println(WARNING_RADICE_NON_ESISTE);

        } while (!esisteRadice);
        return tempNomeRadice;
    }

    private String inserimentoNomeCategoriaRadice() {
        String tempNomeRadice;
        boolean esisteRadice;
        do {
            System.out.println(MSG_INSERIMENTO_RADICE + MSG_PRINT_LISTA_RADICI + radiciToString());
            tempNomeRadice = InputDati.leggiStringaNonVuota(MSG_INPUT_NUOVO_NOME_RADICE);
            esisteRadice = this.esisteRadice(tempNomeRadice);
            if (esisteRadice)
                System.out.println(WARNING_RADICE_ESISTE);
        } while (esisteRadice);

        return tempNomeRadice;
    }

    private String inserimentoNomeNuovaCategoria(String tempRadice) {
        String tempNome;
        boolean isNomeUnivoco; // si setta true se non trova duplicati

        do { //chiedi temp nome
            visualizzaGerarchia(tempRadice); // stampa gerarchia
            tempNome = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_NUOVA_CATEGORIA);

            // cerca dalla radice se c'è questa roba, se ritorna null allora non c'è ed è univoco
            isNomeUnivoco = this.tree.getRadice(tempRadice).cercaCategoria(tempNome) == null;

            if (!isNomeUnivoco) System.out.print(WARNING_CATEGORIA_ESISTE);

        } while (!isNomeUnivoco);

        return tempNome;
    }

    private Categoria inserimentoNomeCategoriaMadre(String tempRadice, String tempNome) {
        String tempNomeMadre;
        boolean esisteMadre; // si setta a true quando (e se) la si trova
        Categoria catMadre;

        do { //chiedi temp nome madre
            visualizzaGerarchia(tempRadice); // stampa la gerarchia
            tempNomeMadre = InputDati.leggiStringaNonVuota(String.format(MSG_INSERIMENTO_NOME_CATEGORIA_MADRE, tempNome));

            // TODO potrebbe essere più bello questo check
            // search for tempmadre e salva l'oggetto
            catMadre = this.tree.getRadice(tempRadice).cercaCategoria(tempNomeMadre);
            esisteMadre = catMadre != null && !catMadre.isFoglia(); // se non è assegnato null, allora l'ha trovata!

            if (!esisteMadre) System.out.print(WARNING_CATEGORIA_NF_NON_ESISTE);

        } while (!esisteMadre);

        return catMadre;
    }

    public void aggiungiCategoriaNonFoglia() {

        // 1. seleziona la radice della gerarchia a cui aggiungere una categoria
        String tempRadice = this.selezioneCategoriaRadice();

        // 2. chiedi nome per nuova radice, verificando che sia univoco
        String tempNome = this.inserimentoNomeNuovaCategoria(tempRadice);

        // 3. chiedi madre per nuova radice, verificando che esista
        Categoria catMadre = this.inserimentoNomeCategoriaMadre(tempRadice, tempNome);

        // 4. chiedi che valore assegnare al dominio ereditato dalla categoria madre
        String tempValoreDominio = this.inserimentoValoreDominio(tempNome, catMadre);

        // 5. chiedi nome del dominio (campo) che questa categoria imporrà alle sue figlie
        String tempCampoFiglie = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_DOMINIO_PER_FIGLIE);
        // non chiedo subito di inserire i valori del dominio -> da inserire quando si aggiunge una figlia

        // creazione oggetto e aggiunta figlia alla madre
        Categoria tempNF = new Categoria(tempNome, tempCampoFiglie, catMadre, tempValoreDominio);
        catMadre.addCategoriaFiglia(tempNF);

        // salvataggio dati
        serializeXML();
    }

    /**
     * Guida l'input di una stringa imponendo un vincolo di univocit&agrave; tra i valori del dominio che le Categorie "sorelle"
     * assumono.
     * @param tempNome, Categoria a cui assegnare il nome del ValoreDominio
     * @param categoriaMadre, Categoria madre da cui si eredita il dominio e da cui ottenere i valori delle "sorelle"
     * @return nome del ValoreDominio
     */
    private String inserimentoValoreDominio(String tempNome, Categoria categoriaMadre) {
        String nomeValore;
        List<String> valoriSorelle = categoriaMadre.getValoriDominioFiglie();

        do {
            // stampare i vari valori così uno sa di non scrivere quelli?
            nomeValore = InputDati.leggiStringaNonVuota(String.format(MSG_INSERIMENTO_VALORE_DOMINIO, tempNome, categoriaMadre.getCampoFiglie()));
        }while(valoriSorelle.contains(nomeValore));

        return nomeValore;
    }

    public void aggiungiCategoriaFoglia() {

        // 1. seleziona la radice della gerarchia a cui aggiungere una categoria
        String tempRadice = this.selezioneCategoriaRadice();

        // 2. chiedi nome per nuova categoria, verificando che sia univoco
        String tempNome = this.inserimentoNomeNuovaCategoria(tempRadice);

        // 3. chiedi madre per nuova categoria, verificando che esista
        Categoria catMadre = this.inserimentoNomeCategoriaMadre(tempRadice, tempNome);

        // 4. chiedi che valore assegnare al dominio ereditato dalla categoria madre
        String tempValoreDominio = this.inserimentoValoreDominio(tempNome, catMadre);

        // 5. creazione oggetto e aggiunta figlia alla madre
        Categoria tempF = new Categoria(tempNome, catMadre, tempValoreDominio);
        catMadre.addCategoriaFiglia(tempF);
        this.tree.incrementNumFoglie();

        // TODO
        // finire questa roba che non va

        // 6. aggiunta del fattore di conversione relativo
        /*System.out.println(MSG_INIZIO_GESTIONE_FDC);

        if (this.tree.getNumFoglie() == 2) {

            String radiceFirstFoglia;
            String nomeFirstFoglia;

            List<String> foglieString = cercaCategorieFoglia();
            // radice:foglia

            String firstFogliaNome = foglieString.getFirst().split(":")[1];
            String firstFogliaRadice = foglieString.getFirst().split(":")[0];
            String lastFogliaNome = foglieString.getLast().split(":")[1];
            String lastFogliaRadice = foglieString.getLast().split(":")[0];


            // richiama inserisciPrimoFattore con radice e nome della nuova categoria + radice e nome della categoria pre-esistente
            if (firstFogliaNome.equals(tempNome))
                this.gestFatt.inserisciFattoreDiConversione(tempRadice, tempNome, lastFogliaRadice, lastFogliaNome);
            else
                this.gestFatt.inserisciFattoreDiConversione(tempRadice, tempNome, firstFogliaRadice, firstFogliaNome);
        } else {

            // TODO
            // fare in modo che l'inserimento della seconda categoria avvenga sotto la supervisione di
            // gestoreCat e non gestFatt (perchè quest'ultimo non ha accesso all'albero delle categorie)

            String nomeRadicePreesistente;
            String nomeFogliaPreesistente;

            //cicla la richiesta della categoria foglia se non esiste nella hashmap la chiave per la foglia preesistente
            // (dovrebbe essere più veloce e funzionare bene comunque)

            do {
                // TODO print lista possibili
                this.gestFatt.visualizzaCategorieConFdC();
                System.out.println(">> Scegli una categoria per la conversione.");

                nomeRadicePreesistente = InputDati.leggiStringaNonVuota(MSG_INSERISCI_RADICE);
                nomeFogliaPreesistente = InputDati.leggiStringaNonVuota(MSG_INSERISCI_FOGLIA);
                // si può sfruttare il cercaCategoria -> instanceOf CategoriaFoglia per il check
            } while (this.gestFatt.esisteEntry(nomeRadicePreesistente, nomeFogliaPreesistente));

            this.gestFatt.inserisciFattoreDiConversione(tempRadice, tempNome, nomeRadicePreesistente, nomeFogliaPreesistente);

        }*/

        serializeXML();
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
     * A partire da una radice, permette la selezione di una delle CategorieNonFoglia contenute nel suo albero
     * gerarchico.
     *
     * @param tempRadice, Categoria radice di riferimento per l'albero
     * @return CategoriaNonFoglia selezionata
     */
    public Categoria selezioneCategoriaNonFoglia(String tempRadice) {
        String tempNomeCNF;
        boolean esisteCNF = false; // si setta a true quando (e se) la si trova
        Categoria tempCatNF;

        do { //chiedi temp nome madre
            visualizzaGerarchia(tempRadice); // stampa la gerarchia
            tempNomeCNF = InputDati.leggiStringaNonVuota(">> Inserisci il nome della categoria non foglia che ti interessa:\n> ");

            // search for tempCNF e salva l'oggetto
            tempCatNF = this.tree.getRadice(tempRadice).cercaCategoria(tempNomeCNF);
            if (tempCatNF != null) // se non è null, allora l'ha trovata
                esisteCNF = true;
            if (!esisteCNF)  // se non la trova stampa un avviso
                System.out.print(WARNING_CATEGORIA_NF_NON_ESISTE);
        } while (!esisteCNF);
        return tempCatNF;
    }

    /**
     * Permette di aggiungere una descrizione al valore di dominio assunto da una qualche categoria.
     * Non permette di modificare descrizioni esistenti.
     */
    public void aggiungiDescrizioneValoriDominio() {

        // 1. seleziona la radice della gerarchia
        String tempRadice = this.selezioneCategoriaRadice();

        // 2. seleziona NF per avere un dominio di riferimento
        Categoria tempCatNF = selezioneCategoriaNonFoglia(tempRadice);

        // 3. seleziona il valore a cui aggiungere la descrizione
        ValoreDominio tempValore = selezioneValoreDominio(tempCatNF, tempRadice);

        // 4. se la descrizione esiste, termina; altrimenti la fa inserire
        if (!tempValore.getDescrizione().isEmpty())
            System.out.println(WARNING_DESCRIZIONE_ESISTE + tempValore);
        else {
            this.inserimentoDescrizione(tempValore);
            System.out.println(CONFIRM_DESCRIZIONE_AGGIUNTA);
        }

        serializeXML(); // salvataggio info
    }

    private ValoreDominio selezioneValoreDominio(Categoria tempCatNF, String tempRadice) {
        boolean esisteValore;
        Categoria tempCat;

        do {
            System.out.println(MSG_SELEZIONE_VALORE_DOMINIO);
            System.out.println(tempCatNF.dominioToString());
            String nomeVal = InputDati.leggiStringaNonVuota(String.format(MSG_INPUT_NOME_VALORE_DOMINIO, tempCatNF.getCampoFiglie()));

            // cerca la categoria a cui appartiene il valore
            tempCat = this.tree.getRadice(tempRadice).cercaValoreDominio(nomeVal);
            esisteValore = tempCat != null; // segna esisteValore = true, se la categoria trovata non è null

            if (!esisteValore) // se non trova la categoria, allora stampa un avviso
                System.out.print(WARNING_CATEGORIA_NF_NON_ESISTE);

        } while (!esisteValore);
        return tempCat.getValoreDominio();
    }

    private void inserimentoDescrizione(ValoreDominio tempValore) {
        String desc;
        desc = InputDati.stringReaderSpecificLength(MSG_INPUT_DESCRIZIONE_VALORE_DOMINIO, 0, 100);
        tempValore.setDescrizione(desc);
    }

    /* prodotto di COPILOT */

    public List<String> cercaCategorieFoglia() {
        List<String> foglieAsString = new ArrayList<>();
        for (Categoria radice : tree.getRadici()) {
            trovaCategorieFoglia(radice, radice.getNome(), foglieAsString);
        }
        return foglieAsString;
    }

    private void trovaCategorieFoglia(Categoria categoria, String nomeRadice, List<String> foglieAsString) {
        if (!categoria.isFoglia())
            for (Categoria figlia : categoria.getCategorieFiglie()) {
                if (figlia.isFoglia()) {
                    foglieAsString.add(nomeRadice + ":" + figlia.getNome());
                } else {
                    trovaCategorieFoglia(figlia, nomeRadice, foglieAsString);
                }
            }
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
        System.out.println(this.tree.getRadice(nomeRadice) + " \n\n");

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
