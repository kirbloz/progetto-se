package it.unibs.projectIngesoft.gestori;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unibs.projectIngesoft.attivita.CategoriaFoglia;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.libraries.InputDati;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GestoreFattori {

    private static final String MSG_INSERISCI_RADICE = "";
    private static final String MSG_INSERISCI_FOGLIA = "";
    private static final String MSG_INSERISCI_FATTORE = "Inserisci fattore:";
    private final String filePath;
    private HashMap<String, ArrayList<FattoreDiConversione>> fattori;

    public GestoreFattori(String filePath) {
        this.filePath = filePath;
        this.fattori = new HashMap<>();
        deserializeXML(); //load dati
    }

    public String getFilePath() {
        return filePath;
    }

    public HashMap<String, ArrayList<FattoreDiConversione>> getFattori() {
        return fattori;
    }

    public void setFattori(HashMap<String, ArrayList<FattoreDiConversione>> fattori) {
        this.fattori = fattori;
    }

    /**
     * Inserisce un nuovo fattore nell'HashMap, verificando eventuali duplicati.
     *
     * @param tempKey,   nome della prima categoria della coppia
     * @param tempValue, oggetto FdC da inserire nella lista
     */
    public void addFattore(String tempKey, FattoreDiConversione tempValue) {
        // TODO
        // string nomeCat, string nomeRadice
        // String key = String.format("%s:%s", nomeRadice, nomeCat);

        // newValue =


        // controlla che esista già un'elemento dell'HashMap con quella chiave
        if (this.fattori.containsKey(tempKey)) {
            // inserisce il fattore se e solo se non esiste già nell'arraylist
            if (!this.fattori.get(tempKey).contains(tempValue)) {
                this.fattori.get(tempKey).add(tempValue);
            }
        } else {
            // se non esiste la chiave, allora crea un nuovo arraylist, aggiunge il fattore e poi inserisce tutto nell'hashmap
            ArrayList<FattoreDiConversione> tempLista = new ArrayList<>();
            tempLista.add(tempValue);
            this.fattori.put(tempKey, tempLista);
        }
    }

    /**
     * Chiede all'utente di inserire un fattore di conversione tra la categoria foglia appena creata
     * e una a scelta, dopodiché lancia il metodo calcolaEAssegnaValoriDiConversione() per il calcolo
     * dei restanti
     */
    public void inserisciFattoreDiConversione(String nomeRadiceNuovaFoglia, String nomeNuovaFoglia){

        String nomeRadicePreesistente;
        String nomeFogliaPreesistente;

        //cicla la richiesta se non esiste nella hashmap la chiave per la foglia preesistente (dovrebbe essere più veloce)
        do {
            nomeRadicePreesistente = InputDati.leggiStringaNonVuota(MSG_INSERISCI_RADICE);
            nomeFogliaPreesistente = InputDati.leggiStringaNonVuota(MSG_INSERISCI_FOGLIA);
        }while (!fattori.containsKey(factorNameBuilder(nomeRadicePreesistente,nomeFogliaPreesistente)));

        double fattore = InputDati.leggiDoubleConRange(MSG_INSERISCI_FATTORE, 0.5, 2.0);

        calcolaEAssegnaValoriDiConversione(nomeRadicePreesistente, nomeFogliaPreesistente, nomeRadiceNuovaFoglia, nomeNuovaFoglia, fattore);
    }

    /**
     * Calcola i valori di conversione per tutte le categorie dato un valore dal configuratore
     */
    private void calcolaEAssegnaValoriDiConversione(String nomeRadicePreesistente, String nomeFogliaPreesistente, String nomeRadiceNuovaFoglia, String nomeNuovaFoglia, double fattoreDato){

        String fogliaNuovaFormattata = factorNameBuilder(nomeRadiceNuovaFoglia, nomeNuovaFoglia);
        String fogliaVecchiaFormattata = factorNameBuilder(nomeRadicePreesistente, nomeFogliaPreesistente);
        ArrayList<FattoreDiConversione> nuoviFattori = new ArrayList<>();


        nuoviFattori.add(new FattoreDiConversione(fogliaNuovaFormattata, fogliaVecchiaFormattata, fattoreDato));
        nuoviFattori.add(new FattoreDiConversione(fogliaVecchiaFormattata, fogliaNuovaFormattata, 1/fattoreDato));

        ArrayList<FattoreDiConversione> fattoriDaSeparare = fattori.get(fogliaVecchiaFormattata);

        //TODO valutare se aggiungere parallelismo nell'esecuzione della parte successiva
        ///int sizeFattoriDaDividere = fattoriDaSeparare.size();

        for (FattoreDiConversione f : fattoriDaSeparare) {
            double fattoreNuovo = fattoreDato * f.getFattore();
            nuoviFattori.add(new FattoreDiConversione(fogliaNuovaFormattata, f.getNome_c2(), fattoreNuovo));
            nuoviFattori.add(new FattoreDiConversione(f.getNome_c2(), fogliaNuovaFormattata, 1/fattoreNuovo));
        }

        aggiungiAllaHashmap(nuoviFattori);

    }

    /**
     * Ciclo per inserire i nuovi fattori nella Hashmap e memorizzazione
     */
    private void aggiungiAllaHashmap(ArrayList<FattoreDiConversione> nuoviFattori){

        for (FattoreDiConversione f : nuoviFattori) {
            if(fattori.containsKey(f.getNome_c1())){
                fattori.get(f.getNome_c1()).add(f);
            }else {
                ArrayList<FattoreDiConversione> tempLista = new ArrayList<>();
                tempLista.add(f);
                fattori.put(f.getNome_c1(), tempLista);
            }
        }
        serializeXML();
    }

    public String parseRadice(String nomeCategoria){
        return nomeCategoria.split(":")[0];
    }

    public String factorNameBuilder(String root, String leaf){
        return root +":"+ leaf;
    }

    /**
     * Per ogni chiave nell'HashMap fattori, si estrae il suo ArrayList e lo si serializza in XML.
     */
    public void serializeXML() {

        try {

            boolean debug = false;

            // creazione mapper e oggetto file
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            File file = new File(this.filePath);
            // se il file non esiste, lo si crea
            if (file.createNewFile()) {
                if (debug)
                    System.out.println("FILE CREATO");
            }

            for (String key : fattori.keySet()) {
                // estrazione dell'arraylist per ogni chiave key
                List<FattoreDiConversione> listaFattori = fattori.get(key);
                // scrittura su file del valore xml dell'arraylist
                xmlMapper.writeValue(file, listaFattori);
            }


        } catch (JsonProcessingException e) {
            // handle exception
            e.getMessage();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("impossibile creare file");
        }
    }


    public void deserializeXML() {
        boolean debug = false;

        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            File file = new File(this.filePath);

            if (!file.exists()) {
                if (debug)
                    System.out.println("FILE NON ESISTE. NON CARICO NIENTE.");
                return;
            }

            List<FattoreDiConversione> listaFattori = xmlMapper.readValue(file, xmlMapper.getTypeFactory().constructCollectionType(List.class, FattoreDiConversione.class));

            for (FattoreDiConversione fattore : listaFattori) {
                String tempKey = fattore.getNome_c1();
                this.addFattore(tempKey, fattore);
            }

            if (debug)
                for (FattoreDiConversione obj : listaFattori) {
                    System.out.println(obj.toString());
                }
        } catch (IOException e) {
            // handle the exception
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : fattori.keySet()) {
            for (FattoreDiConversione fattore : fattori.get(key)) {
                sb.append(fattore.toString()).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Logica grafica.
     * Per ora ci si limita all'utilizzo del terminale.
     */
    private void visualizzaFattori() {
        System.out.println("\n\n --- Visualizza Fattori ---\n\n");
        System.out.println(this.toString());
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
                //visualizza
                this.visualizzaFattori();
                break;
            case 2:
                //modifica - estensione futura
                //serializeXML();
                break;
            default:
                System.out.println("Nulla da mostrare");
        }
    }


}