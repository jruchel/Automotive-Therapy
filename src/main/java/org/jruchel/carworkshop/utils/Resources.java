package org.jruchel.carworkshop.utils;

import java.io.*;

public class Resources {


    public static String getTemplateAsString(String filename) {
        return getResourceAsString(turnIntoSystemUniversalPath(String.format("templates/%s.html", filename)));
    }

    private static String turnIntoSystemUniversalPath(String path) {
        return path.replaceAll("/", String.format("\\%s", System.getProperty("file.separator")));
    }

    private static String getResourceAsString(String resource) {
        return inputStreamToString(getResourceAsStream(resource));
    }

    private static String inputStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        reader.lines().forEach(line -> sb.append(line).append("\n"));
        return sb.toString();
    }

    private static InputStream getResourceAsStream(String resource) {
        return Resources.class.getClassLoader().getResourceAsStream(resource);
    }
}
