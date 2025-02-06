package it.unibs.projectIngesoft.mappers;

public interface Repository<T> {

    void save(T object);
    T load();
}
