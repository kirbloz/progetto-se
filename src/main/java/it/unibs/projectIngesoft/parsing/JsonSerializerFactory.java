package it.unibs.projectIngesoft.parsing;

public class JsonSerializerFactory implements SerializerFactory{
    @Override
    public <T> JacksonSerializer<T> createSerializer() {
        return new SerializerJSON<>();
    }
}
