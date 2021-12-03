package homework;

import fileio.ShowInput;

import java.util.ArrayList;

public class Video {
    private final String title;
    private final int releaseYear;
    private final ArrayList<String> cast;
    private final ArrayList<String> genres;

    public Video(final ShowInput video) {
        title = video.getTitle();
        releaseYear = video.getYear();
        cast = video.getCast();
        genres = video.getGenres();
    }

    /**
     * Method checks for all the filters in query q. First checks if the year is correct,
     * then checks if the video is the correct genre.
     * @param q query that contains the filters needed to be found.
     * @return boolean value representing if the video has all the filters in query q
     */
    public final boolean hasFilters(final Query q) {
        int genreCount = 0;
        if (q.getFilters().get(0).get(0) != null) { // year check
            if (Integer.parseInt(q.getFilters().get(0).get(0)) != this.getReleaseYear()) {
                return false;
            }
        }
        if (q.getFilters().get(1).get(0) != null) { // genre check, iterating through genre list
            for (String filterGenre : q.getFilters().get(1)) {
                for (String videoGenre : this.getGenres()) {
                    if (filterGenre.equals(videoGenre)) {
                        genreCount++;
                    }
                }
            }
            return genreCount == q.getFilters().get(1).size();
        }
        return true;
    }

    /**
     * The method goes through all the users in database and counts how many times the video
     * is found in the users' favorite lists.
     * @param db the database that contains all the users
     * @return the number of times the video is in the users' favorite lists
     */
    public final Integer getNumberOfFavorites(final Database db) {
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

    /**
     * Method iterates through all the users and, if the video is in the history map of the user,
     * then add the views from that map to a sum. In the end return the sum.
     * @param db the database that contains all the users
     * @return number of views for a video
     */
    public final Integer getViews(final Database db) {
        int totalViews = 0;
        for (User user : db.getUserMap().values()) {
            if (user.getHistory().containsKey(this.getTitle())) {
                totalViews = totalViews + user.getHistory().get(this.getTitle());
            }
        }
        return totalViews;
    }

    public final String getTitle() {
        return title;
    }

    public final int getReleaseYear() {
        return releaseYear;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }
}
