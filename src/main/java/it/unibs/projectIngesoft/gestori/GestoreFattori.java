package gestori;

import attivita.FattoreDiConversione;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class GestoreFattori {

    private final String filePath;
    private HashMap<String, FattoreDiConversione> fattori;

    public GestoreFattori(String filePath) {
        this.filePath = filePath;
        this.fattori = new HashMap<>();
        //this.leggiFileFattori();
        //TODO ora la serializzazione e deserializzazione funziona, va solo fatto decentemente
        // e adattato all'hashmap
        deserializeXML();
    }

    public String getFilePath() {
        return filePath;
    }

    public HashMap<String, FattoreDiConversione> getFattori() {
        return fattori;
    }

    public void setFattori(HashMap<String, FattoreDiConversione> fattori) {
        this.fattori = fattori;
    }

    public void addFattore(String key, FattoreDiConversione value) {
        this.fattori.put(key, value);
    }

    public boolean leggiFileFattori() {
        File file;

        HashMap<String, FattoreDiConversione> tempFattori = new HashMap<>();

        try {
            file = new File(this.filePath);
            if (file.createNewFile())
                System.out.println("FILE CREATO");
            //sia che il file venga creato, sia che il file esista ritorno true => nessun problema

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

        //scrivo sul file la stringa formattata
        try {
            Scanner scanner = new Scanner(new File(this.filePath));

            while (scanner.hasNext()) {
                //System.out.println(scanner.nextLine());
                String tempString = scanner.nextLine();
                String[] words = tempString.split("\\W+");

                FattoreDiConversione tempFattore = new FattoreDiConversione(words[0], words[1], Double.parseDouble(words[2]));
                this.addFattore(words[0], tempFattore);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @return true se ha scritto con successo. false altrimenti
     */
    public boolean scriviFileFattori() {

        File file;
        try {
            file = new File(this.filePath);
            if (file.createNewFile())
                System.out.println("FILE CREATO");
            //sia che il file venga creato, sia che il file esista ritorno true => nessun problema

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

        //scrivo sul file la stringa formattata
        try (PrintWriter out = new PrintWriter(file)) {
            for (FattoreDiConversione value : fattori.values()) {
                out.println(value.toString());
            }


            System.out.println("FILE SCRITTO");
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public void serializeXML() {
        //TODO
        try {
            XmlMapper xmlMapper = new XmlMapper();

            // serialize our Object into XML string
            String xmlString = xmlMapper.writeValueAsString(new FattoreDiConversione("c1", "c2", 4.5));

            // write to the console
            System.out.println(xmlString);

            // write XML string to file
            File xmlOutput = new File("serialized.xml");
            FileWriter fileWriter = new FileWriter(xmlOutput);
            fileWriter.write(xmlString);
            fileWriter.close();
        } catch (JsonProcessingException e) {
            // handle exception
            e.getMessage();
        } catch (IOException e) {
            // handle exception
            e.getMessage();
        }
    }

    public void deserializeXML() {
        //TODO

        try {

            XmlMapper xmlMapper = new XmlMapper();
            // read file and put contents into the string
            String readContent = new String(Files.readAllBytes(Paths.get("serialized.xml")));
            // deserialize from the XML into a Phone object
            FattoreDiConversione deserializedData = xmlMapper.readValue(readContent, FattoreDiConversione.class);

            System.out.println("Deserialized data: " + deserializedData);


        } catch (IOException e) {
            // handle the exception
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
                break;
            case 2:
                //aggiungi cat f
                break;
            case 3:
                //aggiungi gerarchia
                break;
            case 4:
                //descrizione
                break;
            case 5:
                //visualizza
                break;
            default:
                System.out.println("Nulla da mostrare");
        }
    }


}