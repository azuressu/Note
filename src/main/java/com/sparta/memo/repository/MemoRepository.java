package com.sparta.memo.repository;

import com.sparta.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository를 달아주지 않아도 자동으로 구현체를 만들고, bean으로 등록됨 !
public interface MemoRepository extends JpaRepository<Memo, Long> { // 마찬가지로 이름이 memoRepository로 저장됨

}
