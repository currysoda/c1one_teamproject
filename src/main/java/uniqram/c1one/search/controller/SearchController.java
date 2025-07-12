package uniqram.c1one.search.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniqram.c1one.search.dto.SearchHistoryDto;
import uniqram.c1one.search.dto.UserSearchResultDto;
import uniqram.c1one.search.entity.SearchHistory;
import uniqram.c1one.search.service.FindUser;
import uniqram.c1one.search.service.SearchHistoryRecord;

@Slf4j
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name ="Search API", description = "검색 기능을 제공하는 api 입니다.")
public class SearchController {
	
	private final FindUser            findUser;
	private final SearchHistoryRecord searchHistoryRecord;
	
	// 유저가 무엇을 어떻게 검색했느냐에 따라 다르게 매핑
	// keyword string formatting 에 따라 (일단 username 만 구현함 추후 수정)
	@Operation(
		summary = "검색어로 검색하는 api 입니다.",
		description = "검색어로 검색하는 api 입니다."
	)
	@GetMapping("/{searchKeyword}")
	ResponseEntity<List<UserSearchResultDto>> searchResult(@PathVariable(name = "searchKeyword") String searchKeyword) {
		
		// 검색 키워드를 가지고 결과값 조회
		List<UserSearchResultDto> searchResultList = findUser.findUser(searchKeyword);
		
		// 현재 접속중인 유저 정보 받아오기 (임시 하드코딩, 현재 접속중인 유저 정보 받아올 것)
		Long userid = 1L;
		
		// 검색 내용을 저장함
		SearchHistory saveResult = searchHistoryRecord.searchHistoryRecord(userid, searchKeyword);
		log.info("검색 기록 저장 = {}", saveResult);
		
		// 검색 키워드 결과값 반환 (결과값 없으면 빈 리스트 반환)
		return ResponseEntity.ok(searchResultList);
	}
	
	// 작업중
	@GetMapping("/search-history")
	ResponseEntity<SearchHistoryDto> searchHistory() {
		
		SearchHistoryDto searchHistoryDto = new SearchHistoryDto();
		
		return ResponseEntity.ok(searchHistoryDto);
	}
}
