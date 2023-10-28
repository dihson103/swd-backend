package com.dihson103.onlinelearning.services;

import com.dihson103.onlinelearning.dto.discount.DiscountRequest;
import com.dihson103.onlinelearning.dto.discount.DiscountResponse;
import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;

import java.util.List;

public interface IDiscountService {
    void createNewDiscount(DiscountRequest discountRequest);

    void updateDiscount(Integer discountId, DiscountRequest discountRequest);

    void deleteDiscount(Integer discountId);

    List<DiscountResponse> filterDiscounts(FilterRequestDto filterRequestDto);
}
