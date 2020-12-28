package org.jruchel.carworkshop.services;

import org.jruchel.carworkshop.entities.Opinion;
import org.jruchel.carworkshop.repositories.OpinionRepository;
import org.jruchel.carworkshop.utils.random.MyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OpinionService {

    @Autowired
    private OpinionRepository opinionRepository;

    public Opinion findById(Integer id) {
        Optional<Opinion> optional = opinionRepository.findById(id);
        return optional.orElse(null);
    }

    public List<Opinion> getRandomOpinions(int amount) {
        List<Opinion> opinions = opinionRepository.getOpinions(amount * 2);
        while (opinions.size() > amount) {
            opinions.remove(MyRandom.getRandom(0, opinions.size()));
        }
        return opinions;
    }

    public void save(Opinion opinion) {
        opinionRepository.save(opinion);
    }
}