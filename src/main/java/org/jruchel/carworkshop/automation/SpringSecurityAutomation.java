package org.jruchel.carworkshop.automation;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpringSecurityAutomation {
    public static List<Endpoint> getAllEndpoints() {
        List<Endpoint> endpoints = new ArrayList<>();
        for (Controller c : Controller.getAccessibleControllers()) {
            RequestMapping controllerMapping = Arrays.stream(c.getClass().getDeclaredAnnotationsByType(RequestMapping.class)).findFirst().orElse(null);
            String controllerPath;
            try {
                controllerPath = Arrays.stream(controllerMapping.value()).findFirst().orElse("");
            } catch (Exception ex) {
                controllerPath = "";
            }
            for (Method m : c.getClass().getDeclaredMethods()) {
                SecuredMapping securedMapping = Arrays.stream(m.getDeclaredAnnotationsByType(SecuredMapping.class)).findFirst().orElse(null);
                if (securedMapping != null) {
                    endpoints.add(new Endpoint(controllerPath + securedMapping.path(), securedMapping.role(), securedMapping.method()));
                }
            }
        }
        return endpoints;
    }
}
