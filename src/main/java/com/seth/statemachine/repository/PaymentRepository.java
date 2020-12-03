/**
 * 
 */
package com.seth.statemachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seth.statemachine.domain.Payment;

/**
 * @author heseth
 *
 */
public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
