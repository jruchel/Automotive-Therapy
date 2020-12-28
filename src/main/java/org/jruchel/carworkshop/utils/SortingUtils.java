package org.jruchel.carworkshop.utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortingUtils {

    public static <E> List<E> sort(List<E> elements, Comparator<E> comparator) {
        return elements.stream().sorted(comparator).collect(Collectors.toList());
    }

}
