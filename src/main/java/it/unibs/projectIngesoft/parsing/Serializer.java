package it.unibs.projectIngesoft.parsing;

import com.fasterxml.jackson.core.type.TypeReference;

public interface Serializer<T> {

    void serialize(String filePath, T data);
    T deserialize(TypeReference<T> type, String filePath);

}
