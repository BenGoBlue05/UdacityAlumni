package com.google.developer.udacityalumni.utils;

import java.util.Collection;

/**
 * Created by benjaminlewis on 1/20/18.
 */

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static boolean isEmpty(Collection collection){
        return collection == null || collection.isEmpty();
    }
}
