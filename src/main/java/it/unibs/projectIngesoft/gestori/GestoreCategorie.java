package it.unibs.projectIngesoft.gestori;


import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.Albero;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.CategoriaFoglia;
import it.unibs.projectIngesoft.attivita.CategoriaNonFoglia;
import it.unibs.projectIngesoft.libraries.InputDati;
import it.unibs.projectIngesoft.libraries.Serializer;

import java.util.List;

public class GestoreCategorie {

    public static final String HEADER_VISUALIZZA_GERARCHIE = ">> Visualizza gerarchie di categorie <<\n";
    public static final String HEADER_VISUALIZZA_RADICE = ">> Visualizza gerarchia di %s <<\n";
    public static final String HEADER_VISUALIZZA_DOMINI = ">> Visualizza i domini ed i loro valori <<\n";

    public static final String MSG_INSERIMENTO_NUOVO_DOMINIO = ">> Inserisci il nome del dominio della nuova categoria:\n> ";
    public static final String MSG_INSERIMENTO_VALORE_DOMINIO = ">> Inserisci il valore di %s nel dominio di {%s}\n> ";
    public static final String MSG_INSERIMENTO_DOMINIO_PER_FIGLIE = ">> Inserisci il nome del dominio per eventuali figlie della nuova categoria:\n> ";

    public static final String MSG_INSERIMENTO_NOME_CATEGORIA_MADRE = ">> Inserisci il nome della CATEGORIA MADRE per %s:\n> ";

    public static final String MSG_SELEZIONE_RADICE = ">> Scegli un albero di categorie.\n";
    public static final String MSG_INSERIMENTO_RADICE = ">> Scegli un nome per il nuovo albero di categorie che non esista già.\n";
    public static final String MSG_PRINT_LISTA_RADICI = ">> Di seguito tutte le categorie radice.\n";

    public static final String MSG_INPUT_NOME_RADICE = ">> Inserisci il nome della categoria radice:\n> ";
    public static final String MSG_INPUT_NUOVO_NOME_RADICE = ">> Inserisci il nome della NUOVA CATEGORIA RADICE:\n> ";
    public static final String MSG_INSERIMENTO_NUOVA_CATEGORIA = ">> Inserisci il nome della NUOVA CATEGORIA:\n> ";

    public static final String WARNING_RADICE_NON_ESISTE = ">> (!!) Per favore indica una categoria radice esistente";
    public static final String WARNING_RADICE_ESISTE = ">> (!!) Per favore indica una categoria radice che non esiste già";
    public static final String WARNING_CATEGORIA_ESISTE = ">> (!!) Per favore indica una categoria che non esista già in questo albero gerarchico.";
    public static final String WARNING_CATEGORIA_NF_NON_ESISTE = ">> (!!) Per favore indica una categoria non foglia dell'albero gerarchico selezionato.\n";

    public static final boolean DEBUG_DATA = false;
    public static final boolean DEBUG_LOGIC = false;


    // non sono sicuro di quale struttura dati utilizzare
    private Albero tree;

    private final String filePath;


    public GestoreCategorie(String filePath) {
        /* legge da file memorizza le radici già presenti
        prepara tutto apposto */
        this.filePath = filePath;
        this.tree = new Albero();
        //deserializeXML(); // load dati

        if (DEBUG_DATA) {
            CategoriaNonFoglia radice1 = new CategoriaNonFoglia("cat1 radice", "materia");
            CategoriaNonFoglia radice2 = new CategoriaNonFoglia("cat2 radice", "lezione");

            tree.radici.add(radice1);
            tree.radici.add(radice2);
            radice1.addCategoriaFiglia(new CategoriaFoglia("figlia1", radice1, "matematica"));
            radice1.addCategoriaFiglia(new CategoriaFoglia("figlia2", radice1, "italiano"));
            radice2.addCategoriaFiglia(new CategoriaFoglia("figlia3", radice2, "online"));
            radice2.addCategoriaFiglia(new CategoriaNonFoglia("figliaNF1", "gradoScuola", radice2, "in presenza"));


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

        List<CategoriaNonFoglia> tempCat = Serializer.deserialize(new TypeReference<List<CategoriaNonFoglia>>() {
        }, this.filePath);

        tree.radici.clear();
        if (tempCat != null)
            tree.radici.addAll(tempCat);

    }


    /**
     * Richiama il metodo necessario in base alla selezione dal menu.
     *
     * @param scelta, selezione dal menu
     */
    public void entryPoint(int scelta) {

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
                //TODO
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
        boolean esisteRadice = false;
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
        boolean esisteRadice = true;
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
        String tempNome = "";
        boolean isNomeUnivoco = false; // si setta true se non trova duplicati

        do { //chiedi temp nome
            visualizzaGerarchia(tempRadice); // stampa gerarchia
            tempNome = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_NUOVA_CATEGORIA);

            // cerca dalla radice se c'è questa roba, se ritorna null allora non c'è ed è univoco
            isNomeUnivoco = this.tree.getRadice(tempRadice).cercaCategoria(tempNome) == null;

            if (!isNomeUnivoco) System.out.print(WARNING_CATEGORIA_ESISTE);

        } while (!isNomeUnivoco);

        return tempNome;
    }

    private CategoriaNonFoglia inserimentoNomeCategoriaMadre(String tempRadice, String tempNome) {
        String tempNomeMadre = "";
        boolean esisteMadre = false; // si setta a true quando (e se) la si trova
        CategoriaNonFoglia catMadre = null;

        do { //chiedi temp nome madre
            visualizzaGerarchia(tempRadice); // stampa la gerarchia
            tempNomeMadre = InputDati.leggiStringaNonVuota(String.format(MSG_INSERIMENTO_NOME_CATEGORIA_MADRE, tempNome));

            // search for tempmadre e salva l'oggetto
            catMadre = (CategoriaNonFoglia) this.tree.getRadice(tempRadice).cercaCategoria(tempNomeMadre);
            esisteMadre = catMadre != null; // se non è assegnato null, allora l'ha trovata!

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
        CategoriaNonFoglia catMadre = this.inserimentoNomeCategoriaMadre(tempRadice, tempNome);

        // 4. chiedi che valore assegnare al dominio ereditato dalla categoria madre
        String tempValoreDominio = InputDati.leggiStringaNonVuota(String.format(MSG_INSERIMENTO_VALORE_DOMINIO, tempNome, catMadre.getCampoFiglie()));

        // 5. chiedi nome del dominio (campo) che questa categoria imporrà alle sue figlie
        String tempCampoFiglie = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_DOMINIO_PER_FIGLIE);
        // non chiedo subito di inserire i valori del dominio -> da inserire quando si aggiunge una figlia

        // creazione oggetto e aggiunta figlia alla madre
        CategoriaNonFoglia tempNF = new CategoriaNonFoglia(tempNome, tempCampoFiglie, catMadre, tempValoreDominio);
        catMadre.addCategoriaFiglia(tempNF);

        // salvataggio dati
        serializeXML();
    }

    public void aggiungiCategoriaFoglia() {

        // 1. seleziona la radice della gerarchia a cui aggiungere una categoria
        String tempRadice = this.selezioneCategoriaRadice();

        // 2. chiedi nome per nuova radice, verificando che sia univoco
        String tempNome = this.inserimentoNomeNuovaCategoria(tempRadice);

        // 3. chiedi madre per nuova radice, verificando che esista
        CategoriaNonFoglia catMadre = this.inserimentoNomeCategoriaMadre(tempRadice, tempNome);

        // 4. chiedi che valore assegnare al dominio ereditato dalla categoria madre
        String tempValoreDominio = InputDati.leggiStringaNonVuota(String.format(MSG_INSERIMENTO_VALORE_DOMINIO, tempNome, catMadre.getCampoFiglie()));

        // creazione oggetto e aggiunta figlia alla madre
        CategoriaFoglia tempF = new CategoriaFoglia(tempNome, catMadre, tempValoreDominio);
        catMadre.addCategoriaFiglia(tempF);

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

        CategoriaNonFoglia tempRadice = new CategoriaNonFoglia(tempNome, tempCampo);
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

    // TODO
    public void visualizzaDomini() {
        System.out.println(HEADER_VISUALIZZA_DOMINI);
        //System.out.println(dominioCategoriaNonFogliaToString());
    }


    public CategoriaNonFoglia selezioneCategoriaNonFoglia(String tempRadice) {

        // stampo radice gerarchia e poi
        // stampo la lista di valori per tute le catNF di queste

        String tempNomeCNF = "";
        boolean esisteCNF = false; // si setta a true quando (e se) la si trova
        CategoriaNonFoglia tempCatNF = null;

        do { //chiedi temp nome madre
            visualizzaGerarchia(tempRadice); // stampa la gerarchia
            tempNomeCNF = InputDati.leggiStringaNonVuota(">> Inserisci il nome della categoria non foglia che ti interessa:\n> ");

            // search for tempCNF e salva l'oggetto
            // questa cosa è orrenda ma non saprei come sistemarla
            // TODO chiedere a copilot
            if (this.tree.getRadice(tempRadice).cercaCategoria(tempNomeCNF) != null) {
                if (this.tree.getRadice(tempRadice).cercaCategoria(tempNomeCNF) instanceof CategoriaNonFoglia temp) {
                    tempCatNF = temp;
                    esisteCNF = true;
                }
            }

            if (!esisteCNF) System.out.print(WARNING_CATEGORIA_NF_NON_ESISTE);

        } while (!esisteCNF);


        return tempCatNF;
    }


    /**
     * TODO
     */
    public void aggiungiDescrizioneValoriDominio() {

       //visualizzaDomini();
        // 0. necessario capire quale dominio
        // ma 1 dominio : 1 catNF

        // 1. chiedo a quale catNF si vuole toccare
        // stampo radice gerarchia e poi
        // stampo la lista di valori per tute le catNF di queste


        // 1. seleziona la radice della gerarchia a cui aggiungere una categoria
        String tempRadice = this.selezioneCategoriaRadice();

        // 2. chiedi nome per nuova radice, verificando che sia univoco
        CategoriaNonFoglia tempCatNF = selezioneCategoriaNonFoglia(tempRadice);

        // FUNZIONA!


        System.out.println("brao wade");

        //TODO CONTINUA DA QUI!

        boolean esisteValore = false;
        Categoria tempCat;

        do{
            System.out.println(">> Ora scegli dai possibili valori, quello a cui vuoi aggiungere una descrizione.");
            System.out.println(tempCatNF.figlieToString());
            System.out.println(tempCatNF.dominioToString());
            // si però se stampo il dominio non posso chiede le categorie zio can
            String nomeCat = InputDati.leggiStringaNonVuota(String.format(">> Inserisci la categoria figlia di %s che assume il valore che ti interessa del dominio {%s}:\n> ", tempCatNF.getNome(), tempCatNF.getCampoFiglie()));

            // TODO forse un metodo apposito per cercare i valori non sarebbe male eh

            // search for tempmadre e salva l'oggetto
            tempCat = this.tree.getRadice(tempRadice).cercaCategoria(nomeCat);
            esisteValore = tempCat != null; // se non è assegnato null, allora l'ha trovata!

            if (!esisteValore) System.out.print(WARNING_CATEGORIA_NF_NON_ESISTE);
        }while(!esisteValore);

        System.out.println(tempCat.getValoreDominio());

        System.out.println("brao wade pt2");



        // 2. stampo solo i valori del suo dominio in loop e chiedo se si vuole aggiungere una descrizione
        // scrivere una parola checka se è un valore -> true allora fa scrivere la desc
        // scrivere "0" fa uscire


        serializeXML();
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
     * Produce una stringa con le info dei domini.
     * Scorre ogni boh vabbe è sbagliato
     * TODO
     *
     * @return stringa formattata
     */
   /* public String dominioCategoriaNonFogliaToString() {


        StringBuilder sb = new StringBuilder();

       for (CategoriaNonFoglia tempNF : tree.radici) {
            ArrayList<ValoreDominio> tempLista = tempNF.getListaValoriDominio();
            sb.append("\n\nNome Dominio: ").append(tempNF.getCampo()).append("\n");
            if (!tempLista.isEmpty()) {
                sb.append("Valori: ");
                for (ValoreDominio val : tempLista)
                    sb.append("\n{ ").append(val.toString()).append(" }");
            } else {
                sb.append("Vuoto.");
            }
        }
        return sb.toString();
    }*/

    /**
     * Produce una stringa con le info delle radici.
     * Si limita al nome delle radici e al nome del loro dominio (che danno alle figlie).
     *
     * @return stringa formattata
     */
    public String radiciToString() {
        StringBuilder sb = new StringBuilder();
        for (CategoriaNonFoglia radice : tree.radici)
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
        for (CategoriaNonFoglia radice : tree.radici)
            sb.append("> RADICE\n").append(radice).append("\n\n");
        return sb.toString();
    }

}
