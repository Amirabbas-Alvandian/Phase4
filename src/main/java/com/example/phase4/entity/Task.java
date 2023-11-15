package com.example.phase4.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {


    @EmbeddedId
    private TaskId id;

    @JoinColumn(unique = true)
    @OneToOne
    @MapsId("orderId")
    //@JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;

    @OneToOne
    @MapsId("offerId")
    //@JoinColumn(name = "offer_id",referencedColumnName = "id")
    private Offer offer;

    public Task(Order order, Offer offer) {
        this.id = new TaskId(order.getId(), offer.getId());
        this.order = order;
        this.offer = offer;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Embeddable
    public static class TaskId implements Serializable {
        private Long orderId;
        private Long offerId;
    }
}
