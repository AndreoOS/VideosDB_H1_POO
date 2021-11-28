package homework;

import actor.ActorsAwards;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;

    public Actor(ActorInputData actor) {
        name = actor.getName();
        careerDescription = actor.getCareerDescription();
        filmography = actor.getFilmography();
        awards = actor.getAwards();
    }

    private Integer getNumOfAwards() {
        Integer result = 0;
        for(Map.Entry<ActorsAwards, Integer> entry : awards.entrySet()) {
            result = result + entry.getValue();
        }
        return result;
    }

    public Double getAverage(HashMap<String, Movie> filmMap, HashMap<String, Serial> serialMap) {
        Double average = 0.0;
        int videosOnDatabase = 0;

        for (String videoTitle : filmography) {
            if (filmMap.containsKey(videoTitle) && filmMap.get(videoTitle).getMovieRating() > 0) {
                videosOnDatabase++;
                average = average + filmMap.get(videoTitle).getMovieRating();
            } else if (serialMap.containsKey(videoTitle) && serialMap.get(videoTitle).getSerialRating() > 0) {
                videosOnDatabase++;
                average = average + serialMap.get(videoTitle).getSerialRating();
            }
        }
        average = average / videosOnDatabase;
        return average;
    }

    public boolean hasFilters(Actor actor, Query q) {
        int count = 0; // numara cate filtre are actor din filter_description
        for(String filter : q.getFilters().get(2)) {
            if (actor.getCareerDescription().contains(filter.toLowerCase(Locale.ROOT))) {
                count++;
                break;
            }
        }
        if (count == q.getFilters().get(2).size()) {
            return true;
        }
        return false;
    }

    public boolean hasAward(Actor actor, Query q) {
        int count = 0; // Numara cate premii din filters are actorul
        for(Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
            for (String award : q.getFilters().get(3)) {
                if (entry.getKey().toString().equals(award)) {
                    count++;
                    break;
                }
            }
        }
        if (count == q.getFilters().get(3).size()) {
            return true;
        }
        return false;
    }


    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }
}
