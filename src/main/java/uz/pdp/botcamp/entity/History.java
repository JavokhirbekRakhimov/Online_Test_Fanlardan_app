package uz.pdp.botcamp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class History {
    private String question;
    private String your_answer;
    private String right_answer;
    private int rate;

}
