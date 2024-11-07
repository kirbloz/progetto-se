package it.unibs.projectIngesoft.gestori;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.libraries.InputDati;
import it.unibs.projectIngesoft.libraries.Serializer;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.List;


public class GestoreProposte {
    public static final String WARNING_PROPOSTA_ANNULLATA = ">> Proposta annullata";
    public static final String MSG_INSERISCI_RICHIESTA = ">> Inserisci una categoria valida di cui vuoi effettuare la RICHIESTA.";
    public static final String MSG_INSERISCI_OFFERTA = ">> Inserisci una categoria valida che sei disposto a OFFRIRE in cambio.";
    public static final String MSG_RICHIESTA_ORE = ">> Inserisci il numero di ORE che vuoi richiedere:\n> ";
    public static final String WARNING_IMPOSSIBILE_CALCOLARE_ORE = ">> Impossibile Calcolare il numero di ore da offrire.\n";
    public static final String MSG_CONFERMA_PROPOSTA = ">> Dovrai offrire %d ore in cambio. Confermi?%n> ";

    @JacksonXmlElementWrapper(localName = "listaProposte")
    @JacksonXmlProperty(localName = "Proposta")
    private ArrayList<Proposta> listaProposte;
    private GestoreFattori gestFatt;
    private String filePath;
    private Utente utenteAttivo;

    public GestoreProposte(String proposteFilepath, String fattoriFilePath, Utente utenteAttivo) {
        this.gestFatt = new GestoreFattori(fattoriFilePath);
        this.filePath = proposteFilepath;
        this.listaProposte = new ArrayList<>();
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

        Proposta tempProposta = new Proposta(categoriaRichiesta, categoriaOfferta, oreRichiesta, oreOfferta, utenteAttivo.getUsername());

        // 3. conferma e memorizza la proposta
        if (InputDati.yesOrNo("\n" + tempProposta + "\n" + MSG_CONFERMA_PROPOSTA.formatted(oreOfferta)))
            addProposta(tempProposta);
        else
            System.out.println(WARNING_PROPOSTA_ANNULLATA);
    }

    public void addProposta(Proposta proposta) {
        listaProposte.add(proposta);
        serializeXML();
    }

}
