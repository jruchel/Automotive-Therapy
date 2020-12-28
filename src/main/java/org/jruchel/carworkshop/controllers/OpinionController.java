package org.jruchel.carworkshop.controllers;

import org.jruchel.carworkshop.entities.Opinion;
import org.jruchel.carworkshop.services.OpinionService;
import org.jruchel.carworkshop.validation.ValidationErrorPasser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/opinions")
public class OpinionController {

    private final OpinionService opinionService;
    private final ValidationErrorPasser errorPasser;

    public OpinionController(OpinionService opinionService) {
        this.opinionService = opinionService;
        this.errorPasser = ValidationErrorPasser.getInstance();
    }

    @GetMapping("/get")
    public ResponseEntity<List<Opinion>> getRandomOpinions(@RequestParam(required = false, defaultValue = "5") int amount) {
        return new ResponseEntity<>(opinionService.getRandomOpinions(amount), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<String> addOpinion(@RequestBody Opinion opinion) {
        try {
            if (opinion.getName() == null || opinion.getName().isEmpty()) {
                opinion.setName("Anonim");
            }
            opinionService.save(opinion);
            return new ResponseEntity<>("Ocena została wysłana", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(errorPasser.getMessagesAsString(), HttpStatus.CONFLICT);
        }
    }

}
