package it.unibs.projectIngesoft.core.domain.model;


import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.persistence.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CategorieModel {
    private List<Categoria> radici;
    private final Repository<List<Categoria>> repository;


    /**
     * Costruttore per inizializzare i percorsi dei file e de-serializzare l'albero.
     */
    public CategorieModel(Repository<List<Categoria>> repository) {
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
        this.radici = radici;
        save();
    }

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

    public List<Categoria> getListaCategorieGerarchiaFiltrata(Categoria radice, Predicate<Categoria> filtro) {
        List<Categoria> lista = Categoria.appiatisciGerarchiaSuLista(radice, new ArrayList<>());
        return lista.stream().filter(filtro).toList();

    }

    public List<String> getListaNomiCategorieGerarchiaFiltrata(Categoria radice, Predicate<Categoria> filtro) {
        return getListaCategorieGerarchiaFiltrata(radice, filtro)
                .stream()
                .map(Categoria::getNome)
                .toList();
    }

}