package com.example.shopingplusassignment.domain.comment.repository;

import java.util.List;

import com.example.shopingplusassignment.domain.comment.dto.CommentResponseDto;
import com.example.shopingplusassignment.domain.comment.entity.QComment;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.example.shopingplusassignment.domain.comment.entity.Comment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

	QComment comment = QComment.comment;

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<CommentResponseDto> findCommentsByDynamicCondition( Long productId, Long min, Long max, Pageable pageable) {

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(comment.rating.between(min, max));
		builder.and(comment.deletedAt.isNull());
		builder.and(comment.product.id.eq(productId));

		List<Comment> results = queryFactory
			.selectFrom(comment)
			.where(
				comment.product.id.eq(productId),
				comment.rating.between(min, max),
				comment.deletedAt.isNull()
			).offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(comment.creatTime.desc())
			.fetch();

		Long count = queryFactory
			.select(comment.count())
			.from(comment)
			.where(builder)
			.fetchOne();

		return new PageImpl<CommentResponseDto>(
			results.stream().map(CommentResponseDto::new).toList(),
			pageable,
			count == null ? 0L : count // ← L 붙이기!
		);
	}
}
