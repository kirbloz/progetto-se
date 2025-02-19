package it.unibs.projectIngesoft.core.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe Categoria rappresenta una categoria che può essere radice, foglia o una categoria intermedia
 * all'interno di un albero di categorie. Ogni categoria può avere un valore di dominio ereditato dalla madre,
 * e può definire un campo per le categorie figlie.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Categoria {

    @JsonProperty("nome")
    protected String nome;

    @JsonProperty("valoreDominio")
    protected ValoreDominio valoreDominio;

    @JsonProperty("isRadice")
    private boolean isRadice;

    @JsonProperty("isFoglia")
    private boolean isFoglia;

    @JsonProperty("nomeMadre")
    private String nomeMadre;

    // campo che QUESTA categoria eredita dalla madre
    @JsonProperty("campo")
    private String campo;

    // campo definito come dominio a cui appartengono le figlie di QUESTA categoria
    @JsonProperty("campoFiglie")
    private String campoFiglie;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ArrayList<Categoria> categorieFiglie;

    /**
     * Costruttore protetto default necessario per la deserializzazione
     * tramite Jackson
     */
    protected Categoria() {

    }

    /**
     * Costruttore per una Categoria RADICE e FOGLIA.
     * Non ha figlie, non ha madre.
     *
     * @param nome, nome della Categoria
     */
    public Categoria(String nome) {
        this.nome = nome;
        this.setFoglia();
        this.setRadice();
    }

    /**
     * Costruttore della Categoria RADICE, NON FOGLIA
     *
     * @param nome,        nome della Categoria
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
     * @param nome,          nome della Categoria
     * @param campoFiglie,   nome del dominio che imprime alle figlie
     * @param madre,         madre della Categoria
     * @param valoreDominio, valore del dominio ereditato
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
     * @param nome,          nome della Categoria
     * @param madre,         madre della Categoria
     * @param valoreDominio, valore del dominio ereditato
     */
    public Categoria(String nome, Categoria madre, ValoreDominio valoreDominio) {
        this.nome = nome;
        this.setFoglia();
        this.setNotRadice(madre, valoreDominio);
    }

    public String getNome() {
        return nome;
    }

    public String getNomeMadre() {
        return nomeMadre;
    }

    public ValoreDominio getValoreDominio() {
        return this.valoreDominio;
    }

    public String getNomeValoreDominio() {
        if(this.valoreDominio == null)
            return "";
        return this.valoreDominio.getNome();
    }

    public String getDescrizioneValoreDominio(){
        if(this.valoreDominio == null)
            return "";
        return this.valoreDominio.getDescrizione(); }

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
        assert campoFiglie != null
                && !campoFiglie.trim().isEmpty() : "Il campo delle figlie non deve essere null o vuoto";

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

    @JsonIgnore
    public boolean isRadice() {
        return isRadice;
    }
    @JsonIgnore
    public boolean isFoglia() {
        return isFoglia;
    }
    @JsonIgnore
    public boolean isNotFoglia() {
        return !isFoglia;
    }
    @JsonIgnore
    public boolean hasFiglie() {
        return isFoglia;
    }

    public List<String> getValoriDominioFiglie() {
        List<String> temp = new ArrayList<>();
        if (categorieFiglie == null)
            return temp; //empty arraylist
        for (Categoria figlia : categorieFiglie)
            temp.add(figlia.getValoreDominio().getNome());
        return temp;
    }

    public List<Categoria> getCategorieFiglie() {
        return (categorieFiglie == null) ? new ArrayList<>() : categorieFiglie;
    }

    public void aggiungiCategoriaFiglia(Categoria categoria) {
        if (this.categorieFiglie == null) this.categorieFiglie = new ArrayList<>();
        if (this.isFoglia()) this.isFoglia = false;
        this.categorieFiglie.add(categoria);
    }

    @JsonIgnore
    public int getNumCategorieFiglie() {
        if(categorieFiglie == null)
            return 0;
        return categorieFiglie.size();
    }

    /**
     * Restituisce la lista delle foglie
     *
     * @return lista delle foglie
     */
    @JsonIgnore
    public List<Categoria> getFoglie() {
        List<Categoria> foglie = new ArrayList<>();

        if (this.isFoglia())
            foglie.add(this);
        else
            for (Categoria figlia : this.categorieFiglie)
                foglie.addAll(figlia.getFoglie());

        return foglie;
    }


    /**
     * Cerca una categoria tra quelle memorizzate partendo dal nome.
     * La cerca prima come sè stessa, e poi tra le sue figlie.
     *
     * @param nomeCat, nome della categoria da cercare
     * @return null se non la trova, oggetto Categoria se trova corrispondenza
     */
    public Categoria cercaCategoria(String nomeCat) {
        assert nomeCat != null : "nomeCat da cercare non può essere null";

        Categoria found = null;
        // prima controlla se stessa
        if (this.getNome().equals(nomeCat))
            return this;
        else if (!this.isFoglia) { // se fosse foglia, allora non ha figlie da controllare
            // altrimenti controlla nelle figlie, richiamando per ognuna di loro la funzione di ricerca
            for (Categoria figlia : this.categorieFiglie) {
                found = figlia.cercaCategoria(nomeCat);
                if (found != null)
                    break;
            }
        }
        return found;
    }


    public static List<Categoria> appiatisciGerarchiaSuLista(Categoria categoria, List<Categoria> lista) {
        if (categoria == null)
            return lista;

        lista.add(categoria);
        if (categoria.isFoglia())
            return lista;

        List<Categoria> figlie = categoria.getCategorieFiglie();
        for (Categoria figlia : figlie) {
            if (!figlia.isFoglia() && figlia.getCategorieFiglie() != null) {
                appiatisciGerarchiaSuLista(figlia, lista);
            }
        }

        return lista;
    }

    /**
     * Partendo dalla radice di una gerarchia, chiama se stessa ricorsivamente per controllare che tutte le categorie
     * che non hanno figlie siano impostate come foglie.
     *
     */
    public void impostaCategorieFoglia() {
        if (this.getNumCategorieFiglie() == 0 && !this.isFoglia()) {
            this.setFoglia();
            return;
        }

        for (Categoria figlia : this.getCategorieFiglie()) {
            if (figlia.getNumCategorieFiglie() == 0 && !figlia.isFoglia())
                figlia.setFoglia();
            else
                figlia.impostaCategorieFoglia();
        }
    }
}
