package com.shopping.rule;

import com.shopping.models.Item;
import com.shopping.models.Rule;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class IpadBulkDiscountRule implements Rule {

    private final Integer GROUP_SIZE = 4;
    private final String  SUPER_IPAD  = "ipd";
    private final BigDecimal SPECIAL_PRICE = BigDecimal.valueOf(499.99);

    @Override
    public boolean isMatch(List<Item> items) {
        return filterElebibleItens(items).size() > GROUP_SIZE;
    }

    @Override
    public BigDecimal execute(List<Item> items) {

        BigDecimal total = items.stream()
                .map(Item::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        List<Item> ipads = filterElebibleItens(items);

        BigDecimal price = ipads.get(0).getPrice();

        BigDecimal discount = price.subtract(SPECIAL_PRICE);

        return BigDecimal.valueOf(ipads.size()).multiply(discount);
    }

    private List<Item> filterElebibleItens(List<Item> itens) {
        return itens.stream()
                .filter(item -> item.getSku().equals(SUPER_IPAD))
                .collect(Collectors.toList());
    }
}
