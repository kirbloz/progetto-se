package it.unibs.projectIngesoft.RepositoryLogic;

public interface Mapper<T> {

    void write(T object);
    T read();
}
