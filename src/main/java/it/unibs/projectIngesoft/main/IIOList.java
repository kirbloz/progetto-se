package it.unibs.projectIngesoft.main;

import java.util.List;

public interface IIOList<T> {

    void write(List<T> list);
    List<T> read();
}
