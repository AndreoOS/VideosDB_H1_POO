package homework;

import actor.ActorsAwards;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public final class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;

    public Actor(final ActorInputData actor) {
        name = actor.getName();
        careerDescription = actor.getCareerDescription();
        filmography = actor.getFilmography();
        awards = actor.getAwards();
    }

    /**
     * Goes through the awards map and sums up all the awards
     * @return number of awards for an actor
     */
    public Integer getNumOfAwards() {
        Integer result = 0;
        for (Map.Entry<ActorsAwards, Integer> entry : awards.entrySet()) {
            result = result + entry.getValue();
        }
        return result;
    }

    /**
     * Method goes through every film and movie in Database. For every
     * video that has the actor in it, it calculates the rating and
     * adds it to a sum, then increments a variable that counts the
     * number of videos. In the end we divide the sum by the number of videos
     * to get the average.
     * @param filmMap all the films in Database
     * @param serialMap all the shows in Database
     * @return a Double representing the average rating for the actor
     */
    public Double getAverage(final HashMap<String, Movie> filmMap,
                             final HashMap<String, Serial> serialMap) {
        Double average = 0.0;
        int videosOnDatabase = 0;
        for (Movie movie : filmMap.values()) {
            if (movie.getCast().contains(this.getName()) && movie.getMovieRating() > 0) {
                average = average + movie.getMovieRating();
                videosOnDatabase++;
            }
        }
        for (Serial serial : serialMap.values()) {
            if (serial.getCast().contains(this.getName()) && serial.getSerialRating() > 0) {
                average = average + serial.getSerialRating();
                videosOnDatabase++;
            }
        }
        if (videosOnDatabase != 0) {
            average = average / videosOnDatabase;
        }
        return average;
    }


    /**
     * Method goes through every word filter in query and checks using
     * method checkFilter if the word is present in the actor's description
     * If the word is present a counter is incremented.
     * Lastly, check if the counter is equal to the number of filters in query
     * @param actor the actor we need to check for filters
     * @param q query used for getting the filters needed
     * @return boolean value depending if the actor has the required filters
     */
    public boolean hasFilters(final Actor actor, final Query q) {
        int count = 0; // numara cate filtre are actor din filter_description
        for (String filter : q.getFilters().get(2)) {
            if (checkFilter(filter, actor.getCareerDescription())) {
                count++;
            }
        }
        return count == q.getFilters().get(2).size();
    }

    /**
     * Method splits the actor's description into words, then iterates
     * through these words and compares them to the filter.
     * @param filter the word we need to find in actor's description
     * @param origin actor's description
     * @return boolean value representing whether the word exists in
     * the actor's description or not.
     */
    private boolean checkFilter(final String filter, final String origin) {
        String[] words = origin.split("\\W+");
        for (String word : words) {
            if (filter.equals(word.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method iterates through every award of actor and checks for the awards in query
     * If an award is found, then a counter is incremented.
     * At the end the counter is compared to the number of awards in query. If the counter
     * is equal to this number, then the actor has all the awards of query and method returns true
     * @param actor actor to be checked
     * @param q query which has all the awards we need to check for.
     * @return boolean value representing if the actor has all the awards in query q.
     */
    public boolean hasAward(final Actor actor, final Query q) {
        int count = 0; // Numara cate premii din filters are actorul
        for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
            for (String award : q.getFilters().get(2 + 1)) {
                if (entry.getKey().toString().equals(award)) {
                    count++;
                }
            }
        }
        return count == q.getFilters().get(2 + 1).size();
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
