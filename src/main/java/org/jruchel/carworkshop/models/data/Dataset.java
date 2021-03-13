package org.jruchel.carworkshop.models.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dataset {
    private String label;
    private List<String> backgroundColor;
    private List<Double> data;
}
