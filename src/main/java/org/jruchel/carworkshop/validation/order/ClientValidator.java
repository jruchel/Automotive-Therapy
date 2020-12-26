package org.jruchel.carworkshop.validation.order;

import org.jruchel.carworkshop.entities.Client;
import org.jruchel.carworkshop.validation.Validator;

public class ClientValidator extends Validator<ClientConstraint, Client> {
    protected boolean Constraint_ClientNotNull(Client value) {
        boolean result = value != null;
        if (!result) addMessage("Client cannot be empty");
        return result;
    }
}
