package com.shopping

import com.shopping.models.Item
import com.shopping.models.Rule
import com.shopping.rule.AppleTVDiscountRule
import com.shopping.rule.FreeVGARule
import com.shopping.rule.IpadBulkDiscountRule
import com.shopping.service.Checkout
import spock.lang.Specification

class CheckoutTest extends Specification {

    def "should apply discount for apple tv"(){

        given: "A user doing checkout"

            List<Rule> pricingRules = new ArrayList<>()

            pricingRules.add(new AppleTVDiscountRule())

            Checkout co = new Checkout(pricingRules)

        when: "A user buy 3 apple tvs"

            co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))
            co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))
            co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))
            co.scan(new Item("vga", "VGA adapter", BigDecimal.valueOf(30)))

        then: "A user will pay the price of 2 only"

        co.total() == BigDecimal.valueOf(249)
    }

    def "should apply discount for many apple tvs"(){

        given: "A user doing checkout"

        List<Rule> pricingRules = new ArrayList<>()

        pricingRules.add(new AppleTVDiscountRule())

        Checkout co = new Checkout(pricingRules)

        when: "A user buy 3 apple tvs"

        co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))
        co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))
        co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))

        co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))
        co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))
        co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))

        co.scan(new Item("vga", "VGA adapter", BigDecimal.valueOf(30)))

        then: "A user will pay the price of 2 only"

        co.total() == BigDecimal.valueOf(468)
    }

    def "should not apply discount for 2 apple tv"(){

        given: "A user doing checkout"

            List<Rule> pricingRules = new ArrayList<>()

            pricingRules.add(new AppleTVDiscountRule())

            Checkout co = new Checkout(pricingRules)

        when: "A user buy 2 apple tvs"

            co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))
            co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))

        then: "A user will pay the price of 2 only"

        co.total() == BigDecimal.valueOf(219)
    }



    def "bulk dicount for ipad"() {

        given: "A user doing checkout"

            List<Rule> pricingRules = new ArrayList<>()

            pricingRules.add(new IpadBulkDiscountRule())

            Checkout co = new Checkout(pricingRules)

        when: "He buy more than 4 IPads"

            co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))
            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))
            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))
            co.scan(new Item("atv", "Apple TV", BigDecimal.valueOf(109.50)))
            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))
            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))
            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))

        then: "The price should decrease to 499.99 each"

        co.total() == BigDecimal.valueOf(2718.95)
    }

    def "not apply bulk dicount for ipad"() {

        given: "A user doing checkout"

            List<Rule> pricingRules = new ArrayList<>()

            pricingRules.add(new IpadBulkDiscountRule())

            Checkout co = new Checkout(pricingRules)

        when: "He buy more than <=4 IPads"

            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))
            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))
            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))
            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))

        then: "The price should be 549.99 each"

        co.total() == BigDecimal.valueOf(2199.96)
    }



    def "user win a VGA Adapter with every MacBook Pro sold - equal quantity"() {

        given: "A user doing checkout"

            List<Rule> pricingRules = new ArrayList<>()

            pricingRules.add(new FreeVGARule())

            Checkout co = new Checkout(pricingRules)

        when: "He buy macbook pro and vga adapter"

            co.scan(new Item("mbp", "MacBook Pro", BigDecimal.valueOf(1399.99)))
            co.scan(new Item("vga", "VGA adapter ", BigDecimal.valueOf(30.00)))
            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))

        then: "We will bundle in a free VGA adapter free of charge with every MacBook Pro sold"

        co.total() == BigDecimal.valueOf(1949.98)
    }

    def "user win a VGA Adapter with every MacBook Pro sold - less cable than macbook"() {

        given: "A user doing checkout"

            List<Rule> pricingRules = new ArrayList<>()

            pricingRules.add(new FreeVGARule())

            Checkout co = new Checkout(pricingRules)

        when: "He buy macbook pro and vga adapter"

            co.scan(new Item("mbp", "MacBook Pro", BigDecimal.valueOf(1399.99)))
            co.scan(new Item("mbp", "MacBook Pro", BigDecimal.valueOf(1399.99)))
            co.scan(new Item("vga", "VGA adapter ", BigDecimal.valueOf(30.00)))
            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))

        then: "We will bundle in a free VGA adapter free of charge with every MacBook Pro sold"

        co.total() == BigDecimal.valueOf(3349.97)
    }

    def "user win a VGA Adapter with every MacBook Pro sold - less macbook than cable"() {

        given: "A user doing checkout"

            List<Rule> pricingRules = new ArrayList<>()

            pricingRules.add(new FreeVGARule())

            Checkout co = new Checkout(pricingRules)

        when: "He buy macbook pro and vga adapter"

            co.scan(new Item("mbp", "MacBook Pro", BigDecimal.valueOf(1399.99)))
            co.scan(new Item("vga", "VGA adapter ", BigDecimal.valueOf(30.00)))
            co.scan(new Item("vga", "VGA adapter ", BigDecimal.valueOf(30.00)))
            co.scan(new Item("ipd", "Super iPad", BigDecimal.valueOf(549.99)))

        then: "We will bundle in a free VGA adapter free of charge with every MacBook Pro sold"

        co.total() == BigDecimal.valueOf(1979.98)
    }
}
