package it.unibs.projectIngesoft.core.domain.model;


import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.FattoreDiConversione;
import it.unibs.projectIngesoft.libraries.Utilitas;
import it.unibs.projectIngesoft.persistence.implementations.FattoriDiConversioneRepository;

import java.util.*;

import static it.unibs.projectIngesoft.libraries.Utilitas.MAX_FATTORE;
import static it.unibs.projectIngesoft.libraries.Utilitas.MIN_FATTORE;

/**
 * Si occupa di calcolare, memorizzare e leggere i Fattori di Conversione relativi alle Categorie.
 * Non conosce chi gestisce le categorie, ma mette a disposizione i suoi metodi.
 */
public class FattoriModel {


    private Map<String, List<FattoreDiConversione>> hashMapFattori;
    private final FattoriDiConversioneRepository repository;

    public FattoriModel(FattoriDiConversioneRepository repository) {
        this.repository = repository;
        load();
    }

    public void save(){
        repository.save(hashMapFattori);
    }

    public void load(){
        Map<String, List<FattoreDiConversione>> data = repository.load();
        this.hashMapFattori = (data == null ? new HashMap<>() : data);
    }

    public void setHashMapFattori( Map<String, List<FattoreDiConversione>> hashMapFattori ){
        if(hashMapFattori!=null)
            this.hashMapFattori = hashMapFattori;
    }


    ////////////////////////////////////////////////////// CORPO //////////////////////////////////////////////////////

    public void inserisciFattoriDiConversione(String nomeFogliaEsternaFormattata, String nomeFogliaInternaFormattata, double fattoreDiConversioneEsternoInterno, List<FattoreDiConversione> nuoviDaNuovaRadice) {
        nuoviDaNuovaRadice.addAll(calcolaInversi(nuoviDaNuovaRadice));

            // 4. fa i calcolini e salva i fattori nella hashmap
            FattoreDiConversione primoFattoreEsternoInterno = new FattoreDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneEsternoInterno);
            ArrayList<FattoreDiConversione> fattoriEsterni = calcoloFattoriEsterni(primoFattoreEsternoInterno, nuoviDaNuovaRadice);

            aggiungiArrayListDiFattori(nuoviDaNuovaRadice);
            aggiungiArrayListDiFattori(fattoriEsterni);

        save();
    }

    public void inserisciSingolaFogliaNellaHashmap(String nomeRadice, List<Categoria> foglie){
        // caso hashmap è vuota e non hai i nuovi => è la prima radice ed hai solo una foglia nuova
        // 1. prepara la chiave e un arraylist vuoto, non puoi calcolare nessun fattore
        hashMapFattori.put(Utilitas.factorNameBuilder(nomeRadice, foglie.getFirst().getNome()), new ArrayList<>());
    }

    public ArrayList<FattoreDiConversione> calcolaInversi(ArrayList<FattoreDiConversione> fattoriIJ) {
        ArrayList<FattoreDiConversione> fattoriInversi = new ArrayList<>();
        for (FattoreDiConversione f : fattoriIJ) {
            fattoriInversi.add(generaInverso(f));
        }
        return fattoriInversi;
    }

    public boolean isEmpty(){
        return hashMapFattori.isEmpty();
    }


    ////////////////////////////////////////////////// RIFATTORIZZATO //////////////////////////////////////////////////

    /**
     * Inserisce un nuovo fattore nell'HashMap, verificando eventuali duplicati.
     * Garantisce il formato di chiave (string root:leaf) e valore (oggetto FdC)
     *
     * @param chiaveFattore,     nome della prima categoria della coppia
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
     *
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
        save();
    }

    /**
     * Restituisce la lista di FattoriDiConversione richiesta tramite coppia radice-foglia
     */
    public Map<String, List<FattoreDiConversione>> getHashMapFattori() {
        return new HashMap<>(hashMapFattori);
    }

    /**
     * Controlla se la stringa "chiave" è una chiave della hashmap
     *
     * @param chiave, stringa da controllare
     * @return true, se la chiave è presente nel keyset, false altrimenti
     */
    public boolean existsKeyInHashmapFattori(String chiave) {
        return hashMapFattori.containsKey(chiave);
    }


    /**
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
     *
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
                int risultato = (int) Math.rint(oreRichiesta * f.getFattore());
                return risultato == 0 ? 1 : risultato;
            }
        }
        return -1;
    }
}