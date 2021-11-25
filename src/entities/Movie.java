package entities;

import entertainment.Genre;
import fileio.MovieInputData;

import java.util.ArrayList;

public class Movie extends Video {
    private int duration;
    public Movie(MovieInputData movie) {
        super(movie);
        duration = movie.getDuration();
    }

    public int getDuration() {
        return duration;
    }
}
