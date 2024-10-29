package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

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
     *
     * @param nome, della Categoria
     */
    public Categoria(String nome) {
        this.nome = nome;
        this.setFoglia();
        this.setRadice();
    }

    /**
     * Costruttore della Categoria RADICE, NON FOGLIA
     *
     * @param nome,        della Categoria
     * @param campoFiglie, dominio impresso alle Categorie figlie
     */
    public Categoria(String nome, String campoFiglie) {
        this.nome = nome;
        this.setNotFoglia(campoFiglie);
        this.setRadice();
    }

    /**
     * Costruttore per Categoria NON RADICE e NON FOGLIA
     *
     * @param nome,          della Categoria
     * @param campoFiglie,   nome del dominio che imprime alle figlie
     * @param madre,         della Categoria
     * @param valoreDominio, del dominio ereditato
     */
    public Categoria(String nome, String campoFiglie, Categoria madre, ValoreDominio valoreDominio) {
        this.nome = nome;
        this.setNotFoglia(campoFiglie);
        this.setNotRadice(madre, valoreDominio);
    }

    /**
     * Costruttore per Categoria NON RADICE e FOGLIA.
     * Ha una madre ma non delle figlie.
     *
     * @param nome,          della Categoria
     * @param madre,         della Categoria
     * @param valoreDominio, del dominio ereditato
     */
    public Categoria(String nome, Categoria madre, ValoreDominio valoreDominio) {
        this.nome = nome;
        this.setFoglia();
        this.setNotRadice(madre, valoreDominio);
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

    public void setFoglia() {
        this.isFoglia = true;
        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = "";
    }

    public void setNotFoglia(String campoFiglie) {
        assert campoFiglie != null : "campoFiglie can't be null";

        // foglia
        this.isFoglia = false;
        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = campoFiglie;
    }

    public void setRadice() {
        this.isRadice = true;
        this.nomeMadre = "";
        this.campo = "";
        this.valoreDominio = null;
    }

    public void setNotRadice(Categoria madre, ValoreDominio valoreDominio) {
        assert madre != null : "madre can't be null";
        assert valoreDominio != null : "valoreDominio can't be null";

        this.isRadice = false;
        this.nomeMadre = madre.getNome();
        this.campo = madre.getCampoFiglie();
        this.valoreDominio = valoreDominio;
    }

    public List<String> getValoriDominioFiglie() {
        List<String> temp = new ArrayList<>();
        for (Categoria figlia : categorieFiglie)
            temp.add(figlia.getValoreDominio().getNome());
        return temp;
    }

    public ArrayList<Categoria> getCategorieFiglie() {
        return categorieFiglie;
    }

    // nelle addCategoriaFiglia non si fa il check se l'arraylist è inizializzato perchè i costruttori
    // lo inizializzano a vuoto in qualsiasi caso
    // ma sto coso muore con xml o che palle
    public void addCategoriaFiglia(Categoria categoria) {
        assert categoria != null : "categoria can't be null";

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

    /**
     * Cerca una categoria partendo dal nome.
     * La cerca prima come sè stessa, e poi tra le sue figlie.
     *
     * @param nomeCat, nome della categoria da cercare
     * @return null se non la trova, oggetto Categoria se trova corrispondenza
     */
    public Categoria cercaCategoria(String nomeCat) {
        assert nomeCat != null : "nomeCat can't be null";

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

    public Categoria cercaValoreDominio(String valoreDominio) {
        assert valoreDominio != null : "valoreDominio can't be null"; // precondizione

        if (!this.isFoglia)
            return this.valoreDominio.getNome().equals(valoreDominio) ? this : null;
        else return null;
    }

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
        if (!this.isFoglia()) {
            sb.append("Dominio Figlie: ").append(this.getCampoFiglie()).append("\n");
        }
        // se non è una radice, allora si stampano i dati della categoria madre
        return sb.toString();
    }

    /**
     * Stampa le figlie della Categoria.
     *
     * @return stringa formattata
     */
    public String figlieToString() {
        StringBuilder sb = new StringBuilder();
        if (getNumCategorieFiglie() > 0) {
            for (int i = 0; i < getNumCategorieFiglie(); i++) {
                sb.append("\n").append(categorieFiglie.get(i));
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

        for (Categoria figlia : categorieFiglie)
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

    @JsonIgnore
    protected List<Categoria> getFoglie() {
        List<Categoria> foglie = new ArrayList<>();

        if (this.isFoglia())
            foglie.add(this);
        else
            for (Categoria figlia : this.categorieFiglie)
                foglie.addAll(figlia.getFoglie());

        return foglie;
    }


}
