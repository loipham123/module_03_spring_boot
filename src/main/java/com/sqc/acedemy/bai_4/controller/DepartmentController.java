package com.sqc.acedemy.bai_4.controller;

import com.sqc.acedemy.bai_4.entity.Department;
import com.sqc.acedemy.bai_4.service.IDepartmentService;
import com.sqc.acedemy.bai_4.service.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
    @RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    private IDepartmentService service;

    @GetMapping
    public Object getAll() {
        return JsonResponse.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public Object getById(@PathVariable Integer id) {
        return JsonResponse.ok(service.getById(id));
    }

    @PostMapping
    public Object create(@RequestBody Department department) {
        return JsonResponse.created(service.create(department));
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable Integer id, @RequestBody Department dep) {
        return JsonResponse.ok(service.update(id, dep));
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Integer id) {
        service.delete(id);
        return JsonResponse.noContent();
    }
}
