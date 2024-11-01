package it.unibs.projectIngesoft.gestori;


import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.attivita.FattoriWrapper;
import it.unibs.projectIngesoft.libraries.InputDati;
import it.unibs.projectIngesoft.libraries.Serializer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//@JacksonXmlRootElement(localName = "GestoreFattori")
//@JsonIgnoreProperties(ignoreUnknown = true)
public class GestoreFattori {


    public static final String INSERISCI_IL_FATTORE_TRA_S_E_S = "Inserisci il fattore tra %s e %s: ";
    public static final double MIN_FATTORE = 0.5;
    public static final double MAX_FATTORE = 2.0;
    public static final String MSG_INSERISCI_NOME_FOGLIA = "Inserisci nome Foglia: ";
    public static final String MSG_INSERISCI_NOME_RADICE = "Inserisci nome radice: ";
    public static final String ERRORE_FATTORI = "Errore nei Fattori";
    public static final String MSG_INSERIRE_FOGLIA_ESTERNA = "Inserire la foglia esterna con cui fare il confronto tra queste: ";
    public static final String MSG_INSERIRE_CATEGORIA_INTERNA = "Inserire la foglia interna con cui fare il confronto tra queste: ";
    private final String filePath;

    private HashMap<String, ArrayList<FattoreDiConversione>> fattori;

    public GestoreFattori(String filePath) {
        this.filePath = filePath;
        this.fattori = new HashMap<>();
        deserializeXML(); //load dati
    }

    /**
     * TODO FIXARE?
     * Inserisce un nuovo fattore nell'HashMap, verificando eventuali duplicati. SERVE SOLO PER XML IN QUESTO MOMENTO
     *
     * @param tempKey,   nome della prima categoria della coppia
     * @param tempValue, oggetto FdC da inserire nella lista
     */
    public void addFattore(String tempKey, FattoreDiConversione tempValue) {
        /// Nota:aggiungo qui il controllo sul valore del fattore

        // controlla che esista già un'elemento dell'HashMap con quella chiave
        if (this.fattori.containsKey(tempKey)) {
            // inserisce il fattore se e solo se non esiste già nell'arraylist
            if (!this.fattori.get(tempKey).contains(controllaESostituisciValoriFuoriScala(tempValue))) {
                this.fattori.get(tempKey).add(controllaESostituisciValoriFuoriScala(tempValue));
            }
        } else {
            // se non esiste la chiave, allora crea un nuovo arraylist, aggiunge il fattore e poi inserisce tutto nell'hashmap
            ArrayList<FattoreDiConversione> tempLista = new ArrayList<>();
            tempLista.add(controllaESostituisciValoriFuoriScala(tempValue));
            this.fattori.put(tempKey, tempLista);
        }
    }

    private FattoreDiConversione controllaESostituisciValoriFuoriScala(FattoreDiConversione fattoreDaControllare){
        if (fattoreDaControllare.getFattore() > MAX_FATTORE){
            return new FattoreDiConversione(fattoreDaControllare.getNome_c1(), fattoreDaControllare.getNome_c2(), MAX_FATTORE);
        }else if (fattoreDaControllare.getFattore() < MIN_FATTORE){
            return new FattoreDiConversione(fattoreDaControllare.getNome_c1(), fattoreDaControllare.getNome_c2(), MIN_FATTORE);
        }
        return fattoreDaControllare;
    }

    /**
     * Chiama addFattore per ogni fattore nell'ArrayList di Fattori
     * @param fattori, ArrayList di fattori
     */
    public void aggiungiArrayListDiFattori(ArrayList<FattoreDiConversione> fattori){
        for (FattoreDiConversione f : fattori) {
            addFattore(f.getNome_c1(), f);
        }
    }


    /**
     * Chiede all'utente i fattori di conversione minimi necessari al calcolo dei restanti e avvia il calcolo degli altri
     */
    public void inserisciFattoriDiConversione(String nomeRadice, List<Categoria> foglie) {

        //dichiaro l'arraylist dove infilo i fattori tra le foglie nella radice
        ArrayList<FattoreDiConversione> nuoviDaNuovaRadice = new ArrayList<>();

        /*Cosa fa questo ciclo:
        * Cicla le foglie dalla prima all'ultima per generare tutte le coppie di valori possibili.
        * (combinazione senza ripetizione)
        *
        * Per ogni coppia generata chiede il fattore all'utente.
        * Genera il FattoreDiConversione e il suo inverso.
        * Li inserisce nell'ArrayList nuoviDaNuovaRadice.
        *
        * Alla fine del for tutti i fattori di conversione interni alla nuova radice
        * sono stati messi in nuoviDaNuovaRadice.
        *
        * Date le foglie A, B, C, D e la radice R dovrebbe calcolare i fattori (segnati x):
        * R:A R:B x
        * R:A R:C x
        * R:A R:D x
        * R:B R:C x
        * R:B R:D x
        * R:C R:D x
        * + gli inversi.
        *
        * Questo funziona anche nel caso la radice sia la prima inserita.
        *
        *   TODO se la radice è l'unica foglia funziona? semplicemente non dovrebbe fare nulla in quanto 1 < 1 == false
        *
        */
        for (int i = 0; i < foglie.size(); i++) {
            String nomeFogliai = factorNameBuilder(nomeRadice, foglie.get(i).getNome());
            for (int j = i+1; j < foglie.size(); j++) {
                String nomeFogliaj = factorNameBuilder(nomeRadice, foglie.get(j).getNome());
                double fattore_ij = InputDati.leggiDoubleConRange(INSERISCI_IL_FATTORE_TRA_S_E_S.formatted(nomeFogliai, nomeFogliaj), MIN_FATTORE, MAX_FATTORE);

                FattoreDiConversione fattoreIJ = new FattoreDiConversione(nomeFogliai, nomeFogliaj, fattore_ij);
                FattoreDiConversione fattoreJI = generaInverso(fattoreIJ);

                nuoviDaNuovaRadice.add(fattoreIJ);
                nuoviDaNuovaRadice.add(fattoreJI);
            }
        }

        /* 1. non è vuota ma c'è solo una chiave => hai solo una foglia esterna.
                => stampa la lista delle chiavi e chiedi quale usare al configuratore (controllo esistenza)
                => stampa le categorie interne e chiedi quale usare al configuratore (controllo esistenza)
                => chiedi il fattore
                => calcola tutti i fattori esterni nuovi (1 * numeroNuoveFoglie)
           2. non è vuota e c'è più di una chiave => situazione standard
                => stampa la lista delle chiavi e chiedi quale usare al configuratore (controllo esistenza)
                => stampa le categorie interne e chiedi quale usare al configuratore (controllo esistenza)
                => chiedi il fattore
                => calcola tutti i fattori esterni nuovi (numeroFoglieEsistenti * numeroNuoveFoglie)
           3. se è vuota e non hai i nuovi => è la prima radice ed hai solo una foglia nuova.
                => metti la chiave ma non i fattori di conversione (arrayList vuoto)
           4. se è vuota e hai i nuovi => è la prima radice ed ha più di una foglia
                => aggiungi i nuovi fattori (sono gli unici)

           NOTA: 1 e 2 sono assimilabili
        */
        if(!fattori.isEmpty()){ //TODO controllare che sia giusto
            ///INTERAZIONE UTENTE
            /*  Stampa categorie esterne (Opzionale) : In realtà stampa le chiavi nella hashmap, così se c'é una sola
             *  categoria già memorizzata stampa e lavora su quella
            */
            System.out.println(MSG_INSERIRE_FOGLIA_ESTERNA);
            for (String key : fattori.keySet()){
                System.out.println(key);
            }
            //Chiedi la foglia esterna e controllo [Old:A in (Old:A New:A x)]
            String nomeFogliaEsternaFormattata;
            do{
                nomeFogliaEsternaFormattata = factorNameBuilder(InputDati.leggiStringaNonVuota(MSG_INSERISCI_NOME_RADICE), InputDati.leggiStringaNonVuota(MSG_INSERISCI_NOME_FOGLIA));
            }while(!fattori.containsKey(nomeFogliaEsternaFormattata));


            //Stampa categorie Interne, basandosi sulle foglie della nuova radice
            System.out.println(MSG_INSERIRE_CATEGORIA_INTERNA);
            for (Categoria foglia : foglie){
                System.out.println(factorNameBuilder(nomeRadice, foglia.getNome()));
            }
            //Chiedi la foglia interna e controllo [New:A in (Old:A New:A x)]
            String nomeFogliaInternaScelta;
            boolean appartieneAlleFoglie = false;
            do{
                nomeFogliaInternaScelta = InputDati.leggiStringaNonVuota(MSG_INSERISCI_NOME_FOGLIA);
                for (Categoria foglia : foglie){
                    if (foglia.getNome().equals(nomeFogliaInternaScelta)){
                        appartieneAlleFoglie = true;
                        break;
                    }
                }
            }while(!appartieneAlleFoglie);
            String nomeFogliaInternaFormattata = factorNameBuilder(nomeRadice, nomeFogliaInternaScelta);


            //Chiedi il Fattore di conversione tra le 2 [x in (Old:A New:A x)]
            double fattoreDiConversioneEsternoInterno = InputDati.leggiDoubleConRange(INSERISCI_IL_FATTORE_TRA_S_E_S.formatted(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata), MIN_FATTORE, MAX_FATTORE);


            ///PARTE IN CUI FACCIO I CONTI
            FattoreDiConversione primoFattoreEsternoInterno = new FattoreDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneEsternoInterno);

            ArrayList<FattoreDiConversione> fattoriEsterni = calcoloFattoriEsterni(primoFattoreEsternoInterno, nuoviDaNuovaRadice);

            aggiungiArrayListDiFattori(fattoriEsterni);

        }else if (!nuoviDaNuovaRadice.isEmpty()) {
            //Se la hashmap è vuota e si sono nuovi fattori => inserisci solo i nuovi
            aggiungiArrayListDiFattori(nuoviDaNuovaRadice);

        }else if (nuoviDaNuovaRadice.isEmpty()) {
            //Se la hashmap è vuota e non si sono fattori => metti la chiave della singola foglia e collega un array vuoto nella hashmap
            fattori.put(factorNameBuilder(nomeRadice, foglie.getFirst().getNome()), new ArrayList<FattoreDiConversione>());

        }else System.out.println(ERRORE_FATTORI);

        //Memorizzazione
        serializeXML();
    }

    /**
     * //TODO controllare non rompa il cazzo se gli passi un ArrayList vuoto perché dovrebbe andare comunque
     * Calcola tutti i fattori che hanno una foglia nuova e una preesistente
     * @param primoFattoreEsternoInterno
     * @param fattoriInterni
     * @return fattoriEsterni
     */
    private ArrayList<FattoreDiConversione> calcoloFattoriEsterni(FattoreDiConversione primoFattoreEsternoInterno, ArrayList<FattoreDiConversione> fattoriInterni){
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
     * @param fattore
     * @return fattoreInverso
     */
    private FattoreDiConversione generaInverso(FattoreDiConversione fattore){
        return new FattoreDiConversione(fattore.getNome_c2(), fattore.getNome_c1(), 1/fattore.getFattore());
    }


    public boolean esisteEntry(String nomeRadice, String nomeFoglia){
        return fattori.containsKey(factorNameBuilder(nomeRadice, nomeFoglia));
    }


    public String[] parseChiave(String nomeCategoria) {
        return nomeCategoria.split(":");
    }

    public String factorNameBuilder(String root, String leaf) {
        return root + ":" + leaf;
    }

    /*
     *  Serializer things
     */

    public void serializeXML() {
        Serializer.serialize(this.filePath, new FattoriWrapper(fattori));
    }

    public void deserializeXML() {
        FattoriWrapper tempWrapper = Serializer.deserialize(new TypeReference<FattoriWrapper>() {
        }, filePath);
        if(tempWrapper != null) {
            fattori = tempWrapper.toHashMap();
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

    /*
     * Logica grafica.
     * Per ora ci si limita all'utilizzo del terminale.
     */

    /**
     * Dato il nome di una categoria ritorna una stringa formattata con tutti i fattori di conversione relativi a quella categoria
     * @param categoriaFormattata
     * @return String
     */
    public String stringaFattoriDataCategoria(String categoriaFormattata){
        StringBuilder sb = new StringBuilder();
        for (FattoreDiConversione f : fattori.get(categoriaFormattata)){
            sb.append(f.getNome_c1()).append(" ").append(f.getNome_c2()).append(" ").append(f.getFattore()).append("\n");
        }
        return sb.toString();
    }

    private void visualizzaFattori() {
        String categoriaFormattata = factorNameBuilder(InputDati.leggiStringaNonVuota(MSG_INSERISCI_NOME_RADICE),InputDati.leggiStringaNonVuota(MSG_INSERISCI_NOME_FOGLIA));
        System.out.println(stringaFattoriDataCategoria(categoriaFormattata));
    }

    public String visualizzaCategorieConFdC(){
        StringBuilder sb = new StringBuilder();
        for (String key : fattori.keySet()) {
            String[] parsed = parseChiave(key);
            sb.append(String.format(">> %s (%s)", parsed[1], parsed[0])).append("\n");
        }
        return sb.toString();
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

    /*
     *  Getters & setters
     */

    public String getFilePath() {
        return filePath;
    }

    public HashMap<String, ArrayList<FattoreDiConversione>> getFattori() {
        return fattori;
    }

    public void setFattori(HashMap<String, ArrayList<FattoreDiConversione>> fattori) {
        this.fattori = fattori;
        serializeXML();
    }
}