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


    private static final String MSG_INSERISCI_FATTORE = ">> Inserisci fattore:\n> ";
    private final String filePath;

    private HashMap<String, ArrayList<FattoreDiConversione>> fattori;

    public GestoreFattori(String filePath) {
        this.filePath = filePath;
        this.fattori = new HashMap<>();
        deserializeXML(); //load dati
    }

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

    /**
     * TODO FIXARE
     * Inserisce un nuovo fattore nell'HashMap, verificando eventuali duplicati. SERVE SOLO PER XML IN QUESTO MOMENTO
     *
     * @param tempKey,   nome della prima categoria della coppia
     * @param tempValue, oggetto FdC da inserire nella lista
     */
    public void addFattore(String tempKey, FattoreDiConversione tempValue) {
        // TODO ? cosa bo

        // controlla che esista già un'elemento dell'HashMap con quella chiave
        if (this.fattori.containsKey(tempKey)) {
            // inserisce il fattore se e solo se non esiste già nell'arraylist
            if (!this.fattori.get(tempKey).contains(tempValue)) {
                this.fattori.get(tempKey).add(tempValue);
            }
        } else {
            // se non esiste la chiave, allora crea un nuovo arraylist, aggiunge il fattore e poi inserisce tutto nell'hashmap
            ArrayList<FattoreDiConversione> tempLista = new ArrayList<>();
            tempLista.add(tempValue);
            this.fattori.put(tempKey, tempLista);
        }
    }

    /**
     * Chiede all'utente di inserire un fattore di conversione tra la categoria foglia appena creata
     * e una a scelta, dopodiché lancia il metodo calcolaEAssegnaValoriDiConversione() per il calcolo
     * dei restanti
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
        *   TODO se la radice è l'unica foglia funziona? e se ci sono poche foglie?
        *
        */
        for (int i = 0; i < foglie.size(); i++) {
            String nomeFogliai = factorNameBuilder(nomeRadice, foglie.get(i).getNome());
            for (int j = i+1; j < foglie.size(); j++) {
                String nomeFogliaj = factorNameBuilder(nomeRadice, foglie.get(j).getNome());
                double fattore_ij = InputDati.leggiDoubleConRange(MSG_INSERISCI_FATTORE,0.5, 2);
                FattoreDiConversione fattoreIJ = new FattoreDiConversione(nomeFogliai, nomeFogliaj, fattore_ij);
                FattoreDiConversione fattoreJI = new FattoreDiConversione(nomeFogliaj, nomeFogliai, 1/fattore_ij);
                nuoviDaNuovaRadice.add(fattoreIJ);
                nuoviDaNuovaRadice.add(fattoreJI);
            }
        }

        /* 1. se è vuota e hai i nuovi => è la prima radice ed ha più di una foglia
                => aggiungi i nuovi fattori (sono gli unici)
        *  2. se è vuota e non hai i nuovi => è la prima radice ed hai solo una foglia nuova.
                => metti la chiave ma non i fattori di conversione (arrayList vuoto)
        *  3. non è vuota ma c'è solo una chiave => hai solo una foglia esterna.
                => stampa la lista delle chiavi e chiedi quale usare al configuratore (controllo esistenza)
                => stampa le categorie interne e chiedi quale usare al configuratore (controllo esistenza)
                => chiedi il fattore
                => calcola tutti i fattori esterni nuovi (1 * numeroNuoveFoglie)
           4. non è vuota e c'è più di una chiave => situazione standard
                => stampa la lista delle chiavi e chiedi quale usare al configuratore (controllo esistenza)
                => stampa le categorie interne e chiedi quale usare al configuratore (controllo esistenza)
                => chiedi il fattore
                => calcola tutti i fattori esterni nuovi (numeroFoglieEsistenti * numeroNuoveFoglie)
           NOTA: 3 e 4 sono assimilabili
        */
        if(!fattori.isEmpty()){ //TODO controllare che sia giusto
            //TODO fattori esterni
            //Stampa categorie esterne (Opzionale)
            for (String key : fattori.keySet()){
                System.out.println(key);
            }
            //Chiedi la foglia esterna e controllo [Old:A in (Old:A New:A x)]
            String nomeFogliaEsternaFormattata;
            do{
                nomeFogliaEsternaFormattata = factorNameBuilder(InputDati.leggiStringaNonVuota("Inserisci radice:"), InputDati.leggiStringaNonVuota("Inserisci nome Foglia"));
            }while(!fattori.containsKey(nomeFogliaEsternaFormattata));

            //Stampa categorie Interne
            for (FattoreDiConversione nuovo : nuoviDaNuovaRadice){
                System.out.println(nuovo.getNome_c1());
            }
            //Chiedi la foglia interna e controllo [New:A in (Old:A New:A x)]
            String nomeFogliaInternaFormattata;
            do{
                nomeFogliaInternaFormattata = factorNameBuilder(InputDati.leggiStringaNonVuota("Inserisci radice:"), InputDati.leggiStringaNonVuota("Inserisci nome Foglia"));
            }while(!fattori.containsKey(nomeFogliaInternaFormattata));

            //Chiedi il Fattore di conversione tra le 2 [x in (Old:A New:A x)]
            double fattoreDiConversioneEsternoInterno = InputDati.leggiDoubleConRange("inserisci fattore tra e", 0.5, 2);

            //(Old:A New:A x)
            FattoreDiConversione primoFattoreEsternoInterno = new FattoreDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneEsternoInterno);


            ArrayList<FattoreDiConversione> nuoviSingoloEsternoAInterni = new ArrayList<>();
            //TODO Calcolo dei Fattori Old:A New:x serve un foglio per controllare che non faccia stronzate
            for (FattoreDiConversione nuovo : nuoviDaNuovaRadice){
                //Se New:A in (Old:A New:A x) == New:A in (New:A New:X y) => genera (Old:A New:X x*y)
                if(nomeFogliaInternaFormattata.equals(nuovo.getNome_c1())){
                    FattoreDiConversione nuovoSingoloEsternoAInterno = new FattoreDiConversione();

                }
            }

            //TODO generazione fattore e chiamata a calcolofattori
        }else if (fattori.isEmpty() && !nuoviDaNuovaRadice.isEmpty()) {
            //TODO metti solo i nuovi
        }else if (fattori.isEmpty() && nuoviDaNuovaRadice.isEmpty()) {
            //TODO metti la chiave della singola foglia e collega un array vuoto nella hashmap
        }
        //TODO memorizzazione ()


        /*
        // se l'hashmap è vuota (in teoria solo quando cè una sola categoria foglia nel sistema)
        // non si fa nulla
        // se non è vuota, procedi ok
        if (!fattori.containsKey(nomeRadiceNuovaFoglia)) {

            // questa parte la spostiamo in gestoreCategorie
            // FATTO
            String nomeRadicePreesistente;
            String nomeFogliaPreesistente;

            //cicla la richiesta della categoria foglia se non esiste nella hashmap la chiave per la foglia preesistente
            // (dovrebbe essere più veloce e funzionare bene comunque)
            do {
                nomeRadicePreesistente = InputDati.leggiStringaNonVuota(MSG_INSERISCI_RADICE);
                nomeFogliaPreesistente = InputDati.leggiStringaNonVuota(MSG_INSERISCI_FOGLIA);
                // TODO cosa garantisce che nomeFogliaPreesistente inserito dall'utente sia corretto?
                // si può sfruttare il cercaCategoria -> instanceOf CategoriaFoglia per il check
            } while (!fattori.containsKey(factorNameBuilder(nomeRadicePreesistente, nomeFogliaPreesistente)));

            //TODO check la roba
            

            //Chiedo Fattore all'utente
            double fattore = InputDati.leggiDoubleConRange(MSG_INSERISCI_FATTORE, 0.5, 2.0);

            //Formatto i nomi delle Categorie Foglia relative al fattore inserito
            String fogliaNuovaFormattata = factorNameBuilder(nomeRadiceNuovaFoglia, nomeNuovaFoglia);
            String fogliaVecchiaFormattata = factorNameBuilder(nomeRadicePreesistente, nomeFogliaPreesistente);

            //
            calcolaEAssegnaValoriDiConversione(fogliaNuovaFormattata, fogliaVecchiaFormattata, fattore);

        }*/
    }

    /*
     * Questo metodo serve per l'inserimento del primo (2) fattore nella hashmap,
     * siccome inserisciFattoreDiConversione non lavora sulla hashmap vuota.
     * <p>
     * Va chiamato da GestoreCategorie nel momento in cui si crea la seconda Categoria
     * in assoluto.
     *
     * @param nomeRadiceNuovaFoglia
     * @param nomeNuovaFoglia
     * @param nomeRadicePreesistente
     * @param nomeFogliaPreesistente

    public void inserisciPrimoFattore(String nomeRadiceNuovaFoglia, String nomeNuovaFoglia, String nomeRadicePreesistente, String nomeFogliaPreesistente) {
        //formatto i nomi delle Categorie Foglia
        String fogliaNuovaFormattata = factorNameBuilder(nomeRadiceNuovaFoglia, nomeNuovaFoglia);
        String fogliaVecchiaFormattata = factorNameBuilder(nomeRadicePreesistente, nomeFogliaPreesistente);
        //chiedo fattore
        double fattore = InputDati.leggiDoubleConRange(MSG_INSERISCI_FATTORE, 0.5, 2.0);

        calcolaEAssegnaValoriDiConversione(fogliaNuovaFormattata, fogliaVecchiaFormattata, fattore);
    }*/

    /**
     * Calcola i valori di conversione per tutte le categorie dato un valore dal configuratore
     */
    private void calcolaEAssegnaValoriDiConversione(String fogliaNuovaFormattata, String fogliaVecchiaFormattata, double fattoreDato) {

        //creo arraylist a cui aggiungerò i nuovi fattori
        ArrayList<FattoreDiConversione> nuoviFattori = new ArrayList<>();

        //ci aggiungo i nuovi fattori dati dall'utente
        nuoviFattori.add(new FattoreDiConversione(fogliaNuovaFormattata, fogliaVecchiaFormattata, fattoreDato));
        nuoviFattori.add(new FattoreDiConversione(fogliaVecchiaFormattata, fogliaNuovaFormattata, 1 / fattoreDato));

        //se l'hashmap è vuota non fa i conti per i nuovi fattori obv
        if (!fattori.isEmpty()) {
            // prendo tutti i fattori che hanno come prima foglia quella già esistente scelta dall'utente
            // (serve per il calcolo di tutti gli altri fattori oltre a quelli appena aggiunti a mano)
            ArrayList<FattoreDiConversione> fattoriDaMoltiplicare = fattori.get(fogliaVecchiaFormattata);

            //TODO valutare se aggiungere parallelismo nell'esecuzione della parte successiva

            // Qui ciclo i fattori presi per calcolare e aggiungere quelli nuovi.
            ///Manca un controllo che non sforino perché non ho più scritto a nessuno sulla questione
            for (FattoreDiConversione f : fattoriDaMoltiplicare) {
                double fattoreNuovo = fattoreDato * f.getFattore();
                nuoviFattori.add(new FattoreDiConversione(fogliaNuovaFormattata, f.getNome_c2(), fattoreNuovo));
                nuoviFattori.add(new FattoreDiConversione(f.getNome_c2(), fogliaNuovaFormattata, 1 / fattoreNuovo));
            }
        }

        //Inserimento dei nuovi fattori nella Hashmap
        for (FattoreDiConversione f : nuoviFattori) {
            addFattore(f.getNome_c1(), f);
        }
        //Serializzazione
        serializeXML();

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


    public void serializeXML() {
        Serializer.serialize(this.filePath, new FattoriWrapper(fattori));
    }

    public void deserializeXML() {
        FattoriWrapper tempWrapper = Serializer.deserialize(new TypeReference<FattoriWrapper>() {
        }, filePath);
        if(tempWrapper != null)
        fattori = tempWrapper.toHashMap();
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

    /**
     * Logica grafica.
     * Per ora ci si limita all'utilizzo del terminale.
     */
    public String stringaFattoriDataCategoria(String categoriaFormattata){
        StringBuilder sb = new StringBuilder();
        for (FattoreDiConversione f : fattori.get(categoriaFormattata)){
            sb.append(f.getNome_c1()+" "+f.getNome_c2()+" "+f.getFattore()+"\n");
        }
        return sb.toString();
    }

    private void visualizzaFattori() {
        System.out.println("\n\n --- Visualizza Fattori ---\n\n");
        System.out.println(this);
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


}