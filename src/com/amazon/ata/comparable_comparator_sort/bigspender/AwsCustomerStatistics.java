package com.amazon.ata.comparable_comparator_sort.bigspender;

import com.amazon.ata.comparable_comparator_sort.bigspender.dao.AwsServiceInvoiceDao;
import com.amazon.ata.comparable_comparator_sort.bigspender.types.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that uses the AWS customer spending data provided by an
 * AwsServiceInvoiceDao to calculate information about the
 * customers.
 */
public class AwsCustomerStatistics {
    private AwsServiceInvoiceDao awsServiceInvoiceDao;

    /**
     * Creates a statistics instance with the provided DAO.
     * @param awsServiceInvoiceDao The AwsServiceInvoiceDao to use
     */
    public AwsCustomerStatistics(AwsServiceInvoiceDao awsServiceInvoiceDao) {
        this.awsServiceInvoiceDao = awsServiceInvoiceDao;
    }

    /**
     * Produces a list of the single service each AWS customer spent the most on,
     * sorted by customer name (ascending).
     * @return A list of CustomerServiceSpend representing the highest service
     *         spend for each customer, sorted by customer name
     */
    public List<CustomerServiceSpend> getTopServiceSpendForEachCustomer() {
        // PARTICIPANTS: Implement according to Javadoc and README
        List<CustomerServiceSpend> highestSpendsPerCustomer = this.awsServiceInvoiceDao.getHighestServiceSpendsForEachCustomer();
        Collections.sort(highestSpendsPerCustomer);

        return highestSpendsPerCustomer;
    }

    /**
     * Produces a list of *all* service spends of each AWS customer,
     * sorted by total spend (descending), then by the service spend (individual
     * service spend (descending), then service name).
     *
     * @return A list of CustomerServiceSpend, sorted by AWS customer
     * total spend and individual service spend.
     */
    public List<CustomerServiceSpend> getTopItemizedSpends() {
        // PARTICIPANTS: Implement according to Javadoc and README
        List<CustomerTotalSpend> customerTotalSpends = this.awsServiceInvoiceDao.getAllServiceSpends();

        Collections.sort(customerTotalSpends, new CustomerTotalSpendComparator().reversed());
        // Another option
        // Collections.reverse(customerServiceSpends); // To make it descending

        List<CustomerServiceSpend> customerServiceSpends = new ArrayList<>();

        for (CustomerTotalSpend cts : customerTotalSpends) {
            List<CustomerServiceSpend> customerList = new ArrayList<>();
            for (ServiceSpend css : cts.getServiceSpends()) {
                customerList.add(new CustomerServiceSpend(cts.getCustomer(), css));
            }
            Collections.sort(customerList, new CustomerServiceSpendComparator().reversed());
            customerServiceSpends.addAll(customerList);
        }
        return customerServiceSpends;
    }
}
