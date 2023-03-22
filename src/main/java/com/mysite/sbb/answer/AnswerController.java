package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final QuestionService questionService;
    @GetMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable Integer id, @RequestParam String content) {
        Question question = questionService.getQuestion(id);

        // TODO: 답변 객체를 만든다
        return "redirect:/question/detail/%d".formatted(id);
    }

}
