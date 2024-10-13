package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.*;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("CategoriaNonFoglia")
@JacksonXmlRootElement(localName = "CategoriaNonFoglia")
public class CategoriaNonFoglia extends Categoria {

    @JacksonXmlProperty(localName = "isRadice")
    private boolean isRadice;

    @JsonIgnore
    private CategoriaNonFoglia madre;
    @JacksonXmlProperty(localName = "nomeMadre")
    private String nomeMadre;
    @JacksonXmlProperty(localName = "campo")
    private String campo;
    @JacksonXmlProperty(localName = "ValoreDominio")
    @JacksonXmlElementWrapper(localName = "listaValoriDominio")
    private ArrayList<ValoreDominio> listaValoriDominio;

    @JacksonXmlElementWrapper(localName = "categorieFiglie")
    @JacksonXmlProperty(localName = "Categoria")
    private ArrayList<Categoria> categorieFiglie;

    @JacksonXmlProperty(localName = "type") // questo attributo esiste solo per un capriccio di jackson
    private final String type = "CategoriaNonFoglia";

    // questo costruttore esiste solo per un capriccio di jackson
    public CategoriaNonFoglia() {
        super();
    }

    // costruttore per categoria radice
    public CategoriaNonFoglia(String nome, String campo, ArrayList<ValoreDominio> dominio) {
        super(nome);
        this.campo = campo;
        this.listaValoriDominio = dominio;
        this.isRadice = true;
        this.categorieFiglie = new ArrayList<>();
        this.madre = null;
        this.nomeMadre = null;
    }

    public CategoriaNonFoglia(String nome, String campo) {
        super(nome);
        this.campo = campo;
        this.listaValoriDominio = new ArrayList<>();
        this.isRadice = true;
        this.categorieFiglie = new ArrayList<>();
        this.madre = null;
        this.nomeMadre = null;
    }

    // costruttore per categoria non radice
    public CategoriaNonFoglia(String nome, String campo, ArrayList<ValoreDominio> dominio, CategoriaNonFoglia madre) {
        super(nome);
        this.campo = campo;
        this.listaValoriDominio = dominio;
        this.madre = madre;
        this.isRadice = false;
        this.categorieFiglie = new ArrayList<>();
        this.nomeMadre = madre.getNome();
    }

    public CategoriaNonFoglia(String nome, String campo, CategoriaNonFoglia madre) {
        super(nome);
        this.campo = campo;
        this.listaValoriDominio = new ArrayList<>();
        this.madre = madre;
        this.isRadice = false;
        this.categorieFiglie = new ArrayList<>();
        this.nomeMadre = madre.getNome();
    }


    public ArrayList<ValoreDominio> getListaValoriDominio() {
        return listaValoriDominio;
    }

    public String getCampo() {
        return campo;
    }

    public CategoriaNonFoglia getMadre() {
        return madre;
    }

    public void setListaValoriDominio(ArrayList<ValoreDominio> listaValoriDominio) {
        this.listaValoriDominio = listaValoriDominio;
    }

    public boolean addDominio(ValoreDominio dominio) {
        return this.listaValoriDominio.add(dominio);
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

    @JsonIgnore
    public int getNumCategorieFiglie() {
        return categorieFiglie.size();
    }

    @JsonIgnore
    public boolean isRadice() {
        return isRadice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.simpleToString());
        // se esistono delle figlie allora le si stampano
        if (this.categorieFiglie != null)
            sb.append(figlieToString());

        return sb.toString();
    }

    public String figlieToString() {
        StringBuilder sb = new StringBuilder();

        if (this.getNumCategorieFiglie() > 0) {
            for (int i = 0; i < this.getNumCategorieFiglie(); i++) {
                // necessario discriminare per capire quale toString richiamare.
                if (this.categorieFiglie.get(i) instanceof CategoriaNonFoglia tempNF) {
                    sb.append("\t﹂").append(tempNF);
                } else if (this.categorieFiglie.get(i) instanceof CategoriaFoglia tempF) {
                    sb.append("\t﹂").append(tempF);
                } else {
                    // se qualcosa non viene riconosciuto come CatNF o CatF allora c'è un GROSSO problema a monte
                    System.out.println("c'è stato un problemino oops");
                }
            }
        } else {
            sb.append("\t﹂ Nessuna figlia.");
        }
        return sb.toString();
    }

    /**
     * Genera una stringa di descrizione della categoria limitata, senza eventuali figlie.
     * @return stringa formattata
     */
    public String simpleToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Categoria: ").append(this.getNome()).append("\n");
        sb.append("Dominio: ").append(this.getCampo()).append("\n");
        // se non è una radice, allora si stampano i dati della categoria madre
        if (!this.isRadice())
            sb.append("Madre: ").append(this.getMadre() != null ? this.getMadre().getNome() : "null").append("\n");
        return sb.toString();
    }

}
