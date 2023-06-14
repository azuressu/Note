package com.sparta.memo.repository;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class MemoRepository { // 마찬가지로 이름이 memoRepository로 저장됨

    private final JdbcTemplate jdbcTemplate;
    public MemoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 실제로 데이터를 저장하는 부분
    public Memo save(Memo memo) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";    // insert문은 항상 값이 바뀌게 될 것. 동적인 처리를 위한 ? 삽입
        jdbcTemplate.update( con -> { // insert, update, delete 모두 update 메소드를 사용하여 요청하게 됨
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, memo.getUsername()); // 첫 번째 물음표
                    preparedStatement.setString(2, memo.getContents()); // 두 번째 물음표
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        memo.setId(id);

        return memo; // 메모에다가 id 넣어주고 그대로 다시 반환하는 것
    }

    public List<MemoResponseDto> findAll() {
        // DB 조회
        String sql = "SELECT * FROM memo";

        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                return new MemoResponseDto(id, username, contents);    // 새로운 객체를 만듦
            }
        });
    }

    public void update(Long id, MemoRequestDto requestDto) {
        String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id); // 물음표의 순서대로 데이터를 넣어줌
    }


    public void delete(Long id) {
        String sql = "DELETE FROM memo WHERE id = ?"; // 물음표에 넣어주면 됨
        jdbcTemplate.update(sql, id);
    }


    // 중복되는 코드는 메소드로 따로 빼놓음 (데이터 베이스에 존재하는지 판별하는 기능)
    public Memo findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Memo memo = new Memo();
                memo.setUsername(resultSet.getString("username"));
                memo.setContents(resultSet.getString("contents"));
                return memo;
            } else {
                return null;
            }
        }, id);
    } // findById()

    @Transactional
    public Memo createMemo(EntityManager em) {
        // 1번을 하나 가져옴
        Memo memo = em.find(Memo.class, 1);
        // Transactional을 붙였으므로 Dirty Checking을 통해 Update 쿼리 실행을 예측해 볼 수 있음
        memo.setUsername("Robbiert");
        memo.setContents("@Transactional 전파 테스트 중! 2");

        System.out.println("createMemo 메서드 종료");
        // 부모 메서드에서 @Transactional, @Rollback(value = false)를 지워주면
        // 여기서 update를 실행하고 다시 부모로 돌아감
        return memo;
    }



}
