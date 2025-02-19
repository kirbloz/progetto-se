package it.unibs.projectIngesoft.libraries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Fornisce metodi di utilità generale per gestire l'input di dati.
 *
 * @author Libreria base fornita dal Prof. Serina Ivan durante il corso di Fondamenti di Programmazione
 * @author Modificata da Wade Giovanni Baisini, Legati Matteo
 */
public class InputDatiTerminale {

    public static final String ERR_STRINGA_TROPPO_CORTA = "Attenzione: La stringa inserita e' troppo corta, la lunghezza minima e' caratteri: ";
    public static final String ERR_STRINGA_TROPPO_LUNGA = "Attenzione: la stringa inserita e' troppo lunga, la lunghezza massima e' di caratteri: ";

    public static final String ERR_VALORI_NON_AMMESSI = "Attenzione: hai utilizzato dei valori non consentiti";
    private static final String ERRORE_FORMATO = "Attenzione: il dato inserito non e' nel formato corretto";
    private static final String ERRORE_FORMATO_VIRGOLA = "Attenzione: usa il punto come separatore decimale";
    private static final String ERRORE_MINIMO = "Attenzione: e' richiesto un valore maggiore o uguale a ";
    private static final String ERRORE_STRINGA_VUOTA = "Attenzione: non hai inserito alcun carattere";
    private static final String ERRORE_MASSIMO = "Attenzione: e' richiesto un valore minore o uguale a ";
    private static final String MESSAGGIO_AMMISSIBILI = "Attenzione: i caratteri ammissibili sono: ";
    private static final char RISPOSTA_SI = 'S';
    private static final char RISPOSTA_NO = 'N';
    private static final BufferedReader lettore = creaBufferedReader();
    private static final String ERRORE_RANGE = "Attenzione, il valore non rientra tra: ";
    public static final String ERRORE_IOEXCEPTION = "Attenzione: si e' verificato un problema con l'input.";

    private static BufferedReader creaBufferedReader() {
        return new BufferedReader((new InputStreamReader(System.in)));
    }

    public static String leggiStringa(String messaggio) {
        System.out.print(messaggio);
        try {
            return lettore.readLine();
        } catch (IOException e) {
            System.out.println(ERRORE_IOEXCEPTION);
        }
        return "";
    }

    /**
     * Metodo String che restituisce una stringa chiesta in input all'utente e presente nell'array valoriAmmessi
     *
     * @param messaggio     Il messaggio che si vuole visualizzare al momento dell'input
     * @param valoriAmmessi Tutti i valori che l'utente può scrivere nell'input
     * @return L'input
     * @author Legati Matteo
     */
    public static String stringReaderFromAvailable(String messaggio, String[] valoriAmmessi) {
        String lettura;
        do {
            System.out.print(messaggio + Arrays.toString(valoriAmmessi));
            lettura = leggiStringa("\n> ");

            if (!Arrays.asList(valoriAmmessi).contains(lettura))
                System.out.println(ERR_VALORI_NON_AMMESSI);
        } while (!Arrays.asList(valoriAmmessi).contains(lettura));
        return lettura;
    }

    public static String leggiStringaNonVuota(String messaggio) {
        boolean finito = false;
        String lettura;
        do {
            lettura = leggiStringa(messaggio);
            lettura = lettura.trim();
            if (!lettura.isEmpty())
                finito = true;
            else
                System.out.println(ERRORE_STRINGA_VUOTA);
        } while (!finito);

        return lettura;
    }

    /**
     * Metodo String che restituisce una stringa chiesta in input all'utente con l'obbligo che sia di una lunghezza compresa tra i valori specificati dai parametri maxLength e minLength
     *
     * @param messaggio Il messaggio che si vuole visualizzare al momento dell'input
     * @param minLength La lunghezza minima consentita
     * @param maxLength La lunghezza massima consentita
     * @return L'input
     * @author Legati Matteo, Wade Giovanni Baisini
     */
    public static String stringReaderSpecificLength(String messaggio, int minLength, int maxLength) {
        boolean finito = true;
        String lettura;
        do {
            lettura = leggiStringa(messaggio);
            lettura = lettura.trim();

            if (lettura.isEmpty()) {
                System.out.println(ERRORE_STRINGA_VUOTA);
                finito = false;
            } else if (lettura.length() < minLength) {
                System.out.println(ERR_STRINGA_TROPPO_CORTA + minLength);
                finito = false;
            }
            if (lettura.length() > maxLength) {
                System.out.println(ERR_STRINGA_TROPPO_LUNGA + maxLength);
                finito = false;
            } else {
                finito = true;
            }
        } while (!finito);

        return lettura;
    }

    public static char leggiChar(String messaggio) {
        boolean finito = false;
        char valoreLetto = '\0';
        do {
            System.out.print(messaggio);
            try {
                String lettura = lettore.readLine();
                if (!lettura.isEmpty()) {
                    valoreLetto = lettura.charAt(0);
                    finito = true;
                } else {
                    System.out.println(ERRORE_STRINGA_VUOTA);
                }
            } catch (IOException e) {
                System.out.println(ERRORE_IOEXCEPTION);
            }
        } while (!finito);
        return valoreLetto;
    }

    public static char leggiUpperChar(String messaggio, String ammissibili) {
        boolean finito = false;
        char valoreLetto;
        do {
            valoreLetto = leggiChar(messaggio);
            valoreLetto = Character.toUpperCase(valoreLetto);
            if (ammissibili.indexOf(valoreLetto) != -1)
                finito = true;
            else
                System.out.println(MESSAGGIO_AMMISSIBILI + ammissibili);
        } while (!finito);
        return valoreLetto;
    }

    public static int leggiIntero(String messaggio) {
        boolean finito = false;
        int valoreLetto = 0;
        do {
            System.out.print(messaggio);
            try {
                String lettura = lettore.readLine();
                valoreLetto = Integer.parseInt(lettura);
                finito = true;
            } catch (IOException e) {
                System.out.println(ERRORE_IOEXCEPTION);
            } catch (NumberFormatException e) {
                System.out.println(ERRORE_FORMATO);
            }
        } while (!finito);
        return valoreLetto;
    }

    public static int leggiInteroPositivo(String messaggio) {
        return leggiInteroConMinimo(messaggio, 1);
    }

    public static int leggiInteroNonNegativo(String messaggio) {
        return leggiInteroConMinimo(messaggio, 0);
    }

    public static int leggiInteroConMinimo(String messaggio, int minimo) {
        boolean finito = false;
        int valoreLetto;
        do {
            valoreLetto = leggiIntero(messaggio);
            if (valoreLetto >= minimo)
                finito = true;
            else
                System.out.println(ERRORE_MINIMO + minimo);
        } while (!finito);

        return valoreLetto;
    }

    public static int leggiIntero(String messaggio, int minimo, int massimo) {
        boolean finito = false;
        int valoreLetto;
        do {
            valoreLetto = leggiIntero(messaggio);
            if (valoreLetto >= minimo && valoreLetto <= massimo)
                finito = true;
            else if (valoreLetto < minimo)
                System.out.println(ERRORE_MINIMO + minimo);
            else
                System.out.println(ERRORE_MASSIMO + massimo);
        } while (!finito);

        return valoreLetto;
    }

    public static int leggiInteroRange(String msg, int min, int max) {
        int read;
        do {
            read = InputDatiTerminale.leggiInteroConMinimo(msg, min);
        } while (read > max);
        return read;
    }

    public static double leggiDouble(String messaggio) {
        boolean finito = false;
        double valoreLetto = 0;
        do {
            System.out.print(messaggio);
            try {
                String lettura = lettore.readLine();
                valoreLetto = Double.parseDouble(lettura);
                finito = true;
            }catch (IOException e){
                System.out.println(ERRORE_IOEXCEPTION);
            } catch (InputMismatchException e) {
                System.out.println(ERRORE_FORMATO);
            } catch (NumberFormatException e) {
            System.out.println(ERRORE_FORMATO_VIRGOLA);
        }
        } while (!finito);
        return valoreLetto;
    }

    public static double leggiDoubleConMinimo(String messaggio, double minimo) {
        boolean finito = false;
        double valoreLetto;
        do {
            valoreLetto = leggiDouble(messaggio);
            if (valoreLetto >= minimo)
                finito = true;
            else
                System.out.println(ERRORE_MINIMO + minimo);
        } while (!finito);

        return valoreLetto;
    }

    public static boolean yesOrNo(String messaggio) {
        String mioMessaggio = messaggio + "(" + RISPOSTA_SI + "/" + RISPOSTA_NO + ")";
        char valoreLetto = leggiUpperChar(mioMessaggio, String.valueOf(RISPOSTA_SI) + RISPOSTA_NO);
        return valoreLetto == RISPOSTA_SI;
    }

    public static double leggiDoubleConRange(String msg, double minimo, double massimo) {
        boolean finito = false;
        double valoreLetto;
        do {
            valoreLetto = leggiDouble(msg);
            if (valoreLetto >= minimo && valoreLetto <= massimo)
                finito = true;
            else
                System.out.println(ERRORE_RANGE + minimo + " e " + massimo);
        } while (!finito);

        return valoreLetto;
    }

    public static List<String> inserisciListaStringheUnivoche(String messaggioDiInserimento, boolean caseSensitive) {
        List<String> listaStringhe = new ArrayList<String>();
        boolean continuare = false;
        do{
            String s = leggiStringaNonVuota(messaggioDiInserimento);
            if(caseSensitive){
                boolean giaPresente = false;
                for(String stringaInLista : listaStringhe){
                    if(stringaInLista.equalsIgnoreCase(s)){
                        giaPresente = true;
                        break;
                    }
                }
                if(!giaPresente){
                    listaStringhe.add(s);
                }
            }else{
                boolean giaPresente = false;
                for(String stringaInLista : listaStringhe){
                    if(stringaInLista.equals(s)){
                        giaPresente = true;
                        break;
                    }
                }
                if(!giaPresente){
                    listaStringhe.add(s);
                }
            }
            continuare = yesOrNo("Continuare l'inserimento?");
        }while(continuare);

        return listaStringhe;
    }

    public static String stringReaderNotInAvailable(String prompt, String[] array){
        String scelto;
        boolean giaInUso;
        do {
            giaInUso = false;
            scelto = leggiStringaNonVuota(prompt);
            for (String s : array) {
                if (s.equalsIgnoreCase(scelto)) {
                    giaInUso = true;
                    break;
                }
            }
        } while (giaInUso);

        return scelto;
    }

}
