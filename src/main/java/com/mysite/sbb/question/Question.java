package com.mysite.sbb.question;

import com.mysite.sbb.answer.Answer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    // OneToMay 자바에서의 편의를 위해 필드 생성
    // 실제 DB 테이블에 Column이 생성되진 않음
    // DB는 배열이나 리스트를 저장할 수 없기에 Column으로 저장할 수 없음
    // 만들어도 되고 만들지 않아도 되지만
    // 만들게 되면 해당 객체(Question)에서 관련된 답변들을 찾을 때 유용
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE) // OneToMany에는 직접 객체 초기화
    @LazyCollection(LazyCollectionOption.EXTRA) // answerList.size() 함수가 실행될 때 SELECT COUNT 실행ㅎ
    private List<Answer> answerList = new ArrayList<>();

    public void addAnswer(Answer a) {
        a.setQuestion(this);
        answerList.add(a);
    }
}
