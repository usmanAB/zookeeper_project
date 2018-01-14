package fr.esipe.usman.controller;

import fr.esipe.usman.models.Score;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/taxapi")
public class TaxRest {

    private static final double TVA = 0.2;

    @RequestMapping(value = "/ttc", method = RequestMethod.GET)
    public double add(@RequestParam("ht") double ht) {
        return ht * (1 + TVA);
    }

    @RequestMapping(value = "/count/{number}", method = RequestMethod.GET)
    public int score(@PathVariable("number") int num) {
        int temp = 0;
        for(int i=0;i< Score.getSize() ; i++){
            Score.getScoreList().get(i).setCounter(num);
            temp = Score.getScoreList().get(i).getCounter();
        }
        return temp;
    }

}
