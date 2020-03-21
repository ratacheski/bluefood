package br.com.ratacheski.bluefood.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionUtils {
    public static <T> List<T> listOf(T... objs){
        if (objs == null){
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(objs.length);
        for (T obj: objs){
            list.add(obj);
        }
        return list;
    }
}
