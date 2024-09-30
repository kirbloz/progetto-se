package attivita;

import java.util.ArrayList;

public class CategoriaRadice extends Categoria {


    public ArrayList<String> dominio;
    public String campo;

    public CategoriaRadice(String nome, ArrayList<String> dominio, String campo) {
        super(nome);
        this.campo = campo;
        this.dominio = dominio;
    }

    public ArrayList<String> getDominio() {
        return dominio;
    }

    public String getCampo() {
        return campo;
    }

    public void setDominio(ArrayList<String> dominio) {
        this.dominio = dominio;
    }

    public boolean addDominio(String dominio) {
        return this.dominio.add(dominio);
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }


}
