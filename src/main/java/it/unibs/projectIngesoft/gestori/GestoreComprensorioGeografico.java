package it.unibs.projectIngesoft.gestori;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unibs.fp.myutils.InputDati;
import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestoreComprensorioGeografico {

    private static final String MESSAGGIO_INSERIMENTO_COMUNE = ">> Inserire nome del comune da inserire oppure fine per terminare l'inserimento: ";
    private static final String MESSAGGIO_INSERIMENTO_NOME_NUOVO_COMPRENSORIO = ">> Inserire il nome del comprensorio Geografico: ";
    private static final String MESSAGGIO_INSERIMENTO_COMUNE_2 = ">> Inserire nome del comune da inserire: ";
    private static final String MESSAGGIO_RICERCA_COMPRENSORIO = ">> Inserire il nome del comprensorio da stampare: ";

    private final String filePath;

    private ArrayList<ComprensorioGeografico> listaComprensoriGeografici;

    public GestoreComprensorioGeografico(String filePath) {
        this.filePath = filePath;
        this.listaComprensoriGeografici = new ArrayList<>();
        deserializeXML(); // load dati
    }


    private void addComprensorio(ComprensorioGeografico comprensorio) { //Aggiunge il comprensorio alla lista e serializza
        if (!this.listaComprensoriGeografici.contains(comprensorio)) {
            this.listaComprensoriGeografici.add(comprensorio);
            serializeXML();
        }

    }

    public void inserisciComprensorio(){
        ArrayList<String> comuniDelComprensorio = new ArrayList<>();

        ///User Interaction per la creazione del comprensorio
        //TODO controllo stupido dell'unicità perché ho fritto il cervello letteralmente sono scemo qualcuno lo cambi
        boolean nomeGiaUsato = false;
        String nomeComprensorio = InputDati.leggiStringaNonVuota(MESSAGGIO_INSERIMENTO_NOME_NUOVO_COMPRENSORIO);
        for (ComprensorioGeografico comprensorio : this.listaComprensoriGeografici) {
            if(comprensorio.getNomeComprensorio().equalsIgnoreCase(nomeComprensorio)){
                nomeGiaUsato = true;
                break;
            }
        }
        while(nomeGiaUsato){
            nomeGiaUsato = false;
            nomeComprensorio = InputDati.leggiStringaNonVuota(MESSAGGIO_INSERIMENTO_NOME_NUOVO_COMPRENSORIO);
            for (ComprensorioGeografico comprensorio : this.listaComprensoriGeografici) {
                if(comprensorio.getNomeComprensorio().equalsIgnoreCase(nomeComprensorio)){
                    nomeGiaUsato = true;
                    break;
                }
            }
        }

        String nomeComuneDaInserire = InputDati.leggiStringaNonVuota(MESSAGGIO_INSERIMENTO_COMUNE);
        while(!nomeComuneDaInserire.equalsIgnoreCase("fine")){
            comuniDelComprensorio.add(nomeComuneDaInserire);
            nomeComuneDaInserire = InputDati.leggiStringaNonVuota(MESSAGGIO_INSERIMENTO_COMUNE);
        }

        //memorizzazione del nuovo comprensorio
        addComprensorio(new ComprensorioGeografico(nomeComprensorio, comuniDelComprensorio));
    }

    public void scegliComprensorioDaStampare(){
        //Per Debug
        for (ComprensorioGeografico comprensorio : this.listaComprensoriGeografici) {
            stampaComprensorio(comprensorio);
        }
        //Fine Debug

        String nomeDaCercare = InputDati.leggiStringaNonVuota(MESSAGGIO_RICERCA_COMPRENSORIO);
        for (ComprensorioGeografico comprensorio : this.listaComprensoriGeografici) {
            if(comprensorio.getNomeComprensorio().equalsIgnoreCase(nomeDaCercare)){
                stampaComprensorio(comprensorio);
                return;
            }
        }
        System.out.println("questo nome non esiste");
    }

    public void stampaComprensorio(ComprensorioGeografico comprensorio){
        System.out.println(comprensorio.toString());
    }

    //non richiesto in v1
    public void aggiungiComuneA(ComprensorioGeografico comprensorio){
        String comune = InputDati.leggiStringaNonVuota(MESSAGGIO_INSERIMENTO_COMUNE_2);
        comprensorio.addComune(comune);
    }


    //copiato da gesorecategorie
    public void entryPoint(int scelta) {
        //TODO
        switch (scelta) {
            case 1:
                //aggiungi comprensoiro
                inserisciComprensorio();
                break;
            case 2:
                //Stampa Comprensorio
                scegliComprensorioDaStampare();
                break;
            default:
                System.out.println("Nulla da mostrare");
        }
    }


    //ROBA COPIATA DAGLI ALTRI GESTORI

    /**
     * TODO copiato e incollato da gestoreutenti
     */
    public void deserializeXML() {
        boolean debug = false;

        try {
            XmlMapper xmlMapper = new XmlMapper();
            File file = new File(this.filePath);

            if (!file.exists()) {
                if (debug)
                    System.out.println("FILE NON ESISTE. NON CARICO NIENTE.");
                return;
            }

            List<ComprensorioGeografico> listaComprensoriGeografici = xmlMapper.readValue(file, new TypeReference<>() {
            });

            for (ComprensorioGeografico comprensorio : listaComprensoriGeografici) {
                this.addComprensorio(comprensorio);
            }

            if (debug)
                for (ComprensorioGeografico obj : listaComprensoriGeografici) {
                    System.out.println(obj.toString());
                }
        } catch (IOException e) {
            // handle the exception
            System.out.println(e.getMessage());
        }
    }


    public void serializeXML() {

        try {

            boolean debug = false;

            // creazione mapper e oggetto file
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

            File file = new File(this.filePath);
            // se il file non esiste, lo si crea
            if (file.createNewFile()) {
                if (debug)
                    System.out.println("FILE CREATO");
            }
            xmlMapper.writeValue(file, this.listaComprensoriGeografici);


        } catch (JsonProcessingException e) {
            // handle exception
            System.out.println(e.getMessage());

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    /*
    //TODO da fixare la lib

    Scanner lettore = creaScanner();
    public String leggiStringaNonVuota(String messaggio) {

        boolean finito = false;
        String lettura;
        do {
            lettura = leggiStringa(messaggio);
            if (lettura.length() > 0)
                finito = true;
            else
                System.out.println("ERRORE_STRINGA_VUOTA");
        } while (!finito);

        return lettura;
    }

    private Scanner creaScanner() {
        return new Scanner(System.in);
    }

    public String leggiStringa(String messaggio) {
        System.out.print(messaggio);
        return lettore.next();
    }
    */

}
