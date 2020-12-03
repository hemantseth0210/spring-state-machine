package com.seth.statemachine.services;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import com.seth.statemachine.domain.Payment;
import com.seth.statemachine.domain.PaymentEvent;
import com.seth.statemachine.domain.PaymentState;
import com.seth.statemachine.repository.PaymentRepository;

@SpringBootTest
class PaymentServiceImplTest {

	@Autowired
	PaymentService paymentService;
	
	@Autowired
	PaymentRepository paymentRepository;
	
	Payment payment;
	
	@BeforeEach
	void setUp() {
		payment = Payment.builder().amount(new BigDecimal("12.99")).build();
	}
	
	@Transactional
	@Test
	void testPreAuth() {
		Payment savedPayment = paymentService.newPayment(payment);
		paymentService.preAuth(savedPayment.getId());
		Payment preAuthPayment = paymentRepository.getOne(savedPayment.getId());
		System.out.println(preAuthPayment);
	}

	@Transactional
	@RepeatedTest(10)
	void testAuth() {
		Payment savedPayment = paymentService.newPayment(payment);
		StateMachine<PaymentState, PaymentEvent> preAuthSM = paymentService.preAuth(savedPayment.getId());
		if(preAuthSM.getState().getId() == PaymentState.PRE_AUTH) {
			System.out.println("Pre-Auth is approved for the payment");
			StateMachine<PaymentState, PaymentEvent> authSM = paymentService.authorizePayment(savedPayment.getId());
			System.out.println("Result of Auth: " + authSM.getState().getId());
		} else {
			System.out.println("Pre-Auth is failed for the payment");
		}
		Payment authPayment = paymentRepository.getOne(savedPayment.getId());
		System.out.println(authPayment);
	}
}
