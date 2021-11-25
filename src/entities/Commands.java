package entities;

import javax.xml.crypto.Data;

public class Commands {
    private String username;
    private String videoTitle;
    public Commands(String username, String videoTitle) {
        this.username = username;
        this.videoTitle = videoTitle;
    }

    public void view (String username, String videoTitle, Database db) {
        if (!db.getUserMap().get(username).getHistory().containsKey(videoTitle)) {
            db.getUserMap().get(username).getHistory().put(videoTitle, 1);
        } else {
            int views = db.getUserMap().get(username).getHistory().get(videoTitle);
            db.getUserMap().get(username).getHistory().put(videoTitle, views + 1);
        }
    }
    public int favorite(String username, String videoTitle, Database db) {
        int ok = 0; // nu a vizionat filmul videoTitle
        if (db.getUserMap().get(username).getHistory().containsKey(videoTitle)) {
            for (String movieTitle : db.getUserMap().get(username).getFavoriteMovies()) {
                if (movieTitle.equals(videoTitle)) {
                    ok = 1; // este deja in lista de favorite
                    return ok;
                }
            }
            db.getUserMap().get(username).getFavoriteMovies().add(videoTitle);
            ok = 2; // a fost adaugat cu succes in lista de favorite
            return ok;
        }
        return ok;

    }

    public String getUsername() {
        return username;
    }

    public String getVideoTitle() {
        return videoTitle;
    }
}
