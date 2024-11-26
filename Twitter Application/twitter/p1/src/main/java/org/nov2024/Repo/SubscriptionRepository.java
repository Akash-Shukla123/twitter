package org.nov2024.Repo;

import jdk.jfr.BooleanFlag;
import org.nov2024.Models.ManageSubscription;
import org.nov2024.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<ManageSubscription, Integer> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM manage_subscription where fksubscriberid = :subscriberid and fkproducerid = :producerid", nativeQuery = true)
    Integer existsBySubscriberAndProducer(Integer subscriberid, Integer producerid);

}