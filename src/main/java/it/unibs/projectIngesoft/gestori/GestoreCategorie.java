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


    public static final String MSG_VISUALIZZA_GERARCHIE = "\n>>Visualizza gerarchie di categorie\n";
    public static final String MSG_VISUALIZZA_RADICE = "\n>>Visualizza gerarchia di %s\n";
    public static final String MSG_VISUALIZZA_DOMINI = "%n>>Visualizza i domini ed i loro valori";
    public static final boolean DEBUG_DATA = false;
    public static final boolean DEBUG_LOGIC = true;


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

        // in funzione delle categorie appena lette, raccoglie i domini ed i loro valori e li spiaccica qui
        // non serve forse
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

            //TODO

            // c'è u bug incredibile cioè: ho ignorato il campo "madre" in CategoriaFoglia per evitare di
            // scrivere ricorsivamente all'infinito le figlie
            // però così durante la deserializzazione le figlie avranno solo il campo nomeMadre popolato e non
            // potranno conoscere un tubo del dominio etc etc
            // quindi l'opzione scimmia: durante la creazione trascrivo i campi da madre a figlia
            // oppure creo un metodo che fixa questo più tardi
            // scelgo la modalità scimmia

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
                //aggiungi cat nf
                aggiungiCategoriaNF();
                break;
            case 2:
                //aggiungi cat f
                aggiungiCategoriaF();
                break;
            case 3: // FATTO
                //aggiungi gerarchia
                aggiungiCategoriaRadice();
                break;
            case 4:
                //descrizione
                visualizzaDomini();
                aggiungiDescrizioneValoreDominio();
                break;
            case 5: // FATTO
                //visualizza
                visualizzaGerarchie();
                break;
            default:
                System.out.println("Nulla da mostrare");
        }
    }

    public void aggiungiCategoriaNF() {
        //TODO
        // controllare che sia robusto rispetto a input barbini


        String tempRadice;
        String tempCampoFiglie;
        String tempValoreDominio;
        boolean check = false;

        // chiede a quale radice si vuole aggiungere
        do {
            System.out.println(">> Scegli un albero di categorie a cui aggiungere la nuova.\n>> Di seguito tutte le categorie radice.");
            System.out.println(radiciToString());
            tempRadice = InputDati.leggiStringaNonVuota(">> Inserisci il nome della categoria radice:\n> ");
            check = this.esisteRadice(tempRadice);
            if (!check)
                System.out.println(">> Per favore indica una categoria radice esistente (!!) ");

        } while (!check);

        // 2. chiedi nome
        // 3. chiedi madre
        // 4. cerca madre e nome

        boolean checkNomeUnivoco = false; // si setta false se si trovano nomi duplicati
        boolean checkMadre = false; // si setta true quando si trova la madre
        String tempNome = "";
        String tempMadre = "";
        CategoriaNonFoglia catMadre = null;

        do {
            if (!checkNomeUnivoco) {
                //chiedi temp nome se non è univoco
                visualizzaGerarchia(tempRadice);
                tempNome = InputDati.leggiStringaNonVuota(">> Inserisci il nome della NUOVA CATEGORIA:%n> ").trim();
                // cerca tra i figli di una radice se c'è questa roba
            }
            if (!checkMadre) {
                // stampa la gerarchia
                visualizzaGerarchia(tempRadice);
                tempMadre = InputDati.leggiStringaNonVuota(String.format(">> Inserisci il nome della CATEGORIA MADRE per %s:%n> ", tempNome)).trim();
                // cerca tra i figli di una radice se c'è questa cosa
            }

            for (CategoriaNonFoglia cat : tree.radici) {
                // search for tempmadre e salva l'oggetto

                // se trova corrispondenza, non mette checkNomeUnivoco = false (già inizializzato)
                // se la trova, la setta true

                if (!checkNomeUnivoco && cat.cercaCategoria(tempNome) == null)
                    checkNomeUnivoco = true;
                if (!checkMadre) { // se non ha ancora trovato la madre..
                    catMadre = (CategoriaNonFoglia) this.tree.getRadice(tempRadice).cercaCategoria(tempMadre);
                    if (catMadre != null) {
                        checkMadre = true;
                    }
                }
                // search for tempnome e rompi il cazzo se lo trovi
            }

        } while (!checkNomeUnivoco || !checkMadre);

        tempValoreDominio = InputDati.leggiStringaNonVuota(String.format(">> Inserisci il valore di %s nel dominio di {%s}%n> ", tempNome, catMadre.getCampoFiglie())).trim();
        // non chiedo subito di inserire i valori del dominio. quelli staranno da decidere nel momento in cui
        // si inserisce una figlia

        tempCampoFiglie = InputDati.leggiStringaNonVuota(">> Inserisci il nome del dominio per eventuali figlie della nuova categoria:%n> ").trim();
        // non chiedo subito di inserire i valori del dominio. quelli staranno da decidere nel momento in cui
        // si inserisce una figlia

        CategoriaNonFoglia tempNF = new CategoriaNonFoglia(tempNome, tempCampoFiglie, catMadre, tempValoreDominio);
        catMadre.addCategoriaFiglia(tempNF);

        serializeXML();
    }

    public void aggiungiCategoriaF() {
        //TODO
        // mostra quali possono essere le categorie MADRE
        // selezionane una
        // crea l'oggetto
        serializeXML();
    }

    /**
     * Permette la creazione di una categoria radice.
     */
    public void aggiungiCategoriaRadice() {
        String tempNome;
        String tempCampo;

        do {
            System.out.println(">> Di seguito tutte le categorie radice.");
            System.out.println(radiciToString());
            tempNome = InputDati.leggiStringaNonVuota(">> Inserisci il nome della nuova categoria radice:%n>");
        } while (this.esisteRadice(tempNome));

        // TODO
        // legge solo i nomi senza spazi wtf
        tempCampo = InputDati.leggiStringaNonVuota(">> Inserisci il nome del dominio della nuova categoria:%n>");

        // non chiedo subito di inserire i valori del dominio. quelli staranno da decidere nel momento in cui
        // si inserisce una figlia
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
     * Stampa a video la struttura tree-like delle gerarchie di radici presenti nel programma.
     */
    public void visualizzaGerarchie() {
        //TODO
        System.out.println(MSG_VISUALIZZA_GERARCHIE);
        System.out.println(this);
    }

    /**
     * Stampa a video le info di un singolo albero gerarchico
     *
     * @param nomeRadice, nome della categoria radice dell'albero da mostrare
     */
    public void visualizzaGerarchia(String nomeRadice) {
        System.out.printf((MSG_VISUALIZZA_RADICE) + "%n", nomeRadice);
        System.out.println(this.tree.getRadice(nomeRadice) + " %n%n");


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
            sb.append("%n%nNome Dominio: ").append(tempNF.getCampo()).append("%n");
            if (!tempLista.isEmpty()) {
                sb.append("Valori: ");
                for (ValoreDominio val : tempLista)
                    sb.append("%n{ ").append(val.toString()).append(" }");
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
        // scorre le radici (tutte catNF)
        for (CategoriaNonFoglia tempNF : tree.radici)
            // stampa solo radici e dominio, niente sulle figlie
            sb.append(tempNF.simpleToString()).append("%n");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (CategoriaNonFoglia tempNF : tree.radici)
            sb.append(tempNF).append("%n%n");
        return sb.toString();
    }

}
