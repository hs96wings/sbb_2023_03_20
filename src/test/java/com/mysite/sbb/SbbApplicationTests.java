package com.mysite.sbb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;

	@BeforeEach // 아래 메서드는 각 테스트케이스가 실행되기 전에 실행된다
	void beforeEach() {
		answerRepository.deleteAll();
		answerRepository.clearAutoIncrement();

		// DELETE FROM question;
		questionRepository.deleteAll();

		questionRepository.clearAutoIncrement();

		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);

		// 답변 1개 생성
		Answer a1 = new Answer();
		a1.setContent("네 자동으로 생성됩니다");
		q2.addAnswer(a1);
		a1.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a1);
	}


	@Test
	void t001() {
		// 질문 1개 생성
		Question q  = new Question();
		q.setSubject("세계에서 가장 부유한 국가가 어디인가요?");
		q.setContent("알고 싶습니다");
		q.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q);

		assertEquals("세계에서 가장 부유한 국가가 어디인가요?", questionRepository.findById(3).get().getSubject());
	}

	/* SQL
	 * SELECT * FROM question
	 */
	@Test
	void t002() {
		List<Question> all = questionRepository.findAll();
		assertEquals(2, all.size());
	}


	/* SQL
	 * SELECT * FROM question
	 * WHERE id = 1
	 */
	@Test
	void t003() {
		Optional<Question> oq = questionRepository.findById(1);

		if (oq.isPresent()) {
			Question q = oq.get();
			assertEquals("sbb가 무엇인가요?", q.getSubject());

		}
	}

	/* SQL
	 * SELECT * FROM question
	 * WHERE subject = 'sbb가 무엇인가요?'
	 */
	@Test
	void t004() {
		Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals(1, q.getId());
	}

	@Test
	@DisplayName("findBySubjectAndContent")
	void t005() {
		Question q = questionRepository.findBySubjectAndContent(
				"sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다"
		);
		assertEquals(1, q.getId());
	}

	@Test
	@DisplayName("findBySubjectLike")
	void t006() {
		List<Question> qList = questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	/*
	 * UPDATE question
	 * SET
	 * 	content = ?,
	 * 	create_date = ?,
	 * 	subject = ?
	 * WHERE
	 * 	id = ?
	 */

	@Test
	@DisplayName("데이터 수정하기")
	void t007() {
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		questionRepository.save(q);
	}

	/*
	 * DELETE FROM question
	 * WHERE id = ?
	 */
	@Test
	@DisplayName("데이터 삭제하기")
	void t008() {
		// questionRepository.count()
		// SQL : SELECT COUNT(*) FROM question;
		assertEquals(2, questionRepository.count());
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		questionRepository.delete(q);
		assertEquals(1, questionRepository.count());
	}

	@Test
	void t009() {
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		/*
		 * v1
		 * Optional<Question> oq = questionRepository.findById(2);
		 * Question q = oq.get();
		 */

		/*
		 * v2
		 * Question q = questionRepository.findById(2).get();
		 */

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
		a.setCreateDate(LocalDateTime.now());
		answerRepository.save(a);
	}

	@Test
	@DisplayName("답변 조회하기")
	void t010() {
		Optional<Answer> oa = answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());
	}
}
