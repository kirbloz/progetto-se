package it.unibs.projectIngesoft.gestori;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.libraries.InputDati;
import it.unibs.projectIngesoft.libraries.Serializer;

import java.util.ArrayList;
import java.util.List;


public class GestoreProposte {
    public static final String MSG_RICHIESTA_RADICE = ">> Inserisci la radice:\n> ";
    private static final String MSG_RICHIESTA_FOGLIA = ">> Inserisci la foglia:\n> ";
    public static final String MSG_INSERISCI_RICHIESTA = ">> Inserisci una categoria valida di cui vuoi effettuare la RICHIESTA.";
    public static final String MSG_INSERISCI_OFFERTA = ">> Inserisci una categoria valida che sei disposto a OFFRIRE in cambio.";
    public static final String MSG_RICHIESTA_ORE = ">> Inserisci il numero di ORE che vuoi richiedere:\n> ";

    @JacksonXmlElementWrapper(localName = "listaProposte")
    @JacksonXmlProperty(localName = "Proposta")
    private ArrayList<Proposta> listaProposte;

    private GestoreFattori gestFatt;

    private String filePath;

    public GestoreProposte(String proposteFilepath, String fattoriFilePath) {
        this.gestFatt = new GestoreFattori(fattoriFilePath);
        this.filePath = proposteFilepath;
        this.listaProposte = new ArrayList<>();
        deserializeXML();
    }

    public void effettuaProposta(){
        String richiesta;
        String offerta;
        int oreRichiesta;
        int oreOfferta;

        richiesta = richiestaFoglia(MSG_INSERISCI_RICHIESTA);
        oreRichiesta = InputDati.leggiInteroPositivo(MSG_RICHIESTA_ORE);
        offerta = richiestaFoglia(MSG_INSERISCI_OFFERTA);

        try {
            oreOfferta = gestFatt.calcolaRapportoOre(richiesta, offerta, oreRichiesta);
        } catch (Exception e) {
            System.out.println("Impossibile Calcolare il numero di ore da offrire");
            throw new RuntimeException(e); //TODO guarda di fare l'eccezione giusta
        }

        if (InputDati.yesOrNo(">> Dovrai offrire %d ore in cambio. Confermi?\n> ".formatted(oreOfferta))){
            addProposta(new Proposta(richiesta, offerta, oreRichiesta, oreOfferta));
        } else System.out.println("Proposta Annullata");

    }

    public String richiestaFoglia(String messaggio){
    String radice;
    String foglia;
    String categoria;

        do {
            System.out.println(messaggio);
            radice = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_RADICE);
            foglia = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_FOGLIA);
            categoria = gestFatt.factorNameBuilder(radice, foglia);
        } while (!gestFatt.esisteCategoriaChiave(categoria));

        return categoria;
    }

    public void addProposta(Proposta proposta){
            listaProposte.add(proposta);
            serializeXML();
    }

    /**
     * Serializza this.listaProposte.
     * Sfrutto l'implementazione statica della classe Serializer.
     */
    private void serializeXML() {
        assert this.listaProposte != null;
        assert this.filePath != null;
        Serializer.serialize(this.filePath, this.listaProposte);
    }

    /**
     * De-serializza this.listaProposte.
     * Sfrutto l'implementazione statica della classe Serializer.
     */
    private void deserializeXML() {
        assert this.listaProposte != null;
        List<Proposta> tempList = Serializer.deserialize(new TypeReference<>() {
        }, this.filePath);

        listaProposte.clear();
        if (tempList != null) listaProposte.addAll(tempList);
    }

}
