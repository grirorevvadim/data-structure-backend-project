package com.example.datastructurebackendproject;

import com.example.datastructurebackendproject.model.BasicDocument;
import com.example.datastructurebackendproject.model.Document;
import com.example.datastructurebackendproject.model.textgen.MarkovTextGeneratorLoL;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/get-data")
@CrossOrigin
public class Controller {
    MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));

    @GetMapping(path = "/{text}")
    public String getAll(@PathVariable String text) {
        System.out.println(text);
        Document doc = new BasicDocument(text);
        return String.format("%.2f", doc.getFleschScore());
    }

    @PostMapping ("/train/{text}")
    public Double train(@PathVariable String text) {
        System.out.println(text);
        gen.retrain(text);
        return null;
    }

    @GetMapping("/markov/{num}")
    public String generateText(@PathVariable int num) {
        System.out.println(num);
        String text = gen.generateText(num);
        System.out.println(text);
        return text;
    }
}
