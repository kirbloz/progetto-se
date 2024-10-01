package attivita;

import java.util.ArrayList;

public class CategoriaNonFoglia extends Categoria {

    private ArrayList<ValoreDominio> dominio;
    private String campo;
    private CategoriaNonFoglia madre;
    private boolean isRadice;


    public CategoriaNonFoglia(String nome, ArrayList<ValoreDominio> dominio, String campo) {
        super(nome);
        this.campo = campo;
        this.dominio = dominio;
    }

    public CategoriaNonFoglia(String nome, String campo, CategoriaNonFoglia madre) {
        super(nome);
        this.campo = campo;
        this.madre = madre;
    }


    public ArrayList<ValoreDominio> getDominio() {
        return dominio;
    }

    public String getCampo() {
        return campo;
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

    public CategoriaNonFoglia getMadre() {
        return madre;
    }

    public void setMadre(CategoriaNonFoglia madre) {
        this.madre = madre;
    }


}
