# Sorting AWS Customer Spending

**Branch name: sorting-classroom**

**RDE workflows:**

* `sort-classroom-customer`
* `sort-classroom-first-report`
* `sort-classroom-second-report`

The AWS Finance team is working on creating a couple of new reports for leadership. Leadership is really interested in
understanding which AWS customers are our biggest spenders and which services they are spending the most money on.

Note that some of the customers may appear similar to real AWS customers,
but all of the associated data are complete fiction made up by ATA curriculum
developers. Do not interpret any of these as representative of actual AWS
customer costs.

## Existing code

Together let’s review the classes that already exist in the system.

### Data classes/POJOs
* **`Customer`**: The customer, including name and the date they joined
  as a customer of AWS.
* **`ServiceSpend`**: The amount spent on a specific AWS service. This
  does not refer to a particular customer (the following class contains
  both a `Customer` and an `ServiceSpend`)
* **`CustomerServiceSpend`**: Contains the amount that a single customer
  spends on a single service. For example, an instance can capture that
  Company XYZ spends $100 on DynamoDB. Contains a `Customer` and a
  `ServiceSpend`.
* **`CustomerTotalSpend`**: Contains the total amount that a given
  customer spends across all AWS services. Contains a
  `Customer` and the total spend amount. It also contains a list of
  the spend for all individual services that the customer uses. 

### Other classes
* **`AwsInvoiceDao`**: Provides methods to query AWS customer spend
* **`AwsCustomerStatistics`**: The class providing methods to return the
  desired reports. You will implement these report methods (as well as
  changes necessary to "type" classes to sort the relevant data objects).

![Image: A class diagram representing the classes currently available for AWS Spending Reports](resources/bigspender.png)

(the .puml for this diagram is in this same directory, in case you want
to make modifications)

## Sort customers (by name, ascending)

Before we get to the reports, let's be sure we can sort our customers
by name. Add and implement `Comparable` on the `Customer` class. The
natural ordering is by customer name. If two companies happen to
have the same exact name (can happen if they are in different
industries), then the tie-breaker is the customer's join date.

As is customary, the natural ordering is 'ascending', so be sure
that your implementation provides this by ordering based on customer
name (ascending), and then by join date (ascending) if the names
are equal.

### Design

Discuss with your group the changes needed.

### Implement

Implement the changes in `Customer`. Implement the indicated tests
in `CustomerTest`. Make sure all tests pass. Run the RDE workflow
`sort-classroom-customer`, which should now pass.

**Doneness criteria:**
- `Customer` implements `Comparable`
- Unimplemented `CustomerTest` unit tests are written
- `CustomerTest` unit tests all pass
- `sort-classroom-customer` RDE workflow passes

## Largest single service spend per customer

The first report we want to create is an ordered list of each of the biggest customers
and the single AWS service that each customer spends the most on.
This report is represented by a `List<CustomerServiceSpend>`.

The `List` must be ordered by customer name (ascending), then by join
date (ascending). To provide an ordering consistent with `equals()`,
then order by by service name (ascending), then by spend
amount (ascending). You may consider this the natural ordering for
`CustomerServiceSpend`.

`AwsServiceInvoiceDao` has a method, `getHighestServiceSpends()`, that provides an
unsorted list of the single biggest AWS service spend for each customer. Each
customer will be represented by a single `CustomerServiceSpend` element.

For instance, if the `AwsServiceInvoiceDao` provides this `List<CustomerServiceSpend>`:

```none
{{"pinteresting", 2009-10-11}, {S3, 300_00}}
{{"reddthem", 2009-10-11}, {LAMBDA, 230_00}}
{{"nedflix", 2008-05-03}, {S3, 350_00}}
```

then `AwsCustomerStatistics.getTopServiceSpendForEachCustomer` should return
the list in the following order:

```none
{{"nedflix", 2008-05-03}, {S3, 350_00}}
{{"pinteresting", 2009-10-11}, {S3, 300_00}}
{{"reddthem", 2009-10-11}, {LAMBDA, 230_00}}
```

because the customer names are ordered "nedflix", "pinteresting", "reddthem".

### Design

Take a moment to discuss as a group which changes are needed
to achieve this ordering. When you’re ready we’ll discuss as a class.

### Implement `Comparable`

Add and implement the `Comparable` interface for the relevant class.
Don’t forget that your `compareTo` method should be consistent with
your `equals()` method. If you implement `equals()` yourself, you can
ignore `hashCode()`. (But if you let IntelliJ create `equals()` and
`hashCode()` for you, that is ok too).

### Unit tests

Create `CustomerServiceSpendTest` in the appropriate place for unit
tests. Write at least 2 unit tests for your `compareTo` method and
2 for your `equals` method.

You only need to worry about testing the ordering by customer
name and join date. Don't worry about adding tests for sorting by
service name and spend amount. (We would probably include for
completeness in production, but let's not spend our time on them here).

### Sort

Now that our `Comparable` is ready for use let’s implement the
`getTopServiceSpendForEachCustomer` method in the `AwsCustomerStatistics`
class. Take a look at the Javadoc for the method to get started.
When you think you have a working solution, try running the
`sort-classroom-first-report` workflow to validate. You can also
run the `GetTopTotalSpendsTest` class directly in IntelliJ.

**Doneness criteria:**
- You've made `CustomerServiceSpend` `Comparable`
- You've created `CustomerServiceSpendTest` and added unit tests for
  both `comapreTo` and `equals`
- All `CustomerServiceSpendTest` tests pass
- You've implemented `getTopServiceSpendForEachCustomer` to sort
  `CustomerServiceSpend` returned by `getHighestServiceSpends` by
  customer name, then join date.
- `sort-classroom-first-report` RDE workflow passes

## Spend Across Services

Now we want to get the spend not just for the AWS service that each customer is spending the most on, but we want to
know how much each customer is spending on each AWS service. Again we will be returning a
`List<CustomerServiceSpend>`, but this time there will be one entry for each AWS service
that each customer uses.

In this report, we must return `CustomerServiceSpend` objects:
- grouped by customer, customers are ordered by total spend (descending)
- within a customer, the individual `CustomerServiceSpend`s for the customer
  must be ordered by individual service spend (descending), then by
  AWS service name (either ascending or descending, your choice).

In the future, we expect these reports to also be requested in alphabetical order
by customer, and perhaps alphabetical order by service. Therefore, a natural
ordering does not seem appropriate in this case.

Discuss with your group: Can we still use a `Comparable`, or do we need to
implement `Comparator`(s)?

The `AwsServiceInvoiceDao`'s `getAllServiceSpends` method provides a `List` of
unsorted `CustomerTotalSpend` objects that you may use to implement this
report.

Assuming the same customer total spends
as before, but with lists of services for each, we might return a result like:

```
[
 {{"nedflix", 2008-05-03}, {S3, 350_00}},
 {{"nedflix", 2008-05-03}, {DYNAMODB, 75_00}},
 {{"nedflix", 2008-05-03}, {EC2, 75_00}},
 {{"pinteresting", 2009-10-11}, {S3, 300_00}},
 {{"reddthem", 2009-10-11}, {LAMBDA, 230_00}},
 {{"reddthem", 2009-10-11}, {EC2, 40_00}},
 {{"reddthem", 2009-10-11}, {DYNAMODB, 20_00}},
 {{"reddthem", 2009-10-11}, {SQS, 10_00}},
]
```

Again, note that all of the biggest customer's spends are first,
and within each customer's spends, the service spends are ordered by
spend amount. In this example, pinteresting only spent on one service,
so it has only one entry. 

### Design

Notice that you have a `List<CustomerTotalSpend>`, and a single
`CustomerTotalSpend` contains a `List<CustomerServiceSpend>`. Consider
how to approach the sorting in your report generator:
- Can it do a single sort of `CustomerServiceSpend` objects? Or does it
  need to break the sorting down into two phases?
- Can we use `Comparable` here for any of the sorting? Or do we need
  to turn to `Comparator`(s)?

Please design all `compare()` to enforce an "ascending" ordering.
**HINT:** If you need to reverse a `Comparator`'s ascending ordering
to be descending, please see `Comparator.reversed()`, which you can
call on an existing object that is-a `Comparator`. This is the
recommended way to provide descending orderings.

Discuss with your group whether to use/implement `Comparable` on any classes,
or whether `Comparator`(s) is/are needed. If so, what should it/they
compare? What are the sorting rules?

### Implement comparison logic

Implement your solution and write tests for your implementation.

If you create any `Comparator` class(es), put it/them in a new `compare` package
under `com.amazon.ata.comparable_comparator_sort.classroom.bigspender`.

### Now implement the report

Implement `AwsCustomerStatistics`'s `getTopItemizedSpends` method, using your
comparison logic implementation above. `sort-classroom-second-report` should pass.
You can also run the `GetTopItemizedSpendsTest` test directly in IntelliJ.

**Doneness criteria:**
- You've implemented logic to satisfy the ordering requirements above
- Any `Comparator` classes are in the `compare` package
- All `compare()` methods provide an ascending ordering (see hint above
  to implement descending logic based on this convention)
- You have created unit tests for any new classes you have created
- You have implemented the `getTopItemizedSpends` method
- `sort-classroom-second-report` tests pass

## Extensions

1. Add unit tests for your `AwsCustomerStatisticsTest` implementation.
   We provide a couple of tests in `GetTopItemizedSpendsTest` and
   `GetTopTotalSpendsTest`, but these would not be considered complete.
1. Provide an alternative method in `AwsCustomerStatistics` to provide an alternative
   version of the total spend report (second report) that sorts customers by
   name, not by total spend.
1. Provide the first extension and the original second report using only a
   single `Comparator<CustomerTotalSpend>` (make the `Comparator` configurable
   to sort either by total spend OR by customer name).
1. Create a new version of the first report that orders customers by total
   AWS spend, not by customer name.
