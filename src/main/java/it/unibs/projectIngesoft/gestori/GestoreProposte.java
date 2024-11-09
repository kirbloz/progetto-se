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

        Fruitore tempUtente = new Fruitore("ciao", "1234", "asdf@culo.iy", "Brescia");

        deserializeXML();

        /*
        addProposta(new Proposta("radice:liv2f3", "radice:liv2f1", 1, 1, tempUtente));
        addProposta(new Proposta("radice:liv2f1", "test:livello1f1", 1, 1, tempUtente));
        addProposta(new Proposta("test:livello1f1", "test:livello1f2", 1, 1, tempUtente));
*/

        serializeXML();
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
        if (InputDati.yesOrNo("\n" + tempProposta + "\n" + MSG_CONFERMA_PROPOSTA.formatted(oreOfferta))) {
            addProposta(tempProposta);
            controllaSeChiuderla(tempProposta);
        } else
            System.out.println(WARNING_PROPOSTA_ANNULLATA);
    }


    private void controllaSeChiuderla(Proposta nuovaProposta) {
        //todo ma che è
        ArrayList<Proposta> proposteCompatibili = cercaProposteCompatibili(nuovaProposta, nuovaProposta);

        if (proposteCompatibili.isEmpty())
            return;

        proposteCompatibili.add(nuovaProposta);
        proposteCompatibili.stream()
                .filter(compatibile -> listaProposte.get(nuovaProposta.getComprensorio()).contains(compatibile))
                .forEach(Proposta::setChiusa);

        serializeXML();
    }

    private ArrayList<Proposta> cercaProposteCompatibili(Proposta propostaDaControllare, Proposta propostaOriginale) {
        assert listaProposte != null;
        ArrayList<Proposta> insiemeDiProposteCompatibili = new ArrayList<>();

        for (Proposta proposta : listaProposte.get(propostaDaControllare.getComprensorio())) {
            if (proposta.getStato() == StatiProposta.APERTA) {
                if (propostaDaControllare.isCompatibile(proposta) && proposta.isCompatibile(propostaOriginale)) {
                    //fine ricorsione, ritorna l' array di Proposte da cui sei passato
                    insiemeDiProposteCompatibili.add(proposta);
                    return insiemeDiProposteCompatibili;
                } else if (propostaDaControllare.isCompatibile(proposta)) {
                    //continua ricorsione
                    insiemeDiProposteCompatibili.addAll(cercaProposteCompatibili(proposta, propostaOriginale));
                    return insiemeDiProposteCompatibili;
                }
            }
        }

        return new ArrayList<>();
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

    private void visualizzaProposteDaNotificare() {
        assert listaProposte != null;
        System.out.println(HEADER_PROPOSTE_PRONTE);

        listaProposte.keySet().stream()
                .flatMap(comprensorio -> listaProposte.get(comprensorio).stream()).filter(Proposta::isDaNotificare)
                .forEach(proposta -> {
                    System.out.println(proposta);
                    String email = GestoreUtenti.getInformazioniFruitore(proposta.getAutore()).getEmail();
                    System.out.println(proposta.getAutore() + " --> Email: " + email + "\n");
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
                default -> System.out.println("Nulla da mostrare");
            }
        } else {
            switch (scelta) {
                case 1 -> visualizzaPropostePerAutore(utenteAttivo.getUsername());
                case 2 -> effettuaProposta();
                default -> System.out.println("Nulla da mostrare");
            }
        }

    }


}
