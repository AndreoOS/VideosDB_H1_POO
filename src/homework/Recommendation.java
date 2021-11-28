package homework;

import fileio.ActionInputData;

import java.util.ArrayList;
import java.util.List;

public class Recommendation {
    private final String type;
    private final String username;
    private final String genre;

    public Recommendation(ActionInputData action) {
        type = action.getType();
        username = action.getUsername();
        genre = action.getGenre();
    }

    public String standardRec(Database db) {
        User user = db.getUserMap().get(username);
        for (Movie movie : db.getMovieMap().values()) {
            if (!user.getHistory().containsKey(movie.getTitle())) {
                return movie.getTitle();
            }
        }
        for (Serial serial : db.getSerialMap().values()) {
            if (!user.getHistory().containsKey(serial.getTitle())) {
                return serial.getTitle();
            }
        }
        return null;
    }

    public String bestUnseenRec(Database db) {
        User user = db.getUserMap().get(username);
        List<Video> videoList = new ArrayList<>();
        for (Movie movie : db.getMovieMap().values()) {
            if (!user.getHistory().containsKey(movie.getTitle())) {
                videoList.add(movie);
            }
        }
        for (Serial serial : db.getSerialMap().values()) {
            if (!user.getHistory().containsKey(serial.getTitle())) {
                videoList.add(serial);
            }
        }

        return null;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getGenre() {
        return genre;
    }
}
