/**
 * 
 */
package com.seth.statemachine.domain;

/**
 * @author heseth
 *
 */
public enum PaymentEvent {
	PRE_AUTHORIZE, PRE_AUTH_APPROVED, PRE_AUTH_DECLINED, AUTHORIZE, AUTH_APPROVED, AUTH_DECLINED
}
