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

    public Double getMovieRating() {
        Double average = 0.0;
        for (Double rating : ratings) {
            average = average + rating;
        }
        if (!ratings.isEmpty()) {
            average = average / ratings.size();
        }
        return average;
    }


    public int getDuration() {
        return duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }
}
