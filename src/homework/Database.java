package homework;

import common.Constants;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.SerialInputData;
import fileio.MovieInputData;
import fileio.UserInputData;
import fileio.Writer;

import fileio.Input;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public final class Database {
    private Input input;
    private final HashMap<String, Movie> movieMap;
    private final HashMap<String, Serial> serialMap;
    private final HashMap<String, User> userMap;
    private final HashMap<String, Actor> actorMap;

    private Database() {
        this.movieMap = new LinkedHashMap<>();
        this.serialMap = new LinkedHashMap<>();
        this.userMap = new LinkedHashMap<>();
        this.actorMap = new LinkedHashMap<>();
    }

    public Database(final Input input) {
        this();
        this.input = input;
        populateDB(input);
    }
    private void populateDB(final Input theInput) {
        for (ActorInputData a : theInput.getActors()) {
            this.actorMap.put(a.getName(), new Actor(a));
        }
        for (UserInputData u : theInput.getUsers()) {
            this.userMap.put(u.getUsername(), new User(u));
        }
        for (MovieInputData m : theInput.getMovies()) {
            this.movieMap.put(m.getTitle(), new Movie(m));
        }
        for (SerialInputData s : theInput.getSerials()) {
            this.serialMap.put(s.getTitle(), new Serial(s));
        }
    }

    public Input getInput() {
        return input;
    }


    /**
     * Method represents the start of the program. Goes through every input in database
     * and calls methods depending on the type of input.
     * @param fileWriter for writing in files
     * @param arrayResult for output
     */
    @SuppressWarnings("unchecked")
    public void start(final Writer fileWriter, final JSONArray arrayResult) throws IOException {
        for (ActionInputData action : this.input.getCommands()) {
            switch (action.getActionType()) {
                case Constants.COMMAND:
                    Command command = new Command(action);
                    if (Objects.equals(action.getType(), "view")) {
                        command.view(command.getUsername(), command.getVideoTitle(), this);
                        arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                "success -> " + action.getTitle()
                                        + " was viewed with total views of "
                                        + this.userMap.get(action.getUsername())
                                        .getHistory().get(action.getTitle())));
                    }

                    if (Objects.equals(action.getType(), "favorite")) {
                        arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                command.favorite(command.getUsername(),
                                        command.getVideoTitle(), this)));
                    }

                    if (Objects.equals(action.getType(), "rating")) {
                        if (action.getSeasonNumber() != 0) {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    command.rateSerial(command.getUsername(),
                                            command.getVideoTitle(), action.getGrade(),
                                            action.getSeasonNumber(), this)));
                        } else {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    command.rateMovie(command.getUsername(),
                                            command.getVideoTitle(), action.getGrade(), this)));
                        }

                    }
                    break;
                case Constants.QUERY:
                    Query query = new Query(action);
                    if (Objects.equals(query.getObjectType(), Constants.ACTORS)) {
                        switch (query.getCriteria()) {
                            case "average" -> arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), "",
                                    "Query result: "
                                            + query.averageQuery(this)));
                            case "awards" -> arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), "",
                                    "Query result: "
                                    + query.awardQuery(this)));
                            case "filter_description" -> arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(),
                                    "", "Query result: "
                                    + query.filterQuery(this)));
                            default -> throw new IllegalStateException("Unknown criteria");
                        }
                    } else if (Objects.equals(query.getObjectType(), Constants.MOVIES)) {
                        switch (query.getCriteria()) {
                            case "ratings" -> arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), "",
                                    "Query result: "
                                    + query.movieRatingQuery(this)));
                            case "favorite" -> arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), "",
                                    "Query result: "
                                    + query.favoriteMovieQuery(this)));
                            case "longest" -> arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), "",
                                    "Query result: "
                                    + query.longestMovieQuery(this)));
                            case "most_viewed" -> arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), "",
                                    "Query result: "
                                    + query.mostViewedMovieQuery(this)));
                            default -> throw new IllegalStateException("Unknown criteria");
                        }
                    } else if (Objects.equals(query.getObjectType(), Constants.SHOWS)) {
                        switch (query.getCriteria()) {
                            case "ratings" -> arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), "",
                                    "Query result: "
                                            + query.showRatingQuery(this)));
                            case "favorite" -> arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), "",
                                    "Query result: "
                                            + query.favoriteShowQuery(this)));
                            case "longest" -> arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), "",
                                    "Query result: "
                                            + query.longestShowQuery(this)));
                            case "most_viewed" -> arrayResult.add(fileWriter
                                    .writeFile(action.getActionId(), "",
                                    "Query result: "
                                            + query.mostViewedShowQuery(this)));
                            default -> throw new IllegalStateException("Unknown criteria");
                        }
                    } else if (Objects.equals(query.getObjectType(), "users")) {
                        arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                "Query result: " + query.activeUsersQuery(this)));
                    }

                    break;
                case Constants.RECOMMENDATION:
                    Recommendation recommendation = new Recommendation(action);
                    if (Objects.equals(recommendation.getType(), "standard")) {
                        if (recommendation.standardRec(this) == null) {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    "StandardRecommendation cannot be applied!"));
                        } else {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    "StandardRecommendation result: "
                                            + recommendation.standardRec(this)));
                        }
                    } else if (Objects.equals(recommendation.getType(), "best_unseen")) {
                       if (recommendation.bestUnseenRec(this) == null) {
                           arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                   "BestRatedUnseenRecommendation cannot be applied!"));
                       } else {
                           arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                   "BestRatedUnseenRecommendation result: "
                                           + recommendation.bestUnseenRec(this)));
                       }
                    } else if (Objects.equals(recommendation.getType(), "favorite")) {
                        if (recommendation.favoriteRec(this) == null) {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    "FavoriteRecommendation cannot be applied!"));
                        } else {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    "FavoriteRecommendation result: "
                                            + recommendation.favoriteRec(this)));
                        }
                    } else if (Objects.equals(recommendation.getType(), "search")) {
                        if (recommendation.searchRec(this) == null) {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    "SearchRecommendation cannot be applied!"));
                        } else {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    "SearchRecommendation result: "
                                            + recommendation.searchRec(this)));
                        }
                    } else if (Objects.equals(recommendation.getType(), "popular")) {
                        if (recommendation.popularityRec(this) == null) {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    "PopularRecommendation cannot be applied!"));
                        } else {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    "PopularRecommendation result: "
                                            + recommendation.popularityRec(this)));
                        }
                    }
                default:
                    break;
            }
        }
    }

    public HashMap<String, Movie> getMovieMap() {
        return movieMap;
    }

    public HashMap<String, Serial> getSerialMap() {
        return serialMap;
    }

    public HashMap<String, User> getUserMap() {
        return userMap;
    }

    public HashMap<String, Actor> getActorMap() {
        return actorMap;
    }
}
