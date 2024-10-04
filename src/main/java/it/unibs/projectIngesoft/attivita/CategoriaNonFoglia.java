package attivita;

import java.util.ArrayList;

public class CategoriaNonFoglia extends Categoria {

    private String campo;
    private ArrayList<ValoreDominio> dominio;
    private CategoriaNonFoglia madre;
    private boolean isRadice;


    public CategoriaNonFoglia(String nome, String campo, ArrayList<ValoreDominio> dominio) {
        super(nome);
        this.campo = campo;
        this.dominio = dominio;
        this.isRadice = true;
    }

    public CategoriaNonFoglia(String nome, String campo, ArrayList<ValoreDominio> dominio, CategoriaNonFoglia madre) {
        super(nome);
        this.campo = campo;
        this.dominio = dominio;
        this.madre = madre;
        this.isRadice = false;
    }


    public ArrayList<ValoreDominio> getDominio() {
        return dominio;
    }

    public String getCampo() {
        return campo;
    }

    public CategoriaNonFoglia getMadre() {
        return madre;
    }

    public void setDominio(ArrayList<ValoreDominio> dominio) {
        this.dominio = dominio;
    }

    public boolean addDominio(ValoreDominio dominio) {
        return this.dominio.add(dominio);
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public void setMadre(CategoriaNonFoglia madre) {
        this.madre = madre;
    }


}
