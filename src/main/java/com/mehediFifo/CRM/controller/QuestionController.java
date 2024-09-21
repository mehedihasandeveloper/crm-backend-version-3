package com.mehediFifo.CRM.controller;

import com.mehediFifo.CRM.config.APIResponse;
import com.mehediFifo.CRM.entity.Question;
import com.mehediFifo.CRM.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"}, allowCredentials = "true")
public class QuestionController {

    @Autowired
    private QuestionService service;

    @PostMapping("/add")
    public List<Question> addQuestions(@RequestBody List<Question> questions) {
        return service.addQuestions(questions);
    }

    @GetMapping("/getAll")
    public List<Question> getAllQuestions() {
        return service.findAllQuestions();
    }

    @DeleteMapping("/deleteQuestion/{id}")
    public void removeQuestion(@PathVariable Long id){
        service.deleteQuestion(id);
    }

    @PutMapping("/updateQuestion")
    public Question updateQuestion(@RequestBody Question question) {
        return service.updateQuestion(question);
    }

    @GetMapping("/getQuestion/{id}")
    public Question findQuestionById(@PathVariable("id") Long id) {
        return service.getQuestionById(id);
    }

    @GetMapping("/getToSetLogic/{campaignId}")
    public List<Question> getToSetQuestions(@PathVariable("campaignId") String campaignId){
        return service.getToSetLogicQuestions(campaignId);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    public APIResponse<Page<Question>> getQuestionListWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        Page<Question> QuestionsWithPagination = service.findQuestionsWithPagination(offset, pageSize);
        return new APIResponse<>(QuestionsWithPagination.getSize(), QuestionsWithPagination);
    }

    @GetMapping("/searchQuestion")
    public List<Question> search(@RequestParam(value = "campaignId", required = false) String campaignId,
                              @RequestParam(value = "title", required = false) String title,
                              @RequestParam(value = "type", required = false) String type,
                              @RequestParam(value = "options", required = false) String options,
                              @RequestParam(value = "optionalValue", required = false) String optionalValue,
                              @RequestParam(value = "instruction", required = false) String instruction,
                              @RequestParam(value = "status", required = false) Boolean status) {
        return service.search(campaignId, title, type, options, optionalValue, instruction, status);
    }
}
