package com.sparta.memo.entity;

import com.sparta.memo.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Memo {

    private Long id;          // 메모끼리 구분하기 위해서 넣음 (데이터베이스에는 필수로 넣어야 하는 것)
    private String username;  // 메모를 작성한 사람의 이름
    private String contents;  // 메모 내용

    public Memo(MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }
}
