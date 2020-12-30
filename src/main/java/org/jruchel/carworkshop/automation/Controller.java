package org.jruchel.carworkshop.automation;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class Controller {

    private static List<Controller> accessibleControllers;
    static {
        accessibleControllers = new ArrayList<>();
    }

    public Controller() {
        accessibleControllers.add(this);
    }

    public static List<Controller> getAccessibleControllers() {
        return accessibleControllers;
    }
}
