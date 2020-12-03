/**
 * 
 */
package com.seth.statemachine.config.actions;

import java.util.Random;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.seth.statemachine.domain.PaymentEvent;
import com.seth.statemachine.domain.PaymentState;
import com.seth.statemachine.services.PaymentServiceImpl;

/**
 * @author heseth
 *
 */
@Component
public class PreAuthAction implements Action<PaymentState, PaymentEvent>{

	@Override
	public void execute(StateContext<PaymentState, PaymentEvent> context) {
		System.out.println("Pre Auth was called");
		
		if(new Random().nextInt(10) < 8) {
			System.out.println("Approved !!!");
			context.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_APPROVED)
					.setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
					.build());
		} else {
			System.out.println("Declined No Credit !!!");
			context.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_DECLINED)
					.setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
					.build());
		}
		
	}

}
