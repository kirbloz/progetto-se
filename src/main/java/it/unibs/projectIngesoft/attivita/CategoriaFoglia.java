package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonRootName("")
public class CategoriaFoglia extends Categoria {

    @JacksonXmlProperty(localName = "madre")
    private CategoriaNonFoglia madre;
    @JacksonXmlProperty(localName = "NomeValoreDominio")
    private String NomeValoreDominio;

    public CategoriaFoglia(String nome, CategoriaNonFoglia madre, String NomeValoreDominio) {
        super(nome);
        this.madre = madre;
        this.NomeValoreDominio = NomeValoreDominio;
    }

    public CategoriaFoglia() {super();}

    public void setNomeValoreDominio(String NomeValoreDominio) {
        this.NomeValoreDominio = NomeValoreDominio;
    }

    public String getNomeValoreDominio() {
        return this.NomeValoreDominio;
    }

    public CategoriaNonFoglia getMadre() {
        return this.madre;
    }

    public void setMadre(CategoriaNonFoglia madre) {
        this.madre = madre;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\tCategoria: ").append(this.getNome()).append("\n");
        sb.append("\tDominio: ").append(this.getMadre().getCampo()).append("\n");
        sb.append("\tValore Dominio: ").append(this.getNomeValoreDominio()).append("\n");
        sb.append("\tMadre: ").append(this.getMadre().getNome()).append("\n");

        return sb.toString();
    }

}
