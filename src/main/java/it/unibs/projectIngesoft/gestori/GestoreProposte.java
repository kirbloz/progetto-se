package it.unibs.projectIngesoft.gestori;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.attivita.StatiProposta;
import it.unibs.projectIngesoft.libraries.InputDati;
import it.unibs.projectIngesoft.libraries.Serializer;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GestoreProposte {
    public static final String WARNING_PROPOSTA_ANNULLATA = ">> Proposta annullata";
    public static final String MSG_INSERISCI_RICHIESTA = ">> Inserisci una categoria valida di cui vuoi effettuare la RICHIESTA.";
    public static final String MSG_INSERISCI_OFFERTA = ">> Inserisci una categoria valida che sei disposto a OFFRIRE in cambio.";
    public static final String MSG_RICHIESTA_ORE = ">> Inserisci il numero di ORE che vuoi richiedere:\n> ";
    public static final String WARNING_IMPOSSIBILE_CALCOLARE_ORE = ">> Impossibile Calcolare il numero di ore da offrire.\n";
    public static final String MSG_CONFERMA_PROPOSTA = ">> Dovrai offrire %d ore in cambio. Confermi?%n> ";
    private static final String MSG_INSERISCI_CATEGORIA = "Inserisci una categoria di cui ricercare le proposte";

    @JacksonXmlElementWrapper(localName = "listaProposte")
    @JacksonXmlProperty(localName = "Proposta")
    private HashMap<String, ArrayList<Proposta>> listaProposte;
    private GestoreFattori gestFatt;
    private String filePath;
    private Utente utenteAttivo;

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
        //if (tempList != null) listaProposte.addAll(tempList); //TODO WADE
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
        if (InputDati.yesOrNo("\n" + tempProposta + "\n" + MSG_CONFERMA_PROPOSTA.formatted(oreOfferta)))
            addProposta(tempProposta);
        else
            System.out.println(WARNING_PROPOSTA_ANNULLATA);

        controllaSeChiuderla(tempProposta);
    }

    public void addProposta(Proposta proposta) {
        this.listaProposte.computeIfAbsent(proposta.getComprensorio(), k -> new ArrayList<>()).add(proposta);
        serializeXML();
    }

    private void controllaSeChiuderla(Proposta nuovaProposta) {

        ArrayList<Proposta> proposteCompatibili = cercaProposteCompatibili(nuovaProposta, nuovaProposta);

        if (proposteCompatibili != null) {
            for (Proposta pComp : proposteCompatibili) {
                for (Proposta p : listaProposte.get(nuovaProposta.getComprensorio())) {
                    if (pComp == p) {
                        p.setClosed();
                    }
                }
            }

        }


        //TODO chiuderla (mandare la roba al configuratore etc...(FLAG appenaChiusa???))

    }

    private ArrayList<Proposta> cercaProposteCompatibili(Proposta propostaDaCheckare, Proposta propostaOriginale) {
        ArrayList<Proposta> insiemeDiProposteCompatibili = new ArrayList<>();

        for (Proposta proposta : listaProposte.get(propostaDaCheckare.getComprensorio())) {
            if (proposta.getStato() == StatiProposta.APERTA) {
                if (propostaDaCheckare.isCompatibile(proposta) && proposta.isCompatibile(propostaOriginale)) {
                    //fine ricorsione, ritorna l' array di Proposte da cui sei passato
                    insiemeDiProposteCompatibili.add(proposta);
                    return insiemeDiProposteCompatibili;
                } else if (propostaDaCheckare.isCompatibile(proposta)) {
                    //continua ricorsione
                    insiemeDiProposteCompatibili.addAll(cercaProposteCompatibili(proposta, propostaOriginale)); //forse addall o concatenate o che cazzo ne se
                    return insiemeDiProposteCompatibili;
                }
            }
        }

        return null; // forse basta fare return null;
    }

    public void mostraPropostePerCategoria() {
        String categoria = gestFatt.selezioneFoglia(MSG_INSERISCI_CATEGORIA);

        StringBuilder aperte = new StringBuilder();
        StringBuilder chiuse = new StringBuilder();
        StringBuilder ritirate = new StringBuilder();

        for (ArrayList<Proposta> arraylist : listaProposte.values()) {
            for (Proposta p : arraylist) {
                if (p.getOfferta().equals(categoria) || p.getRichiesta().equals(categoria)) {
                    switch (p.getStato()) {
                        case StatiProposta.APERTA -> aperte.append(p);
                        case StatiProposta.CHIUSA -> chiuse.append(p);
                        case StatiProposta.RITIRATA -> ritirate.append(p);
                    }
                }
            }
        }

        System.out.println(aperte.append(chiuse).append(ritirate).toString());
    }

    public void mostraPropostePerAutore() {
        StringBuilder aperte = new StringBuilder();
        StringBuilder chiuse = new StringBuilder();
        StringBuilder ritirate = new StringBuilder();

        for (Proposta p : listaProposte.get(((Fruitore) utenteAttivo).getComprensorioDiAppartenenza())) {
            if (p.getAutore().getUsername().equals(utenteAttivo.getUsername())) {
                switch (p.getStato()) {
                    case StatiProposta.APERTA -> aperte.append(p);
                    case StatiProposta.CHIUSA -> chiuse.append(p);
                    case StatiProposta.RITIRATA -> ritirate.append(p);
                }
            }
        }

        System.out.println(aperte.append(chiuse).append(ritirate).toString());
    }


    public void entryPoint(int scelta) {
        if (utenteAttivo.getClass() == Configuratore.class) {
            switch (scelta) {
                case 1 -> mostraPropostePerCategoria();
                default -> System.out.println("Nulla da mostrare");
            }
        }else{
            switch (scelta) {
                case 1 -> mostraPropostePerAutore();
                default -> System.out.println("Nulla da mostrare");
            }
        }

    }
}
