package it.unibs.projectIngesoft.core.domain.model;

import it.unibs.projectIngesoft.core.domain.entities.ComprensorioGeografico;
import it.unibs.projectIngesoft.persistence.Repository;

import java.util.ArrayList;
import java.util.List;

public class ComprensorioGeograficoModel {

    private List<ComprensorioGeografico> listaComprensoriGeografici;
    private final Repository<List<ComprensorioGeografico>> repository;

    public ComprensorioGeograficoModel(Repository<List<ComprensorioGeografico>> repository) {
        this.repository = repository;
        this.listaComprensoriGeografici =  repository.load();
        if(this.listaComprensoriGeografici == null) {
            this.listaComprensoriGeografici = new ArrayList<>();
        }
    }

    public List<String> getListaNomiComprensoriGeografici() {
        return listaComprensoriGeografici.stream()
                .map(ComprensorioGeografico::getNomeComprensorio)
                .toList();
    }

    public List<String> getStringComuniByComprensorioName(String comprensorio) {
        for (ComprensorioGeografico c : this.listaComprensoriGeografici) {
            if (c.getNomeComprensorio().equals(comprensorio)) {
                return c.getListaComuni();
            }
        }
        return new ArrayList<>();
    }

    public void aggiungiComprensorio(String nomeComprensorio, List<String> comuni) {
        ComprensorioGeografico tempComp = new ComprensorioGeografico(nomeComprensorio, comuni);
        if (getStringComuniByComprensorioName(nomeComprensorio).isEmpty()) {
            this.listaComprensoriGeografici.add(tempComp);
            repository.save(listaComprensoriGeografici);
        }
    }

    public boolean isEmpty() {
        return this.listaComprensoriGeografici.isEmpty();
    }

}
