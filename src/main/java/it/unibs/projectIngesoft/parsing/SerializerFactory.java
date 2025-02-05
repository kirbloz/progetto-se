package it.unibs.projectIngesoft.parsing;

public interface SerializerFactory {
    <T> JacksonSerializer<T> createSerializer();
}
