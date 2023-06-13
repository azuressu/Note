package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController // html을 따로 반환하지 않기 때문
@RequestMapping("/api")
public class MemoController {

    // Map 자료구조 사용
    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // RequestDto -> Entity로 변경하기 (저장해야 하기 때문에)
        Memo memo = new Memo(requestDto);

        // Memo의 Max ID를 찾아야 함 (id값으로 Memo를 구분하는데, 중복이 되면 안되고, 현재 데이터베이스에 마지막 값을 구해서 +1을 하면 max id 만들기)
        // memoList.keySet을 해주면 앞의 값 즉 여기서는 Long의 값을 가져옴 - 그 중에서도 max를 가져오는 것임
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);
        // DB 저장
        memoList.put(memo.getId(), memo);

        // Entity -> ResponseDto로 변경
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() { // list인 이유는 메모가 하나일 수는 없기 때문 !
        // Map to List
        List<MemoResponseDto> responseList = memoList.values().stream()
                .map(MemoResponseDto::new).toList(); // :: new를 하면 생성자(Memo를 파라미터로 갖는)가 사용됨

        return responseList;
    }

    @PutMapping("/memos/{id}") // path valuable 방식
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        // 업데이트할 메모의 id, 수정할 내용을 requestbody로 받아옴
        // 메모가 실제로 DB에 존재하는지 확인하기
        if (memoList.containsKey(id)) {
            // 해당 메모를 가져오기
            Memo memo = memoList.get(id); // map 자료구조에서 그 id에 맞는 메모 객체가 반환될 것임

            // 가져온 메모를 수정하기
            memo.update(requestDto);

            return memo.getId(); // 혹은 return id; 도 가능
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다/");
        }
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) { // 지우는 것이니 RequestDto는 필요없음
        // 해당 메모가 데이터베이스에 존재하는지 확인하기
        if (memoList.containsKey(id)) {
            // 해당 메모를 삭제하기 (key를 넣어주면 가능)
            memoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다");
        }
    }


}
