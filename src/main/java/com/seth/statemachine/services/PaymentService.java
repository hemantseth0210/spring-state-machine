/**
 * 
 */
package com.seth.statemachine.services;

import org.springframework.statemachine.StateMachine;

import com.seth.statemachine.domain.Payment;
import com.seth.statemachine.domain.PaymentEvent;
import com.seth.statemachine.domain.PaymentState;

/**
 * @author heseth
 *
 */
public interface PaymentService {

	Payment newPayment(Payment payment);
	
	StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);
	
	StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);
	
	StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);
}
