package com.sparta.memo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
// JAP 엔티티 클래스들이 해당 출생 클래스들을 상속할 경우, 추상 클래스의 멤버 변수를 컬럼으로 인식함 (자바에서의 상속과 똑같은 개념)
@MappedSuperclass
// 해당 클래스의 어우디팅 기능을 추가해줌 (자동으로 시간을 가져오는 기능 추가)
@EntityListeners(AuditingEntityListener.class)
// 추상클래스가 아니어도 상관없음
// 굳이 추상을 하는 이유는 ? 다른 엔티티 클래스에 상속하기 위해서 만들어진 클래스인데,
// 이 자체를 엔티티 클래스로 사용할 일이 없이 때문에
public abstract class Timestamped {


    @CreatedDate
    @Column(updatable = false)        // 이후에는 값이 수정되지 않도록 막는 옵션
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column                           // 변경된 시간 값이 자동으로 저장됨
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;

    // Date: 2023-01-01
    // Time: 20:21:14
    // TimeStamp : 2023-01-01 20:21:14.9999909
}
