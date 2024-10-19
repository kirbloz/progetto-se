package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CategoriaNonFoglia.class, name = "CategoriaNonFoglia"),
        @JsonSubTypes.Type(value = CategoriaFoglia.class, name = "CategoriaFoglia")
})
@JacksonXmlRootElement(localName = "Categoria")
public abstract class Categoria {

    @JacksonXmlProperty(localName = "nome")
    private String nome;

    protected Categoria() {

    }

    protected Categoria(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public abstract Categoria cercaCategoria(String nomeCat);



}
