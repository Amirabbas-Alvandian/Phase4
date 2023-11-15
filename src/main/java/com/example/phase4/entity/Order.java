package com.example.phase4.entity;

import com.example.phase4.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"customer_id", "city", "street", "unit", "subCategory_id", "startdate"})})

public class Order {

    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customer;


    @ManyToOne
    @JoinColumn(name = "subCategory_id",referencedColumnName = "id")
    private SubCategory subCategory;

    @OneToMany(mappedBy = "order")
    private List<Offer> offers;

    @OneToOne(mappedBy = "order")
    private Rating rating;


    private Double customerPrice;

    private String description;

    private LocalDate startDate;

    private LocalDate orderDate;

    @Embedded
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Address address;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Customer customer, SubCategory subCategory, Double customerPrice, String description, Address address, LocalDate startDate) {
        this.customer = customer;
        this.subCategory = subCategory;
        this.customerPrice = customerPrice;
        this.description = description;
        this.address = address;
        this.startDate = startDate;
        this.orderStatus = OrderStatus.AWAITING_FOR_SPECIALIST_OFFER;
        this.orderDate = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", subCategory=" + subCategory +
                ", customerPrice=" + customerPrice +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", address=" + address +
                ", orderStatus=" + orderStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(customer, order.customer) && Objects.equals(subCategory, order.subCategory) && Objects.equals(customerPrice, order.customerPrice) && Objects.equals(description, order.description) && Objects.equals(startDate, order.startDate) && Objects.equals(address, order.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, subCategory, customerPrice, description, startDate, address);
    }
}
