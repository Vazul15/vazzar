package com.vazzarmoviedb.radarrbackend.controller;

import org.springframework.expression.spel.ast.Indexer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/indexer")
public class IndexerController {

    @PostMapping("/add-indexer")
    public String addIndexer(@RequestBody String indexer) {

    }
}
