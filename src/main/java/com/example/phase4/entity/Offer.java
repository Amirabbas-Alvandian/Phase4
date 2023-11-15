package com.example.phase4.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"specialist_id", "order_id"})})

public class Offer {

    @Id
    @SequenceGenerator(
            name = "offer_sequence",
            sequenceName = "offer_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "offer_sequence"
    )
    private Long id;

    @ManyToOne
    //@JoinColumn(name = "specialist_id",referencedColumnName = "id")
    private Specialist specialist;

    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;

    private LocalDate offerDate;

    private Double specialistPrice;

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalTime finishTime;

    private Boolean isAccepted;

    public Offer(Specialist specialist,
                 Order order,
                 Double specialistPrice,
                 LocalDate startDate,
                 LocalTime startTime,
                 LocalTime finishTime) {
        this.specialist = specialist;
        this.order = order;
        this.specialistPrice = specialistPrice;
        this.startDate = startDate;
        this.offerDate = LocalDate.now();
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.isAccepted = false;
        offerDate = LocalDate.now();
    }


    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", specialist=" + specialist +
                ", order=" + order +
                ", offerDate=" + offerDate +
                ", specialistPrice=" + specialistPrice +
                ", StartDate=" + startDate +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", isAccepted=" + isAccepted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id) && Objects.equals(specialist, offer.specialist) && Objects.equals(order, offer.order) && Objects.equals(offerDate, offer.offerDate) && Objects.equals(specialistPrice, offer.specialistPrice) && Objects.equals(startDate, offer.startDate) && Objects.equals(startTime, offer.startTime) && Objects.equals(finishTime, offer.finishTime) && Objects.equals(isAccepted, offer.isAccepted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, specialist, order, offerDate, specialistPrice, startDate, startTime, finishTime, isAccepted);
    }
}
