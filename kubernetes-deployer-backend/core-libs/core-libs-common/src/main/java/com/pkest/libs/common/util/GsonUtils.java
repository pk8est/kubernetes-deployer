package com.pkest.libs.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Helper class with commonly used methods
 */
public final class GsonUtils {

    final static Gson gson = GsonUtils.setupGson();


    GsonUtils() {
        throw new AssertionError("No instances for you!");
    }

    public static Gson getGson() {
        return gson;
    }

    private static Gson setupGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }
}

