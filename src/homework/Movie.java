package homework;

import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.List;

public final class Movie extends Video {
    private final int duration;
    private final List<Double> ratings;
    public Movie(final MovieInputData movie) {
        super(movie);
        duration = movie.getDuration();
        ratings = new ArrayList<>();
    }

    /**
     * The rating of a movie is the average of all the ratings.
     * Method sums all ratings then divides the sum by the number of ratings.
     * @return rating of movie
     */
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
