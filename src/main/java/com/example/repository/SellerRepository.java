package com.example.repository;

import com.example.entities.SellerEntity;
import com.example.generated.types.SellerFilter;
import com.example.generated.types.SellerSortBy;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface SellerRepository extends JpaRepository<SellerEntity, UUID>, JpaSpecificationExecutor<SellerEntity> {
    interface Specs {
        static Specification<SellerEntity> byFilter(SellerFilter filter) {
            return (root, query, builder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (filter.getSearchByName() != null) {
                    predicates.add(builder.equal(root.get("sellerInfo").get("name"), filter.getSearchByName()));
                }

                if (filter.getMarketplaceIds() != null && !filter.getMarketplaceIds().isEmpty()) {
                    predicates.add(root.get("sellerInfo").get("marketplace").get("id").in(filter.getMarketplaceIds()));
                }

                if (filter.getProducerIds() != null && !filter.getProducerIds().isEmpty()) {
                    final List<UUID> uuids = filter.getProducerIds().stream().map(UUID::fromString).collect(Collectors.toList());
                    predicates.add(root.get("producer").get("id").in(uuids));
                }

                return builder.and(predicates.toArray(Predicate[]::new));
            };
        }

        static Sort sortBy(SellerSortBy sortBy) {
            return switch (sortBy) {
                case SELLER_INFO_EXTERNAL_ID_ASC -> Sort.by(Sort.Direction.ASC, "sellerInfo.externalId");
                case SELLER_INFO_EXTERNAL_ID_DESC -> Sort.by(Sort.Direction.DESC, "sellerInfo.externalId");
                case NAME_ASC -> Sort.by(Sort.Direction.ASC, "sellerInfo.name");
                case NAME_DESC -> Sort.by(Sort.Direction.DESC, "sellerInfo.name");
                case MARKETPLACE_ID_ASC -> Sort.by(Sort.Direction.ASC, "sellerInfo.marketplace.id");
                case MARKETPLACE_ID_DESC -> Sort.by(Sort.Direction.DESC, "sellerInfo.marketplace.id");
            };
        }
    }
}
