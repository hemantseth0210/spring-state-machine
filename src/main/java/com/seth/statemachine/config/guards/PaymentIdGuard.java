/**
 * 
 */
package com.seth.statemachine.config.guards;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import com.seth.statemachine.domain.PaymentEvent;
import com.seth.statemachine.domain.PaymentState;
import com.seth.statemachine.services.PaymentServiceImpl;

/**
 * @author heseth
 *
 */
@Component
public class PaymentIdGuard implements Guard<PaymentState, PaymentEvent>{

	@Override
	public boolean evaluate(StateContext<PaymentState, PaymentEvent> context) {
		return context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER) != null;
	}

}
