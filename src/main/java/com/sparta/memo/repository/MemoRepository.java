package com.sparta.memo.repository;

import com.sparta.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Repository를 달아주지 않아도 자동으로 구현체를 만들고, bean으로 등록됨 !
public interface MemoRepository extends JpaRepository<Memo, Long> { // 마찬가지로 이름이 memoRepository로 저장됨

    // 모든 메모들을 수정된 시간을 기준으로 정렬됨
    List<Memo> findAllByOrderByModifiedAtDesc();
    List<Memo> findAllByContentsContainsOrderByModifiedAtDesc(String keyword);


    // Username이 ~ 인 것만 가져와라
    // sql에서 where 조건으로 조건을 걸어주는 것과 같음
    // SELECT * FROM Memo WHRER username = "";
    // List<Memo> findAllByUsername(String username);

}
