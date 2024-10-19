package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "CategoriaFoglia")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("CategoriaFoglia")
public class CategoriaFoglia extends Categoria {

    @JacksonXmlProperty(localName = "nomeMadre")
    private String nomeMadre;

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
        this.valoreDominio = new ValoreDominio(NomeValoreDominio);
        this.nomeMadre = madre.getNome();
        this.campo = madre.getCampoFiglie();
    }

    public CategoriaFoglia() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tCategoria: ").append(this.getNome()).append("\n");
        sb.append("\t\tMadre: ").append(this.nomeMadre).append("\n");
        sb.append("\t\t").append(this.campo).append(" = ").append(this.valoreDominio).append("\n");
        return sb.toString();
    }

    @Override
    public Categoria cercaCategoria(String nomeCat) {
        return this.getNome().equals(nomeCat) ? this : null;
    }

}
