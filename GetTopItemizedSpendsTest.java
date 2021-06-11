package com.amazon.ata.comparable_comparator_sort.classroom.bigspender;

import com.amazon.ata.comparable_comparator_sort.classroom.bigspender.dao.AwsServiceInvoiceDao;
import com.amazon.ata.comparable_comparator_sort.classroom.bigspender.types.CustomerServiceSpend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetTopItemizedSpendsTest {
    private AwsCustomerStatistics customerStatistics;

    private AwsServiceInvoiceDao dao = new AwsServiceInvoiceDao();

    @BeforeEach
    public void setup() {
        customerStatistics = new AwsCustomerStatistics(dao);
    }

    @Test
    public void getTopItemizedSpends_get_spendsAreSorted() {
        // WHEN
        List<CustomerServiceSpend> result = customerStatistics.getTopItemizedSpends();

        // THEN
        // nedflix is the largest total spender so they should be first
        // nedflix spends the greatest amount on S3
        CustomerServiceSpend firstResult = result.get(0);
        assertEquals(dao.getCustomerData().get("nedflix"), firstResult.getCustomer());
        assertEquals(dao.getCharges().get("nedflix").get(0), firstResult.getServiceSpend());

        // nedflix spends the second greatest amount on EC2
        CustomerServiceSpend secondResult = result.get(1);
        assertEquals(dao.getCustomerData().get("nedflix"), secondResult.getCustomer());
        assertEquals(dao.getCharges().get("nedflix").get(2), secondResult.getServiceSpend());

        // nedflix spends the least amount on DDB
        CustomerServiceSpend thirdResult = result.get(2);
        assertEquals(dao.getCustomerData().get("nedflix"), thirdResult.getCustomer());
        assertEquals(dao.getCharges().get("nedflix").get(1), thirdResult.getServiceSpend());

        // backup is the second largest total spender so they should be second
        // backup spends the greatest amount on S3
        CustomerServiceSpend fourthResult = result.get(3);
        assertEquals(dao.getCustomerData().get("backup"), fourthResult.getCustomer());
        assertEquals(dao.getCharges().get("backup").get(0), fourthResult.getServiceSpend());

        // godiddy is the smallest total spender so they should be last
        // godiddy spends the greatest amount on EC2
        CustomerServiceSpend fifthResult = result.get(4);
        assertEquals(dao.getCustomerData().get("godiddy"), fifthResult.getCustomer());
        assertEquals(dao.getCharges().get("godiddy").get(0), fifthResult.getServiceSpend());

        // godiddy spends the least amount on S3
        CustomerServiceSpend sixthResult = result.get(5);
        assertEquals(dao.getCustomerData().get("godiddy"), sixthResult.getCustomer());
        assertEquals(dao.getCharges().get("godiddy").get(1), sixthResult.getServiceSpend());
    }
}
