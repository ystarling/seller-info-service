package com.example.controller;

import com.example.generated.types.PageInput;
import com.example.generated.types.SellerFilter;
import com.example.generated.types.SellerPageableResponse;
import com.example.generated.types.SellerSortBy;
import com.example.service.SellerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellerController {

    private final SellerService sellerInfoService;

    public SellerController(SellerService sellerInfoService) {
        this.sellerInfoService = sellerInfoService;
    }

    @QueryMapping("sellers")
    public SellerPageableResponse sellers(@Argument SellerFilter filter,
                                          @Argument PageInput page,
                                          @Argument SellerSortBy sortBy) {
        return sellerInfoService.findAll(filter, page, sortBy);
    }
}
