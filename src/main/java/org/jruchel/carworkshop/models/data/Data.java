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
public class Data {
    private List<String> labels;
    private List<Dataset> datasets;
    private Options options;

}
