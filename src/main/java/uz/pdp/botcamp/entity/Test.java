package uz.pdp.botcamp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@NoArgsConstructor

public class Test {
private int id;
private int user_id;
private Timestamp start_time;
private Timestamp end_time;
private boolean active;
}
