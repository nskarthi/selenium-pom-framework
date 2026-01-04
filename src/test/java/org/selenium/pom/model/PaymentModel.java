package org.selenium.pom.model;

/**
 * Payment Model for handling checkout options.
 */
public class PaymentModel {
    public String paymentMethod = "bacs"; // bacs (Direct Bank Transfer), cod (Cash on Delivery), etc.

    public PaymentModel setPaymentMethod(String method) {
        this.paymentMethod = method;
        return this;
    }
}
