package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "CategoriaFoglia")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("CategoriaFoglia")
public class CategoriaFoglia extends Categoria {

    //@JsonIgnore
    //private CategoriaNonFoglia madre;
    @JacksonXmlProperty(localName = "nomeMadre")
    private String nomeMadre;
    @JacksonXmlProperty(localName = "NomeValoreDominio")
    private String NomeValoreDominio;
    @JacksonXmlProperty(localName = "campo")
    private String campo;


    @JacksonXmlProperty(localName = "type")
    private final String type = "CategoriaFoglia";

    /*
     * Memorizzare anche la categoria madre è ridondante. Mi segno solo quei due attributi
     * utili e necessari per il toString.
     */
    public CategoriaFoglia(String nome, CategoriaNonFoglia madre, String NomeValoreDominio) {
        super(nome);
        //this.madre = madre;
        this.NomeValoreDominio = NomeValoreDominio;
        this.nomeMadre = madre.getNome();
        this.campo = madre.getCampoFiglie();
    }

    public CategoriaFoglia() {
        super();
    }

    public void setNomeValoreDominio(String NomeValoreDominio) {
        this.NomeValoreDominio = NomeValoreDominio;
    }

    public String getNomeValoreDominio() {
        return this.NomeValoreDominio;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tCategoria: ").append(this.getNome()).append("\n");
        sb.append("\t\tMadre: ").append(this.nomeMadre).append("\n");
        sb.append("\t\t").append(this.campo).append(" = ").append(this.getNomeValoreDominio()).append("\n");

        return sb.toString();
    }

}
