package uz.pdp.botcamp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Subject {
    private int id;
    private String name;
    private boolean active;
}
