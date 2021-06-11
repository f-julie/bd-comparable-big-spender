package com.amazon.ata.comparable_comparator_sort.classroom.bigspender.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerTotalSpendTest {
    private CustomerTotalSpend nedflixTotalSpend;
    private Customer nedflix;
    private Customer reddthem;
    private LocalDate nedflixJoinDate;
    private LocalDate reddthemJoinDate;
    private long nedflixSpend = 500_00;
    private long reddthemSpend = 400_00;
    private List<ServiceSpend> nedflixSpends = Arrays.asList(
        new ServiceSpend(ServiceType.S3, 350_00),
        new ServiceSpend(ServiceType.DYNAMODB, 75_00),
        new ServiceSpend(ServiceType.EC2, 75_00));
    private List<ServiceSpend> reddthemSpends = Arrays.asList(
        new ServiceSpend(ServiceType.LAMBDA, 230_00),
        new ServiceSpend(ServiceType.EC2, 40_00),
        new ServiceSpend(ServiceType.DYNAMODB, 20_00),
        new ServiceSpend(ServiceType.SQS, 10_00));

    @BeforeEach
    void setUp() throws Exception {
        nedflixJoinDate = LocalDate.of(2008, 10, 11);
        nedflix = new Customer("nedflix", nedflixJoinDate);
        nedflixTotalSpend = new CustomerTotalSpend(nedflix, nedflixSpends);

        reddthemJoinDate = LocalDate.of(2009, 1, 1);
        reddthem = new Customer("reddthem", reddthemJoinDate);
    }

    @Test
    void getTotalSpend_withMultipleServiceSpends_returnsCorrectTotalSpend() {
        // GIVEN - nedflix and its multiple spends

        // WHEN
        long result = nedflixTotalSpend.getTotalSpend();

        // THEN
        assertEquals(nedflixSpend,
                     result,
                     String.format("Expected total spend for %s to be %d", nedflixTotalSpend, nedflixSpend)
        );
    }

    @Test
    void equals_inputObjectNull_returnsFalse() {
        // WHEN
        boolean result = nedflixTotalSpend.equals(null);

        // THEN
        assertFalse(result, "CustomerTotalSpend should not be equal to null!");
    }

    @Test
    void equals_inputObjectDifferentType_returnsFalse() {
        // WHEN
        boolean result = nedflixTotalSpend.equals("nedflixTotalSpend");

        // THEN
        assertFalse(result, "CustomerTotalSpend should not be equal to a different type!");
    }

    @Test
    void equals_sameObject_returnsTrue() {
        // WHEN
        boolean result = nedflixTotalSpend.equals(nedflixTotalSpend);

        // THEN
        assertTrue(result, "CustomerTotalSpend should be equal to a itself!");
    }

    @Test
    void equals_equalAttributes_returnsTrue() {
        // GIVEN
        CustomerTotalSpend same = new CustomerTotalSpend(nedflix, nedflixSpends);

        // WHEN
        boolean result = nedflixTotalSpend.equals(same);

        // THEN
        assertTrue(result, "CustomerTotalSpend should be equal to one with same attributes!");
    }

    @Test
    void equals_notEqualCustomer_returnsFalse() {
        // GIVEN
        CustomerTotalSpend other = new CustomerTotalSpend(reddthem, nedflixSpends);

        // WHEN
        boolean result = nedflixTotalSpend.equals(other);

        // THEN
        assertFalse(result, "CustomerTotalSpend should not be equal to one with a different customer!");
    }

    @Test
    void equals_notEqualSpend_returnsFalse() {
        // GIVEN
        CustomerTotalSpend other = new CustomerTotalSpend(nedflix, reddthemSpends);

        // WHEN
        boolean result = nedflixTotalSpend.equals(other);

        // THEN
        assertFalse(result, "CustomerTotalSpend should not be equal to one with a different spend!");
    }
}
