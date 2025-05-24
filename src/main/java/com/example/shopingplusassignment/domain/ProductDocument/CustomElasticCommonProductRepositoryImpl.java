package com.example.shopingplusassignment.domain.ProductDocument;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

@RequiredArgsConstructor
@Slf4j

public class CustomElasticCommonProductRepositoryImpl implements CustomElasticCommonProductRepository{

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Slice<ProductDocument> searchByKeyword(String keyword, Pageable pageable) {

        Query query = MatchQuery.of(m -> m.field("name").query("엄청난"))._toQuery();

        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();

        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(nativeQuery, ProductDocument.class);

        List<ProductDocument> result = searchHits.stream().map(SearchHit::getContent).toList();

        log.info(result.toString());

        boolean hasNext = result.size() > pageable.getPageSize();

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
