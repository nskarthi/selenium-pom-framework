package org.selenium.pom.model;

/**
 * Billing Model with additional unique fields.
 */
public class BillingModel extends AddressModel<BillingModel> {
	public String phone = "1234567890";
	public String email = "john.doe@example.com";
	public boolean createAnAccount = false;
	public boolean shipToDifferentAddress = false;
	public String orderNotes = "Please leave at the front door.";

	public BillingModel setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public BillingModel setEmail(String email) {
		this.email = email;
		return this;
	}

	public BillingModel setCreateAnAccount(boolean value) {
		this.createAnAccount = value;
		return this;
	}

	public BillingModel setShipToDifferentAddress(boolean value) {
		this.shipToDifferentAddress = value;
		return this;
	}

	public BillingModel setOrderNotes(String notes) {
		this.orderNotes = notes;
		return this;
	}

	public boolean isShipToDifferentAddress() {
		return shipToDifferentAddress;
	}
	
	
}
