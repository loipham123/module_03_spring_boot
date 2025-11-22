package com.sqc.acedemy.bai_2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {
    public static final Map<String,String> dictionary =Map.of(
            "hello","Xin chào",
            "apple","Táo",
            "goodbye","Tạm biệt",
            "good night","Chúc ngủ ngon"
    );
@GetMapping
    public ResponseEntity<?> getDictionary(@RequestParam String word) {

        String cleanWord = word.toLowerCase();

        if(dictionary.containsKey(word)) {
            return ResponseEntity.ok(dictionary.get(cleanWord));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy từ này trong từ điển");
        }
    }
}
