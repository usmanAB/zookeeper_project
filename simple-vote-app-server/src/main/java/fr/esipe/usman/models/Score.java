package fr.esipe.usman.models;

import java.util.ArrayList;
import java.util.List;

public class Score {

    public Score(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int count) {
        this.counter = this.counter+count;
    }

    public static int getSize(){
        return scoreList.size();

    }

    private int counter;


    public static List<Score> getScoreList() {
        return scoreList;
    }

    public static void setScoreList(List<Score> scoreList) {
        Score.scoreList = scoreList;
    }

    private static List<Score> scoreList;
    static{
        scoreList = new ArrayList<Score>();
        scoreList.add(new Score(15));
    }
}
