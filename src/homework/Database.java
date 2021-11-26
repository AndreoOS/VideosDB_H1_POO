package homework;

import common.Constants;
import fileio.*;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class Database {
    private Input input;
    private HashMap<String, Movie> movieMap;
    private HashMap<String, Serial> serialMap;
    private HashMap<String, User> userMap;
    private HashMap<String, Actor> actorMap;

    private Database() {
        this.movieMap = new HashMap<>();
        this.serialMap = new HashMap<>();
        this.userMap = new HashMap<>();
        this.actorMap = new HashMap<>();
    }

    public Database(Input input) {
        this();
        this.input = input;
        populateDB(input);
    }
    private void populateDB(Input input) {
        for (ActorInputData a : input.getActors()) {
            this.actorMap.put(a.getName(), new Actor(a));
        }
        for (UserInputData u : input.getUsers()) {
            this.userMap.put(u.getUsername(), new User(u));
        }
        for (MovieInputData m : input.getMovies()) {
            this.movieMap.put(m.getTitle(), new Movie(m));
        }
        for (SerialInputData s : input.getSerials()) {
            this.serialMap.put(s.getTitle(), new Serial(s));
        }
    }

    public Input getInput() {
        return input;
    }

    public void start(Writer fileWriter, JSONArray arrayResult) throws IOException {
        for (ActionInputData action : this.input.getCommands()) {
            switch (action.getActionType()) {
                case Constants.COMMAND:
                    Commands command = new Commands(action.getUsername(), action.getTitle());
                    if (Objects.equals(action.getType(), "view")) {
                        command.view(command.getUsername(), command.getVideoTitle(), this);
                        arrayResult.add(fileWriter.writeFile(action.getActionId(), "", "success -> " +
                                action.getTitle() + " was viewed with total views of " +
                                this.userMap.get(action.getUsername()).getHistory().get(action.getTitle())));
                    }

                    if(Objects.equals(action.getType(), "favorite")) {
                        arrayResult.add(fileWriter.writeFile(action.getActionId(),"", command.favorite(
                                command.getUsername(), command.getVideoTitle(), this)));
                    }

                    if(Objects.equals(action.getType(), "rating")) {
                        if (action.getSeasonNumber() != 0) {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    command.rateSerial(command.getUsername(), command.getVideoTitle(),
                                            action.getGrade(), action.getSeasonNumber(), this)));
                        } else {
                            arrayResult.add(fileWriter.writeFile(action.getActionId(), "",
                                    command.rateMovie(command.getUsername(), command.getVideoTitle(), action.getGrade(),
                                            this)));
                        }

                    }

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
