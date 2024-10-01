package attivita;

public class FattoreDiConversione {
    private String nome_c1, nome_c2;
    private float fattore;

    public FattoreDiConversione(String nome_c1, String nome_c2, float fattore){
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

    public float getFattore() {
        return fattore;
    }

    public void setFattore(float fattore) {
        this.fattore = fattore;
    }
}
