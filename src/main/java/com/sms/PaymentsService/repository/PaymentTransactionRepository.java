package com.sms.PaymentsService.repository;


import com.sms.PaymentsService.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {

    List<PaymentTransaction> findBySubscriptionId(Integer subscriptionId);

}
