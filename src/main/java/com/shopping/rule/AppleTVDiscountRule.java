package com.shopping.rule;

import com.shopping.models.Item;
import com.shopping.models.Rule;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class AppleTVDiscountRule implements Rule {

    private final String APPLE_TV_SKU = "atv";
    private final Integer GROUP_SIZE = 3;

    @Override
    public boolean isMatch(List<Item> items) {
        return filterEligibleItems(items).size() >= GROUP_SIZE;
    }

    @Override
    public BigDecimal execute(List<Item> items) {
        BigDecimal total = items.stream()
                .map(Item::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        List<Item> appleTvs = filterEligibleItems(items);

        BigDecimal price = appleTvs.get(0).getPrice();

        return price.multiply(BigDecimal.valueOf(appleTvs.size() / GROUP_SIZE));
    }

    private List<Item> filterEligibleItems(List<Item> items){
        return items.stream()
                .filter(item -> item.getSku().equals(APPLE_TV_SKU))
                .collect(Collectors.toList());
    }
}
