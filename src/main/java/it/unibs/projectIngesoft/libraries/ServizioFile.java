package it.unibs.fp.myutils;

import java.io.*;


public class ServizioFile {
    private static final String HEADER_WARNING = " ** ATTENZIONE ";
    private static final String WARNING_NO_FILE = "File non trovato";
    private static final String MSG_FILE_CREATO = "++ File creato con successo! ++";
    private static final String MSG_FILE_SCRITTO = "++ File scritto con successo! ++";
    private static final String WARNING_NO_LETTURA = "Problemi con la lettura del File";
    private static final String WARNING_NO_SCRITTURA = "Problemi con la scrittura del File";
    private static final String WARNING_NO_CHIUSURA = "Problemi con la chiusura del File";
    private static final String WARNING_IOEXC = "Grave errore di I/O";
    private static final String FOOTER_WARNING = " **";


    public static Object caricaSingoloOggetto(File f) {
        Object letto = null;
        ObjectInputStream ingresso = null;

        try {
            ingresso = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));

            letto = ingresso.readObject();

        } catch (FileNotFoundException excNotFound) {
            System.out.println(WARNING_NO_FILE + f.getName());
        } catch (IOException excLettura) {
            System.out.println(WARNING_NO_LETTURA + f.getName());
        } catch (ClassNotFoundException excLettura) {
            System.out.println(WARNING_NO_LETTURA + f.getName());
        } finally {
            if (ingresso != null) {
                try {
                    ingresso.close();
                } catch (IOException excChiusura) {
                    System.out.println(WARNING_NO_CHIUSURA + f.getName());
                }
            }
        } // finally

        return letto;

    } // metodo caricaSingoloOggetto


    public static void salvaSingoloOggetto(File f, Object daSalvare) {
        ObjectOutputStream uscita = null;

        try {
            uscita = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)));

            uscita.writeObject(daSalvare);

        } catch (IOException excScrittura) {
            System.out.println(WARNING_NO_SCRITTURA + f.getName());
        } finally {
            if (uscita != null) {
                try {
                    uscita.close();
                } catch (IOException excChiusura) {
                    System.out.println(WARNING_NO_CHIUSURA + f.getName());
                }
            }
        } // finally

    } // metodo salvaSingoloOggetto

    /**
     * Salva una stringa in un file.
     *
     * @param file                    File riferimento al file di salvataggio
     * @param daSalvare               String gia' formattata da salvare
     * @param createNewFileIfNotFound boolean,
     *                                true = tento di creare il file se non esiste.
     *                                false = fermo l'esecuzione del metodo se il file non esiste
     * @return true se la scrittura è avvenuta senza problemi
     */
    public static boolean salvaStringaSuFile(String file, String daSalvare, boolean createNewFileIfNotFound) {

        //controlla l'esistenza del file e tenta di crearlo
        if (createNewFileIfNotFound)
            if (!creaFileSeNonEsiste(file))
                return false;

        //scrivo sul file la stringa formattata
        try (PrintWriter out = new PrintWriter(file)) {
            out.println(daSalvare);
            System.out.println(MSG_FILE_SCRITTO);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(HEADER_WARNING + WARNING_NO_FILE + FOOTER_WARNING);
            return false;
        }
    }

    private static boolean creaFileSeNonEsiste(String directory) {
        try {
            File file = new File(directory);
            if (file.createNewFile())
                System.out.println(MSG_FILE_CREATO);
            //sia che il file venga creato, sia che il file esista ritorno true => nessun problema
            return true;

        } catch (IOException e) {
            System.out.println(HEADER_WARNING + WARNING_IOEXC + FOOTER_WARNING);
            return false;
        }
    }


}

