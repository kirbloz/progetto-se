package it.unibs.projectIngesoft.model;


import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.mappers.FattoriMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibs.projectIngesoft.libraries.Utilitas;
import static it.unibs.projectIngesoft.libraries.Utilitas.MAX_FATTORE;
import static it.unibs.projectIngesoft.libraries.Utilitas.MIN_FATTORE;

/**
 * Si occupa di calcolare, memorizzare e leggere i Fattori di Conversione relativi alle Categorie.
 * Non conosce chi gestisce le categorie, ma mette a disposizione i suoi metodi.
 */
public class FattoriModel {


    private Map<String, List<FattoreDiConversione>> hashMapFattori;
    private final FattoriMapper mapper;

    public FattoriModel(FattoriMapper mapper) {
        this.mapper = mapper;
        hashMapFattori = mapper.read();
        if(hashMapFattori == null) {
            hashMapFattori = new HashMap<>();
        }
    }

    /**
     * Costruttore vuoto e finto. Non fa nulla. /todo e allora perché esiste ed è usato 3 volte diomerda?
     */
    public FattoriModel() {
        this.mapper = null;
        //empty constructor
        System.out.println("fake fattoriModel");
    }

    ////////////////////////////////////////////////////// CORPO //////////////////////////////////////////////////////

    //TODO refactoring in progress
    public void inserisciFattoriDiConversione(String nomeFogliaEsternaFormattata, String nomeFogliaInternaFormattata, double fattoreDiConversioneEsternoInterno, ArrayList<FattoreDiConversione> nuoviDaNuovaRadice) {
        /// Nuovo calcolo inversi
        nuoviDaNuovaRadice.addAll(calcolaInversi(nuoviDaNuovaRadice));

        // caso hashmap non è vuota ma c'è solo una chiave => hai solo una foglia esterna
        // OPPURE hashmap non è vuota e c'è più di una chiave => situazione standard
        if (!hashMapFattori.isEmpty()) {
            // 4. fa i calcolini e salva i fattori nella hashmap
            FattoreDiConversione primoFattoreEsternoInterno = new FattoreDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneEsternoInterno);
            ArrayList<FattoreDiConversione> fattoriEsterni = calcoloFattoriEsterni(primoFattoreEsternoInterno, nuoviDaNuovaRadice);

            aggiungiArrayListDiFattori(nuoviDaNuovaRadice);
            aggiungiArrayListDiFattori(fattoriEsterni);
            //todo this is checked: should work
        }
        // caso hashmap è vuota e hai i nuovi => è la prima radice ed ha più di una foglia,
        else if (!nuoviDaNuovaRadice.isEmpty()) {
            // 1. aggiungi i nuovi fattori (sono gli unici)
            aggiungiArrayListDiFattori(nuoviDaNuovaRadice);
            //todo this is checked: should work
        }

        mapper.write(hashMapFattori);
    }

    public void inserisciSingolaFogliaNellaHashmap(String nomeRadice, List<Categoria> foglie){
        // caso hashmap è vuota e non hai i nuovi => è la prima radice ed hai solo una foglia nuova
        // 1. prepara la chiave e un arraylist vuoto, non puoi calcolare nessun fattore
        hashMapFattori.put(Utilitas.factorNameBuilder(nomeRadice, foglie.getFirst().getNome()), new ArrayList<>());
        //todo if(wade != stronzo) this.should_work
    }

    private ArrayList<FattoreDiConversione> calcolaInversi(ArrayList<FattoreDiConversione> fattoriIJ) {
        ArrayList<FattoreDiConversione> fattoriInversi = new ArrayList<>();
        for (FattoreDiConversione f : fattoriIJ) {
            fattoriInversi.add(generaInverso(f));
        }
        return fattoriInversi;
    }

    public boolean isEmpty(){
        return hashMapFattori.isEmpty();
    }

    /*
     * Dato il nome di una categoria ritorna una stringa formattata con tutti i fattori di conversione relativi a quella categoria
     *
     * @param categoriaFormattata, nome categoria nel formato root:leaf
     * @return String
     *
    public String stringaFattoriDataCategoria(String categoriaFormattata) {
        StringBuilder sb = new StringBuilder();
        if (hashListaFattori.containsKey(categoriaFormattata)) {
            for (FattoreDiConversione f : hashListaFattori.get(categoriaFormattata)) {
                String valoreFormattato = String.format("%.3f", f.getFattore());
                sb.append("[ ")
                        .append(f.getNome_c1()).append(", ").append(f.getNome_c2())
                        .append(", ").append(valoreFormattato).append(" ]\n");
            }
            return sb.toString();
        } else return WARNING_CATEGORIA_NON_ESISTE;
    }

    /*
    /**
     * Richiama il metodo necessario in base alla selezione dal menu.
     *
     * @param scelta, selezione dal menu
     *
    public void entryPoint(int scelta) {
        switch (scelta) {
            case 1 -> visualizzaFattori();
            default -> System.out.println("Nulla da mostrare");
        }
    }
    */




    ////////////////////////////////////////////////// RIFATTORIZZATO //////////////////////////////////////////////////

    /**
     * Inserisce un nuovo fattore nell'HashMap, verificando eventuali duplicati.
     * Garantisce il formato di chiave (string root:leaf) e valore (oggetto FdC)
     *
     * @param chiaveFattore,   nome della prima categoria della coppia
     * @param fattoreDaInserire, oggetto FdC da inserire nella lista
     */
    public void addFattore(String chiaveFattore, FattoreDiConversione fattoreDaInserire) {
        /// Nota: aggiungo qui il controllo sul valore del fattore
        FattoreDiConversione fattore = controllaESostituisciValoriFuoriScala(fattoreDaInserire);
        // computeIfAbsent verifica se la chiave chiaveFattore esiste già: se non esiste, la aggiunge e crea una nuova arraylist come valore.
        // In entrambi i casi, aggiunge il fattore alla lista.
        this.hashMapFattori.computeIfAbsent(chiaveFattore, k -> new ArrayList<>()).add(fattore);
    }

    /**
     * controlla ed eventualmente sostituisce (se fuori scala) il valore del fattoreDaControllare
     * @param fattoreDaControllare, fattore da controllare
     */
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
        mapper.write(hashMapFattori);
    }


    /**
     * Getter normalissimo
     */
    public Map<String, List<FattoreDiConversione>> getHashMapFattori() {
        return new HashMap<>(hashMapFattori);
    }

    /**
     * Controlla se la stringa "chiave" è una chiave della hashmap
     * @param chiave, stringa da controllare
     * @return true, se la chiave è presente nel keyset, false altrimenti
     */
    public boolean existsKeyInHashmapFattori(String chiave) {
        return hashMapFattori.containsKey(chiave);
    }


    /**TODO doublecheck funzionamento logico
     * Calcola tutti i fattori che hanno una foglia nuova e una preesistente,
     * ovvero appartenente a una gerarchia diversa da quella nuova.
     *
     * @param primoFattoreEsternoInterno, fattore creato tra una foglia preesistente e una nuova.
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
            for (FattoreDiConversione fattoreInMemoria : hashMapFattori.get(fattoreInternoASingoloEsterno.getNome_c2())) {
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
     * Restituisce la lista di FattoriDiConversione richiesta tramite coppia radice-foglia
     */
    public List<FattoreDiConversione> getFattoriFromFoglia(String radice, String foglia) {
        if (hashMapFattori.isEmpty()) {
            return null;
        }
        return hashMapFattori.get(Utilitas.factorNameBuilder(radice, foglia));
    }

    /**
     * Restituisce array di stringhe contenenti i keyset
     * @return keySet, array di stringhe contenenti i keyset
     */
    public String[] getKeysets(){
        return hashMapFattori.keySet().toArray(new String[0]);
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
        for (FattoreDiConversione f : hashMapFattori.get(richiesta)) {
            if (f.getNome_c2().equals(offerta)) {
                return (int) Math.rint(oreRichiesta * f.getFattore());
            }
        }
        return -1;
    }
}