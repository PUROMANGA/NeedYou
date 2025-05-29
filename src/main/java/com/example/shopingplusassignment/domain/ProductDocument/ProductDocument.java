package com.example.shopingplusassignment.domain.ProductDocument;


import com.example.shopingplusassignment.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setting(settingPath = "/static/elastic-settings.json")
@Document(indexName = "products")
@ToString

public class ProductDocument {

    @Id
    @Field(name = "id", type = FieldType.Long)
    private Long productId;

    @Field(name = "name", type = FieldType.Text)
    private String productName;

    @Field(name = "description", type = FieldType.Text)
    private String productDescription;

    @Field(name = "price", type = FieldType.Long)
    private Long productPrice;

    @Field(name = "stock", type = FieldType.Long)
    private Long productStock;

    @Field(type = FieldType.Text)
    private String productCategory;

    @Field(name = "brandName", type = FieldType.Text)
    private String brandName;

    @Field(name = "sellerId", type = FieldType.Long)
    private Long sellerId;

    public ProductDocument(Product product) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.productDescription = product.getDescription();
        this.productPrice = product.getPrice();
        this.productStock = product.getStock();
        this.productCategory = product.getProductCategory().toString();
        this.brandName = product.getBrand().getName();
        this.sellerId = product.getSellerId();
    }
}
