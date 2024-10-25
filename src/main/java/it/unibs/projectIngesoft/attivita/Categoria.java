package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;


/*@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CategoriaNonFoglia.class, name = "CategoriaNonFoglia"),
        @JsonSubTypes.Type(value = CategoriaFoglia.class, name = "CategoriaFoglia")
})*/
@JacksonXmlRootElement(localName = "Categoria")
public class Categoria {

    @JacksonXmlProperty(localName = "nome")
    protected String nome;

    @JacksonXmlProperty(localName = "valoreDominio")
    protected ValoreDominio valoreDominio;

    @JacksonXmlProperty(localName = "isRadice")
    private boolean isRadice;
    @JacksonXmlProperty(localName = "isFoglia")
    private boolean isFoglia;

    @JacksonXmlProperty(localName = "nomeMadre")
    private String nomeMadre;

    // campo che QUESTA categoria eredita dalla madre
    @JacksonXmlProperty(localName = "campo")
    private String campo;

    // campo definito come dominio a cui appartengono le figlie di QUESTA categoria
    @JacksonXmlProperty(localName = "campoFiglie")
    private String campoFiglie;

    @JacksonXmlElementWrapper(localName = "categorieFiglie")
    @JacksonXmlProperty(localName = "Categoria")
    private ArrayList<Categoria> categorieFiglie;

    protected Categoria() {

    }

    /**
     * Costruttore per una Categoria RADICE e FOGLIA.
     * Non ha figlie, non ha madre.
     * @param nome, della Categoria
     */
    public Categoria(String nome) {
        this.nome = nome;

        // foglia
        this.isFoglia = true;
        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = "";

        //radice
        this.isRadice = true;
        this.nomeMadre = "";
        this.campo = "";
        this.valoreDominio = null;
    }

    /**
     * Costruttore della Categoria RADICE, NON FOGLIA
     * @param nome, della Categoria
     * @param campoFiglie, dominio impresso alle Categorie figlie
     */
    public Categoria(String nome, String campoFiglie) {
        this.nome = nome;

        // foglia
        this.isFoglia = false;
        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = campoFiglie;

        //radice
        this.isRadice = true;
        this.nomeMadre = null;
        this.campo = null;
        this.valoreDominio = null;
    }

    /**
     * Costruttore per Categoria NON RADICE e NON FOGLIA
     * @param nome, della Categoria
     * @param campoFiglie, nome del dominio che imprime alle figlie
     * @param madre, della Categoria
     * @param nomeValoreDominio, del dominio ereditato
     */
    public Categoria(String nome, String campoFiglie, Categoria madre, String nomeValoreDominio) {
        this.nome = nome;

        // foglia
        this.isFoglia = false;
        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = campoFiglie;

        //radice
        this.isRadice = false;
        this.nomeMadre = madre.getNome();
        this.campo = madre.getCampoFiglie();
        this.valoreDominio = new ValoreDominio(nomeValoreDominio);
    }

    /**
     * Costruttore per Categoria NON RADICE e FOGLIA.
     * Ha una madre ma non delle figlie.
     * @param nome, della Categoria
     * @param madre, della Categoria
     * @param nomeValoreDominio, del dominio ereditato
     */
    public Categoria(String nome, Categoria madre, String nomeValoreDominio) {
        this.nome = nome;

        // foglia
        this.isFoglia = true;
        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = "";

        //radice
        this.isRadice = false;
        this.nomeMadre = madre.getNome();
        this.campo = madre.getCampoFiglie();
        this.valoreDominio = new ValoreDominio(nomeValoreDominio);

    }

    /*
     *  GETTER
     */

    public String getNome() {
        return nome;
    }

    public ValoreDominio getValoreDominio() {
        return this.valoreDominio;
    }

    public String getCampo() {
        return campo;
    }

    public String getCampoFiglie() {
        return campoFiglie;
    }

   public ArrayList<Categoria> getCategorieFiglie() {
        return categorieFiglie;
    }

    // nelle addCategoriaFiglia non si fa il check se l'arraylist è inizializzato perchè i costruttori
    // lo inizializzano a vuoto in qualsiasi caso
    // ma sto coso muore con xml o che palle
    public void addCategoriaFiglia(Categoria categoria) {
        if (this.categorieFiglie == null)
            this.categorieFiglie = new ArrayList<>();
        this.categorieFiglie.add(categoria);
    }

    @JsonIgnore
    public int getNumCategorieFiglie() {
        return categorieFiglie.size();
    }

    @JsonIgnore
    public boolean isRadice() {
        return isRadice;
    }

    @JsonIgnore
    public boolean isFoglia() {
        return isFoglia;
    }


    /*
     * METODI PAZZI
     */

    //public abstract Categoria cercaCategoria(String nomeCat);

    /**
     * Cerca una categoria partendo dal nome.
     * La cerca prima come sè stessa, e poi tra le sue figlie.
     *
     * @param nomeCat, nome della categoria da cercare
     * @return null se non la trova, oggetto Categoria se trova corrispondenza
     */
    public Categoria cercaCategoria(String nomeCat) {
        Categoria found = null;
        // prima controlla se stessa
        if (this.getNome().equals(nomeCat))
            return this;
        else if (!this.isFoglia) {
            // altrimenti controlla nelle figlie, richiamando per ognuna di loro la funzione di ricerca
            // appena trova qualcosa (cioè found non null) esce e ritorna
            for (Categoria figlia : this.categorieFiglie) {
                found = figlia.cercaCategoria(nomeCat);
                if (found != null)
                    break;
            }
        }
        return found;
    }

        /*public Categoria cercaCategoria(String nomeCat) {
        return this.getNome().equals(nomeCat) ? this : null;
    }*/


    public Categoria cercaValoreDominio(String valoreDominio) {
        if (!this.isFoglia)
            return this.valoreDominio.getNome().equals(valoreDominio) ? this : null;
        else return null;
    }

    //public abstract Categoria cercaValoreDominio(String valoreDominio);


    // CAT NF
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.simpleToString());


        // se esistono delle figlie allora le si stampano
        if (this.categorieFiglie != null && !this.categorieFiglie.isEmpty())
            sb.append(figlieToString());

        return sb.toString();
    }

    /**
     * Genera una stringa di descrizione della categoria limitata, senza eventuali figlie.
     *
     * @return stringa formattata
     */
    public String simpleToString() {

        StringBuilder sb = new StringBuilder();
        sb.append("[ ").append(this.getNome()).append(" ]\n");

        if (!this.isRadice()) {
            sb.append("Madre: ").append(this.nomeMadre).append("\n");
            sb.append("Dominio: ").append(this.getCampo()).append(" = ").append(this.valoreDominio).append("\n");

        }
        if(!this.isFoglia()){
            sb.append("Dominio Figlie: ").append(this.getCampoFiglie()).append("\n");
        }

        // se non è una radice, allora si stampano i dati della categoria madre
        return sb.toString();
    }


    public String figlieToString() {
        StringBuilder sb = new StringBuilder();

        if (this.getNumCategorieFiglie() > 0) {
            for (int i = 0; i < this.getNumCategorieFiglie(); i++) {
                sb.append("\n").append(this.categorieFiglie.get(i));
                // necessario discriminare per capire quale toString richiamare.
                /*if (this.categorieFiglie.get(i) instanceof CategoriaNonFoglia tempNF) {

                } else if (this.categorieFiglie.get(i) instanceof CategoriaFoglia tempF) {
                    sb.append("\t﹂").append(tempF);
                } else {
                    // se qualcosa non viene riconosciuto come CatNF o CatF allora c'è un GROSSO problema a monte
                    System.out.println("c'è stato un problemino oops");
                }*/
            }
        } else {
            sb.append("\t﹂ Nessuna figlia.");
        }
        return sb.toString();
    }




    /**
     * Stampa il nome del dominio che imprime alle figlie, e tutti i valori che al momento assume + descrizioni.
     *
     * @return stringa formattata
     */
    public String dominioToString() {

        StringBuilder sb = new StringBuilder();
        ArrayList<ValoreDominio> tempLista = new ArrayList<>();

        sb.append("\n\nNome Dominio: ").append(this.getCampoFiglie()).append("\n");

        for (Categoria figlia : this.categorieFiglie)
            tempLista.add(figlia.getValoreDominio());

        if (!tempLista.isEmpty()) {
            sb.append("Valori: ");
            for (ValoreDominio val : tempLista)
                if (val != null)
                    sb.append("\n{ ").append(val).append(" }");
        } else {
            sb.append("Vuoto.");
        }

        return sb.toString();
    }



}
