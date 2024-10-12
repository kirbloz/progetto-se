package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;

@JsonRootName("")
public class CategoriaNonFoglia extends Categoria {

    @JacksonXmlProperty(localName = "campo")
    private String campo;
    @JacksonXmlProperty(localName = "")
    private ArrayList<ValoreDominio> dominio;
    @JacksonXmlProperty(localName = "madre")
    private CategoriaNonFoglia madre;

    @JacksonXmlProperty(localName = "")
    private ArrayList<Categoria> categorieFiglie;
    @JacksonXmlProperty(localName = "isRadice")
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

    // nelle addCategoriaFiglia non si fa il check se l'arraylist è inizializzato perchè i costruttori
    // lo inizializzano a vuoto in qualsiasi caso
    // ma sto coso muore con xml o che palle
    public void addCategoriaFiglia(CategoriaFoglia categoria) {
        if (this.categorieFiglie == null)
            this.categorieFiglie = new ArrayList<>();
        this.categorieFiglie.add(categoria);
    }

    public void addCategoriaFiglia(CategoriaNonFoglia categoria) {
        if (this.categorieFiglie == null)
            this.categorieFiglie = new ArrayList<>();
        this.categorieFiglie.add(categoria);
    }

    public void removeCategoriaFiglia(CategoriaFoglia categoria) {
        this.categorieFiglie.remove(categoria);
    }

    public int getNumCategorieFiglie() {
        return categorieFiglie.size();
    }

    public boolean isRadice() {
        return isRadice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Categoria: ").append(this.getNome()).append("\n");
        sb.append("Dominio: ").append(this.getCampo()).append("\n");
        if (!this.isRadice())
            sb.append("Madre: ").append(this.getMadre().getNome()).append("\n");

        if (this.categorieFiglie != null)
            sb.append(figlieToString());

        return sb.toString();
    }

    public String figlieToString() {
        StringBuilder sb = new StringBuilder();
        for (Categoria f : this.categorieFiglie) {
            sb.append("\t﹂").append(f.toString()).append("\n");
        }
        return sb.toString();
    }


}
