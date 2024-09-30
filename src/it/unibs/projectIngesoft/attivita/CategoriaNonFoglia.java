package attivita;

import java.util.ArrayList;

public class CategoriaNonFoglia extends Categoria {

    //campo, dominio, madre, descrizione
    public Categoria madre;
    public ArrayList<String> dominio;
    public String campo;


    public CategoriaNonFoglia(String nome) {
        super(nome);
    }

    public ArrayList<String> getDominio() {
        return dominio;
    }

    public String getCampo() {
        return campo;
    }

    public Categoria getMadre() {
        return madre;
    }

    public void setMadre(Categoria madre) {
        this.madre = madre;
    }

    public void setDominio(ArrayList<String> dominio) {
        this.dominio = dominio;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }
}
