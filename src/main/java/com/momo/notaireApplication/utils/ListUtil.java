package com.momo.notaireApplication.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListUtil {
    public static List initList(List liste) {
        if (Objects.isNull(liste)) {
            return new ArrayList();
        }
        return liste;
    }
}
