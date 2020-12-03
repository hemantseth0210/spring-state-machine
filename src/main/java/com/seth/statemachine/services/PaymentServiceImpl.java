/**
 * 
 */
package com.seth.statemachine.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import com.seth.statemachine.domain.Payment;
import com.seth.statemachine.domain.PaymentEvent;
import com.seth.statemachine.domain.PaymentState;
import com.seth.statemachine.repository.PaymentRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author heseth
 *
 */
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

	public static final String PAYMENT_ID_HEADER = "payment_id";
	private final PaymentRepository paymentRepository;
	private final StateMachineFactory<PaymentState, PaymentEvent> factory;
	private final PaymentStateChangeInterceptor paymentStateChangeInterceptor;
	
	@Override
	public Payment newPayment(Payment payment) {
		payment.setState(PaymentState.NEW);
		return paymentRepository.save(payment);
	}

	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
		sendEvent(paymentId, sm, PaymentEvent.PRE_AUTHORIZE);
		return sm;
	}
	
	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
		sendEvent(paymentId, sm, PaymentEvent.AUTHORIZE);
		return sm;
	}

	@Deprecated
	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> sm = build(paymentId);
		sendEvent(paymentId, sm, PaymentEvent.AUTH_DECLINED);
		return sm;
	}

	private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> sm, PaymentEvent event) {
		Message<PaymentEvent> message = MessageBuilder.withPayload(event)
				.setHeader(PAYMENT_ID_HEADER, paymentId)
				.build();
		sm.sendEvent(message);
	}
	
	private StateMachine<PaymentState, PaymentEvent> build(Long paymentId){
		Payment payment = paymentRepository.getOne(paymentId);
		StateMachine<PaymentState, PaymentEvent> sm = factory.getStateMachine(Long.toString(payment.getId()));
		sm.stop();
		
		sm.getStateMachineAccessor()
			.doWithAllRegions(sma -> {
				sma.addStateMachineInterceptor(paymentStateChangeInterceptor);
				sma.resetStateMachine(new DefaultStateMachineContext<PaymentState, PaymentEvent>(payment.getState(), null, null, null));
			});
		sm.start();
		return sm;
	}
}
