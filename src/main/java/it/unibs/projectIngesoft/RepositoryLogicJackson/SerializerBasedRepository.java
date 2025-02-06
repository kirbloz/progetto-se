package it.unibs.projectIngesoft.RepositoryLogicJackson;

import it.unibs.projectIngesoft.RepositoryLogic.Repository;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.parsing.Serializer;

import java.util.List;

public abstract class SerializerBasedRepository<T> implements Repository<T> {

    protected final String filePath;
    protected final Serializer<List<Categoria>> serializer;

    protected SerializerBasedRepository(String filePath, Serializer<List<Categoria>> serializer){
        this.filePath = filePath;
        this.serializer = serializer;
    }

}
