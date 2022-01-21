package uz.pdp.botcamp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Answer {
    private int id;
    private String right_answer;
    private String wrong_answer_1;
    private String wrong_answer_2;
    private String wrong_answer_3;
}
