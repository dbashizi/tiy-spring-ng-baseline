package com.tiy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameTrackerJSONController {

    @Autowired
    GameRepository games;


    @RequestMapping(path = "/games.json", method = RequestMethod.GET)
    public List<Game> getGames() {

        List<Game> gameList = new ArrayList<Game>();
        Iterable<Game> allGames = games.findAll();
        for (Game game : allGames) {
            gameList.add(game);
        }

        try {
            System.out.println("Catching a nap!");
            Thread.sleep(3000);
            System.out.println("Power naps are the best!!!");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return gameList;
    }

}
