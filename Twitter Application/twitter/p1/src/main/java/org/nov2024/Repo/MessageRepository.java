package org.nov2024.Repo;

import org.hibernate.dialect.Replacer;
import org.nov2024.Models.Message;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    // You can add custom query methods if needed

    @Query(value = "select content,username from " +
            "message m join manage_subscription ms on m.producerid = ms.fkproducerid " +
            "join user u on u.userid = ms.fkproducerid " +
            "where ms.fksubscriberid = :subscriberid", nativeQuery = true)
    public List<Object[]> messagesForSubs(Integer subscriberid);
}