package com.example.phase4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @SequenceGenerator(
            name = "wallet_sequence",
            sequenceName = "wallet_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "wallet_sequence"
    )
    private Long id;

    @Min(value = 0, message = "balance cant be negative")
    private double balance;

    @OneToOne
    //@JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;


    public Wallet(User user) {
        this.user = user;
        this.balance = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(id, wallet.id) && Objects.equals(user, wallet.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", balance=" + balance +
                ", user=" + user.id +
                '}';
    }
}
