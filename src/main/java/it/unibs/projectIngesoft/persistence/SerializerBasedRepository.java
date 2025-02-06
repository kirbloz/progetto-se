package it.unibs.projectIngesoft.persistence;

import it.unibs.projectIngesoft.persistence.serialization.Serializer;


public abstract class SerializerBasedRepository<T> implements Repository<T> {

    protected final String filePath;
    protected final Serializer<T> serializer;

    protected SerializerBasedRepository(String filePath, Serializer<T> serializer){
        this.filePath = filePath;
        this.serializer = serializer;
    }

}
