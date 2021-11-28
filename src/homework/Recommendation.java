package homework;

import fileio.ActionInputData;

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
}
