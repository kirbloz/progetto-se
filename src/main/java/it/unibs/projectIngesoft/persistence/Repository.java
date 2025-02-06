package it.unibs.projectIngesoft.persistence;

public interface Repository<T> {

    void save(T object);
    T load();
}
