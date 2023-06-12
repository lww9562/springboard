package org.springboard.board.models.file;

import lombok.RequiredArgsConstructor;
import org.springboard.board.commons.AuthorizationException;
import org.springboard.board.commons.MemberUtil;
import org.springboard.board.entities.FileInfo;
import org.springboard.board.repositories.FileInfoRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileDeleteService {
	private final FileInfoService infoService;
	private final FileInfoRepository repository;

	private final MemberUtil memberUtil;

	/**
	 * 파일 등록번호로 파일 정보, 업로드 파일 삭제
	 *
	 * @param id
	 */
	public void delete(Long id) {
		FileInfo fileInfo = infoService.get(id);

		/** 관리자 및 파일을 등록한 사용자만 삭제 가능하도록 S */
		String createdBy = fileInfo.getCreatedBy();

		if(fileInfo.isDone() && createdBy != null && !memberUtil.isAdmin()
			&& (!memberUtil.isLogin() || !memberUtil.getMember().getUserId().equals(createdBy))) {

			throw new AuthorizationException("File.notYours");
		}
		/** 관리자 및 파일을 등록한 사용자만 삭제 가능하도록 E */

		/**
		 * 1. 정보 삭제
		 * 2. 실제 파일 삭제
		 */
		String filePath = fileInfo.getFilePath();
		repository.delete(fileInfo);
		repository.flush();

		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * GID로 파일 정보, 업로드 된 파일 삭제
	 *
	 * @param gid
	 */
	public void delete(String gid) {
		List<FileInfo> files = infoService.gets(gid);

		files.stream().forEach(f -> delete(f.getId()));
	}
}
