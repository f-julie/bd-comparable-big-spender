package com.amazon.ata.comparable_comparator_sort.bigspender.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the total amount, in cents, that a customer spent on all AWS services.
 *
 * May seem like a special case of AWSCustomerSpendReportGkj, but that's tied to a particular service.
 */
public class CustomerTotalSpend {
    private Customer customer;
    private long totalSpend;
    private List<ServiceSpend> serviceSpends;

    /**
     * Constructor taking the customer and the amount in cents that customer spent on AWS services.
     * @param customer the customer.
     * @param serviceSpends The individual CustomerSpend for each service for this customer
     */
    public CustomerTotalSpend(Customer customer, List<ServiceSpend> serviceSpends) {
        this.customer = customer;
        this.serviceSpends = new ArrayList<>(serviceSpends);
        this.totalSpend = this.serviceSpends.stream()
                              .mapToLong(ServiceSpend::getSpend)
                              .sum();
    }

    public Customer getCustomer() {
        return customer;
    }

    public long getTotalSpend() {
        return totalSpend;
    }

    /**
     * Returns a copy of the customer's ServiceSpends.
     * @return A copy list of the customer's ServiceSpend
     */
    public List<ServiceSpend> getServiceSpends() {
        return new ArrayList<>(serviceSpends);
    }

    @Override
    public String toString() {
        return "CustomerTotalSpend{" +
               "customer=" + customer +
               ", totalSpend=" + totalSpend +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        CustomerTotalSpend that = (CustomerTotalSpend) o;
        return totalSpend == that.totalSpend &&
            Objects.equals(customer, that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, totalSpend);
    }
}
