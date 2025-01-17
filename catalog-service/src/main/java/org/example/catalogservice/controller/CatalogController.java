package org.example.catalogservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.catalogservice.jpa.CatalogEntity;
import org.example.catalogservice.service.CatalogService;
import org.example.catalogservice.vo.ResponseCatalog;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CatalogController {
    private final Environment env;
    private final CatalogService catalogService;

    @GetMapping("/health_check")
    public String healthCheck() {
        return String.format("It's Working in Catalog Service on PORT %s", env.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getUsers(){
        Iterable<CatalogEntity> userList = catalogService.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();
        userList.forEach(user->{
            result.add(new ModelMapper().map(user,ResponseCatalog.class));
        });

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
