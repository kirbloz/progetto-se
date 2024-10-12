package it.unibs.projectIngesoft.attivita;

import java.util.ArrayList;

public class CategoriaNonFoglia extends Categoria {

    private String campo;
    private ArrayList<ValoreDominio> dominio;
    private CategoriaNonFoglia madre;

    private ArrayList<Categoria> categorieFiglie;
    private boolean isRadice;

    // questo costruttore esiste solo per un capriccio di jackson
    public CategoriaNonFoglia() {
        super();
    }

    // costruttore per categoria radice
    public CategoriaNonFoglia(String nome, String campo, ArrayList<ValoreDominio> dominio) {
        super(nome);
        this.campo = campo;
        this.dominio = dominio;
        this.isRadice = true;
        this.categorieFiglie = new ArrayList<>();
    }

    // costruttore per categoria non radice
    public CategoriaNonFoglia(String nome, String campo, ArrayList<ValoreDominio> dominio, CategoriaNonFoglia madre) {
        super(nome);
        this.campo = campo;
        this.dominio = dominio;
        this.madre = madre;
        this.isRadice = false;
        this.categorieFiglie = new ArrayList<>();
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

    public ArrayList<Categoria> getCategorieFiglie() {
        return categorieFiglie;
    }

    public void setCategorieFiglie(ArrayList<Categoria> categorieFiglie) {
        this.categorieFiglie = categorieFiglie;
    }

    public int getNumCategorieFiglie() {
        return categorieFiglie.size();
    }

    public boolean isRadice() {
        return isRadice;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Categoria: ").append(this.getNome()).append("\n");
        sb.append("Dominio: ").append(this.getDominio()).append("\n");
        if(!this.isRadice())
            sb.append("Madre: ").append(this.getMadre().getNome()).append("\n");

        return sb.toString();
    }

    public String figlieToString(){
        StringBuilder sb = new StringBuilder();
        for (Categoria f : categorieFiglie) {
            sb.append("\t﹂").append(f.toString()).append("\n");
        }
        return sb.toString();
    }


}
