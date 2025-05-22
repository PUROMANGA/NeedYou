package error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor

public enum ExceptionCode implements ErrorCode {
    // 공통
    VALID_EXCEPTION(HttpStatus.BAD_REQUEST, "VALID_EXCEPTION가 발생했습니다, 인텔리제이 로그를 확인해주세요"),
    BRAND_CANT_FIND(HttpStatus.BAD_REQUEST, "브랜드를 찾을 수 없습니다"),
    USER_CANT_FIND(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다"),
    PRODUCT_CANT_FIND(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다"),
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND, "판매자를 찾을 수 없습니다."),


    // 배송지 정보
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "배송지 정보를 찾을 수 없습니다"),
    ADDRESS_LIMIT_OVER(HttpStatus.BAD_REQUEST, "배송지 정보는 최대 10개까지 등록할 수 있습니다"),
    DEFAULT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "기본 배송지는 한 개만 설정할 수 있습니다"),
    UNAUTHORIZED_ADDRESS_ACCESS(HttpStatus.FORBIDDEN, "해당 배송지 정보에 대한 권한이 없습니다"),
    NO_DEFAULT_ADDRESS(HttpStatus.BAD_REQUEST, "기본 배송지가 존재하지 않습니다"),
    CANNOT_DELETE_DEFAULT_ADDRESS(HttpStatus.BAD_REQUEST, "기본 배송지는 삭제할 수 없습니다."),
    CANNOT_UNSET_DEFAULT_ADDRESS(HttpStatus.BAD_REQUEST, "기본 배송지는 해제할 수 없으며, 다른 배송지를 기본으로 설정하면 기존 기본 배송지가 해제됩니다."),

    // 판매자 정보
    UNAUTHORIZED_SELLER_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 브랜드
    UNAUTHORIZED_BRAND_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    UNAUTHORIZED_BRAND_CREATION(HttpStatus.FORBIDDEN, "판매자 등록이 되어있지 않은 사용자입니다."),
    //리뷰
    UNAUTHORIZED_REVIEW_ACCESS(HttpStatus.FORBIDDEN,"주문자와 유저가 일치 하지 않습니다."),
    PAYMENT_REQUIRED(HttpStatus.BAD_REQUEST, "상품 배송 전입니다."),
    UNAUTHORIZED_COMMENT_DELETE(HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 삭제할 수 있습니다.");
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
