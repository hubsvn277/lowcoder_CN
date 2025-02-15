package org.lowcoder.domain.application.repository;


import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.lowcoder.domain.application.model.Application;
import org.lowcoder.domain.application.model.ApplicationStatus;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ApplicationRepository extends ReactiveMongoRepository<Application, String>, CustomApplicationRepository {

    @Query(fields = "{ publishedApplicationDSL : 0 , editingApplicationDSL : 0 }")
    Flux<Application> findByOrganizationId(String organizationId);


    @Override
    @Nonnull
    @Query(fields = "{ publishedApplicationDSL : 0 , editingApplicationDSL : 0 }")
    Mono<Application> findById(@Nonnull String id);

    Mono<Long> countByOrganizationIdAndApplicationStatus(String organizationId, ApplicationStatus applicationStatus);

    @Query("{$or : [{'publishedApplicationDSL.queries.datasourceId':?0},{'editingApplicationDSL.queries.datasourceId':?0}]}")
    Flux<Application> findByDatasourceId(String datasourceId);

    Flux<Application> findByIdIn(List<String> ids);

    @Query(value = "{$and:[{'publicToAll':true},{'$or':[{'publicToMarketplace':?0},{'agencyProfile':?1}]}, {'_id': { $in: ?2}}]}", fields = "{_id : 1}")
    Flux<Application> findByPublicToAllIsTrueAndPublicToMarketplaceIsOrAgencyProfileIsAndIdIn
            (Boolean publicToMarketplace, Boolean agencyProfile, Collection<String> ids);

    Flux<Application> findByPublicToAllIsTrueAndPublicToMarketplaceIsAndAgencyProfileIsAndIdIn(Boolean publicToMarketplace, Boolean agencyProfile, Collection<String> ids);

    Flux<Application> findByPublicToAllIsTrueAndPublicToMarketplaceIsTrue();

    Flux<Application> findByPublicToAllIsTrueAndAgencyProfileIsTrue();

}
