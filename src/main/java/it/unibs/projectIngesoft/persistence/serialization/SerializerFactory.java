package it.unibs.projectIngesoft.persistence.serialization;

public interface SerializerFactory {
    <T> Serializer<T> createSerializer();
}
