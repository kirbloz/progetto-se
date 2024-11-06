package it.unibs.projectIngesoft.gestori;

import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.libraries.InputDati;

import java.util.ArrayList;


public class GestoreProposte {
    public static final String MSG_RICHIESTA_RADICE = "Inserisci la radice: ";
    private static final String MSG_RICHIESTA_FOGLIA = "Inserisci la foglia: ";
    public static final String MSG_INSERISCI_RICHIESTA = ">> Inserisci una categoria valida di cui vuoi effettuare la richiesta.";
    public static final String MSG_INSERISCI_OFFERTA = ">> Inserisci una categoria valida che sei disposto a offrire in cambio.";
    public static final String MSG_RICHIESTA_ORE = "Inserisci il numero di ore che vuoi richiedere: ";

    private ArrayList<Proposta> listaProposte;
    private GestoreFattori gestFatt;

    public GestoreProposte(String proposteFilepath, String fattoriFilePath) {
        this.gestFatt = new GestoreFattori(fattoriFilePath);
    }

    public void effettuaProposta(){
        String richiesta, offerta;
        int oreRichiesta, oreOfferta;


        richiesta = richiestaFoglia();
        oreRichiesta = InputDati.leggiInteroPositivo(MSG_RICHIESTA_ORE);

        offerta = richiestaFoglia();
        try {
            oreOfferta = gestFatt.calcolaRapportoOre(richiesta, offerta, oreRichiesta);
        } catch (Exception e) {
            System.out.println("Impossibile Calcolare il numero di ore da offrire");
            throw new RuntimeException(e); //TODO guarda di fare l'eccezione giusta
        }

        if (InputDati.yesOrNo("Dovrai offrire %d ore in cambio, confermi? S/N".formatted(oreOfferta))){
            addProposta(new Proposta(richiesta, offerta, oreRichiesta, oreOfferta));
        } else System.out.println("Proposta Annullata");

    }

    public String richiestaFoglia(){
    String radice, foglia, categoria;

        do {
            System.out.println(MSG_INSERISCI_RICHIESTA);
            radice = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_RADICE);
            foglia = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_FOGLIA);
            categoria = gestFatt.factorNameBuilder(radice, foglia);
        } while (gestFatt.hashmapContainsKey(categoria));

        return categoria;
    }

    public void addProposta(Proposta proposta){
            listaProposte.add(proposta);
    }
}
