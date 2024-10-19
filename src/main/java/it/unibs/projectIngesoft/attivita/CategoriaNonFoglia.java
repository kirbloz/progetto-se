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

    @JacksonXmlProperty(localName = "nomeMadre")
    private String nomeMadre;
    // campo che QUESTA categoria eredita dalla madre
    @JacksonXmlProperty(localName = "campo")
    private String campo;
    // campo definito come dominio a cui appartengono le figlie di QUESTA categoria
    @JacksonXmlProperty(localName = "campoFiglie")
    private String campoFiglie;

    @JacksonXmlProperty(localName = "valoreDominio")
    private ValoreDominio valoreDominio;

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
    public CategoriaNonFoglia(String nome, String campoFiglie/*, ArrayList<ValoreDominio> dominio*/) {
        super(nome);

        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = campoFiglie;

        //radice
        this.isRadice = true;
        this.nomeMadre = null;
        this.campo = null;

        this.valoreDominio = null;
    }


    public CategoriaNonFoglia(String nome, String campoFiglie, CategoriaNonFoglia madre, String nomeValoreDominio) {
        super(nome);

        this.categorieFiglie = new ArrayList<>();
        this.campoFiglie = campoFiglie;

        //radice
        this.isRadice = false;
        this.nomeMadre = madre.getNome();
        this.campo = madre.getCampoFiglie();

        this.valoreDominio = new ValoreDominio(nomeValoreDominio);
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

    public ArrayList<Categoria> getCategorieFiglie() {
        return categorieFiglie;
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
            // appena trova qualcosa (cioè found non null) esce e ritorna
            for (Categoria figlia : this.categorieFiglie) {
                found = figlia.cercaCategoria(nomeCat);
                if (found != null)
                    break;
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
                    sb.append("\n﹂").append(tempNF);
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
            sb.append("Dominio: ").append(this.getCampo()).append(" = ").append(this.valoreDominio).append("\n");
        }
        sb.append("Dominio Figlie: ").append(this.getCampoFiglie()).append("\n");
        // se non è una radice, allora si stampano i dati della categoria madre
        return sb.toString();
    }

    /**
     * Stampa il nome del dominio che imprime alle figlie, e tutti i valori che al momento assume + descrizioni.
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
                sb.append("\n{ ").append(val.toString()).append(" }");
        } else {
            sb.append("Vuoto.");
        }

        return sb.toString();
    }

}
