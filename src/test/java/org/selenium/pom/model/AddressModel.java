package org.selenium.pom.model;

/**
 * Base Address Model containing all shared fields.
 */
class AddressModel<T extends AddressModel<T>> {
    public String firstname = "John";
    public String lastname = "Doe";
    public String company = "";
    public String country = "United States (US)";
    public String address1 = "123 Test St";
    public String address2 = "";
    public String city = "New York";
    public String state = "New York";
    public String zip = "10001";

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    public T setFirstname(String firstname) { this.firstname = firstname; return self(); }
    public T setLastname(String lastname) { this.lastname = lastname; return self(); }
    public T setCompany(String company) { this.company = company; return self(); }
    public T setCountry(String country) { this.country = country; return self(); }
    public T setAddress1(String address1) { this.address1 = address1; return self(); }
    public T setAddress2(String address2) { this.address2 = address2; return self(); }
    public T setCity(String city) { this.city = city; return self(); }
    public T setState(String state) { this.state = state; return self(); }
    public T setZip(String zip) { this.zip = zip; return self(); }
}
