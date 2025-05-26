package com.jovan.erp_v1.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovan.erp_v1.service.IHelpService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/help")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class HelpController {

    private final IHelpService helpService;

}
