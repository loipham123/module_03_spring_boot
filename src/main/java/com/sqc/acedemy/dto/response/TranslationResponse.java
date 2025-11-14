package com.sqc.acedemy.dto.response;

public class TranslationResponse {
    private String englishWord;
    private String vietnameseWord;

    public TranslationResponse() {
    }
    public TranslationResponse(String englishWord, String vietnameseWord) {
        this.englishWord = englishWord;
        this.vietnameseWord = vietnameseWord;
    }
    public String getEnglishWord() {
        return englishWord;
    }
    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }
}
