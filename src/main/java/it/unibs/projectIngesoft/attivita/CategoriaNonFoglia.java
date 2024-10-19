package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("CategoriaNonFoglia")
@JacksonXmlRootElement(localName = "CategoriaNonFoglia")
public class CategoriaNonFoglia extends Categoria {

    @JacksonXmlProperty(localName = "isRadice")
    private boolean isRadice;

    //@JsonIgnore
    //private CategoriaNonFoglia madre;
    @JacksonXmlProperty(localName = "nomeMadre")
    private String nomeMadre;
    // campo che QUESTA categoria eredita dalla madre
    @JacksonXmlProperty(localName = "campo")
    private String campo;
    // campo definito come dominio a cui appartengono le figlie di QUESTA categoria
    @JacksonXmlProperty(localName = "campoFiglie")
    private String campoFiglie;
    // valore del dominio, potrebbe essere un ValoreDominio eh
    //TODO
    @JacksonXmlProperty(localName = "NomeValoreDominio")
    private String NomeValoreDominio;


    /*
     * si rimuove questo perchè se già la classe contiene categorieFiglie,
     * allora una lista di valoredominio si può ricavare ogni qualvolta sia necessario
     * E INVECE NO. E' NECESSARIO PER AVERE UN PUNTO "CENTRALIZZATO" DOVE SI MEMORIZZANO
     * I VALORI DEL DOMINIO E PER POTER MODIFICARE/AGGIUNGERE LE DESCRIZIONI FACILMENTE
     */
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

    // costruttore per categoria RADICE
    public CategoriaNonFoglia(String nome, String campoFiglie, ArrayList<ValoreDominio> dominio) {
        super(nome);

        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = campoFiglie;
        this.listaValoriDominio = dominio;

        //this.madre = null;
        //radice
        this.isRadice = true;
        this.nomeMadre = null;
        this.campo = null;
        this.NomeValoreDominio = null;
    }

    public CategoriaNonFoglia(String nome, String campoFiglie) {
        super(nome);

        //this.madre = null;
        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = campoFiglie;
        this.listaValoriDominio = new ArrayList<>();

        //this.madre = null;
        //radice
        this.isRadice = true;
        this.nomeMadre = null;
        this.campo = null;
        this.NomeValoreDominio = null;
    }

    // costruttore per categoria NON RADICE
    public CategoriaNonFoglia(String nome, String campoFiglie, ArrayList<ValoreDominio> dominio, CategoriaNonFoglia madre, String nomeValoreDominio) {
        super(nome);

        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = campoFiglie;
        this.listaValoriDominio = dominio;

        //this.madre = madre;
        //radice
        this.isRadice = false;
        this.nomeMadre = madre.getNome();
        this.campo = madre.getCampoFiglie();
        this.NomeValoreDominio = nomeValoreDominio;
    }

    public CategoriaNonFoglia(String nome, String campoFiglie, CategoriaNonFoglia madre, String nomeValoreDominio) {
        super(nome);

        //this.madre = madre;
        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = campoFiglie;
        this.listaValoriDominio = new ArrayList<>();

        //this.madre = madre
        //radice
        this.isRadice = false;
        this.nomeMadre = madre.getNome();
        this.campo = madre.getCampoFiglie();
        this.NomeValoreDominio = nomeValoreDominio;
    }

    public ArrayList<ValoreDominio> getListaValoriDominio() {
        return listaValoriDominio;
    }

    public String getCampo() {
        return campo;
    }

    public String getCampoFiglie() {
        return campoFiglie;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public void setCampoFiglie(String campoFiglie) {
        this.campoFiglie = campoFiglie;
    }

    public void setListaValoriDominio(ArrayList<ValoreDominio> listaValoriDominio) {
        this.listaValoriDominio = listaValoriDominio;
    }

    public boolean addValoreDominio(ValoreDominio valoreDominio) {
        return this.listaValoriDominio.add(valoreDominio);
    }

    public String getNomeValoreDominio() {
        return NomeValoreDominio;
    }

    public void setNomeValoreDominio(String nomeValoreDominio) {
        this.NomeValoreDominio = nomeValoreDominio;
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
        this.listaValoriDominio.add(new ValoreDominio((categoria.getNomeValoreDominio())));
    }

    public void addCategoriaFiglia(CategoriaNonFoglia categoria) {
        if (this.categorieFiglie == null)
            this.categorieFiglie = new ArrayList<>();
        this.categorieFiglie.add(categoria);
        this.listaValoriDominio.add(new ValoreDominio((categoria.getNomeValoreDominio())));
    }

    public void removeCategoriaFiglia(CategoriaFoglia categoria) {
        this.categorieFiglie.remove(categoria);
        // TODO implementare la rimozione del valore che ha quel nome come string nomeValore in categoria
        /*ValoreDominio tempValore =
        this.listaValoriDominio.remove();*/
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
    public Categoria cercaCategoria(String nomeCat) {
        Categoria found = null;

        // prima controlla se stessa
        if (this.getNome().equals(nomeCat))
            return this;
        else {
            // altrimenti controlla nelle figlie, richiamando per ognuna di loro la funzione di ricerca
            for (int i = 0; i < this.getNumCategorieFiglie(); i++) {
                // controllo la classe dell'oggetto e richiamo il metodo giusto
                if (this.categorieFiglie.get(i) instanceof CategoriaNonFoglia tempNF) {
                    found = tempNF.cercaCategoria(nomeCat);
                } else if (this.categorieFiglie.get(i) instanceof CategoriaFoglia tempF) {
                    found = tempF.cercaCategoria(nomeCat);
                }
            }
        }

        return found;
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
     *
     * @return stringa formattata
     */
    public String simpleToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Categoria: ").append(this.getNome()).append("\n");

        if (!this.isRadice()) {
            sb.append("Madre: ").append(this.nomeMadre).append("\n");
            // dominio e valore dominio che eredita dalla madre (this non è radice)
            sb.append("Dominio: ").append(this.getCampo()).append(" = ").append(this.getNomeValoreDominio()).append("\n");
        }
        sb.append("Dominio Figlie: ").append(this.getCampoFiglie()).append("\n");
        // se non è una radice, allora si stampano i dati della categoria madre
        return sb.toString();
    }

}
