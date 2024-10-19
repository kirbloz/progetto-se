package it.unibs.fp.myutils;

import java.util.ArrayList;

public interface Elenco<T> {

    T getOne(int index);

    ArrayList<T> getAll();

    T setOne(int index);

    void setAll(ArrayList<T> list);

    int getSize();

    boolean removeOne(T element);

    boolean addOne(T element);
    
    boolean replaceOne(T element, int index);
}
