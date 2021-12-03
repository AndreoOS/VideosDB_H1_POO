package homework;

import fileio.ActionInputData;

import java.util.ArrayList;

public final class Command {
    private String username;
    private String videoTitle;
    public Command(final ActionInputData action) {
        this.username = action.getUsername();
        this.videoTitle = action.getTitle();
    }

    /**
     * Method uses the database to get the user with the name userName then adds the video to
     * the history of user if it wasn't seen before, or increments the views if it was seen before
     * @param userName the name of the user that will view the video
     * @param videoName the name of the video that the user will view
     * @param db database used to get the userMap
     */
    public void view(final String userName, final String videoName, final Database db) {
        if (!db.getUserMap().get(userName).getHistory().containsKey(videoName)) {
            db.getUserMap().get(userName).getHistory().put(videoName, 1);
        } else {
            int views = db.getUserMap().get(userName).getHistory().get(videoName);
            db.getUserMap().get(userName).getHistory().put(videoName, views + 1);
        }
    }

    /**
     * Method first checks if the user has seen the video. If not returns error message.
     * If the user has seen the video then checks if the video is already in the favorite list.
     * If the video is already in the list returns error message. If not, then adds the video
     * to the list and returns success message.
     * @param userName name of user
     * @param videoName name of video
     * @param db database with userMap
     * @return string for output to files
     */
    public String favorite(final String userName, final String videoName, final Database db) {
        if (db.getUserMap().get(userName).getHistory().containsKey(videoName)) {
            for (String movieTitle : db.getUserMap().get(userName).getFavoriteVideos()) {
                if (movieTitle.equals(videoName)) {
                    // este deja in lista de favorite
                    return "error -> " + videoName + " is already in favourite list";
                }
            }
            db.getUserMap().get(userName).getFavoriteVideos().add(videoName);
            // a fost adaugat cu succes in lista de favorite
            return "success -> " + videoName + " was added as favourite";
        }
        // nu a fost vizionat video-ul
        return "error -> " + videoName + " is not seen";

    }

    /**
     * Method adds to the movie's ratings the rating of the user and updates the user's
     * rated movies list with the name of the newly rated movie.
     * First it checks if the user has seen the movie. If he didn't see it returns error message
     * If he did see it then check if it has been rated before. If it was return error message.
     * If it wasn't rated then add the rating to the rating list of movie and the movie name to
     * the user's rated list.
     * @param userName name of user
     * @param videoName name of movie
     * @param rating rating of movie by user
     * @param db database with userMap, movieMap
     * @return string for output to files
     */
    public String rateMovie(final String userName, final String videoName,
                            final Double rating, final Database db) {
        if (db.getUserMap().get(userName).getHistory().containsKey(videoName)) {
            if (db.getUserMap().get(userName).getRatedMovies().contains(videoName)) {
                return "error -> " + videoName + " has been already rated";
            } else {
                db.getMovieMap().get(videoName).getRatings().add(rating);
                db.getUserMap().get(userName).getRatedMovies().add(videoName);
                return "success -> " + videoName + " was rated with " + rating + " by " + userName;
            }
        }
        return "error -> " + videoName + " is not seen";
    }

    /**
     * Method works similarly with rateMovie, except it adds the rating to a certain season.
     * @param userName name of user
     * @param videoName name of show
     * @param rating rating of show's season by user
     * @param seasonNum season to be rated
     * @param db database with userMap and serialMap
     * @return string for output to files
     */
    public String rateSerial(final String userName, final String videoName, final Double rating,
                             final Integer seasonNum, final Database db) {
        if (db.getUserMap().get(userName).getHistory().containsKey(videoName)) {
            if (db.getUserMap().get(userName).getRatedSerials().containsKey(videoName)) {
                 if (db.getUserMap().get(userName).getRatedSerials().get(videoName)
                         .contains(seasonNum)) {
                     return "error -> " + videoName + " has been already rated";
                 }
            }
            db.getSerialMap().get(videoName).getSeasons()
                    .get(seasonNum - 1).getRatings().add(rating);
            if (!db.getUserMap().get(userName).getRatedSerials().containsKey(videoName)) {
                db.getUserMap().get(userName).getRatedSerials().put(videoName, new ArrayList<>());
            }
            db.getUserMap().get(userName).getRatedSerials().get(videoName).add(seasonNum);
            return "success -> " + videoName + " was rated with " + rating + " by " + userName;
        }
        return "error -> " + videoName + " is not seen";
    }

    public String getUsername() {
        return username;
    }

    public String getVideoTitle() {
        return videoTitle;
    }
}
