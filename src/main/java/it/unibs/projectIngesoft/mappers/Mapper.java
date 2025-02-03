package it.unibs.projectIngesoft.mappers;

public interface Mapper<T> {

    void write(T object);
    T read();
}
