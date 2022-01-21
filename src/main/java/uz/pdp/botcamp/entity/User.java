package uz.pdp.botcamp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private int id;
    private String first_name;
    private String last_name;
    private String phone;
    private String password;
    private boolean active;
    private String role;

}
