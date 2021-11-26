package homework;

import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.List;

public class Movie extends Video {
    private int duration;
    private List<Double> ratings;
    public Movie(MovieInputData movie) {
        super(movie);
        duration = movie.getDuration();
        ratings = new ArrayList<>();
    }

    public int getDuration() {
        return duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }
}
