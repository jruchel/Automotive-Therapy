package org.jruchel.carworkshop.validation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationErrorPasser {

    private Map<String, String> messages = new HashMap<>();
    private static ValidationErrorPasser instance;

    public static ValidationErrorPasser getInstance() {
        if (instance == null) instance = new ValidationErrorPasser();
        return instance;
    }

    public void addMessage(String parameter, String message) {
        messages.put(parameter, message);
    }

    public String getMessage(String parameter) {
        return messages.get(parameter);
    }

    public List<String> getMessagesAsList() {
        List<String> result = new ArrayList<>();
        for (String s : messages.keySet()) {
            String stemp = getMessageFromField(s);
            if (stemp != null) result.add(stemp);
        }
        markAllAsRead();
        return result;
    }

    public String getMessagesAsString() {
        StringBuilder sb = new StringBuilder();
        for (String s : messages.keySet()) {
            String stemp = getMessageFromField(s);
            if (stemp != null) {
                sb.append(stemp);
                sb.append("\n");
            }
        }
        markAllAsRead();
        return sb.toString();
    }

    private String getMessageFromField(String field) {
        if (messages.get(field) == null || messages.get(field).isEmpty()) return null;
        return String.format("'%s' - %s", field, messages.get(field));
    }

    public void markAsRead(String parameter) {
        messages.remove(parameter);
    }

    private void markAllAsRead() {
        messages.clear();
        messages = new HashMap<>();
    }

}
