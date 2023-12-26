package com.mlc.controller;

import com.mlc.service.AppModuleService;
import com.mlc.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/v2/")
@RequiredArgsConstructor
public class ExcelController {

    private final AppModuleService appModuleService;

    private final ProductService productService;


    @Autowired
    public ExcelController(ProductService productService,AppModuleService appModuleService) {
        this.productService = productService;
        this.appModuleService=appModuleService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importExcel(@RequestParam("file")MultipartFile file) throws IOException {
        appModuleService.importModulesFromExcel(file);
        return ResponseEntity.ok("Excel import successfully");
    }

    @GetMapping("/export")
    public void generateExcelReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey="Content-Disposition";
        String headerValue="attachment;filename-products.xls";
        response.setHeader(headerKey,headerValue);
        productService.generateExcel(response);
    }
}



