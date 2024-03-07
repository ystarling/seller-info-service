package com.example.service;

import com.example.entities.SellerEntity;
import com.example.entities.SellerInfoEntity;
import com.example.generated.types.*;
import com.example.repository.SellerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public SellerPageableResponse findAll(SellerFilter filter, @NonNull PageInput page, SellerSortBy sortBy) {

        final Sort sort = (sortBy != null) ? SellerRepository.Specs.sortBy(sortBy) : Sort.unsorted();

        final PageRequest pageRequest = PageRequest.of(page.getPage(), page.getSize(), sort);

        Page<SellerEntity> entities;;
        if (filter != null) {
            entities = sellerRepository.findAll(SellerRepository.Specs.byFilter(filter), pageRequest);
        } else {
            entities = sellerRepository.findAll(pageRequest);
        }

        final SellerPageableResponse sellerPageableResponse = new SellerPageableResponse();

        List<Seller> sellers = new ArrayList<>();
        Map<SellerInfoEntity, List<SellerEntity>> map = entities.getContent().stream().collect(Collectors.groupingBy(SellerEntity::getSellerInfo));

        map.forEach((sellerInfo, sellerEntities) -> {
            List<ProducerSellerState> producerSellerStates = sellerEntities.stream()
                    .map(SellerService::mapToProducerSellerState)
                    .collect(Collectors.toList());

            Seller seller = Seller.newBuilder()
                    .sellerName(sellerInfo.getName())
                    .externalId(sellerInfo.getExternalId())
                    .producerSellerStates(producerSellerStates)
                    .marketplaceId(sellerInfo.getMarketplace().getId())
                    .build();

            sellers.add(seller);
        });

        sellerPageableResponse.setData(sellers);

        final PageMeta pageMeta = PageMeta.newBuilder().page(page.getPage()).size(sellers.size()).build();
        sellerPageableResponse.setMeta(pageMeta);

        return sellerPageableResponse;
    }

    private static ProducerSellerState mapToProducerSellerState(SellerEntity entity) {
        return ProducerSellerState.newBuilder()
                .producerId(entity.getProducer().getId().toString())
                .producerName(entity.getProducer().getName())
                .sellerState(SellerState.valueOf(entity.getState()))
                .sellerId(entity.getId().toString())
                .build();
    }
}
