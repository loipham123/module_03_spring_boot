package com.sqc.acedemy.controller;


import com.sqc.acedemy.dto.request.TranslationRequest;
import com.sqc.acedemy.dto.response.TranslationResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/en-vi")
public class DictionaryController {
    @PostMapping("/en-vi")
    public TranslationResponse translate(@RequestBody TranslationRequest request) {
        String word = request.getWord();

        String translatedVietnamese = callExternalTranslationService(word);
        return new TranslationResponse(word, translatedVietnamese);
    }

    private String callExternalTranslationService(String word) {
        if("hello".equals(word)){
            return "xin chào";
        }
        return "Không tìm thấy từ này trong từ điển";
    }
}
