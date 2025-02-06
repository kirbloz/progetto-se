package it.unibs.projectIngesoft.model;


import it.unibs.projectIngesoft.attivita.Albero;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.mappers.CategorieMapper;

import java.util.ArrayList;
import java.util.List;

public class CategorieModel  {
    private Albero tree;
    private CategorieMapper mapper;


    /**
     * Costruttore per inizializzare i percorsi dei file e de-serializzare l'albero.
     *
     */
    public CategorieModel(CategorieMapper mapper) {
        this.tree = new Albero();
        this.mapper = mapper;
        load();
    }

    public void save(){
        mapper.write(tree.getRadici());
    }

    public void load(){
        this.tree.setRadici(mapper.read());
    }

    public List<Categoria> getRadici(){
        return tree.getRadici();
    }

    public Categoria getRadice(String nomeRadice){
        return tree.getRadice(nomeRadice);
    }

    public void setRadici(List<Categoria> radici) {
        if(radici == null)
            radici = new ArrayList<>();
        tree.setRadici(radici);
        this.save();
    }

    public List<Categoria> getFoglie(String nomeRadice){
        return tree.getFoglie(nomeRadice);
    }


    /**
     * Verifica che la stringa passata non sia già il nome di un'altra Categoria della stessa gerarchia
     *
     * @param tempNome    nome da controllare
     * @param tempRadice, radice della gerarchia
     * @return true se esiste una Categoria con quel nome
     */
    public boolean esisteCategoriaNellaGerarchia(String tempNome, String tempRadice) {
        return this.tree.getRadice(tempRadice).cercaCategoria(tempNome) != null;
    }

    public void aggiungiCategoriaRadice(String nomeCategoria, String nomeCampo) {
      this.tree.aggiungiRadice(new Categoria(nomeCategoria, nomeCampo));

    }


    public boolean esisteRadice(String tempNome) {
        return this.tree.contains(tempNome);
    }

}