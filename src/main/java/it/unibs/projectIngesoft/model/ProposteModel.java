package it.unibs.projectIngesoft.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.attivita.StatiProposta;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.mappers.ProposteMapper;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class ProposteModel {

    public static final String HEADER_PROPOSTE_PRONTE = ">> PROPOSTE PRONTE <<";
    public static final String HEADER_PROPOSTE_MODIFICABILI = ">> PROPOSTE MODIFICABILI<<\n";
    public static final String HEADER_PROPOSTE_AUTORE = ">> PROPOSTE DI %s <<\n";
    public static final String HEADER_PROPOSTE_CATEGORIA = ">> PROPOSTE CON %s <<\n";

    public static final String HEADER_PROPOSTE_CHIUSE = ">> PROPOSTE CHIUSE\n";
    public static final String HEADER_PROPOSTE_RITIRATE = ">> PROPOSTE RITIRATE\n";
    public static final String HEADER_PROPOSTE_APERTE = ">> PROPOSTE APERTE\n";



    private static final String MSG_INSERISCI_CATEGORIA = ">> Inserisci una categoria di cui ricercare le proposte";


    public static final String MSG_SELEZIONE_CATEGORIA_RICHIESTA = ">> Inserisci la categoria RICHIESTA per la selezione: ";
    public static final String MSG_SELEZIONE_ORE = ">> Inserisci il monte ORE RICHIESTE per la selezione: ";
    public static final String MSG_SELEZIONE_CATEGORIA_OFFERTA = ">> Inserisci la categoria OFFERTA per la selezione: ";
    public static final String MSG_CONFERMA_CAMBIO_STATO = ">> Vuoi cambiare lo stato della proposta da %s a %s?";
    public static final String MSG_STATO_MODIFICATO = ">> Stato modificato in %s";

    public static final String WARNING_IMPOSSIBILE_CALCOLARE_ORE = ">> Impossibile Calcolare il numero di ore da offrire.\n";
    public static final String WARNING_PROPOSTA_ANNULLATA = ">> Proposta annullata";
    public static final String WARNING_PROPOSTA_DUPLICATA = ">> Proposta duplicata! Procedura annullata.";
    public static final String MSG_NON_HAI_PROPOSTE_NON_CHIUSE = ">> Non hai proposte non chiuse";

    @JacksonXmlElementWrapper(localName = "listaProposte")
    @JacksonXmlProperty(localName = "Proposta")
    private Map<String, List<Proposta>> hashListaProposte;
    // TODO sostituire con un'interazione con il controller
    //private final FattoriModel gestFatt;
    private final Utente utenteAttivo;

    private final ProposteMapper mapper;

    public ProposteModel(Utente utenteAttivo, ProposteMapper mapper) {
       // this.gestFatt = new FattoriModel();

        this.hashListaProposte = new HashMap<>();
        this.utenteAttivo = utenteAttivo;

        this.mapper = mapper;
        hashListaProposte = mapper.read();
        if(hashListaProposte == null) {
            hashListaProposte = new HashMap<>();
        }

    }


    public Map<String, List<Proposta>> getHashListaProposte() {
        return new HashMap<>(hashListaProposte);
    }

    public void addProposta(Proposta proposta) {
        assert this.hashListaProposte != null;
        this.hashListaProposte.computeIfAbsent(proposta.getComprensorio(), k -> new ArrayList<>()).add(proposta);
        save();
    }

    /**
     * Ritorna la lista di proposte afferenti a un certo comprensorio.
     *
     * @param comprensorio, nome del comprensorio
     * @return new ArrayList() se non esistono proposte da un certo comprensorio, altrimenti lista di proposte
     */
    public List<Proposta> getListaProposteComprensorio(String comprensorio) {
        assert this.hashListaProposte != null;
        if (this.hashListaProposte.containsKey(comprensorio))
            return this.hashListaProposte.get(comprensorio);
        else return new ArrayList<>();
    }

    public void save(){
        mapper.write(new HashMap<>(hashListaProposte));
    }

    /**
     * Controlla se esiste già una proposta così nel comprensorio, da parte dello stesso utente.
     *
     * @return true se esiste, false altrimenti
     */
	public boolean controllaPropostaDuplicata(Proposta proposta) {
        String comprensorio = UtentiModel.getInformazioniFruitore(proposta.getAutoreUsername()).getComprensorioDiAppartenenza();
        return getListaProposteComprensorio(comprensorio).stream()
                .filter(p -> p.getStato() == StatiProposta.APERTA)
                .anyMatch(p -> p.getRichiesta().equals(proposta.getRichiesta())
                        && p.getOfferta().equals(proposta.getOfferta())
                        && p.getOreRichiesta() == proposta.getOreRichiesta()
                        && p.getOreOfferta() == proposta.getOreOfferta()
                );
    }


    public void cercaProposteDaChiudere(Proposta nuovaProposta) {
        assert hashListaProposte != null;

        ArrayList<Proposta> catena = new ArrayList<>();
        ArrayList<Proposta> proposteComprensorio = new ArrayList<>();

        proposteComprensorio.addAll(getListaProposteComprensorio(nuovaProposta.getComprensorio()));
        catena.addAll(concatenaCompatibili(nuovaProposta, nuovaProposta, proposteComprensorio));

        if (catena.isEmpty()) // non esiste una catena di proposte che con l'aggiunta della nuova verrebbero soddisfatte
            return;

        catena.add(nuovaProposta); // aggiungo la nuova così posso chiuderle tutte
        catena.forEach(Proposta::setChiusa);
        mapper.write(new HashMap<>(hashListaProposte)); // aggiorno i dati salvati con i nuovi stati //todo perché fai new?
    }

    private ArrayList<Proposta> concatenaCompatibili(Proposta first, Proposta last, ArrayList<Proposta> proposteComprensorio) {
        assert proposteComprensorio != null;

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
        return new ArrayList<>();// nessuna concatenazione
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

    /**
     * L'autore di una proposta può cambiare il suo stato tra RITIRATA e APERTA.
     * Guida la selezione della proposta.
     * L'autore è sempre un Fruitore.
     */
    private void cambiaStatoProposta() {
        assert utenteAttivo instanceof Fruitore;
        String comprensorio = ((Fruitore) utenteAttivo).getComprensorioDiAppartenenza();

        boolean esisteAlmenoUnaPropostaPerLUtenteLoggatoOra = false;
        if (hashListaProposte != null && hashListaProposte.get(comprensorio) != null) {
            for (Proposta proposta : hashListaProposte.get(comprensorio)) {
                if (proposta.getStato() != StatiProposta.CHIUSA && proposta.getAutoreUsername().equals(utenteAttivo.getUsername())) {
                    esisteAlmenoUnaPropostaPerLUtenteLoggatoOra = true;
                    break;
                }
            }
        }

        if (!esisteAlmenoUnaPropostaPerLUtenteLoggatoOra) {
            System.out.println(MSG_NON_HAI_PROPOSTE_NON_CHIUSE);
            return;
        }

        String categoriaRichiesta;
        String categoriaOfferta;
        int oreRichiesta;
        Proposta daCambiare = null;

        // 1. inserimento categoria richiesta, ore, e categoria offerta
        boolean found = false;
        do {
            visualizzaProposteModificabili();
            //categoriaRichiesta = gestFatt.inserimentoNomeFogliaFormattato(MSG_SELEZIONE_CATEGORIA_RICHIESTA);
            oreRichiesta = InputDatiTerminale.leggiInteroPositivo(MSG_SELEZIONE_ORE);
            //categoriaOfferta = gestFatt.inserimentoNomeFogliaFormattato(MSG_SELEZIONE_CATEGORIA_OFFERTA);

            //daCambiare = cercaProposta(comprensorio, categoriaOfferta, categoriaRichiesta, oreRichiesta);

            if (daCambiare != null && daCambiare.getAutoreUsername().equals(utenteAttivo.getUsername())) {
                found = daCambiare.getStato() != StatiProposta.CHIUSA;
            }
        } while (!found);

        // 2. cambio stato guidato e conferma
        StatiProposta statoAttuale = daCambiare.getStato();
        StatiProposta statoNuovo = (statoAttuale == StatiProposta.APERTA) ? StatiProposta.RITIRATA : StatiProposta.APERTA;

        if (!InputDatiTerminale.yesOrNo(MSG_CONFERMA_CAMBIO_STATO.formatted(statoAttuale, statoNuovo)))
            return; // non conferma
        if (statoAttuale == StatiProposta.RITIRATA) {
            daCambiare.setAperta();
            cercaProposteDaChiudere(daCambiare);
        } else {
            daCambiare.setRitirata();
        }
        System.out.println(MSG_STATO_MODIFICATO.formatted(statoNuovo));

        mapper.write(new HashMap<>(hashListaProposte));
    }

    /**
     * A partire dai dettagli di una proposta, verifica se questa esiste tra le proposte memorizzate.
     *
     * @param categoriaOfferta,   categoria dell'offerta
     * @param categoriaRichiesta, categoria della richiesta
     * @param oreRichiesta,       ore richieste
     * @return Proposta se esiste, null altrimenti
     */
	public Proposta cercaPropostaCambiabile(String categoriaOfferta, String categoriaRichiesta, int oreRichiesta, Fruitore autore) {

        Predicate<Proposta> filtro = p -> p.getOfferta().equals(categoriaOfferta)
                && p.getOreRichiesta() == oreRichiesta
                && p.getRichiesta().equals(categoriaRichiesta)
                && p.getComprensorio().equals(autore.getComprensorioDiAppartenenza())
				&& p.getAutore().equals(autore)
				&& p.getStato() != StatiProposta.CHIUSA;

        return getFilteredProposte(filtro)
                .findFirst()
                .orElse(null);
    }


    /**
     * Visualizza le proposte a schermo dopo averle filtrate a partire da quelle memorizzate.
     *
     * @param filtro, predicato per selezionare le proposte da visualizzare.
     */
    private void visualizzaProposte(String header, Predicate<Proposta> filtro) {
        System.out.println(header);
        System.out.println(proposteToString(filtro));
    }


    /**
     * Mostra le proposte filtrando per autore.
     *
     * @param usernameAutore, username del Fruitore che ha creato la proposta.
     */
    public void visualizzaPropostePerAutore(String usernameAutore) {
        assert hashListaProposte != null;
        Predicate<Proposta> filtro = p -> p.getAutoreUsername().equals(usernameAutore);
        visualizzaProposte(HEADER_PROPOSTE_AUTORE.formatted(usernameAutore), filtro);
    }

    /**
     * Mostra le proposte modificabili. Solo l'autore - loggato - può modificare le sue proposte.
     */
    public void visualizzaProposteModificabili() {
        assert utenteAttivo != null;
        assert hashListaProposte != null;

        Predicate<Proposta> filtro = p -> p.getAutoreUsername().equals(utenteAttivo.getUsername()) && p.getStato() != StatiProposta.CHIUSA;
        visualizzaProposte(HEADER_PROPOSTE_MODIFICABILI, filtro);
    }


    /*public void visualizzaProposteDaNotificare() {
        assert hashListaProposte != null;
        System.out.println(HEADER_PROPOSTE_PRONTE);

        getFilteredProposte(Proposta::isDaNotificare)
                .forEach(proposta -> {
                    System.out.println(proposta);
                    Fruitore autore = proposta.getAutore();
                    String email = autore.getEmail();
                    String comprensorio = autore.getComprensorioDiAppartenenza();
                    System.out.println(MSG_FORMATTED_PROPOSTA_PRONTA.formatted(autore.getUsername(), comprensorio, email));
                    proposta.notificata();
                });
        mapper.write(new HashMap<>(hashListaProposte));
    }*/

    public String proposteToString(Predicate<Proposta> filtro) {
        assert hashListaProposte != null;

        StringBuilder aperte = new StringBuilder();
        StringBuilder chiuse = new StringBuilder();
        StringBuilder ritirate = new StringBuilder();
        aperte.append(HEADER_PROPOSTE_APERTE);
        chiuse.append(HEADER_PROPOSTE_CHIUSE);
        ritirate.append(HEADER_PROPOSTE_RITIRATE);

        getFilteredProposte(filtro)
                .forEach(proposta -> {
                    switch (proposta.getStato()) {
                        case StatiProposta.APERTA -> aperte.append(proposta).append("\n");
                        case StatiProposta.CHIUSA -> chiuse.append(proposta).append("\n");
                        case StatiProposta.RITIRATA -> ritirate.append(proposta).append("\n");
                    }
                });

        return aperte.append(chiuse).append(ritirate).toString();
    }

    /**
     * restituisce uno stream di Proposte Filtrato
     * @param filtro
     * @return
     */
    public Stream<Proposta> getFilteredProposte(Predicate<Proposta> filtro) {
        return hashListaProposte.keySet().stream().flatMap(comprensorio -> hashListaProposte.get(comprensorio).stream()).filter(filtro);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* todo either useless or to move
    public void entryPoint(int scelta) {
        if (utenteAttivo.getClass() == Configuratore.class) {
            switch (scelta) {
                case 1 -> visualizzaPropostePerCategoria();
                case 2 -> visualizzaProposteDaNotificare();
                case 3 -> cambiaStatoProposta();
                default -> {
                }
            }
        } else {
            switch (scelta) {
                case 1 -> visualizzaPropostePerAutore(utenteAttivo.getUsername());
                case 2 -> effettuaProposta();
                case 3 -> cambiaStatoProposta();
                default -> {
                }
            }
        }
    }*/
	
	
	//////////////////// Nuovi Metodi /////////////////////////////////

    // todo questo metodo è replicabile semplicemente chiamando getFilteredProposte e controllando che il risultato
    // non sia empty
    // ho cambiato il nome perchè si cerca per autore, non utente.
    // c'è sempre la certezza che questi metodi siano chiamati con fruitori
	public boolean esisteAlmenoUnaPropostaPerAutore(Fruitore autore){
        /*String comprensorio = utente.getComprensorioDiAppartenenza();
		
		if (hashListaProposte != null && hashListaProposte.get(comprensorio) != null) {
            for (Proposta proposta : hashListaProposte.get(comprensorio)) {
                if (proposta.getStato() != StatiProposta.CHIUSA && proposta.getAutoreUsername().equals(utente.getUsername())) {
                    return true;
                }
            }
        }
		return false;*/
        Predicate<Proposta> filtro = p -> p.getAutoreUsername().equals(/*utenteAttivo*/autore.getUsername())
                && p.getStato() != StatiProposta.CHIUSA;
        return getFilteredProposte(filtro).findAny().isPresent(); //versione stream equivalente a !isEmpty()
	}

    //c'è già il metodo visualizzaProposteModificabili -> todo ok modifico questo
	public List<Proposta> getProposteModificabiliPerAutore(Fruitore autore){
		/*
        List<Proposta> proposteValide = new ArrayList<>();
		for(Proposta proposta : hashListaProposte.get(autore.getComprensorioDiAppartenenza())){
			if (proposta.getStato() != StatiProposta.CHIUSA && proposta.getAutoreUsername().equals(autore.getUsername())) {
                    proposteValide.add(proposta);
                }
		}
        return proposteValide;
        */

        Predicate<Proposta> filtro = p -> p.getAutoreUsername().equals(autore.getUsername())
                && p.getStato() != StatiProposta.CHIUSA;
		return getFilteredProposte(filtro).toList();

	}
	
	public void cambiaStato(Proposta daCambiare){
		//(daCambiare.getStato() == StatiProposta.APERTA) ? daCambiare.setStato(StatiProposta.RITIRATA) : daCambiare.setStato(StatiProposta.APERTA);
        if(daCambiare.getStato() == StatiProposta.APERTA) daCambiare.setRitirata();
        else {
            daCambiare.setAperta();
            cercaProposteDaChiudere(daCambiare);
        }
		
        mapper.write(new HashMap<>(hashListaProposte));
	}

    public List<Proposta> getPropostePerAutore(Fruitore autore) {
        Predicate<Proposta> filtro = p -> p.getAutoreUsername().equals(autore.getUsername());
        return getFilteredProposte(filtro).toList();
    }
}
