package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonRootName("")
@JsonDeserialize(as = CategoriaNonFoglia.class)
public abstract class Categoria {
    // se questa classe diventa astratta, jackson impazzisce

    @JacksonXmlProperty(localName = "nome")
    private String nome;

    protected Categoria(){

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

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getNome());
        return sb.toString();
    }
}
