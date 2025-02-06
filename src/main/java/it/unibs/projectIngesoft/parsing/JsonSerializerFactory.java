package it.unibs.projectIngesoft.parsing;

public class JsonSerializerFactory implements SerializerFactory{
    @Override
    public <T> Serializer<T> createSerializer() {
        return new SerializerJSON<>();
    }
}
