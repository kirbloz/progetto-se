package it.unibs.projectIngesoft.gestori;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.parsing.SerializerJSON;

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

    private final String filePath;
    private List<ComprensorioGeografico> listaComprensoriGeografici;

    public ComprensorioGeograficoModel(String filePath) {
        this.filePath = filePath;
        this.listaComprensoriGeografici = new ArrayList<>();
        deserializeXML(); // load dati
    }

    /**
     * De-serializza l'attributo listaComprensoriGeografici.
     * Sfrutto l'implementazione statica della classe Serializer.
     */
    public void deserializeXML() {
        assert this.filePath != null;
        if (this.listaComprensoriGeografici == null)
            listaComprensoriGeografici = new ArrayList<>();

        /*List<ComprensorioGeografico> listaLetta = SerializerJSON.deserialize(new TypeReference<>() {
        }, this.filePath);
        this.listaComprensoriGeografici.clear();
        if (listaLetta != null)
            this.listaComprensoriGeografici.addAll(listaLetta);*/
    }

    /**
     * Serializza l'attributo listaComprensoriGeografici.
     * Sfrutto l'implementazione statica della classe Serializer.
     */
    public void serializeXML() {
        assert this.listaComprensoriGeografici != null;
        assert this.filePath != null;
        //SerializerJSON.serialize(this.filePath, this.listaComprensoriGeografici);
    }

    /**
     * Richiama il metodo necessario in base alla selezione dal menu principale.
     *
     * @param scelta, selezione dal menu
     */
    public void entryPoint(int scelta) {
        switch (scelta) {
            case 1 -> aggiungiComprensorio();
            case 2 -> scegliComprensorioDaStampare();
            default -> System.out.println("Nulla da mostrare");
        }
    }

    private void addComprensorio(ComprensorioGeografico comprensorio) { //Aggiunge il comprensorio alla lista e serializza
        if (!this.listaComprensoriGeografici.contains(comprensorio)) {
            this.listaComprensoriGeografici.add(comprensorio);
            serializeXML();
        }
    }

    public void aggiungiComprensorio() {
        String nomeComprensorio;
        do {
            nomeComprensorio = InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_NUOVO_COMPRENSORIO);
        } while (isNomeGiaUsato(nomeComprensorio));

        ComprensorioGeografico nuovoComprensorio = new ComprensorioGeografico(nomeComprensorio, new ArrayList<>());

        String nomeComuneDaInserire;
        do {
            nomeComuneDaInserire = InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_COMUNE);
            if (!nomeComuneDaInserire.equalsIgnoreCase(STR_END_INPUT)) {
                nuovoComprensorio.addComune(nomeComuneDaInserire);
            }
        } while (!nomeComuneDaInserire.equalsIgnoreCase(STR_END_INPUT) || nuovoComprensorio.getListaComuni().isEmpty());
        // Memorizzazione del nuovo comprensorio
        addComprensorio(nuovoComprensorio);
    }

    private boolean isNomeGiaUsato(String nomeComprensorio) {
        for (ComprensorioGeografico comprensorio : this.listaComprensoriGeografici) {
            if (comprensorio.getNomeComprensorio().equalsIgnoreCase(nomeComprensorio)) {
                System.out.println(WARNING_NOME_COMPRENSORIO_GIA_USATO);
                return true;
            }
        }
        return false;
    }

    private void scegliComprensorioDaStampare() {
        if (listaComprensoriGeografici.isEmpty()) {
            System.out.println(WARNING_NO_COMPRENSORI);
            return;
        }
        for (ComprensorioGeografico comprensorio : this.listaComprensoriGeografici) {
            System.out.println(comprensorio.getNomeComprensorio());
        }

        String nomeDaCercare = InputDatiTerminale.leggiStringaNonVuota(MSG_RICERCA_COMPRENSORIO);
        // Cerca e stampa il comprensorio
        ComprensorioGeografico comprensorioTrovato =
                this.listaComprensoriGeografici.stream()
                        .filter(comprensorio -> comprensorio.getNomeComprensorio().equalsIgnoreCase(nomeDaCercare))
                        .findFirst()
                        .orElse(null);

        if (comprensorioTrovato != null)
            visualizzaComprensorio(comprensorioTrovato);
        else
            System.out.println(WARNING_NOME_COMPRENSORIO_NON_ESISTE);
    }

    /**
     * Visualizza le informazioni di un certo comprensorio geografico.
     *
     * @param comprensorio, nome del comprensorio da visualizzare
     */
    public void visualizzaComprensorio(ComprensorioGeografico comprensorio) {
        System.out.println(comprensorio.toString());
    }

    public ArrayList<String> listaNomiComprensoriGeografici() {
        ArrayList<String> listaNomiComprensoriGeografici = new ArrayList<>();
        for(ComprensorioGeografico comprensorio : listaComprensoriGeografici){
            listaNomiComprensoriGeografici.add(comprensorio.getNomeComprensorio());
        }
        return listaNomiComprensoriGeografici;
    }
}
