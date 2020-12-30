package org.jruchel.carworkshop.automation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMethod;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endpoint {
    private String path;
    private String role;
    private RequestMethod[] method;
}
