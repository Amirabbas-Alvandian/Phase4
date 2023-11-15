package com.example.phase4.entity;


import com.example.phase4.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
//@ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User implements UserDetails {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    protected Long id;

    @Pattern(regexp = "[a-zA-Z]{3,50}", message = "name -> only alphabet allowed")
    protected String firstName;

    @Pattern(regexp = "[a-zA-Z]{3,70}", message = "lastName -> only alphabet allowed")
    protected String lastName;


    @Column(unique = true)
    @Email
    protected String email;

/*    @Pattern.List({
            @Pattern(regexp = "(?=.*[0-9]).+", message = "Password must contain one digit."),
            @Pattern(regexp = "(?=.*[a-z]).+", message = "Password must contain one lowercase letter."),
            @Pattern(regexp = "(?=.*[A-Z]).+", message = "Password must contain one upper letter."),
            @Pattern(regexp = "(?=.*[&%#@]).+", message ="Password must contain one special character."),
            @Pattern(regexp = "(?=\\S+$).+", message = "Password must contain no whitespace."),
    })*/
    protected String password;
    protected LocalDate signUpDate;

    @OneToOne(mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    protected Wallet wallet;

    @Enumerated(EnumType.STRING)
    protected Role role;

    protected boolean enabled;

    public User(String firstName, String lastName, String email, String password,Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.signUpDate = LocalDate.now();
        this.wallet = new Wallet(this);
        this.role = role;
        this.enabled = false;
    }

    public User(Long id, String firstName, String lastName, String email, LocalDate signUpDate, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.signUpDate = signUpDate;
        this.role = role;
        this.enabled = false;

    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", signUpDate=" + signUpDate +
                ", role=" + role +
                ", enabled=" + enabled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(signUpDate, user.signUpDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, signUpDate);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
