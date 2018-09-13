package com.dickson.job.ads

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RootPathController {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

}