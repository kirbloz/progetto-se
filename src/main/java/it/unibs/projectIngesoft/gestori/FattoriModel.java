package it.unibs.projectIngesoft.gestori;


import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.attivita.FattoriWrapper;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.XMLparsing.Serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Si occupa di calcolare, memorizzare e leggere i Fattori di Conversione relativi alle Categorie.
 * Non conosce chi gestisce le categorie, ma mette a disposizione i suoi metodi.
 */
public class FattoriModel {

    public static final double MIN_FATTORE = 0.5;
    public static final double MAX_FATTORE = 2.0;


    private final String filePath;

    private HashMap<String, ArrayList<FattoreDiConversione>> fattori;

    public FattoriModel(String filePath) {
        this.filePath = filePath;
        this.fattori = new HashMap<>();
        deserializeXML(); //load dati
    }

    /**
     * Serializza l'oggetto fattori trasformato dalla classe FattoriWrapper.
     * Sfrutto l'implementazione statica della classe Serializer.
     */
    public void serializeXML() {
        assert this.filePath != null;
        assert this.fattori != null;
        Serializer.serialize(this.filePath, new FattoriWrapper(fattori));
    }

    /**
     * De-serializza l'oggetto fattori tramite la classe FattoriWrapper.
     * Sfrutto l'implementazione statica della classe Serializer.
     */
    public void deserializeXML() {
        FattoriWrapper tempWrapper = Serializer.deserialize(new TypeReference<>() {
        }, filePath);
        if (tempWrapper != null) {
            fattori = tempWrapper.toHashMap();
        }
    }

    public boolean esisteCategoriaChiave(String chiave) {
        return fattori.containsKey(chiave);
    }

    /**
     * Inserisce un nuovo fattore nell'HashMap, verificando eventuali duplicati.
     * Garantisce il formato di chiave (string root:leaf) e valore (oggetto FdC)
     *
     * @param tempKey,   nome della prima categoria della coppia
     * @param tempValue, oggetto FdC da inserire nella lista
     */
    public void addFattore(String tempKey, FattoreDiConversione tempValue) {
        /// Nota: aggiungo qui il controllo sul valore del fattore
        FattoreDiConversione fattore = controllaESostituisciValoriFuoriScala(tempValue);
        // computeIfAbsent verifica se la chiave tempKey esiste già: se non esiste, la aggiunge e crea una nuova arraylist come valore
        // in entrambi i casi, aggiunge il fattore alla lista.
        this.fattori.computeIfAbsent(tempKey, k -> new ArrayList<>()).add(fattore);
    }

    private FattoreDiConversione controllaESostituisciValoriFuoriScala(FattoreDiConversione fattoreDaControllare) {
        if (fattoreDaControllare.getFattore() > MAX_FATTORE) {
            return new FattoreDiConversione(fattoreDaControllare.getNome_c1(), fattoreDaControllare.getNome_c2(), MAX_FATTORE);
        } else if (fattoreDaControllare.getFattore() < MIN_FATTORE) {
            return new FattoreDiConversione(fattoreDaControllare.getNome_c1(), fattoreDaControllare.getNome_c2(), MIN_FATTORE);
        }
        return fattoreDaControllare;
    }

    /**
     * Chiama addFattore per ogni fattore di quelli in parametro.
     *
     * @param fattori, list di fattori
     */
    public void aggiungiArrayListDiFattori(List<FattoreDiConversione> fattori) {
        for (FattoreDiConversione f : fattori) {
            addFattore(f.getNome_c1(), f);
        }
    }


    /**
     * Chiede all'utente i fattori di conversione minimi necessari al calcolo dei restanti e avvia il calcolo degli altri.
     *
     * @param nomeRadice, categoria radice della gerarchia per cui si aggiungono i fattori
     * @param foglie,     foglie della gerarchia
     */
    public void inserisciFattoriDiConversione(String nomeRadice, List<Categoria> foglie) {
        //0. preparo i fattori tra le foglie della nuova gerarchia
        ArrayList<FattoreDiConversione> nuoviDaNuovaRadice = calcolaNuoviFattori(nomeRadice, foglie);

        // caso hashmap non è vuota ma c'è solo una chiave => hai solo una foglia esterna
        // OPPURE hashmap non è vuota e c'è più di una chiave => situazione standard
        if (!fattori.isEmpty()) {
            // 1. scegliere una categoria per cui i fattori siano GIA' stati calcolati
            // -> permette di calcolare tutti i nuovi chiedendo un solo inserimento di valore del fattore
            for (String key : fattori.keySet()) {
                System.out.println(key);
            }
            String nomeFogliaEsternaFormattata = selezioneFoglia(MSG_INSERISCI_FOGLIA_ESTERNA);
            // 2. scegliere una categoria delle nuove, da utilizzare per il primo fattore di conversione
            String nomeFogliaInternaFormattata = selezioneFogliaDaLista(nomeRadice, foglie);

            //3. chiedi il fattore di conversione tra le 2 [x in (Old:A New:A x)]
            double fattoreDiConversioneEsternoInterno = InputDatiTerminale.leggiDoubleConRange(
                    INSERISCI_IL_FATTORE_TRA.formatted(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata),
                    MIN_FATTORE, MAX_FATTORE);

            // 4. fa i calcolini e salva i fattori nella hashmap
            FattoreDiConversione primoFattoreEsternoInterno = new FattoreDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneEsternoInterno);
            ArrayList<FattoreDiConversione> fattoriEsterni = calcoloFattoriEsterni(primoFattoreEsternoInterno, nuoviDaNuovaRadice);

            aggiungiArrayListDiFattori(nuoviDaNuovaRadice);
            aggiungiArrayListDiFattori(fattoriEsterni);

        }
        // caso hashmap è vuota e hai i nuovi => è la prima radice ed ha più di una foglia,
        else if (!nuoviDaNuovaRadice.isEmpty()) {
            // 1. aggiungi i nuovi fattori (sono gli unici)
            aggiungiArrayListDiFattori(nuoviDaNuovaRadice);
        }
        // caso hashmap è vuota e non hai i nuovi => è la prima radice ed hai solo una foglia nuova
        else {
            // 1. prepara la chiave e un arraylist vuoto, non puoi calcolare nessun fattore
            fattori.put(factorNameBuilder(nomeRadice, foglie.getFirst().getNome()), new ArrayList<>());
        }

        serializeXML(); // memorizza su file
    }

    /**
     * Cicla le foglie dalla prima all'ultima per generare tutte le coppie di valori possibili
     * (combinazione senza ripetizione) e per ogni coppia generata chiede il fattore all'utente.
     * Genera il FattoreDiConversione + il suo inverso e li memorizza.
     *
     * @param nomeRadice, nome della radice delle foglie
     * @param foglie,     lista di foglie
     * @return lista di fattori di conversione
     */
    private ArrayList<FattoreDiConversione> calcolaNuoviFattori(String nomeRadice, List<Categoria> foglie) {
        if (foglie.isEmpty())
            return new ArrayList<>();
        // se esiste almeno una foglia, allora calcola i fattori
        ArrayList<FattoreDiConversione> nuoviDaNuovaRadice = new ArrayList<>();
        for (int i = 0; i < foglie.size(); i++) {
            String nomeFogliai = factorNameBuilder(nomeRadice, foglie.get(i).getNome());
            for (int j = i + 1; j < foglie.size(); j++) {
                String nomeFogliaj = factorNameBuilder(nomeRadice, foglie.get(j).getNome());
                double fattore_ij = InputDatiTerminale.leggiDoubleConRange(INSERISCI_IL_FATTORE_TRA.formatted(nomeFogliai, nomeFogliaj), MIN_FATTORE, MAX_FATTORE);

                FattoreDiConversione fattoreIJ = new FattoreDiConversione(nomeFogliai, nomeFogliaj, fattore_ij);
                FattoreDiConversione fattoreJI = generaInverso(fattoreIJ);

                nuoviDaNuovaRadice.add(fattoreIJ);
                nuoviDaNuovaRadice.add(fattoreJI);
            }
        }
        return nuoviDaNuovaRadice;
    }

    /**
     * Permette di scegliere una categoria foglia tra quelle appena create.
     * Guida l'immissione del nome e radice della categoria.
     *
     * @param nomeRadice, nome della radice delle foglie
     * @param foglie,     lista di foglie appena create
     * @return stringa formattata come "radice:foglia"
     */
    private String selezioneFogliaDaLista(String nomeRadice, List<Categoria> foglie) {
        // stampa categorie interne (foglie della nuova radice)
        System.out.println(MSG_INSERISCI_FOGLIA_INTERNA);
        for (Categoria foglia : foglie) {
            System.out.println(factorNameBuilder(nomeRadice, foglia.getNome()));
        }
        // immissione della foglia e verifica che sia corretto [New:A in (Old:A New:A x)]
        String nomeFogliaNonFormattato;
        boolean fogliaEsiste = false;
        do {
            nomeFogliaNonFormattato = InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_FOGLIA);
            for (Categoria foglia : foglie) {
                if (foglia.getNome().equals(nomeFogliaNonFormattato)) {
                    fogliaEsiste = true;
                    break;
                }
            }
        } while (!fogliaEsiste);

        return factorNameBuilder(nomeRadice, nomeFogliaNonFormattato);
    }

    /**
     * Permette di scegliere una categoria foglia tra quelle che già hanno tutti i fattori di conversione calcolati
     * e memorizzati. Guida l'immissione del nome e radice della categoria.
     *
     * @return stringa formattata come "radice:foglia"
     */
    public String selezioneFoglia(String messaggio) {
        for (String key : fattori.keySet()) {
            System.out.println(key);
        }
        // inserimento guidato e controllo [Old:A in (Old:A New:A x)]
        return inserimentoNomeFogliaFormattato(messaggio);
    }

    public String inserimentoNomeFogliaFormattato(String messaggio) {
        System.out.println(messaggio);
        String nomeFogliaFormattato;
        do {
            nomeFogliaFormattato = factorNameBuilder(
                    InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_RADICE),
                    InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_FOGLIA)
            );
        } while (!fattori.containsKey(nomeFogliaFormattato));
        return nomeFogliaFormattato;
    }


    /**
     * Calcola tutti i fattori che hanno una foglia nuova e una preesistente,
     * ovvero appartenente a una gerarchia diversa da quella nuova.
     *
     * @param primoFattoreEsternoInterno, fattore creato tra una foglia nuova e una preesistente.
     *                                    Permette il calcolo di tutti gli altri
     * @param fattoriInterni,             fattori di conversione tra tutte le foglie della gerarchia appena creata
     * @return fattoriEsterni, lista di fattori appena calcolati
     */
    private ArrayList<FattoreDiConversione> calcoloFattoriEsterni(FattoreDiConversione primoFattoreEsternoInterno, ArrayList<FattoreDiConversione> fattoriInterni) {
        ArrayList<FattoreDiConversione> fattoriCalcolati = new ArrayList<>();

        //L'insieme di tutti i fattori che hanno come <c1> la foglia esterna scelta e come <c2> tutte le foglie Interne (quelle passate dal nuovo albero)
        ArrayList<FattoreDiConversione> nuoviSingoloEsternoAInterni = new ArrayList<>();
        //L'insieme di tutti i fattori che hanno come <c1> tutte le foglie Interne e come <c2> la foglia esterna scelta (Gli inversi)
        ArrayList<FattoreDiConversione> nuoviInterniASingoloEsterno = new ArrayList<>();

        //Infilo il fattore dato (Old:A New:A x)
        nuoviSingoloEsternoAInterni.add(primoFattoreEsternoInterno);
        //Creo e infilo il reciproco del fattore dato(New:A Old:A x)
        FattoreDiConversione primoFattoreInternoEsterno = generaInverso(primoFattoreEsternoInterno);
        nuoviInterniASingoloEsterno.add(primoFattoreInternoEsterno);


        ///Calcolo fattori Esterno-Interni (se c'è più di una foglia interna)
        if (!fattoriInterni.isEmpty()) {
            for (FattoreDiConversione nuovo : fattoriInterni) {
                //Se New:A in (Old:A New:A x) == New:A in (New:A New:X y) => genera (Old:A New:X x*y)
                if (primoFattoreEsternoInterno.getNome_c2().equals(nuovo.getNome_c1())) {
                    FattoreDiConversione nuovoSingoloEsternoAInterno = new FattoreDiConversione(primoFattoreEsternoInterno.getNome_c1(), nuovo.getNome_c2(), primoFattoreEsternoInterno.getFattore() * nuovo.getFattore());
                    FattoreDiConversione nuovoInternoASingoloEsterno = generaInverso(nuovoSingoloEsternoAInterno);

                    nuoviSingoloEsternoAInterni.add(nuovoSingoloEsternoAInterno);
                    nuoviInterniASingoloEsterno.add(nuovoInternoASingoloEsterno);
                }
            }
        }

        ArrayList<FattoreDiConversione> fattoriInternoATuttiEsterni = new ArrayList<>();
        //per ogni nuovo fattore nel formato (Interno:X Esterno:A)
        for (FattoreDiConversione fattoreInternoASingoloEsterno : nuoviInterniASingoloEsterno) {
            //Per ogni Fattore che ha come Key la foglia Esterno:A
            for (FattoreDiConversione fattoreInMemoria : fattori.get(fattoreInternoASingoloEsterno.getNome_c2())) {
                //Genero i fattori con tutto il resto del fuori
                FattoreDiConversione fattoreInternoEsterno = new FattoreDiConversione(fattoreInternoASingoloEsterno.getNome_c1(), fattoreInMemoria.getNome_c2(), fattoreInternoASingoloEsterno.getFattore() * fattoreInMemoria.getFattore());
                FattoreDiConversione fattoreEsternoInterno = generaInverso(fattoreInternoEsterno);
                fattoriInternoATuttiEsterni.add(fattoreInternoEsterno);
                fattoriInternoATuttiEsterni.add(fattoreEsternoInterno);
            }
        }

        fattoriCalcolati.addAll(fattoriInternoATuttiEsterni); //Aggiungo l'array di fattori esterni
        fattoriCalcolati.addAll(nuoviSingoloEsternoAInterni); //Aggiungo l'array di diretti
        fattoriCalcolati.addAll(nuoviInterniASingoloEsterno); //Aggiungo l'array di inversi al primo

        return fattoriCalcolati;
    }

    /**
     * Dato un fattore ne restituisce l'inverso
     *
     * @param fattore, fattore di conversione di cui calcolare l'inverso
     * @return fattoreInverso
     */
    private FattoreDiConversione generaInverso(FattoreDiConversione fattore) {
        return new FattoreDiConversione(fattore.getNome_c2(), fattore.getNome_c1(), 1 / fattore.getFattore());
    }

    /**
     * Costruisce la stringa che rappresenta univocamente un fattore.
     * Concatena il nome della radice, un ":" e il nome della categoria foglia di riferimento.
     *
     * @param root, categoria radice della categoria foglia
     * @param leaf, categoria foglia di riferimento
     * @return stringa formattata
     */
    public String factorNameBuilder(String root, String leaf) {
        return root + ":" + leaf;
    }

    /**
     * Dato il nome di una categoria ritorna una stringa formattata con tutti i fattori di conversione relativi a quella categoria
     *
     * @param categoriaFormattata, nome categoria nel formato root:leaf
     * @return String
     */
    public String stringaFattoriDataCategoria(String categoriaFormattata) {
        StringBuilder sb = new StringBuilder();
        if (fattori.containsKey(categoriaFormattata)) {
            for (FattoreDiConversione f : fattori.get(categoriaFormattata)) {
                String valoreFormattato = String.format("%.3f", f.getFattore());
                sb.append("[ ")
                        .append(f.getNome_c1()).append(", ").append(f.getNome_c2())
                        .append(", ").append(valoreFormattato).append(" ]\n");
            }
            return sb.toString();
        } else return WARNING_CATEGORIA_NON_ESISTE;
    }

    /**
     * Visualizza in maniera ordinata i fattori di conversione con tutte le loro informazioni.
     * I fattori sono raggruppati in base alla prima categoria.
     */
    private void visualizzaFattori() {
        if (fattori.isEmpty()) {
            System.out.println(WARNING_NO_FATTORI_MEMORIZZATI);
            return;
        }
        for (String key : fattori.keySet()) {
            System.out.println(key);
        }
        System.out.println(MSG_INSERISCI_CATEGORIA_VISUALIZZA_FATTORI);
        String radice = InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_RADICE);
        String foglia = InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_FOGLIA);
        System.out.println(stringaFattoriDataCategoria(factorNameBuilder(radice, foglia)));
    }

    /**
     * Richiama il metodo necessario in base alla selezione dal menu.
     *
     * @param scelta, selezione dal menu
     */
    public void entryPoint(int scelta) {
        switch (scelta) {
            case 1 -> visualizzaFattori();
            default -> System.out.println("Nulla da mostrare");
        }
    }

    /**
     * Calcola le ore da erogare della categoria offerta, dato un certo numero di ore
     * della categoria richiesta. Si usano i fattori di conversione.
     *
     * @param richiesta,    categoria richiesta dal propositore
     * @param offerta,      categoria offerta dal propositore
     * @param oreRichiesta, ore di categoria richiesta indicate dal propositore
     * @return numero di ore arrontondato all'intero più vicino;
     * se non esistono fattori di conversione tra le categorie indicate, ritorna -1
     */
    public int calcolaRapportoOre(String richiesta, String offerta, int oreRichiesta) {
        for (FattoreDiConversione f : fattori.get(richiesta)) {
            if (f.getNome_c2().equals(offerta)) {
                return (int) Math.rint(oreRichiesta * f.getFattore());
            }
        }
        return -1;
    }
}