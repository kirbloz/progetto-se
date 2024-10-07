package attivita;

public abstract class Categoria {

    private String nome;

    protected Categoria(){

    }

    protected Categoria(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getNome());
        return sb.toString();
    }
}
