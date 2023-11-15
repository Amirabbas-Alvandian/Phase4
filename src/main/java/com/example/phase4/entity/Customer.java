package com.example.phase4.entity;


import com.example.phase4.entity.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends User{

    public Customer(@Pattern(regexp = "[a-zA-Z]{3,100}", message = "only alphabet allowed") String firstName,
                    @Pattern(regexp = "[a-zA-Z]", message = "only alphabet allowed") String LastName,
                    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", message = "invalid email format") String email,
                    @Pattern(regexp = "[0-9A-Z-a-z]", message = "alphabet and numbers") String password
                    ) {
        super(firstName, LastName, email, password , Role.ROLE_CUSTOMER);
    }


    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @Override
    public String toString() {
        return "Customer{" +
                + getId() +
                ", firstName='" + firstName + '\'' +
                ", LastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", signUpDate=" + signUpDate +
                '}';
    }



}
