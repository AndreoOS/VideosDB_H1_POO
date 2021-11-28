package homework;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteVideos;
    private final ArrayList<String> ratedMovies;
    private final Map<String, ArrayList<Integer>> ratedSerials;


    public User(UserInputData user) {
        this.username = user.getUsername();
        this.subscriptionType = user.getSubscriptionType();
        this.history = user.getHistory();
        this.favoriteVideos = user.getFavoriteMovies();
        this.ratedMovies = new ArrayList<>();
        this.ratedSerials = new HashMap<>();
    }

    public Integer getNumberOfRatings() {
        return ratedMovies.size() + ratedSerials.size();
    }

    public String getUsername() {
        return username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public ArrayList<String> getFavoriteVideos() {
        return favoriteVideos;
    }

    public ArrayList<String> getRatedMovies() {
        return ratedMovies;
    }
    public Map<String, ArrayList<Integer>> getRatedSerials() {
        return ratedSerials;
    }

}
