package com.example.phase4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "category_id"})})

public class SubCategory {

    @Id
    @SequenceGenerator(
            name = "service_sequence",
            sequenceName = "service_sequence",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "service_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "category_id")
    private Category category;

    @Pattern(regexp = "[a-zA-Z\\s]{3,100}", message = "subCategory name -> only alphabet allowed")
    @NotNull
    private String name;


    @Column(name = "base_price")
    @Positive
    private Double basePrice;

    private String description;

    @OneToMany(mappedBy = "subCategory")
    private List<Order> orders;

    @ManyToMany(mappedBy = "subCategoryList")
    private List<Specialist> specialists;

    public SubCategory(Category category, String name, Double basePrice,String description) {
        this.category = category;
        this.name = name;
        this.basePrice = basePrice;
        this.description = description;
    }

    @Override
    public String toString() {
        return "SubCategory{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", basePrice=" + basePrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubCategory that = (SubCategory) o;
        return Objects.equals(id, that.id) && Objects.equals(category, that.category) && Objects.equals(name, that.name) && Objects.equals(basePrice, that.basePrice) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, name, basePrice, description);
    }
}
