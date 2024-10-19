package it.unibs.fp.myutils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Wade Giovanni Baisini
 */
public class CollectionsUtility {

    /**
     * Riordina una lista di oggetti secondo il reverseOrder di keyExtractor
     *
     * @param tempList     lista da ordinare
     * @param keyExtractor must be: Comparator.comparing(Class::method), Comparator che usa una proprietà di T per ordinare
     * @param <T>          Tipo di oggetto contenuto nella lista
     * @return lista ordinata
     */
    public static <T> List<T> reverseSort(List<T> tempList, Comparator<T> keyExtractor) {
        tempList.sort(Collections.reverseOrder(keyExtractor));
        return tempList;
    }

}
