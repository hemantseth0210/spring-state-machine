/**
 * 
 */
package com.seth.statemachine.config.actions;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.seth.statemachine.domain.PaymentEvent;
import com.seth.statemachine.domain.PaymentState;

/**
 * @author heseth
 *
 */
@Component
public class AuthDeclinedAction implements Action<PaymentState, PaymentEvent>{

	@Override
	public void execute(StateContext<PaymentState, PaymentEvent> context) {
		System.out.println("Sending Notification of Auth Declined");
		
	}
}
