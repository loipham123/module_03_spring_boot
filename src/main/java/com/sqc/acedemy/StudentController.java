package com.sqc.acedemy;

import com.sqc.acedemy.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@RestController
@RequestMapping("/students")
public class StudentController {
    private final List<Student> students = new ArrayList<>(
    Arrays.asList(
            Student.builder().id(1).name("Lợi").score(2.0).age(22).build(),
            Student.builder().id(2).name("Vy").score(2.0).age(24).build(),
            Student.builder().id(3).name("Linh").score(2.0).age(26).build()
            )
    );
    @GetMapping
    public ResponseEntity<?> getStudents() {
        return ResponseEntity.ok(ApiResponse.builder().data(students).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getById(@PathVariable("id") Integer id) {
        for(Student student : students) {
            if(student.getId() == id) {
                //return ResponseEntity.status(HttpStatus.OK).body(student);
                return ResponseEntity.ok(ApiResponse.<Student>builder()
                        .data(student)
                        .build());
            }
        }
        throw new ApiException(ErrorCode.STUDENT_NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Student student) {
        student.setId((int) (Math.random() * 10000000) +1 );
        students.add(student);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder().data(student)
                .build());
    }
}
