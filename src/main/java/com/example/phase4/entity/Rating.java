package com.example.phase4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rating {

    @Id
    @SequenceGenerator(
            name = "rating_sequence",
            sequenceName = "rating_sequence",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "rating_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    @Min(value = 1,message = "score cant be lower than 1")
    @Max(value = 5,message = "score cant be more than 5")
    private int score;

    private String comment;

    @OneToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;

    public Rating(int score, String comment, Order order) {
        this.score = score;
        this.comment = comment;
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return score == rating.score && Objects.equals(id, rating.id) && Objects.equals(comment, rating.comment) && Objects.equals(order, rating.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score, comment, order);
    }
}
