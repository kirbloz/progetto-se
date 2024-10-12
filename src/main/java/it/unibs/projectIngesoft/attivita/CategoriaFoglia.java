package it.unibs.projectIngesoft.attivita;

public class CategoriaFoglia extends Categoria {

    private CategoriaNonFoglia madre;
    private String NomeValoreDominio;

    public CategoriaFoglia(String nome, CategoriaNonFoglia madre, String NomeValoreDominio) {
        super(nome);
        this.madre = madre;
        this.NomeValoreDominio = NomeValoreDominio;
    }

    public void setNomeValoreDominio(String NomeValoreDominio) {
        this.NomeValoreDominio = NomeValoreDominio;
    }

    public String getNomeValoreDominio() {
        return this.NomeValoreDominio;
    }

    public Categoria getMadre() {
        return this.madre;
    }

    public void setMadre(CategoriaNonFoglia madre) {
        this.madre = madre;
    }

}
