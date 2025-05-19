package com.example.shopingplusassignment.domain.product.controller;


import com.example.shopingplusassignment.domain.product.dto.RequestProductDto;
import com.example.shopingplusassignment.domain.product.dto.ResponseProductDto;
import com.example.shopingplusassignment.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor

public class ProductController {

    private final ProductService productService;

    /**
     * 로그인된 판매자의 email로 seller 객체를 찾아주고, brandId로 brand를 찾아준다음, product 객체에 저장시키고, db에도 저장합니다.
     * @param requestProductDto
     * @param brandId
     * @param userDeatil
     * @return
     */
    @PostMapping("/brands/{brandId}/products")
    public ResponseEntity<ResponseProductDto> postProductController(
            @RequestBody @Validated RequestProductDto requestProductDto,
            @PathVariable Long brandId,
            @AuthenticationPrincipal UserDeatil userDeatil) {
        ResponseProductDto responseProductDto = productService.postProductService(requestProductDto, brandId, userDeatil.getUsername());
        return ResponseEntity.ok(responseProductDto);
    }

    /**
     * 상품을 찾고, 판매자를 찾고, 상품을 등록할 때 등록된 판매자와, 현재 로그인된 판매자를 비교한 뒤, 같다면 수정을 할 수 있게끔 도와줍니다.
     * @param requestProductDto
     * @param brandId
     * @param productId
     * @param userDeatil
     * @return
     */
    @PatchMapping("/products/{productId}")
    public ResponseEntity<ResponseProductDto> patchProductController(
            @RequestBody @Validated RequestProductDto requestProductDto,
            @PathVariable Long brandId,
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDeatil userDeatil) {

        ResponseProductDto responseProductDto = productService.patchProductService(requestProductDto, brandId, productId, userDeatil.getUsername());
        return ResponseEntity.ok(responseProductDto);
    }

    /**
     * 상품을 찾고, 판매자를 찾고, 상품을 등록할 때 판매자와 현재 로그인된 판매자를 비교한 뒤, 같다면 삭제를 합니다.
      * @param productId
     * @param userDeatil
     * @return
     */
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProductController(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDeatil userDeatil) {

        productService.deleteProductService(productId, userDeatil.getUsername());
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }

    /**
     * 상품 단 건 조회
     * @param productId
     * @return
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ResponseProductDto> getProductController(
            @PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductService(productId));
    }

    /**
     * keyword로 db에서 제목 혹은 설명과 같은 상품들을 페이징네이션 해서 보여준다
     * @param keyword
     * @param pageable
     * @return
     */

    @GetMapping("/search")
    public ResponseEntity<Slice<ResponseProductDto>> searchProductController(
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "creatTime", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(productService.searchProductService(keyword, pageable));
    }
}
