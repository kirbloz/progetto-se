package it.unibs.projectIngesoft.gestori;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unibs.fp.myutils.InputDati;
import it.unibs.projectIngesoft.attivita.Albero;
import it.unibs.projectIngesoft.attivita.CategoriaFoglia;
import it.unibs.projectIngesoft.attivita.CategoriaNonFoglia;
import it.unibs.projectIngesoft.attivita.ValoreDominio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestoreCategorie {

    public static final String MSG_VISUALIZZA_GERARCHIE = ">> Visualizza gerarchie di categorie <<\n";
    public static final String MSG_VISUALIZZA_RADICE = ">> Visualizza gerarchia di %s <<\n";
    public static final String MSG_VISUALIZZA_DOMINI = ">> Visualizza i domini ed i loro valori <<";

    public static final String MSG_INSERIMENTO_NUOVO_DOMINIO = ">> Inserisci il nome del dominio della nuova categoria:\n>";
    public static final String MSG_INSERIMENTO_VALORE_DOMINIO = ">> Inserisci il valore di %s nel dominio di {%s}\n> ";
    public static final String MSG_INSERIMENTO_DOMINIO_PER_FIGLIE = ">> Inserisci il nome del dominio per eventuali figlie della nuova categoria:\n> ";

    public static final String MSG_PRINT_LISTA_RADICI = ">> Di seguito tutte le categorie radice.\n";
    public static final String MSG_INSERIMENTO_NUOVA_RADICE = ">> Inserisci il nome della nuova categoria radice:\n>";

    public static final boolean DEBUG_DATA = false;
    public static final boolean DEBUG_LOGIC = false;
    public static final String WARNING_RADICE_NON_ESISTE = ">> Per favore indica una categoria radice esistente (!!)";
    public static final String MSG_INSERIMENTO_NOME_CATEGORIA_MADRE = ">> Inserisci il nome della CATEGORIA MADRE per %s:\n> ";

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
            ArrayList<ValoreDominio> valori = new ArrayList<>();
            ArrayList<ValoreDominio> valori2 = new ArrayList<>();
            //a che cazzo serve sta roba dei valori
            valori.add(new ValoreDominio("valore1", "desc1"));
            valori.add(new ValoreDominio("valore2", "desc2"));
            valori2.add(new ValoreDominio("valore3", "desc3"));
            CategoriaNonFoglia radice1 = new CategoriaNonFoglia("cat1 radice", "materia", valori);
            CategoriaNonFoglia radice2 = new CategoriaNonFoglia("cat2 radice", "lezione", valori);
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
     * TODO questo va commentato a dovere
     */
    public void serializeXML() {

        try {

            // creazione mapper e oggetto file
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);


            File file = new File(this.filePath);

            // se il file non esiste, lo si crea
            if (file.createNewFile()) {
                if (DEBUG_LOGIC)
                    System.out.println("FILE CREATO");
            }

            xmlMapper.writeValue(file, tree);


        } catch (JsonProcessingException e) {
            // handle exception
            System.out.println("Errore di serializzazione: " + e.getMessage());

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    /**
     * TODO questo va commentato a dovere
     */
    public void deserializeXML() {

        try {
            XmlMapper xmlMapper = new XmlMapper();
            File file = new File(this.filePath);

            if (!file.exists()) {
                if (DEBUG_LOGIC)
                    System.out.println("FILE NON ESISTE. NON CARICO NIENTE.");
                return;
            }

            List<CategoriaNonFoglia> tempCat = xmlMapper.readValue(file, new TypeReference<>() {
            });

            tree.radici.clear();
            tree.radici.addAll(tempCat);

        } catch (IOException e) {
            // handle the exception
            System.out.println(e.getMessage());
        }
    }


    /**
     * Richiama il metodo necessario in base alla selezione dal menu.
     *
     * @param scelta, selezione dal menu
     */
    public void entryPoint(int scelta) {
        //TODO
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
                //descrizione
                visualizzaDomini();
                aggiungiDescrizioneValoreDominio();
                break;
            case 5:
                visualizzaGerarchie();
                break;
            default:
                break;
        }
    }

    private String selezioneCategoriaRadice() {
        String tempRadice;
        boolean esisteRadice = false;
        do {
            System.out.println(">> Scegli un albero di categorie a cui aggiungere la nuova.\n>> Di seguito tutte le categorie radice."+radiciToString());
            tempRadice = InputDati.leggiStringaNonVuota(">> Inserisci il nome della categoria radice:\n> ");
            esisteRadice = this.esisteRadice(tempRadice);
            if (!esisteRadice)
                System.out.println(WARNING_RADICE_NON_ESISTE);

        } while (!esisteRadice);
        return tempRadice;
    }

    private String inserimentoNomeCategoriaRadice() {
        String tempNomeRadice;
        do {
            System.out.println(MSG_PRINT_LISTA_RADICI + radiciToString());
            tempNomeRadice = InputDati.leggiStringaNonVuota(MSG_INSERIMENTO_NUOVA_RADICE);
        } while (this.esisteRadice(tempNomeRadice));

        return tempNomeRadice;
    }

    private String inserimentoNomeNuovaCategoria(String tempRadice) {
        String tempNome = "";
        boolean isNomeUnivoco = false; // si setta true se non trova duplicati

        do {
            if (!isNomeUnivoco) { //chiedi temp nome se non è univoco
                visualizzaGerarchia(tempRadice); // stampa gerarchia
                tempNome = InputDati.leggiStringaNonVuota(">> Inserisci il nome della NUOVA CATEGORIA:\n> ");
            }

            if (!isNomeUnivoco) { // cerca tra i figli di una radice se c'è questa roba
                isNomeUnivoco = this.tree.getRadice(tempRadice).cercaCategoria(tempNome) == null;
            }

        } while (!isNomeUnivoco);

        return tempNome;
    }

    private CategoriaNonFoglia inserimentoNomeCategoriaMadre(String tempRadice, String tempNome) {
        String tempMadre = "";
        boolean esisteMadre = false; // si setta a true quando (e se) la si trova
        CategoriaNonFoglia catMadre = null;

        do {
            if (!esisteMadre) { //chiedi nome madre se il precedente non esisteva
                visualizzaGerarchia(tempRadice); // stampa la gerarchia
                tempMadre = InputDati.leggiStringaNonVuota(String.format(MSG_INSERIMENTO_NOME_CATEGORIA_MADRE, tempNome)).trim();
            }

            // search for tempmadre e salva l'oggetto
            if (!esisteMadre) { // se non ha ancora trovato la madre.. cerca tra i figli della radice se c'è
                catMadre = (CategoriaNonFoglia) this.tree.getRadice(tempRadice).cercaCategoria(tempMadre);
                esisteMadre = catMadre != null; // se non è assegnato null, allora l'ha trovata!
            }
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
        System.out.println(MSG_VISUALIZZA_DOMINI);
        System.out.println(dominiToString());
    }


    /**
     * Aggiunge la descrizione al valore di dominio di una qualche categoria.
     */
    public void aggiungiDescrizioneValoreDominio() {
        //TODO
        serializeXML();
    }

    /**
     * Stampa a video una struttura pseudo-tree-like delle gerarchie di radici presenti nel programma.
     */
    public void visualizzaGerarchie() {
        System.out.println(MSG_VISUALIZZA_GERARCHIE);
        System.out.println(this);
    }

    /**
     * Stampa a video le info di un singolo albero gerarchico.
     *
     * @param nomeRadice, nome della categoria radice dell'albero da mostrare
     */
    public void visualizzaGerarchia(String nomeRadice) {
        System.out.println(String.format(MSG_VISUALIZZA_RADICE, nomeRadice));
        System.out.println(this.tree.getRadice(nomeRadice) + " \n\n");


    }

    /**
     * Produce una stringa con le info dei domini.
     * Scorre ogni boh vabbe è sbagliato
     * TODO
     *
     * @return stringa formattata
     */
    public String dominiToString() {
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
    }

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
