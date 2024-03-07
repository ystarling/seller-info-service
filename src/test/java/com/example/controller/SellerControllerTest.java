package com.example.controller;


import com.example.entities.MarketplaceEntity;
import com.example.entities.ProducerEntity;
import com.example.entities.SellerEntity;
import com.example.entities.SellerInfoEntity;
import com.example.generated.types.*;
import com.example.repository.SellerRepository;
import com.example.service.SellerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.*;

@Import({SellerService.class, SellerRepository.class})
@GraphQlTest(SellerController.class)
public class SellerControllerTest {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private SellerRepository sellerRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testFindAllSellersWithAllArguments() {

        final SellerEntity sellerEntity = createSeller("Nike");

        Page<SellerEntity> entities = new PageImpl<>(List.of(sellerEntity));

        ArgumentCaptor<Pageable> pageableArgument = ArgumentCaptor.forClass(Pageable.class);

        Mockito.when(sellerRepository.findAll(Mockito.any(Specification.class), pageableArgument.capture())).thenReturn(entities);

        String document = """      
            query sellers ($filter: SellerFilter, $page: PageInput!, $sortBy: SellerSortBy) {
                sellers(filter: $filter, page: $page, sortBy: $sortBy)
                {
                    data {
                      sellerName
                      externalId
                      marketplaceId
                      producerSellerStates {
                        producerId
                      }
                    }
                    meta {
                      page: page
                      size: size
                    }
              }
            }
            """;

        SellerFilter sellerFilter = SellerFilter.newBuilder().searchByName("Nike").marketplaceIds(List.of()).producerIds(List.of()).build();
        Map<String, Object> sellerFilterMap = objectMapper.convertValue(sellerFilter, new TypeReference<>() {});

        PageInput pageInput = PageInput.newBuilder().page(0).size(10).build();
        Map<String, Object> pageInputMap = objectMapper.convertValue(pageInput, new TypeReference<>() {});

        tester.document(document)
                .variable("filter", sellerFilterMap)
                .variable("page", pageInputMap)
                .variable("sortBy", SellerSortBy.NAME_DESC)

                .execute()
                .path("sellers")
                .entity(SellerPageableResponse.class)
                .satisfies(response -> {
                    Pageable pageable = pageableArgument.getValue();
                    Assertions.assertEquals(Sort.Direction.DESC, Objects.requireNonNull(pageable.getSort().getOrderFor("sellerInfo.name")).getDirection());

                    Assertions.assertEquals(0, pageable.getPageNumber());
                    Assertions.assertEquals(10, pageable.getPageSize());

                    Assertions.assertEquals("Nike", response.getData().get(0).getSellerName());
                });
    }

    private static SellerEntity createSeller(String name) {
        final SellerEntity seller = new SellerEntity();
        seller.setId(UUID.randomUUID());
        seller.setState(SellerState.REGULAR.name());

        final SellerInfoEntity sellerInfo = new SellerInfoEntity();
        sellerInfo.setId(UUID.randomUUID());
        sellerInfo.setName(name);
        sellerInfo.setExternalId("1");

        final MarketplaceEntity marketplace = new MarketplaceEntity();
        marketplace.setId("marketplaceId");
        marketplace.setDescription("");
        sellerInfo.setMarketplace(marketplace);
        seller.setSellerInfo(sellerInfo);

        ProducerEntity producer = new ProducerEntity();
        producer.setId(UUID.randomUUID());
        producer.setName("producerName");
        seller.setProducer(producer);

        return seller;
    }
}
