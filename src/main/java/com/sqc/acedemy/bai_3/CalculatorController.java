package com.sqc.acedemy.bai_3;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    @GetMapping
    public ResponseEntity<?> calculator(
        @RequestParam String firstNumber,
        @RequestParam String secondNumber,
        @RequestParam String operator){

        double num1,num2;

        try{
            num1= Double.parseDouble(firstNumber);
        }catch (NumberFormatException e){
            return ResponseEntity.badRequest().body("First number is not valid");
        }

        try{
            num2= Double.parseDouble(secondNumber);
        }catch (NumberFormatException e){
            return ResponseEntity.badRequest().body("Second number is not valid");
        }

        switch (operator){
            case "+":
                return ResponseEntity.ok(num1 + num2);
            case  "-":
                return ResponseEntity.ok(num1 - num2);
            case "*":
                return ResponseEntity.ok(num1 * num2);
            case  "/":
                if (num2 == 0){
                    return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Division by zero");}
                return ResponseEntity.ok(num1 / num2);
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Operator is not valid");
        }
    }
}
