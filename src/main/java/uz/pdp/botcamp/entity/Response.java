package uz.pdp.botcamp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response {
    private boolean success;
    private String message;
}
