package entities;

import entertainment.Genre;
import fileio.ShowInput;

import java.util.ArrayList;

public class Video {
    private String title;
    private int releaseYear;
    private ArrayList<String> cast;
    private ArrayList<String> genres;

    public Video(ShowInput video) {
        title = video.getTitle();
        releaseYear = video.getYear();
        cast = video.getCast();
        genres = video.getGenres();
    }

    public String getTitle() {
        return title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }
}
