package attivita;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class GestoreFattori {

    private final String filePath;
    private HashMap<String, FattoreDiConversione> fattori;

    public GestoreFattori(String filePath) {
        this.filePath = filePath;
        this.fattori = new HashMap<>();
        this.leggiFileFattori();
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
     *
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

}
