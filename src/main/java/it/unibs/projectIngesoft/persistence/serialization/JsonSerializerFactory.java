package it.unibs.projectIngesoft.persistence.serialization;

public class JsonSerializerFactory implements SerializerFactory{
    @Override
    public <T> Serializer<T> createSerializer() {
        return new SerializerJSON<>();
    }
}
