package it.unibs.projectIngesoft.core.domain.model;


import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.persistence.Repository;
import it.unibs.projectIngesoft.persistence.implementations.CategorieRepository;

import java.util.ArrayList;
import java.util.List;

public class CategorieModel {
    //private Albero tree;
    private List<Categoria> radici;
    private final Repository<List<Categoria>> repository;


    /**
     * Costruttore per inizializzare i percorsi dei file e de-serializzare l'albero.
     */
    public CategorieModel(CategorieRepository repository) {
        //this.tree = new Albero();
        this.radici = new ArrayList<>();
        this.repository = repository;
        load();
    }

    public void save() {
        repository.save(this.radici);
    }

    public void load() {
        this.radici = (new ArrayList<>(repository.load()));
    }

    public List<Categoria> getRadici() {
        return radici;
    }

    public Categoria getRadice(String nomeRadice) {
        if(nomeRadice == null || nomeRadice.isEmpty())
            return null;

        for (Categoria tempRadice : this.radici) {
            if (tempRadice.getNome().equals(nomeRadice))
                return tempRadice;
        }
        return null;

    }

    public void setRadici(List<Categoria> radici) {
        if (radici == null)
            radici = new ArrayList<>();
        this.setRadici(radici);
        save();
    }

    /*public List<Categoria> getFoglie(String nomeRadice) {
        return new ArrayList<>(radici.getFoglie(nomeRadice));
    }*/


    /**
     * Verifica che la stringa passata non sia già il nome di un'altra Categoria della stessa gerarchia
     *
     * @param tempNome    nome da controllare
     * @param tempRadice, radice della gerarchia
     * @return true se esiste una Categoria con quel nome
     */
    public boolean esisteCategoriaNellaGerarchia(String tempNome, String tempRadice) {
        return this.getRadice(tempRadice).cercaCategoria(tempNome) != null;
    }

    public void aggiungiCategoriaRadice(String nomeCategoria, String nomeCampo) {
        this.radici.add(new Categoria(nomeCategoria, nomeCampo));
    }

    public boolean esisteRadice(String nomeRadice) {
        if (radici == null || radici.isEmpty() || nomeRadice.isEmpty()) {
            return false;
        }

        for (Categoria tempRadice : this.radici) {
            if (tempRadice.getNome().equals(nomeRadice))
                return true;
        }
        return false;
    }


}