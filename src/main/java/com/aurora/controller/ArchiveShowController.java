package com.aurora.controller;

import com.aurora.queryvo.BlogQuery;
import com.aurora.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Description: 时间轴控制器
 */
@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model) {
        List<BlogQuery> blogs = blogService.getAllBlog();
        model.addAttribute("blogs", blogs);
        return "archives";
    }
}
