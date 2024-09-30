package attivita;

public class CategoriaFoglia extends Categoria{

    public Categoria madre;
    public String dominio;

    public CategoriaFoglia(String nome) {
        super(nome);
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
}
