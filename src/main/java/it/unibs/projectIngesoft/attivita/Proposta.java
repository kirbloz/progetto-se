package it.unibs.projectIngesoft.attivita;

public class Proposta {
    private String richiesta;
    private String offerta;
    private int oreRichiesta;
    private int oreOfferta;

    public Proposta(String richiesta, String offerta, int oreRichiesta, int oreOfferta) {
        this.richiesta = richiesta;
        this.offerta = offerta;
        this.oreRichiesta = oreRichiesta;
        this.oreOfferta = oreOfferta;
    }
}

