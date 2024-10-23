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

    private static final String MSG_INSERISCI_RADICE = ">> Inserisci la radice dell'albero cui appartiene la foglia che ti interessa:\n> ";
    private static final String MSG_INSERISCI_FOGLIA = ">> Inserisci la foglia che ti interessa:\n> ";
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
    public void inserisciFattoreDiConversione(String nomeRadiceNuovaFoglia, String nomeNuovaFoglia) {

        // se l'hashmap è vuota (in teoria solo quando cè una sola categoria foglia nel sistema)
        if (fattori.containsKey(nomeRadiceNuovaFoglia)) {
            return; // TODO chiedere a martino xk fa questo
        } else {

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

            //Chiedo Fattore all'utente
            double fattore = InputDati.leggiDoubleConRange(MSG_INSERISCI_FATTORE, 0.5, 2.0);

            //Formatto i nomi delle Categorie Foglia relative al fattore inserito
            String fogliaNuovaFormattata = factorNameBuilder(nomeRadiceNuovaFoglia, nomeNuovaFoglia);
            String fogliaVecchiaFormattata = factorNameBuilder(nomeRadicePreesistente, nomeFogliaPreesistente);

            //
            calcolaEAssegnaValoriDiConversione(fogliaNuovaFormattata, fogliaVecchiaFormattata, fattore);

        }
    }

    /**
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
     */
    public void inserisciPrimoFattore(String nomeRadiceNuovaFoglia, String nomeNuovaFoglia, String nomeRadicePreesistente, String nomeFogliaPreesistente) {
        //formatto i nomi delle Categorie Foglia
        String fogliaNuovaFormattata = factorNameBuilder(nomeRadiceNuovaFoglia, nomeNuovaFoglia);
        String fogliaVecchiaFormattata = factorNameBuilder(nomeRadicePreesistente, nomeFogliaPreesistente);
        //chiedo fattore
        double fattore = InputDati.leggiDoubleConRange(MSG_INSERISCI_FATTORE, 0.5, 2.0);

        calcolaEAssegnaValoriDiConversione(fogliaNuovaFormattata, fogliaVecchiaFormattata, fattore);
    }

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

    public String parseRadice(String nomeCategoria) {
        return nomeCategoria.split(":")[0];
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
    private void visualizzaFattori() {
        System.out.println("\n\n --- Visualizza Fattori ---\n\n");
        System.out.println(this);
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