package com.archer.wfd.controller;

import com.archer.wfd.repository.StudentRepository;
import com.archer.wfd.repository.entity.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * /-------------------------------------------------------------------------\
 * |                                                                         |
 * |                  ***   ***           ***                                |
 * |                  ***   ***           ***                                |
 * |                  ***   ***           ***                         **     |
 * |                        ***           ***                        ***     |
 * |                        ***           ***                        ***     |
 * | ***    ***   *** ***   ***      **** ***     *****    *****   *******   |
 * | ***    ****  *** ***   ***    **********   *******   *******  *******   |
 * |  **   *****  **  ***   ***    **********   *******   *    *** *******   |
 * |  ***  *****  **  ***   ***   ****   ****  ****   *        ***   ***     |
 * |  ***  ** **  **  ***   ***   ***     ***  ***         *******   ***     |
 * |   **  ** ******  ***   ***   ***     ***  ***       *********   ***     |
 * |   *****  *****   ***   ***   ****   ****  ****   *  ***   ***   ***     |
 * |   *****  *****   ***   ***   ***********   *******  ***   ***   *****   |
 * |    ****   ****   ***   ***    ****** ***   *******  *********   *****   |
 * |    ***    ***    ***   ***     ****  ***     ****    **** ***    ****   |
 * |                                                                         |
 * \-------------------------------------------------------------------------/
 */
@RestController
public class HelloController {

    @Resource
    private StudentRepository studentRepository;

    @GetMapping("/hello")
    public String handle() {
        return "Hello WebFlux";
    }

    @GetMapping("/getAllStudent")
    public Flux<Student> hello2() {
        return studentRepository.findAll();
    }

    @GetMapping("/getOneStudent/{id}")
    public Mono<Student> getOneStudent(@PathVariable Long id) {
        return studentRepository.findById(id);
    }

    @GetMapping("/removeStudent/{id}")
    public Mono<Void> deleteStudent(@PathVariable Long id) {
        return studentRepository.deleteById(id);
    }

    @GetMapping("/modifyStudent/{id}/{name}")
    public Mono modifyStudent(@PathVariable Long id, @PathVariable String name) {
        return studentRepository.findById(id)
                .flatMap(student -> {
                    student.setName(name);
                    return studentRepository.save(student);
                })
                .map(updateStudent -> new ResponseEntity<>(updateStudent, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/addStudent")
    public Mono addStudent(@RequestBody StudentRequest studentRequest) {
        //这里有问题，需要看Mono的具体使用(目前的现象是能够保存到数据库中，但是会出现错误)
        System.out.println(studentRequest);
        Student student = new Student(studentRequest.getName(), studentRequest.getMemo());
        return studentRepository.save(student).thenReturn(student);
//        return studentRepository.selfSave(studentRequest.getName(), studentRequest.getMemo());
    }
}
