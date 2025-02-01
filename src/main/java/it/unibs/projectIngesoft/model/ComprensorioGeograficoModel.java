package it.unibs.projectIngesoft.model;

import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.mappers.CompGeoMapper;

import java.util.ArrayList;
import java.util.List;

public class ComprensorioGeograficoModel {

    private static final String MSG_INSERISCI_COMUNE = ">> Inserire nome del comune da inserire oppure fine per terminare l'inserimento:\n> ";
    private static final String MSG_INSERISCI_NOME_NUOVO_COMPRENSORIO = ">> Inserire il nome del comprensorio geografico:\n> ";
    private static final String MSG_RICERCA_COMPRENSORIO = ">> Inserire il nome del comprensorio da visualizzare:\n> ";

    private static final String WARNING_NOME_COMPRENSORIO_GIA_USATO = ">> (!!) Attenzione, esiste già un comprensorio con questo nome";
    public static final String WARNING_NOME_COMPRENSORIO_NON_ESISTE = ">> (!!) Non esiste un comprensorio con questo nome";
    public static final String STR_END_INPUT = "fine";
    public static final String WARNING_NO_COMPRENSORI = ">> (!!) Non ci sono comprensori memorizzati.";

    private List<ComprensorioGeografico> listaComprensoriGeografici;
    private final CompGeoMapper mapper;

    public ComprensorioGeograficoModel(CompGeoMapper mapper) {
        this.mapper = mapper;
        this.listaComprensoriGeografici =  mapper.read();
        if(this.listaComprensoriGeografici == null) {
            this.listaComprensoriGeografici = new ArrayList<>();
        }
    }

    /////////////////////////////////////////////// Rifattorizzati /////////////////////////////////////////////////////
    /**
     * Aggiunge il comprensorio alla lista e serializza
     */

    public void addComprensorio(ComprensorioGeografico comprensorio) {
        if (!this.listaComprensoriGeografici.contains(comprensorio)) {
            this.listaComprensoriGeografici.add(comprensorio);
            mapper.write(listaComprensoriGeografici);
        }
    }

    //Todo messo public da usare nel controller, ma se uso le String[] come in fattori non serve
    public boolean isNomeGiaUsato(String nomeComprensorio) {
        for (ComprensorioGeografico comprensorio : this.listaComprensoriGeografici) {
            if (comprensorio.getNomeComprensorio().equalsIgnoreCase(nomeComprensorio)) {
                System.out.println(WARNING_NOME_COMPRENSORIO_GIA_USATO);
                return true;
            }
        }
        return false;
    }

    //todo da usare nel controller per il controllo di unicità nella view
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

    //todo chiamare dal controller una volta ottenute le robe dall'input
    public void aggiungiComprensorio(String nomeComprensorio, List<String> comuni) {
        addComprensorio(new ComprensorioGeografico(nomeComprensorio, comuni));
    }

    public boolean isEmpty() {
        return this.listaComprensoriGeografici.isEmpty();
    }

}
