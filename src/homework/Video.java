package homework;

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

    public boolean hasFilters(Queries q) {
        int genreCount = 0;
        if (q.getFilters().get(0).get(0) != null) {
            if (Integer.parseInt(q.getFilters().get(0).get(0)) != this.getReleaseYear()) {
                return false;
            }
        }
        if (q.getFilters().get(1).get(0) != null ) {
            for (String filterGenre : q.getFilters().get(1)) {
                for (String videoGenre : this.getGenres()) {
                    if (filterGenre.equals(videoGenre)) {
                        genreCount++;
                        break;
                    }
                }
            }
            if (genreCount != q.getFilters().get(1).size()) {
                return false;
            }
        }
        return true;
    }

    public Integer getNumberOfFavorites(Database db) {
        Integer result = 0;
        for (User user : db.getUserMap().values()) {
            for (String videoTitle : user.getFavoriteVideos()) {
                if (this.getTitle().equals(videoTitle)) {
                    result++;
                    break;
                }
            }
        }
        return result;
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
