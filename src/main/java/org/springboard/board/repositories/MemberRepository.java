package org.springboard.board.repositories;

import org.springboard.board.entities.Member;
import org.springboard.board.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {
	Member findByUserId(String userId);

	default boolean exists(String userId){
		QMember member = QMember.member;

		return exists(member.userId.eq(userId));
	}
}
