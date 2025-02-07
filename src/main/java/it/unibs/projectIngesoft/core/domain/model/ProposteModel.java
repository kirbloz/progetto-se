package it.unibs.projectIngesoft.core.domain.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import it.unibs.projectIngesoft.core.domain.entities.Proposta;
import it.unibs.projectIngesoft.core.domain.entities.StatiProposta;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Fruitore;
import it.unibs.projectIngesoft.persistence.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class ProposteModel {
    @JacksonXmlElementWrapper(localName = "listaProposte")
    @JacksonXmlProperty(localName = "Proposta")
    private Map<String, List<Proposta>> hashListaProposte;

    private final Repository<Map<String, List<Proposta>>> repository;

    public ProposteModel(Repository<Map<String, List<Proposta>>> repository) {
        this.hashListaProposte = new HashMap<>();

        this.repository = repository;
        hashListaProposte = repository.load();
        if(hashListaProposte == null) {
            hashListaProposte = new HashMap<>();
        }
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
        repository.save(hashListaProposte);
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
        save();
    }

    private List<Proposta> concatenaCompatibili(Proposta first, Proposta last, List<Proposta> proposteComprensorio) {
        assert proposteComprensorio != null;

        List<Proposta> catena = new ArrayList<>();
        for (Proposta proposta : proposteComprensorio) {
            if (last.isOffertaCompatibile(proposta)) { // si concatena! ma si richiude anche?
                catena.add(proposta);

                if (!isCatenaChiusa(first, proposta)) { // non si chiude.. continuo la ricerca
                    List<Proposta> proposteComprensorioRidotte = new ArrayList<>(proposteComprensorio);
                    proposteComprensorioRidotte.remove(proposta);

                    List<Proposta> continuoCatena = new ArrayList<>();
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
				&& p.getAutoreUsername().equals(autore.getUsername())
				&& p.getStato() != StatiProposta.CHIUSA;

        return getFilteredProposte(filtro)
                .findFirst()
                .orElse(null);
    }

    public Stream<Proposta> getFilteredProposte(Predicate<Proposta> filtro) {
        return hashListaProposte.keySet().stream().flatMap(comprensorio -> hashListaProposte.get(comprensorio).stream()).filter(filtro);
    }
	
	
	//////////////////// Nuovi Metodi /////////////////////////////////

    // todo questo metodo è replicabile semplicemente chiamando getFilteredProposte e controllando che il risultato
    // non sia empty
    // ho cambiato il nome perchè si cerca per autore, non utente.
    // c'è sempre la certezza che questi metodi siano chiamati con fruitori
	public boolean esisteAlmenoUnaPropostaPerAutore(Fruitore autore){
        Predicate<Proposta> filtro = p -> p.getAutoreUsername().equals(/*utenteAttivo*/autore.getUsername())
                && p.getStato() != StatiProposta.CHIUSA;
        return getFilteredProposte(filtro).findAny().isPresent(); //versione stream equivalente a !isEmpty()
	}

	public List<Proposta> getProposteModificabiliPerAutore(Fruitore autore){
        Predicate<Proposta> filtro = p -> p.getAutoreUsername().equals(autore.getUsername())
                && p.getStato() != StatiProposta.CHIUSA;
		return getFilteredProposte(filtro).toList();

	}
	
	public void cambiaStato(Proposta daCambiare){
		if(daCambiare.getStato() == StatiProposta.APERTA) daCambiare.setRitirata();
        else {
            daCambiare.setAperta();
            cercaProposteDaChiudere(daCambiare);
        }
        save();
	}

    public List<Proposta> getPropostePerAutore(Fruitore autore) {
        Predicate<Proposta> filtro = p -> p.getAutoreUsername().equals(autore.getUsername());
        return getFilteredProposte(filtro).toList();
    }
}
