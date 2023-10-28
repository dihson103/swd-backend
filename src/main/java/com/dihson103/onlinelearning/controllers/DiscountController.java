package com.dihson103.onlinelearning.controllers;

import com.dihson103.onlinelearning.dto.common.ApiResponse;
import com.dihson103.onlinelearning.dto.discount.DiscountRequest;
import com.dihson103.onlinelearning.dto.discount.DiscountResponse;
import com.dihson103.onlinelearning.dto.filter.FilterRequestDto;
import com.dihson103.onlinelearning.services.IDiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final IDiscountService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse createDiscount(@RequestBody @Valid DiscountRequest discountRequest){
        service.createNewDiscount(discountRequest);
        return ApiResponse.builder()
                .message("Add new discount success.")
                .build();
    }

    @PutMapping("{discount-id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse updateDiscount(@RequestBody @Valid DiscountRequest discountRequest,
                                      @PathVariable("discount-id") Integer discountId){
        service.updateDiscount(discountId, discountRequest);
        return ApiResponse.builder()
                .message("Update discount success.")
                .build();
    }

    @DeleteMapping("{discount-id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse deleteDiscount(@PathVariable("discount-id") Integer discountId){
        service.deleteDiscount(discountId);
        return ApiResponse.builder()
                .message("Delete discount success.")
                .build();
    }

    @PostMapping("filter")
    @ResponseStatus(OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<DiscountResponse>> filterDiscounts(@RequestBody FilterRequestDto filterRequestDto){
        List<DiscountResponse> discountResponses = service.filterDiscounts(filterRequestDto);
        return ApiResponse.<List<DiscountResponse>>builder()
                .message("Filter discounts success.")
                .data(discountResponses)
                .build();
    }

}
