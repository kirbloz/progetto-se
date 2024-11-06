package it.unibs.projectIngesoft.gestori;

import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.libraries.InputDati;

import java.util.ArrayList;


public class GestoreProposte {
    public static final String MSG_RICHIESTA_RADICE = "Inserisci la radice: ";
    private static final String MSG_RICHIESTA_FOGLIA = "Inserisci la foglia: ";

    private ArrayList<Proposta> listaProposte;
    private GestoreFattori gestFatt;

    public GestoreProposte(String proposteFilepath, String fattoriFilePath) {
        this.gestFatt = new GestoreFattori(fattoriFilePath);
    }

    public void effettuaProposta(){
        String richiesta, offerta;
        int oreRichiesta, oreOfferta;

        richiesta = richiestaFoglia();//TODO controllo esistenza foglia
        offerta = richiestaFoglia();
        oreRichiesta = InputDati.leggiInteroPositivo("Inserisci il numero di ore richieste: ");
        try {
            oreOfferta = gestFatt.calcolaRapportoOre(richiesta, offerta, oreRichiesta);
        } catch (Exception e) {
            throw new RuntimeException(e); //TODO guarda di fare l'eccezione giusta
        }


        addProposta(new Proposta(richiesta, offerta, oreRichiesta, oreOfferta));

    }

    public String richiestaFoglia(){  //TODO chiedere a wade se usare categorie o fattori di conversione per controllo
    String radice, foglia;          //TODO chiedere a wade se ha senso spostare factorNameBuilder fuori da gestoreFattori
        radice = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_RADICE);
        foglia = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_FOGLIA);

        return gestFatt.factorNameBuilder(radice, foglia);
    }



    public void addProposta(Proposta proposta){    //TODO

    }
}
