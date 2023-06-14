package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Bean으로 등록됨 (Bean 객체로 등록되는 것)
           // 일반 class를 spring이 관리하는 객체로 등록하는 방법
public class MemoService { // memoService 라는 이름으로 등록이 됨
    @Autowired
    private final MemoRepository memoRepository;

//    @Autowired
//    public void setDi(MemoRepository memoRepository) {
//        this.memoRepository = memoRepository;
//    }

    @Autowired //가 생략이 가능하게 됨. 단 생성자 선언이 하나일 경우만 가능
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
        // 이 생성자 없이 @RequiredArgsConstructor를 붙일 수도 있지만, 여기서는 생성자로 사용
    }

    // bean에서 받아오기
//    public MemoService(ApplicationContext context) {
//        // 1. Bean의 이름으로 가져오는 방법
//       MemoRepository memoRepository = (MemoRepository) context.getBean("memoRepository");
//
//        // 2. Bean 클래스 형식으로 가져오기
//        MemoRepository memoRepository = context.getBean(MemoRepository.class);
//        this.memoRepository = memoRepository;
//    }

    // 데이터를 반환 ..? 하는 부분 ?
    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
        Memo saveMemo = memoRepository.save(memo); // 데이터베이스에 연결하는 거니까 메소드명 동일하게 해주지 않아도 됨

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(saveMemo); // 그냥 memo 인가 ?

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        // DB 조회
//        return memoRepository.findAll().stream().map(MemoResponseDto::new).toList(); // 전달해주는 파라미터 필요없음
        return memoRepository.findAllByOrderByModifiedAtDesc().stream().map(MemoResponseDto::new).toList();
    }

    @Transactional
    // update에는 적용해주어야 함
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        // memo 내용 수정
        memo.update(requestDto);
        return id;

    } // updateMemo

    public Long deleteMemo(Long id) {
        // 1. 지울 메모를 가져온다
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);
        // 2. 지울 entity 객체를 파라미터로 전달한다
        // memo 삭제
        memoRepository.delete(memo);
        return id;

    } // deleteMemo

    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
        );  // 접근 제어자 주의하기
    }

}
