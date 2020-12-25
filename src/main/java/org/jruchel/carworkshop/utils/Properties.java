package org.jruchel.carworkshop.utils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Properties {

    private static Properties instance;

    private Properties() {

    }

    public static Properties getInstance() {
        if (instance == null) instance = new Properties();
        return instance;
    }

    private String getAbsolutePath(String path) {
        return new File("").getAbsolutePath() + path;
    }

    public String readProperty(String key) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getAbsolutePath("/src/main/resources/application.properties")));
        String regex = String.format("%s\\=(.+)", key);
        String str;
        Pattern propertyPattern = Pattern.compile(regex);
        Matcher matcher;
        while ((str = reader.readLine()) != null) {
            matcher = propertyPattern.matcher(str);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        return null;
    }
}
