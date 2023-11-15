package com.example.phase4.entity;

import com.example.phase4.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@NoArgsConstructor
//@AllArgsConstructor
@Setter
@Getter
@ToString
public class Admin extends User  {
/*    @Id
    @SequenceGenerator(
            name = "admin_sequence",
            sequenceName = "admin_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "admin_sequence"
    )*/
/*    private Long id;

    @Column(unique = true)
    private String username;

    @Pattern(regexp = "[0-9A-Za-z]{4,25}", message = "password -> alphabet and numbers")
    private String password;*/

/*    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }*/

    public Admin(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, Role.ROLE_ADMIN);
    }
}
