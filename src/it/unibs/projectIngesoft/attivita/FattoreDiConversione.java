package attivita;

public class FattoreDiConversione {
    private String nome_c1, nome_c2;
    private double fattore;

    /**
     *
     * @param nome_c1, nome della prima categoria
     * @param nome_c2, nome della seconda categoria
     * @param fattore, valore del fattore di conversione, dalle ore della prima categoria alle ore della seconda
     */
    public FattoreDiConversione(String nome_c1, String nome_c2, double fattore){
        this.nome_c1 = nome_c1;
        this.nome_c2 = nome_c2;
        this.fattore = fattore;
    }

    public String getNome_c1() {
        return nome_c1;
    }

    public void setNome_c1(String nome_c1) {
        this.nome_c1 = nome_c1;
    }

    public String getNome_c2() {
        return nome_c2;
    }

    public void setNome_c2(String nome_c2) {
        this.nome_c2 = nome_c2;
    }

    public double getFattore() {
        return fattore;
    }

    public void setFattore(double fattore) {
        this.fattore = fattore;
    }

    /**
     * UTILIZZATO DAL GESTORE DI FATTORI.
     * FARE ATTENZIONE SE SI MODIFICA
     * TODO
     * PREDISPORRE UN ALTRO "TOSTRING" PER IL GESTORE MAGARI FATTO AD HOC
     * @return
     */
    public String toString(){
        return  nome_c1 + " " + nome_c2 + " " + fattore;
    }
}
