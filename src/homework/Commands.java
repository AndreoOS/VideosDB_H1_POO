package homework;

import fileio.ActionInputData;

import java.util.ArrayList;

public class Commands {
    private String username;
    private String videoTitle;
    public Commands(ActionInputData action) {
        this.username = action.getUsername();
        this.videoTitle = action.getTitle();
    }

    public void view (String username, String videoTitle, Database db) {
        if (!db.getUserMap().get(username).getHistory().containsKey(videoTitle)) {
            db.getUserMap().get(username).getHistory().put(videoTitle, 1);
        } else {
            int views = db.getUserMap().get(username).getHistory().get(videoTitle);
            db.getUserMap().get(username).getHistory().put(videoTitle, views + 1);
        }
    }
    public String favorite(String username, String videoTitle, Database db) {
        if (db.getUserMap().get(username).getHistory().containsKey(videoTitle)) {
            for (String movieTitle : db.getUserMap().get(username).getFavoriteVideos()) {
                if (movieTitle.equals(videoTitle)) {
                    // este deja in lista de favorite
                    return "error -> " + videoTitle + " is already in favourite list";
                }
            }
            db.getUserMap().get(username).getFavoriteVideos().add(videoTitle);
            // a fost adaugat cu succes in lista de favorite
            return "success -> " + videoTitle + " was added as favourite";
        }
        // nu a fost vizionat video-ul
        return "error -> " + videoTitle + " is not seen";

    }

    public String rateMovie(String username, String videoTitle, Double rating, Database db) {
        if(db.getUserMap().get(username).getHistory().containsKey(videoTitle)) {
            if(db.getUserMap().get(username).getRatedMovies().contains(videoTitle)) {
                return "error -> " + videoTitle + " has been already rated";
            } else {
                db.getMovieMap().get(videoTitle).getRatings().add(rating);
                db.getUserMap().get(username).getRatedMovies().add(videoTitle);
                return "success -> " + videoTitle + " was rated with " + rating + " by " + username;
            }
        }
        return "error -> " + videoTitle + " is not seen";
    }

    public String rateSerial(String username, String videoTitle, Double rating, Integer seasonNum, Database db) {
        if(db.getUserMap().get(username).getHistory().containsKey(videoTitle)) {
            if (db.getUserMap().get(username).getRatedSerials().containsKey(videoTitle)) {
                 if (db.getUserMap().get(username).getRatedSerials().get(videoTitle).contains(seasonNum)) {
                     return "error -> " + videoTitle + " was already rated";
                 }
            }
            db.getSerialMap().get(videoTitle).getSeasons().get(seasonNum - 1).getRatings().add(rating);
            if (!db.getUserMap().get(username).getRatedSerials().containsKey(videoTitle)) {
                db.getUserMap().get(username).getRatedSerials().put(videoTitle, new ArrayList<>());
                db.getUserMap().get(username).getRatedSerials().get(videoTitle).add(seasonNum);
            } else {
                db.getUserMap().get(username).getRatedSerials().get(videoTitle).add(seasonNum);
            }
            return "success -> " + videoTitle + " was rated with " + rating + " by " + username;
        }
        return "error -> " + videoTitle + " is not seen";
    }

    public String getUsername() {
        return username;
    }

    public String getVideoTitle() {
        return videoTitle;
    }
}
