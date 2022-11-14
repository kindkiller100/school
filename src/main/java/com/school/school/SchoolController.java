package com.school.school;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RestController
public class SchoolController {


    @GetMapping("/{name}/{age}/{strength}")
    public String home(
            @PathVariable String name,
            @PathVariable("age") Integer temp,
            @PathVariable(value = "strength", required = false) Integer strength
    ) {
        String s = strength == null ? "" : strength.toString();
        return String.format("Hello, %s!", name + "age= " + temp + s);
    }

//    @GetMapping("/{name}/{age}")
//    public String home1(
//            @PathVariable String name,
//            @PathVariable("age") Integer temp
//
//    ) {
//        return String.format("Hello, %s!", name + "age= " + temp);
//    }
}
