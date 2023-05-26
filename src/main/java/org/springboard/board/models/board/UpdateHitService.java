package org.springboard.board.models.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springboard.board.commons.MemberUtil;
import org.springboard.board.entities.BoardData;
import org.springboard.board.entities.BoardView;
import org.springboard.board.repositories.BoardDataRepository;
import org.springboard.board.repositories.BoardViewRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 조회수 업데이트
 */
@Service
@RequiredArgsConstructor
public class UpdateHitService {
	private final BoardViewRepository boardViewRepository;
	private final BoardDataRepository boardDataRepository;
	private final HttpServletRequest request;
	private final MemberUtil memberUtil;

	public void update(Long id) {
		try{
			BoardView boardView = new BoardView();
			boardView.setId(id);
			boardView.setUid(""+getUid());
			boardViewRepository.saveAndFlush(boardView);


		} catch(Exception e){
			e.printStackTrace();
		}

		long cnt = boardViewRepository.getHit(id);
		BoardData boardData = boardDataRepository.findById(id).orElse(null);

		if(boardData != null) {
			boardData.setHit((int)cnt);
			boardDataRepository.flush();
		}
	}

	private int getUid() {
		String ip = request.getRemoteAddr();
		String ua = request.getHeader("User-Agent");
		Long userNo = memberUtil.isLogin() ? memberUtil.getMember().getUserNo() : 0L;

		return Objects.hash(ip, ua, userNo);
	}
}
