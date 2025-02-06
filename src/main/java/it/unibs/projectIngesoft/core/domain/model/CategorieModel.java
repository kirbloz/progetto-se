package it.unibs.projectIngesoft.core.domain.model;


import it.unibs.projectIngesoft.core.domain.entities.Albero;
import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.persistence.implementations.CategorieRepository;
import it.unibs.projectIngesoft.persistence.Repository;

import java.util.ArrayList;
import java.util.List;

public class CategorieModel  {
    private Albero tree;
    private final Repository<List<Categoria>> repository;


    /**
     * Costruttore per inizializzare i percorsi dei file e de-serializzare l'albero.
     *
     */
    public CategorieModel(CategorieRepository repository) {
        this.tree = new Albero();
        this.repository = repository;
        load();
    }

    public void save(){
        repository.save(tree.getRadici());
    }

    public void load(){
        this.tree.setRadici(new ArrayList<>(repository.load()));
    }

    public List<Categoria> getRadici(){
        return new ArrayList<>(tree.getRadici());
    }

    public Categoria getRadice(String nomeRadice){
        return tree.getRadice(nomeRadice);
    }

    public void setRadici(List<Categoria> radici) {
        if(radici == null)
            radici = new ArrayList<>();
        tree.setRadici(radici);
        save();
    }

    public List<Categoria> getFoglie(String nomeRadice){
        return new ArrayList<>(tree.getFoglie(nomeRadice));
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