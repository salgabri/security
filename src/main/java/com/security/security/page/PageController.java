package com.security.security.page;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("page-api/page")
public class PageController {

    @GetMapping
    public String getPage() {
        return "page";
    }

}
