package org.jruchel.carworkshop.models.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Options {
    private Title title;
    private boolean responsive;
    private boolean maintainAspectRatio;
}
