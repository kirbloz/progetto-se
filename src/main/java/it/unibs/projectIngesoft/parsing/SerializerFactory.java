package it.unibs.projectIngesoft.parsing;

public interface SerializerFactory {
    <T> Serializer<T> createSerializer();
}
