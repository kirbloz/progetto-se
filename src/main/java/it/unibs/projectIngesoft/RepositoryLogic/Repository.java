package it.unibs.projectIngesoft.RepositoryLogic;

public interface Repository<T> {

    void save(T object);
    T load();
}
