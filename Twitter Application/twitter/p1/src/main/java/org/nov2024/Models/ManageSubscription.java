package org.nov2024.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "manage_subscription")
public class ManageSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subscriptionid;

    @ManyToOne
    @JoinColumn(name = "fksubscriberid")
    private User subscriber;

    @ManyToOne
    @JoinColumn(name = "fkproducerid")
    private User producer;

    public User getProducer() {
        return producer;
    }

    public void setProducer(User producer) {
        this.producer = producer;
    }

    public User getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }
}
