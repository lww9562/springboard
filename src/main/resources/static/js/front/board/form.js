/**
* 게시판 작성, 수정 양식 공통 스크립트
*/

window.addEventListener("DOMContentLoaded", function() {
	if (CKEDITOR) {		// 에디터를 사용하는 경우
		CKEDITOR.replace("content", {
			height: 350
		});
	}
});
