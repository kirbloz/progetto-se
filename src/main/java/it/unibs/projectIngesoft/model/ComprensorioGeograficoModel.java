package it.unibs.projectIngesoft.model;

import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.RepositoryLogic.CompGeoRepository;

import java.util.ArrayList;
import java.util.List;

public class ComprensorioGeograficoModel {

    //todo rimuovere?
    private static final String MSG_INSERISCI_COMUNE = ">> Inserire nome del comune da inserire oppure fine per terminare l'inserimento:\n> ";
    private static final String MSG_INSERISCI_NOME_NUOVO_COMPRENSORIO = ">> Inserire il nome del comprensorio geografico:\n> ";
    private static final String MSG_RICERCA_COMPRENSORIO = ">> Inserire il nome del comprensorio da visualizzare:\n> ";

    private static final String WARNING_NOME_COMPRENSORIO_GIA_USATO = ">> (!!) Attenzione, esiste già un comprensorio con questo nome";
    public static final String WARNING_NOME_COMPRENSORIO_NON_ESISTE = ">> (!!) Non esiste un comprensorio con questo nome";
    public static final String STR_END_INPUT = "fine";
    public static final String WARNING_NO_COMPRENSORI = ">> (!!) Non ci sono comprensori memorizzati.";

    private List<ComprensorioGeografico> listaComprensoriGeografici;
    private final CompGeoRepository repository;

    public ComprensorioGeograficoModel(CompGeoRepository repository) {
        this.repository = repository;
        this.listaComprensoriGeografici =  repository.load();
        if(this.listaComprensoriGeografici == null) {
            this.listaComprensoriGeografici = new ArrayList<>();
        }
    }

    /////////////////////////////////////////////// Rifattorizzati /////////////////////////////////////////////////////

    public List<String> getListaNomiComprensoriGeografici() {
        return listaComprensoriGeografici.stream()
                .map(ComprensorioGeografico::getNomeComprensorio)
                .toList();
    }

    public List<String> getStringComuniByComprensorioName(String comprensorio) {
        for (ComprensorioGeografico c : this.listaComprensoriGeografici) {
            if (c.getNomeComprensorio().equals(comprensorio)) {
                return c.getListaComuni();
            }
        }
        return null;
    }

    public void aggiungiComprensorio(String nomeComprensorio, List<String> comuni) {
        ComprensorioGeografico tempComp = new ComprensorioGeografico(nomeComprensorio, comuni);
        if (getStringComuniByComprensorioName(nomeComprensorio) == null) {
            this.listaComprensoriGeografici.add(tempComp);
            repository.save(listaComprensoriGeografici);
        }
    }

    public boolean isEmpty() {
        return this.listaComprensoriGeografici.isEmpty();
    }

}
