package com.aphisiit.spring_temporal.controller

import com.aphisiit.spring_temporal.service.SayHelloService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hello")
class SayHelloController(
    private val sayHelloService: SayHelloService
) {

    @GetMapping("/greeting")
    fun greeting(@RequestParam("name") name: String): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("message" to sayHelloService.sayHello(name)))
    }
}