package com.example.shopingplusassignment.domain.comment;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import com.example.shopingplusassignment.domain.address.entity.Address;
import com.example.shopingplusassignment.domain.address.repository.AddressRepository;
import com.example.shopingplusassignment.domain.brand.entity.Brand;
import com.example.shopingplusassignment.domain.brand.repository.BrandRepository;
import com.example.shopingplusassignment.domain.cart.dto.CartResponseOrderDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentRequestDto;
import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;
import com.example.shopingplusassignment.domain.comment.repository.CommentRepository;
import com.example.shopingplusassignment.domain.comment.service.CommentCacheServiceImpl;
import com.example.shopingplusassignment.domain.comment.service.CommentServiceImpl;
import com.example.shopingplusassignment.domain.order.common.OrderStatus;
import com.example.shopingplusassignment.domain.order.entity.Order;
import com.example.shopingplusassignment.domain.order.repository.OrderRepository;
import com.example.shopingplusassignment.domain.product.common.ProductCategory;
import com.example.shopingplusassignment.domain.product.dto.RequestProductDto;
import com.example.shopingplusassignment.domain.product.entity.Product;
import com.example.shopingplusassignment.domain.product.repository.ProductRepository;
import com.example.shopingplusassignment.domain.productOrder.entity.ProductOrder;
import com.example.shopingplusassignment.domain.productOrder.repository.ProductOrderRepository;
import com.example.shopingplusassignment.domain.seller.entity.Seller;
import com.example.shopingplusassignment.domain.seller.repository.SellerRepository;
import com.example.shopingplusassignment.domain.user.entity.User;
import com.example.shopingplusassignment.domain.user.enums.UserRole;
import com.example.shopingplusassignment.domain.user.repository.UserRepository;
@Slf4j
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class CommentCacheServiceTest {

	private final RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private final CommentRepository commentRepository;
	@Autowired
	private final ProductRepository productRepository;
	@Autowired
	private final OrderRepository orderRepository;
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final SellerRepository sellerRepository;
	@Autowired
	private final BrandRepository brandRepository;
	@Autowired
	private final AddressRepository addressRepository;
	@Autowired
	private final ProductOrderRepository productOrderRepository;
	@Autowired
	private final CommentCacheServiceImpl commentCacheService;
	@Autowired
	private CommentServiceImpl commentService;

	public Order setOrder(){
		List<User> users =List.of(
			new User("paragon", "test123@gamil.com","12345", "010-1111-4444", UserRole.SELLER),
			new User("paragon1", "test4@gamil.com","12345", "010-1111-4444", UserRole.USER)
		);
		userRepository.saveAll(users);
		userRepository.flush();

		Seller seller = new Seller("pap", "사장", "teach@gamil.com", "156486463", "서울 어딘가", "163541685",users.get(0));
		sellerRepository.save(seller);
		sellerRepository.flush();

		Brand brand = Brand.create("옷가게", seller );
		brandRepository.save(brand);
		brandRepository.flush();

		RequestProductDto productDto = new RequestProductDto("옷","옷이에요", 12000L,100L, ProductCategory.CLOTHES);
		Product product1 = new Product(productDto, brand, seller.getId() );
		productRepository.save(product1);
		productRepository.flush();

		Address address = new Address(users.get(1),"경기도 어딘가", 11111L,"카무라","배송","156163516351",true);
		addressRepository.save(address);
		addressRepository.flush();

		Order order = new Order(users.get(1), address.getId());
		order.changeOrderStatus(OrderStatus.PENDING);
		Order savedOrder = orderRepository.save(order);

		List<CartResponseOrderDto> carts = List.of(new CartResponseOrderDto(product1.getId(), product1.getName(), product1.getPrice(), 1L,12000L,
			order, LocalDateTime.now(), LocalDateTime.now()));

		List<ProductOrder> productOrderList = carts
			.stream()
			.map(CartResponseOrderDto::toEntity)
			.toList();

		productOrderRepository.saveAll(productOrderList);
		productOrderRepository.flush();

		Order finalOrder = new Order(order, productOrderList);
		orderRepository.save(finalOrder);
		orderRepository.flush();

		return order;
	}
	public List<Order> setOrder2(){

		User owner = new User("paragon", "test123@gamil.com","12345", "010-1111-4444", UserRole.SELLER);
		userRepository.save(owner);

		List<User> users =List.of(
			new User("paragon1", "test4@gamil.com","12345", "010-1111-4444", UserRole.USER),
			new User("paragon2", "test5@gamil.com","12345", "010-1111-4444", UserRole.USER),
			new User("paragon3", "test6@gamil.com","12345", "010-1111-4444", UserRole.USER),
			new User("paragon4", "test7@gamil.com","12345", "010-1111-4444", UserRole.USER)
		);
		userRepository.saveAll(users);

		Seller seller = new Seller("pap", "사장", "teach@gamil.com", "156486463", "서울 어딘가", "163541685",owner);
		sellerRepository.save(seller);

		Brand brand = Brand.create("옷가게", seller );
		brandRepository.save(brand);

		RequestProductDto productDto = new RequestProductDto("옷","옷이에요", 12000L,100L, ProductCategory.CLOTHES);
		Product product1 = new Product(productDto, brand, seller.getId());
		productRepository.save(product1);
		productRepository.flush();

		List<Address> addressList = new ArrayList<>();
		for(int i =0; i< users.size(); i++){
			addressList.add(new Address(users.get(i), "경기도 어딘가", 11111L, "카무라", "배송", "156163516351", true));
		}
		addressRepository.saveAll(addressList);


		List<Order> orders = new ArrayList<>();
		for(int i =0; i< users.size(); i++){
			Order order = new Order(users.get(i), addressList.get(i).getId());
			order.changeOrderStatus(OrderStatus.PENDING);
			orders.add(order);
		}
		orderRepository.saveAll(orders);

		// cart → ProductOrder 변환
		List<CartResponseOrderDto> carts = IntStream.range(0, 4)
			.mapToObj(i -> new CartResponseOrderDto(
				product1.getId(), product1.getName(), product1.getPrice(), 1L, 12000L,
				orders.get(i), LocalDateTime.now(), LocalDateTime.now())
			)
			.toList();

		List<ProductOrder> productOrderList = carts
			.stream()
			.map(CartResponseOrderDto::toEntity)
			.toList();

		productOrderRepository.saveAll(productOrderList);
		productOrderRepository.flush();

		List<Order> findOrder = List.of(
			new Order(orders.get(0), productOrderList),
			new Order(orders.get(1), productOrderList),
			new Order(orders.get(2), productOrderList),
			new Order(orders.get(3), productOrderList)
		);
		findOrder.get(0).changeOrderStatus(OrderStatus.PAID);
		findOrder.get(1).changeOrderStatus(OrderStatus.PAID);
		findOrder.get(2).changeOrderStatus(OrderStatus.PAID);
		findOrder.get(3).changeOrderStatus(OrderStatus.PAID);
		orderRepository.saveAll(findOrder);
		return findOrder;
	}

	@Test
	void 캐시_테스트() {
		log.info("프록시 클래스: {}", commentCacheService.getClass());
	}

	@Transactional
	@Rollback(false)
	@Test
	void 리뷰_등록후_캐시_무효화_테스트() {
		// given
		// 댓글 목록 조회 → 캐시 생성
		List<Order> orders = setOrder2();
		//System.out.println(orders.size());
		Random random = new Random();
		List<CommentRequestDto> dto = List.of(
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6)),
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6)),
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6))
				);
		Long userId = 2L;
		for(int i = 0; i < dto.size(); i++){

			commentService.saveComment(orders.get(i).getId(), 1L, userId++ , dto.get(i));
		}
		log.info("프록시 클래스: {}", commentCacheService.getClass());
		long start1 = System.currentTimeMillis();
		commentCacheService.getCommentByRatingCache(1L, 0L, 5L, 0, 5);
		long end1 = System.currentTimeMillis();
		System.out.println("첫 번째 실행 시간: " + (end1 - start1) + "ms"); // 캐시 hit

		// when
		// commentCacheService.saveCommentCache(...)
		CommentRequestDto commentRequestDto =
			 new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6));

		commentService.saveComment(orders.get(3).getId(), 1L, 5L , commentRequestDto);
		// then
		// 같은 조건으로 getCommentByRatingCache 호출 → 캐시 miss 확인 (로그 or 시간 차이)
		long start2 = System.currentTimeMillis();
		commentCacheService.getCommentByRatingCache(1L, 0L, 5L, 0, 5);
		long end2 = System.currentTimeMillis();
		System.out.println("두 번째 실행 시간: " + (end2 - start2) + "ms"); // 캐시 hit

	}

	@Transactional
	@Test
	void 리뷰_삭제후_캐시_무효화_테스트() {
		// given
		// 댓글 목록 조회 → 캐시 생성
		List<Order> orders = setOrder2();
		//System.out.println(orders.size());
		Random random = new Random();
		List<CommentRequestDto> dto = List.of(
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6)),
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6)),
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6))
		);
		Long userId = 2L;
		for(int i = 0; i < dto.size(); i++){

			commentService.saveComment(orders.get(i).getId(), 1L, userId++ , dto.get(i));
		}
		log.info("프록시 클래스: {}", commentCacheService.getClass());
		long start1 = System.currentTimeMillis();
		commentCacheService.getCommentByRatingCache(1L, 0L, 5L, 0, 5);
		long end1 = System.currentTimeMillis();
		System.out.println("첫 번째 실행 시간: " + (end1 - start1) + "ms"); // 캐시 hit
		// when
		 commentCacheService.deleteCommentCache(1L, orders.get(1).getUser().getId(), 2L);

		// then
		// 캐시가 삭제되었는지 재호출 시간 확인
		long start2 = System.currentTimeMillis();
		commentCacheService.getCommentByRatingCache(1L, 0L, 5L, 0, 5);
		long end2 = System.currentTimeMillis();
		System.out.println("두 번째 실행 시간: " + (end2 - start2) + "ms"); // 캐시 hit
	}

	@Transactional
	@Test
	void 리뷰_수정후_캐시_무효화_테스트() {
		// given
		// 댓글 목록 조회 → 캐시 생성
		List<Order> orders = setOrder2();
		//System.out.println(orders.size());
		Random random = new Random();
		List<CommentRequestDto> dto = List.of(
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6)),
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6)),
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6))
		);
		Long userId = 2L;
		for(int i = 0; i < dto.size(); i++){

			commentService.saveComment(orders.get(i).getId(), 1L, userId++ , dto.get(i));
		}
		log.info("프록시 클래스: {}", commentCacheService.getClass());
		long start1 = System.currentTimeMillis();
		commentCacheService.getCommentByRatingCache(1L, 0L, 5L, 0, 5);
		long end1 = System.currentTimeMillis();
		System.out.println("첫 번째 실행 시간: " + (end1 - start1) + "ms"); // 캐시 hit
		// when
		// commentCacheService.updateCommentCache(...)
		CommentRequestDto commentRequestDto =
			new CommentRequestDto("별루에요", "그지같은 상품입니다.", 1);
		commentCacheService.updateCommentCache(orders.get(1).getUser().getId(),2L,commentRequestDto);
		// then
		// 캐시가 무효화되어 새로 조회되는지 확인
		long start2 = System.currentTimeMillis();
		commentCacheService.getCommentByRatingCache(1L, 0L, 5L, 0, 5);
		long end2 = System.currentTimeMillis();
		System.out.println("두 번째 실행 시간: " + (end2 - start2) + "ms"); // 캐시 hit

	}

	@Transactional
	@Test
	void 좋아요_캐싱_정상작동_테스트() {
		// given
		// 댓글 목록 조회 → 캐시 생성
		List<Order> orders = setOrder2();
		//System.out.println(orders.size());
		Random random = new Random();
		List<CommentRequestDto> dto = List.of(
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6)),
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6)),
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6))
		);
		Long userId = 2L;
		for(int i = 0; i < dto.size(); i++){

			commentService.saveComment(orders.get(i).getId(), 1L, userId++ , dto.get(i));
		}
		log.info("프록시 클래스: {}", commentCacheService.getClass());
		long start1 = System.currentTimeMillis();
		commentCacheService.getCommentByRatingCache(1L, 0L, 5L, 0, 5);
		long end1 = System.currentTimeMillis();
		System.out.println("첫 번째 실행 시간: " + (end1 - start1) + "ms"); // 캐시 hit
		// when
		Long reviewTestId = 2L;
		commentCacheService.updateCommentLikeStatusCache(orders.get(2).getUser().getId(), reviewTestId, true);

		// then
		Object raw = redisTemplate.opsForHash().get("comment:likes", reviewTestId.toString());
		assertThat(raw).isNotNull();
		assertThat(Long.parseLong(raw.toString())).isGreaterThan(0);
	}

	@Transactional
	@Rollback(value = false)
	@Test
	void 리뷰_평점별로_가져오기() {

		List<Order> orders = setOrder2();
		//System.out.println(orders.size());

		Random random = new Random();
		List<CommentRequestDto> dto = List.of(
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6)),
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6)),
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6)),
			new CommentRequestDto("빠른 배송 감사합니다.", "빨리 받았어요", random.nextInt(0,6))
		);
		Long userId = 2L;
		for(int i = 0; i < dto.size(); i++){

			commentService.saveComment(orders.get(i).getId(), 1L, userId++ , dto.get(i));
		}
		long start = System.currentTimeMillis();
		List<CommentResponseDto> commentGetInfoResponseDtoList =  commentService.getCommentByRating(1L, 0L, 5L, 0, 5);
		long end = System.currentTimeMillis();
		log.info("단건 조회 WD시간: {}ms",(end - start));


		//then
		assertThat(commentGetInfoResponseDtoList.get(0).getLikeCount());
		System.out.println(commentGetInfoResponseDtoList.size());
		for(int i = 0; i < dto.size(); i++){
			System.out.println(commentGetInfoResponseDtoList.get(i).getRating());

		}
		log.info("프록시 클래스: {}", commentCacheService.getClass());
		long start1 = System.currentTimeMillis();
		commentCacheService.getCommentByRatingCache(1L, 0L, 5L, 0, 5);
		long end1 = System.currentTimeMillis();

		long start2 = System.currentTimeMillis();
		commentCacheService.getCommentByRatingCache(1L, 0L, 5L, 0, 5);
		long end2 = System.currentTimeMillis();

		System.out.println("첫 실행 시간: " + (end1 - start1) + "ms"); // DB 조회 + 캐시 저장
		System.out.println("두 번째 실행 시간: " + (end2 - start2) + "ms"); // 캐시 hit

	}
	@Transactional
	@Test
	public void 좋아요_적용(){
		Order order = setOrder();
		order.changeOrderStatus(OrderStatus.PAID);

		CommentRequestDto dto = new CommentRequestDto("안녕", "받았어", 5);

		CommentResponseDto getDto = commentService.saveComment(1L, 1L,2L, dto);

		Long commentId =getDto.getId();
		// when - 좋아요 누르기
		commentService.applyLikeStatus(order.getUser().getId(), commentId, true);

		Object raw = redisTemplate.opsForHash().get("comment:likes", commentId.toString());
		assertThat(raw).isNotNull();
		assertThat(Long.parseLong(raw.toString()));

		// when - 좋아요 취소
		commentService.applyLikeStatus(order.getUser().getId(), commentId, false);

		Object afterCancel = redisTemplate.opsForHash().get("comment:likes", commentId.toString());
		assertThat(afterCancel).isNotNull();
		assertThat(Long.parseLong(afterCancel.toString()));
		System.out.println(Long.parseLong(raw.toString()));

	}
}
