package attivita;

public class CategoriaFoglia extends Categoria {

    public Categoria madre;
    public String dominio;
    public String campo;

    public CategoriaFoglia(String nome, Categoria madre, String campo, String dominio) {
        super(nome);
        this.madre = madre;
        this.campo = campo;
        this.dominio = dominio;
    }

    public void setDominio(String dominio) {
        dominio = dominio;
    }

    public String getDominio() {
        return dominio;
    }

    public Categoria getMadre() {
        return madre;
    }

    public void setMadre(Categoria madre) {
        madre = madre;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }
}
