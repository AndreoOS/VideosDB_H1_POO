package homework;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteVideos;
    private final ArrayList<String> ratedMovies;
    private final Map<String, ArrayList<Integer>> ratedSerials;


    public User(final UserInputData user) {
        this.username = user.getUsername();
        this.subscriptionType = user.getSubscriptionType();
        this.history = user.getHistory();
        this.favoriteVideos = user.getFavoriteMovies();
        this.ratedMovies = new ArrayList<>();
        this.ratedSerials = new HashMap<>();
    }

    /**
     * Method iterates through all the videos in database and adds to the list the ones
     * that are not found in user's history
     * @param db the database used for getting all the movies and shows
     * @return list of videos that the user hasn't seen yet.
     */
    public List<Video> getUnseenVids(final Database db) {
        List<Video> videoList = new ArrayList<>();
        for (Movie movie : db.getMovieMap().values()) {
            if (!this.getHistory().containsKey(movie.getTitle())) {
                videoList.add(movie);
            }
        }
        for (Serial serial : db.getSerialMap().values()) {
            if (!this.getHistory().containsKey(serial.getTitle())) {
                videoList.add(serial);
            }
        }
        return videoList;
    }

    /**
     * Method adds to a list all the unseen videos that have the specific genre.
     * @param db the database with all the videos
     * @param genre genre for the videos
     * @return list of videos that the user hasn't seen yet from a specific genre
     */
    public List<Video> getUnseenVidsByGenre(final Database db, final String genre) {
        List<Video> videoList = new ArrayList<>();
        for (Movie movie : db.getMovieMap().values()) {
            if (!this.getHistory().containsKey(movie.getTitle())
                    && movie.getGenres().contains(genre)) {
                videoList.add(movie);
            }
        }
        for (Serial serial : db.getSerialMap().values()) {
            if (!this.getHistory().containsKey(serial.getTitle())
                    && serial.getGenres().contains(genre)) {
                videoList.add(serial);
            }
        }
        return videoList;
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
