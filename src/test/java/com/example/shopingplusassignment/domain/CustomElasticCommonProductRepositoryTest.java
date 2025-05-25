package com.example.shopingplusassignment.domain;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.shopingplusassignment.domain.ProductDocument.ElasticCommonProductRepository;
import com.example.shopingplusassignment.domain.ProductDocument.ProductDocument;
import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.brand.repository.BrandRepository;
import com.example.shopingplusassignment.domain.product.common.ProductCategory;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")

public class CustomElasticCommonProductRepositoryTest {

    final Logger log = LoggerFactory.getLogger(CustomElasticCommonProductRepositoryTest.class);

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ElasticCommonProductRepository elasticCommonProductRepository;

    @Test
    @DisplayName("'엄청난' 키워드가 제대로 search되는지 확인합니다")
    public void testSearchByKeyword() {

        //given
        User user = new User(
                "김땡중",
                "testpw123",
                "test@email.com",
                "0101234564",
                UserRole.USER,
                false
        );

        userRepository.save(user);

        //given
        Seller seller = new Seller(
                "스파르타",
                "김호중",
                "seller@email.com",
                "1234",
                "우리집",
                "5678",
                user
        );

        sellerRepository.save(seller);

        Brand brand = new Brand(
                "삼성",
                seller
        );

        brandRepository.save(brand);

        List<Product> products = new ArrayList<>();

        for(int i = 1; i <= 500; i ++) {
            Product product = new Product(
                    "엄청난 상품" + i,
                    "설명",
                    5000000L,
                    5L,
                    ProductCategory.COOLER,
                    brand,
                    seller.getId()
            );
            products.add(product);
        }

        List<Product> savedProducts = productRepository.saveAll(products);

        for(Product product : savedProducts) {
            ProductDocument productDocument = new ProductDocument(product);
            elasticCommonProductRepository.save(productDocument);
        }

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Query query = MatchQuery.of(m -> m.field("name").query("엄청난"))._toQuery();

        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();

        SearchHits<ProductDocument> searchHit = elasticsearchOperations.search(nativeQuery, ProductDocument.class);
        List<ProductDocument> result = searchHit.stream().map(SearchHit::getContent).toList();

        //then
        log.info("검색결과 = {}", result);
        assertThat(result).isNotNull();
    }
}
