package it.unibs.projectIngesoft.core.domain.model;


import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.FattoreDiConversione;
import it.unibs.projectIngesoft.libraries.Utilitas;
import it.unibs.projectIngesoft.persistence.Repository;

import java.util.*;

import static it.unibs.projectIngesoft.libraries.Utilitas.MAX_FATTORE;
import static it.unibs.projectIngesoft.libraries.Utilitas.MIN_FATTORE;

/**
 * Si occupa di calcolare, memorizzare e leggere i Fattori di Conversione relativi alle Categorie.
 * Non conosce chi gestisce le categorie, ma mette a disposizione i suoi metodi.
 */
public class FattoriModel {


    private Map<String, List<FattoreDiConversione>> hashMapFattori;
    private final Repository<Map<String, List<FattoreDiConversione>>> repository;

    public FattoriModel(Repository<Map<String, List<FattoreDiConversione>>> repository) {
        this.repository = repository;
        this.hashMapFattori = repository.load();
    }

    public void setHashMapFattori( Map<String, List<FattoreDiConversione>> hashMapFattori ){
        if(hashMapFattori!=null)
            this.hashMapFattori = hashMapFattori;
    }

    public void calcolaEInserisciFattoriDiConversione(String nomeFogliaEsternaFormattata, String nomeFogliaInternaFormattata, double fattoreDiConversioneEsternoInterno, List<FattoreDiConversione> nuoviDaNuovaRadice) {
        nuoviDaNuovaRadice.addAll(calcolaInversi(nuoviDaNuovaRadice));

        FattoreDiConversione primoFattoreEsternoInterno = new FattoreDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneEsternoInterno);
        List<FattoreDiConversione> fattoriEsterni = calcoloFattoriEsterni(primoFattoreEsternoInterno, nuoviDaNuovaRadice);

        aggiungiListDiFattori(nuoviDaNuovaRadice);
        aggiungiListDiFattori(fattoriEsterni);

        repository.save(hashMapFattori);
    }

    public void inserisciSingolaFogliaNellaHashmap(String nomeRadice, List<Categoria> foglie) {
        hashMapFattori.put(Utilitas.factorNameBuilder(nomeRadice, foglie.getFirst().getNome()), new ArrayList<>());
    }

    public List<FattoreDiConversione> calcolaInversi(List<FattoreDiConversione> fattoriIJ) {
        List<FattoreDiConversione> fattoriInversi = new ArrayList<>();
        for (FattoreDiConversione f : fattoriIJ) {
            fattoriInversi.add(generaInverso(f));
        }
        return fattoriInversi;
    }

    public boolean isEmpty(){
        return hashMapFattori.isEmpty();
    }

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
        this.hashMapFattori.computeIfAbsent(chiaveFattore, _ -> new ArrayList<>()).add(fattore);
    }

    /**
     * Controlla ed eventualmente sostituisce (se fuori scala) il valore del fattoreDaControllare
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
    public void aggiungiListDiFattori(List<FattoreDiConversione> fattori) {
        for (FattoreDiConversione f : fattori) {
            addFattore(f.getNome_c1(), f);
        }
        repository.save(hashMapFattori);
    }

    /**
     * Restituisce la lista di FattoriDiConversione richiesta tramite coppia radice-foglia
     */
    public List<FattoreDiConversione> getFattoriFromFoglia(String categoriaFormattata) {
        if (!esisteCategoria(categoriaFormattata)) {
            return new ArrayList<>();
        }
        return hashMapFattori.get(categoriaFormattata);
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
     * @param primoFattoreFogliaEsternaInterna, fattore creato tra una foglia preesistente e una nuova.
     *                                    Permette il calcolo di tutti gli altri
     * @param fattoriInterni,             fattori di conversione tra tutte le foglie della gerarchia appena creata
     * @return fattoriEsterni, lista di fattori appena calcolati
     */
    private List<FattoreDiConversione> calcoloFattoriEsterni(FattoreDiConversione primoFattoreFogliaEsternaInterna, List<FattoreDiConversione> fattoriInterni) {
        List<FattoreDiConversione> fattoriCalcolati = new ArrayList<>();
        List<FattoreDiConversione> nuoviFattoriDaSingolaFogliaEsternaATutteLeInterne = new ArrayList<>();
        List<FattoreDiConversione> nuoviFattoriDaTutteLeInterneAllaSingolaEsterna = new ArrayList<>();

        nuoviFattoriDaSingolaFogliaEsternaATutteLeInterne.add(primoFattoreFogliaEsternaInterna);

        FattoreDiConversione primoFattoreInternoEsterno = generaInverso(primoFattoreFogliaEsternaInterna);
        nuoviFattoriDaTutteLeInterneAllaSingolaEsterna.add(primoFattoreInternoEsterno);

        calcoloFattoriDaEsternaAInterne(primoFattoreFogliaEsternaInterna, fattoriInterni, nuoviFattoriDaSingolaFogliaEsternaATutteLeInterne, nuoviFattoriDaTutteLeInterneAllaSingolaEsterna);

        List<FattoreDiConversione> fattoriInternoATuttiEsterni = new ArrayList<>();

        calcoloDaTutteLeInterneATutteLeEsterneRimanentiConInversi(nuoviFattoriDaTutteLeInterneAllaSingolaEsterna, fattoriInternoATuttiEsterni);

        fattoriCalcolati.addAll(fattoriInternoATuttiEsterni); //Aggiungo l'array di fattori esterni
        fattoriCalcolati.addAll(nuoviFattoriDaSingolaFogliaEsternaATutteLeInterne); //Aggiungo l'array di diretti
        fattoriCalcolati.addAll(nuoviFattoriDaTutteLeInterneAllaSingolaEsterna); //Aggiungo l'array di inversi al primo

        return fattoriCalcolati;
    }

    private void calcoloDaTutteLeInterneATutteLeEsterneRimanentiConInversi(List<FattoreDiConversione> nuoviFattoriDaTutteLeInterneAllaSingolaEsterna, List<FattoreDiConversione> fattoriInternoATuttiEsterni) {
        for (FattoreDiConversione fattoreInternoASingoloEsterno : nuoviFattoriDaTutteLeInterneAllaSingolaEsterna) {
            //Per ogni Fattore che ha come Key la foglia Esterno:A
            for (FattoreDiConversione fattoreInMemoria : hashMapFattori.get(fattoreInternoASingoloEsterno.getNome_c2())) {
                //Genero i fattori con tutto il resto del fuori
                FattoreDiConversione fattoreInternoEsterno = new FattoreDiConversione(fattoreInternoASingoloEsterno.getNome_c1(), fattoreInMemoria.getNome_c2(), fattoreInternoASingoloEsterno.getFattore() * fattoreInMemoria.getFattore());
                FattoreDiConversione fattoreEsternoInterno = generaInverso(fattoreInternoEsterno);
                fattoriInternoATuttiEsterni.add(fattoreInternoEsterno);
                fattoriInternoATuttiEsterni.add(fattoreEsternoInterno);
            }
        }
    }

    private void calcoloFattoriDaEsternaAInterne(FattoreDiConversione primoFattoreFogliaEsternaInterna, List<FattoreDiConversione> fattoriInterni, List<FattoreDiConversione> nuoviFattoriDaSingolaFogliaEsternaATutteLeInterne, List<FattoreDiConversione> nuoviFattoriDaTutteLeInterneAllaSingolaEsterna) {
        if (!fattoriInterni.isEmpty()) {
            for (FattoreDiConversione nuovoFattore : fattoriInterni) {
                if (primoFattoreFogliaEsternaInterna.getNome_c2().equals(nuovoFattore.getNome_c1())) {
                    FattoreDiConversione nuovoFogliaSingolaEsternaAInterna = new FattoreDiConversione(primoFattoreFogliaEsternaInterna.getNome_c1(), nuovoFattore.getNome_c2(), primoFattoreFogliaEsternaInterna.getFattore() * nuovoFattore.getFattore());
                    FattoreDiConversione nuovoFogliaInternaASingolaEsterna = generaInverso(nuovoFogliaSingolaEsternaAInterna);

                    nuoviFattoriDaSingolaFogliaEsternaATutteLeInterne.add(nuovoFogliaSingolaEsternaAInterna);
                    nuoviFattoriDaTutteLeInterneAllaSingolaEsterna.add(nuovoFogliaInternaASingolaEsterna);
                }
            }
        }
    }


    /**
     * Dato un fattore ne restituisce l'inverso
     *
     * @param fattore, fattore di conversione di cui calcolare l'inverso
     * @return fattoreInverso
     */
    public FattoreDiConversione generaInverso(FattoreDiConversione fattore) {
        return new FattoreDiConversione(fattore.getNome_c2(), fattore.getNome_c1(), 1 / fattore.getFattore());
    }

    /**
     * Restituisce array di stringhe contenenti i keyset
     *
     * @return keySet, array di stringhe contenenti i keyset
     */
    public String[] getKeysets(){
        return hashMapFattori.keySet().toArray(new String[0]);
    }


    public Optional<Integer> calcolaRapportoOre(String richiesta, String offerta, int oreRichiesta) {
        for (FattoreDiConversione f : hashMapFattori.get(richiesta)) {
            if (f.getNome_c2().equals(offerta)) {
                int risultato = (int) Math.rint(oreRichiesta * f.getFattore());
                return Optional.of(risultato == 0 ? 1 : risultato);
            }
        }
        return Optional.empty();
    }

    public boolean esisteCategoria(String categoriaFormattata) {
        return hashMapFattori.containsKey(categoriaFormattata);
    }

    public boolean esistonoFattoriPerCategoria(String categoriaFormattata) {
        return !hashMapFattori.get(categoriaFormattata).isEmpty();
    }
}