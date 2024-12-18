package com.tofit.mvc.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tofit.mvc.jwt.JwtUtil;
import com.tofit.mvc.model.dto.FavoritesView;
import com.tofit.mvc.model.dto.Records;
import com.tofit.mvc.model.dto.RecordsView;
import com.tofit.mvc.model.service.RecordService;

@RestController
@RequestMapping("/tofit/record")
@CrossOrigin("*")
public class RecordRestController {

	private final RecordService recordService;
	private final JwtUtil jwtUtil;
	private final OpenAiChatModel openAiChatModel;

	public RecordRestController(RecordService recordService, JwtUtil jwtUtil, OpenAiChatModel openAiChatModel) {
		super();
		this.recordService = recordService;
		this.jwtUtil = jwtUtil;
		this.openAiChatModel = openAiChatModel;
	}

	// chatGPT : 운동 요약 + 운동 추천
	@GetMapping("/chatGPT")
	public ResponseEntity<?> chat(@RequestHeader(value = "Authorization") String token) {
		token = token.replace("Bearer ", "");
		String userId = jwtUtil.getUserIdFromToken(token);

		List<RecordsView> list = recordService.getMonthlyList(userId);
		String message = "너는 경력 10년차 열정 넘치는 운동 트레이너쌤이야. 지금부터 내가 회원님의 한달간 운동 정보를 보내줄게."
				+ "우리 서비스의 운동 목표는 다이어트, 근력강화, 체형교정, 스트레칭, 명상, 식단 총 6가지야" + "운동 요약을 간략히 해주고, 다음에 운동해야 할 목표를 추천해줘!"
				+ "참고로 우리는 여성 회원들이 많으니까 말투를 부드럽게 토닥이듯이 말해주고, 칭찬도 섞어서 회원의 의지를 활활 불타오르게 해야돼!";
		String response = openAiChatModel.call(message + Arrays.toString(list.toArray()));
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	// 달력 기록 조회
	@GetMapping()
	public ResponseEntity<?> list(@RequestHeader(value = "Authorization") String token) {
		token = token.replace("Bearer ", "");
		String userId = jwtUtil.getUserIdFromToken(token);

		List<RecordsView> list = recordService.getList(userId);

		if (list == null || list.size() == 0)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<List<RecordsView>>(list, HttpStatus.OK);
	}

	// 달력 기록 등록
	@PostMapping()
	public ResponseEntity<?> write(@RequestHeader(value = "Authorization") String token,
			@RequestBody FavoritesView fv) {
		token = token.replace("Bearer ", "");
		String userId = jwtUtil.getUserIdFromToken(token);

		Records rec = new Records();

		rec.setUserId(userId);
		rec.setVideoId(fv.getVideoId());

		// 같은 날짜에 같은 데이터 이미 등록되어 있는지 여부 확인
		boolean alreadyHave = recordService.searchRecord(rec);

		// 이미 등록되어 있으면 등록불가
		if (alreadyHave)
			return new ResponseEntity<Void>(HttpStatus.ALREADY_REPORTED);

		boolean isOk = recordService.writeRecords(rec);

		if (isOk)
			return new ResponseEntity<String>("complete", HttpStatus.CREATED);

		return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
	}

	// 달력 기록 삭제
	@DeleteMapping()
	public ResponseEntity<?> remove(@RequestHeader(value = "Authorization") String token, @RequestBody Records rec) {
		token = token.replace("Bearer ", "");
		String userId = jwtUtil.getUserIdFromToken(token);

		rec.setUserId(userId);
		boolean isOk = recordService.removeRecords(rec);

		if (isOk)
			return new ResponseEntity<String>("삭제완료", HttpStatus.ACCEPTED);

		return new ResponseEntity<String>("삭제불가", HttpStatus.BAD_REQUEST);
	}

}
