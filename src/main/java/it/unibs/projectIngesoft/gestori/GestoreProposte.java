package it.unibs.projectIngesoft.gestori;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.attivita.ProposteWrapper;
import it.unibs.projectIngesoft.attivita.StatiProposta;
import it.unibs.projectIngesoft.libraries.InputDati;
import it.unibs.projectIngesoft.libraries.Serializer;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;


public class GestoreProposte {
    public static final String WARNING_PROPOSTA_ANNULLATA = ">> Proposta annullata";
    public static final String WARNING_PROPOSTA_DUPLICATA = ">> Proposta duplicata! Procedura annullata.";
    public static final String MSG_INSERISCI_RICHIESTA = ">> Inserisci una categoria valida di cui vuoi effettuare la RICHIESTA.";
    public static final String MSG_INSERISCI_OFFERTA = ">> Inserisci una categoria valida che sei disposto a OFFRIRE in cambio.";
    public static final String MSG_RICHIESTA_ORE = ">> Inserisci il numero di ORE che vuoi richiedere:\n> ";
    public static final String WARNING_IMPOSSIBILE_CALCOLARE_ORE = ">> Impossibile Calcolare il numero di ore da offrire.\n";
    public static final String MSG_CONFERMA_PROPOSTA = ">> Dovrai offrire %d ore in cambio. Confermi?%n> ";
    public static final String HEADER_PROPOSTE_APERTE = ">> PROPOSTE APERTE\n";
    private static final String MSG_INSERISCI_CATEGORIA = "Inserisci una categoria di cui ricercare le proposte";
    public static final String HEADER_PROPOSTE_CHIUSE = ">> PROPOSTE CHIUSE\n";
    public static final String HEADER_PROPOSTE_RITIRATE = ">> PROPOSTE RITIRATE\n";
    public static final String HEADER_PROPOSTE_PRONTE = ">> PROPOSTE PRONTE <<";
    public static final String MSG_FORMATTED_PROPOSTA_PRONTA = "%s, %s\n >>> Indirizzo email: %s\n";
    public static final String MSG_SELEZIONE_CATEGORIA_RICHIESTA = "Inserisci la categoria richiesta per la selezione: ";
    public static final String MSG_SELEZIONE_ORE = "Inserisci il monte ore richieste per la selezione: ";
    public static final String MSG_SELEZIONE_CATEGORIA_OFFERTA = "Inserisci la categoria offerta per la selezione: ";

    @JacksonXmlElementWrapper(localName = "listaProposte")
    @JacksonXmlProperty(localName = "Proposta")
    private HashMap<String, ArrayList<Proposta>> listaProposte;
    private final GestoreFattori gestFatt;
    private final String filePath;
    private final Utente utenteAttivo;

    public GestoreProposte(String proposteFilepath, String fattoriFilePath, Utente utenteAttivo) {
        this.gestFatt = new GestoreFattori(fattoriFilePath);
        this.filePath = proposteFilepath;
        this.listaProposte = new HashMap<>();
        this.utenteAttivo = utenteAttivo;

        deserializeXML();
    }

    /**
     * Serializza this.listaProposte.
     * Sfrutto l'implementazione statica della classe Serializer.
     */
    private void serializeXML() {
        assert this.listaProposte != null;
        assert this.filePath != null;
        Serializer.serialize(this.filePath, new ProposteWrapper(listaProposte));
    }

    /**
     * De-serializza this.listaProposte.
     * Sfrutto l'implementazione statica della classe Serializer.
     */
    private void deserializeXML() {
        assert this.listaProposte != null;

        ProposteWrapper tempWrapper = Serializer.deserialize(new TypeReference<>() {
        }, filePath);
        if (tempWrapper != null) {
            listaProposte = tempWrapper.toHashMap();
        }

    }

    public void addProposta(Proposta proposta) {
        assert this.listaProposte != null;
        this.listaProposte.computeIfAbsent(proposta.getComprensorio(), k -> new ArrayList<>()).add(proposta);
        serializeXML();
    }

    /**
     * Ritorna la lista di proposte afferenti a un certo comprensorio.
     *
     * @param comprensorio, nome del comprensorio
     * @return new ArrayList() se non esistono proposte da un certo comprensorio, altrimenti lista di proposte
     */
    public ArrayList<Proposta> getListaProposteComprensorio(String comprensorio) {
        assert this.listaProposte != null;
        if (this.listaProposte.containsKey(comprensorio))
            return this.listaProposte.get(comprensorio);
        else return new ArrayList<>();
    }

    /**
     * Guida la creazione di una nuova proposta di scambio di prestazioni d'opera
     */
    public void effettuaProposta() {
        String categoriaRichiesta;
        String categoriaOfferta;
        int oreRichiesta;
        int oreOfferta;

        // 1. inserimento categoria richiesta, ore, e categoria offerta
        categoriaRichiesta = gestFatt.selezioneFoglia(MSG_INSERISCI_RICHIESTA);
        oreRichiesta = InputDati.leggiInteroPositivo(MSG_RICHIESTA_ORE);
        categoriaOfferta = gestFatt.selezioneFoglia(MSG_INSERISCI_OFFERTA);

        // 2. calcolo ore per l'offerta
        oreOfferta = gestFatt.calcolaRapportoOre(categoriaRichiesta, categoriaOfferta, oreRichiesta);
        if (oreOfferta == -1) {
            System.out.println(WARNING_IMPOSSIBILE_CALCOLARE_ORE + WARNING_PROPOSTA_ANNULLATA);
            return;
        }
        Proposta tempProposta = new Proposta(categoriaRichiesta, categoriaOfferta, oreRichiesta, oreOfferta, (Fruitore) utenteAttivo);

        // 3. conferma e memorizza la proposta
        if (!InputDati.yesOrNo("\n" + tempProposta + "\n" + MSG_CONFERMA_PROPOSTA.formatted(oreOfferta))) {
            System.out.println(WARNING_PROPOSTA_ANNULLATA);
            return;
        }

        // 3.1 se confermi ma è duplicata, segnala e non aggiunge
        if (controllaPropostaDuplicata(tempProposta)) {
            System.out.println(WARNING_PROPOSTA_DUPLICATA);
            return;
        }
        addProposta(tempProposta);
        cercaProposteDaChiudere(tempProposta);
    }

    /**
     * Controlla se esiste già una proposta così nel comprensorio, da parte dello stesso utente.
     *
     * @return true se esiste, false altrimenti
     */
    private boolean controllaPropostaDuplicata(Proposta proposta) {
        String comprensorio = GestoreUtenti.getInformazioniFruitore(proposta.getAutore()).getComprensorioDiAppartenenza();
        return getListaProposteComprensorio(comprensorio).stream()
                .filter(p -> p.getStato() == StatiProposta.APERTA)
                .anyMatch(p -> p.getRichiesta().equals(proposta.getRichiesta())
                        && p.getOfferta().equals(proposta.getOfferta())
                        && p.getOreRichiesta() == proposta.getOreRichiesta()
                        && p.getOreOfferta() == proposta.getOreOfferta()
                );
    }


    private void cercaProposteDaChiudere(Proposta nuovaProposta) {
        assert listaProposte != null;

        ArrayList<Proposta> catena = new ArrayList<>();
        ArrayList<Proposta> proposteComprensorio = new ArrayList<>();

        proposteComprensorio.addAll(getListaProposteComprensorio(nuovaProposta.getComprensorio()));
        catena.addAll(concatenaCompatibili(nuovaProposta, nuovaProposta, proposteComprensorio));

        if (catena.isEmpty()) // non esiste una catena di proposte che con l'aggiunta della nuova verrebbero soddisfatte
            return;

        catena.add(nuovaProposta); // aggiungo la nuova così posso chiuderle tutte
        catena.forEach(Proposta::setChiusa);
        serializeXML(); // aggiorno i dati salvati con i nuovi stati
    }

    private ArrayList<Proposta> concatenaCompatibili(Proposta first, Proposta last, ArrayList<Proposta> proposteComprensorio) {
        assert proposteComprensorio != null;

        if (proposteComprensorio.isEmpty())
            return new ArrayList<>();

        ArrayList<Proposta> catena = new ArrayList<>();
        for (Proposta proposta : proposteComprensorio) {

            if (last.isOffertaCompatibile(proposta)) { // si concatena! ma si richiude anche?
                catena.add(proposta);

                if (!isCatenaChiusa(first, proposta)) { // non si chiude.. continuo la ricerca
                    ArrayList<Proposta> proposteComprensorioRidotte = new ArrayList<>(proposteComprensorio);
                    proposteComprensorioRidotte.remove(proposta);

                    ArrayList<Proposta> continuoCatena = new ArrayList<>();
                    continuoCatena.addAll(concatenaCompatibili(first, proposta, proposteComprensorioRidotte));

                    if (continuoCatena.isEmpty()) { // allora proposta non porta a nessuna catena conclusa
                        catena.remove(proposta);
                    } else { // la catena è stata chiusa nella ricorsione appena precedente, continuo a ritornare
                        catena.addAll(continuoCatena);
                        return catena;
                    }
                } else {
                    return catena; // catena chiusa! (la prima non è inserita in catena)
                }
            }
        }
        return new ArrayList<>(); // nessuna concatenazione
    }

    /**
     * Controlla che l'offerta dell'ultima proposta aggiunta soddisfi la richiesta della prima proposta della catena
     *
     * @param first, prima proposta
     * @param last,  ultima proposta concatenata
     * @return true se si richiude, false altrimenti
     */
    private boolean isCatenaChiusa(Proposta first, Proposta last) {
        return first.getRichiesta().equals(last.getOfferta())
                && first.getOreRichiesta() == last.getOreOfferta();
    }

    private void cambiaStatoProposta() {
        String categoriaRichiesta;
        String categoriaOfferta;
        int oreRichiesta;
        Proposta daCambiare = null;

        // 1. inserimento categoria richiesta, ore, e categoria offerta
        boolean found = false;
        do{
            visualizzaProposteModificabili(utenteAttivo.getUsername());
            categoriaRichiesta = gestFatt.selezioneFogliaPerCambioStatoProposta(MSG_SELEZIONE_CATEGORIA_RICHIESTA);
            visualizzaProposteModificabili(utenteAttivo.getUsername());
            oreRichiesta = InputDati.leggiInteroPositivo(MSG_SELEZIONE_ORE);
            visualizzaProposteModificabili(utenteAttivo.getUsername());
            categoriaOfferta = gestFatt.selezioneFogliaPerCambioStatoProposta(MSG_SELEZIONE_CATEGORIA_OFFERTA);

            for (Proposta proposta : listaProposte.get(((Fruitore)utenteAttivo).getComprensorioDiAppartenenza())){
                if(proposta.getOfferta().equals(categoriaOfferta) && proposta.getOreRichiesta() == oreRichiesta && proposta.getRichiesta().equals(categoriaRichiesta)){
                    found = true;
                    daCambiare = proposta;
                    break;
                };
            }
        }while (!found);

        assert daCambiare != null;
        StatiProposta statoAttuale = daCambiare.getStato();
        StatiProposta statoNuovo = statoAttuale==StatiProposta.APERTA ? StatiProposta.RITIRATA : StatiProposta.APERTA;
        if (InputDati.yesOrNo("Vuoi cambiare lo stato della proposta da %s a %s?".formatted(statoAttuale, statoNuovo))) {
            if (statoAttuale == StatiProposta.RITIRATA) {
                daCambiare.setAperta();
            }else {
                daCambiare.setRitirata();
            }
            serializeXML();
            System.out.println("Stato modificato in %s".formatted(statoNuovo));
            return;
        }
    }

    public void visualizzaPropostePerCategoria() {
        assert listaProposte != null;
        String categoria = gestFatt.selezioneFoglia(MSG_INSERISCI_CATEGORIA);
        Predicate<Proposta> filtro = p -> p.getOfferta().equals(categoria) || p.getRichiesta().equals(categoria);
        System.out.println(proposteToString(filtro));
    }

    public void visualizzaPropostePerAutore(String usernameAutore) {
        assert listaProposte != null;
        Predicate<Proposta> filtro = p -> p.getAutore().equals(usernameAutore);
        System.out.println(proposteToString(filtro));
    }

    public void visualizzaProposteModificabili(String usernameAutore) {
        assert listaProposte != null;
        Predicate<Proposta> filtro = p -> p.getAutore().equals(usernameAutore) && p.getStato() != StatiProposta.CHIUSA;
        System.out.println(proposteToString(filtro));
    }

    private void visualizzaProposteDaNotificare() {
        assert listaProposte != null;
        System.out.println(HEADER_PROPOSTE_PRONTE);

        listaProposte.keySet().stream()
                .flatMap(comprensorio -> listaProposte.get(comprensorio).stream()).filter(Proposta::isDaNotificare)
                .forEach(proposta -> {
                    System.out.println(proposta);
                    Fruitore autore = GestoreUtenti.getInformazioniFruitore(proposta.getAutore());
                    String email = autore.getEmail();
                    String comprensorio = autore.getComprensorioDiAppartenenza();
                    System.out.println(MSG_FORMATTED_PROPOSTA_PRONTA.formatted(autore.getUsername(), comprensorio, email));
                    proposta.notificata();
                });

        serializeXML();
    }

    public String proposteToString(Predicate<Proposta> filtro) {
        assert listaProposte != null;

        StringBuilder aperte = new StringBuilder();
        StringBuilder chiuse = new StringBuilder();
        StringBuilder ritirate = new StringBuilder();
        aperte.append(HEADER_PROPOSTE_APERTE);
        chiuse.append(HEADER_PROPOSTE_CHIUSE);
        ritirate.append(HEADER_PROPOSTE_RITIRATE);

        listaProposte.keySet().stream()
                .flatMap(comprensorio -> listaProposte.get(comprensorio).stream()).filter(filtro).forEach(proposta -> {
                    switch (proposta.getStato()) {
                        case StatiProposta.APERTA -> aperte.append(proposta).append("\n");
                        case StatiProposta.CHIUSA -> chiuse.append(proposta).append("\n");
                        case StatiProposta.RITIRATA -> ritirate.append(proposta).append("\n");
                    }
                });

        return aperte.append(chiuse).append(ritirate).toString();
    }

    public void entryPoint(int scelta) {
        if (utenteAttivo.getClass() == Configuratore.class) {
            switch (scelta) {
                case 1 -> visualizzaPropostePerCategoria();
                case 2 -> visualizzaProposteDaNotificare();
                case 3 -> cambiaStatoProposta();
                default ->{}
            }
        } else {
            switch (scelta) {
                case 1 -> visualizzaPropostePerAutore(utenteAttivo.getUsername());
                case 2 -> effettuaProposta();
                case 3 -> cambiaStatoProposta();
                default -> {}
            }
        }

    }


}
