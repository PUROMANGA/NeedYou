package error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor

public enum ExceptionCode implements ErrorCode {
    VALID_EXCEPTION(HttpStatus.BAD_REQUEST, "VALID_EXCEPTION가 발생했습니다, 인텔리제이 로그를 확인해주세요"),
    BRAND_CANT_FIND(HttpStatus.BAD_REQUEST, "브랜드를 찾을 수 없습니다"),
    USER_CANT_FIND(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다"),
    PRODUCT_CANT_FIND(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
