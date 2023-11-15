package com.example.phase4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "category_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    @Pattern(regexp = "[a-zA-Z]{3,100}", message = "only alphabet allowed")
    @Column(unique = true)
    @NotNull
    private String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
