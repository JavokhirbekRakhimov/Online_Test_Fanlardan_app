package uz.pdp.botcamp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Question {
    private int id;
    private String content;
    private int subject_id;
    private int difficulty_id;
    private int answer_id;
    private boolean active;
}
