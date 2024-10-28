package it.unibs.projectIngesoft.gestori;


import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.attivita.FattoriWrapper;
import it.unibs.projectIngesoft.libraries.InputDati;
import it.unibs.projectIngesoft.libraries.Serializer;


import java.util.ArrayList;
import java.util.HashMap;


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
    public void inserisciFattoreDiConversione(String nomeRadiceNuovaFoglia, String nomeNuovaFoglia, String nomeRadicePreesistente, String nomeFogliaPreesistente) {

        // se l'hashmap è vuota (in teoria solo quando cè una sola categoria foglia nel sistema)
        // non si fa nulla
        // se non è vuota, procedi ok
        if (!fattori.containsKey(nomeRadiceNuovaFoglia)) {

            // questa parte la spostiamo in gestoreCategorie
            // FATTO
            /*String nomeRadicePreesistente;
            String nomeFogliaPreesistente;

            //cicla la richiesta della categoria foglia se non esiste nella hashmap la chiave per la foglia preesistente
            // (dovrebbe essere più veloce e funzionare bene comunque)
            do {
                nomeRadicePreesistente = InputDati.leggiStringaNonVuota(MSG_INSERISCI_RADICE);
                nomeFogliaPreesistente = InputDati.leggiStringaNonVuota(MSG_INSERISCI_FOGLIA);
                // TODO cosa garantisce che nomeFogliaPreesistente inserito dall'utente sia corretto?
                // si può sfruttare il cercaCategoria -> instanceOf CategoriaFoglia per il check
            } while (!fattori.containsKey(factorNameBuilder(nomeRadicePreesistente, nomeFogliaPreesistente)));*/

            //TODO check la roba
            

            //Chiedo Fattore all'utente
            double fattore = InputDati.leggiDoubleConRange(MSG_INSERISCI_FATTORE, 0.5, 2.0);

            //Formatto i nomi delle Categorie Foglia relative al fattore inserito
            String fogliaNuovaFormattata = factorNameBuilder(nomeRadiceNuovaFoglia, nomeNuovaFoglia);
            String fogliaVecchiaFormattata = factorNameBuilder(nomeRadicePreesistente, nomeFogliaPreesistente);

            //
            calcolaEAssegnaValoriDiConversione(fogliaNuovaFormattata, fogliaVecchiaFormattata, fattore);

        }
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