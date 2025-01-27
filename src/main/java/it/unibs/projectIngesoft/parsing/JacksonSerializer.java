package it.unibs.projectIngesoft.parsing;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public interface JacksonSerializer<T> {

    void serialize(String filePath, T obj);
    T deserialize(TypeReference<T> type, String filePath);

}
