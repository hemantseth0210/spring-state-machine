package com.seth.statemachine.services;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.seth.statemachine.domain.Payment;
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

}
