package com.bestbenefits.takoyaki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserNotRestController {
    @GetMapping("/oauth_example") void oauth_example() {}
    @GetMapping("/oauth") void oauth() {}
    @GetMapping("/nickname") void nickname() {}
}
