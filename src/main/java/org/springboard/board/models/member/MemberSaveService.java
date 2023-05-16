package org.springboard.board.models.member;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springboard.board.controllers.members.JoinForm;
import org.springboard.board.entities.Member;
import org.springboard.board.repositories.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSaveService {
	private final MemberRepository memberRepository;

	private final PasswordEncoder passwordEncoder;

	public void save(JoinForm joinForm){
		Member member = new ModelMapper().map(joinForm, Member.class);
		member.setUserPw(passwordEncoder.encode(joinForm.getUserPw()));

		memberRepository.saveAndFlush(member);
	}
}
