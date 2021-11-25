package entities;

import common.Constants;
import fileio.*;
import org.json.simple.JSONArray;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    public void startCommands (Writer fileWriter, JSONArray arrayResult) throws IOException {
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
                        int res = command.favorite(command.getUsername(), command.getVideoTitle(), this);
                        if (res == 2) { // a fost adaugat in lista
                            arrayResult.add(fileWriter.writeFile(action.getActionId(),"","success -> " +
                                    action.getTitle() + " was added as favourite"));
                        } else if (res == 1) { // este deja in lista
                            arrayResult.add(fileWriter.writeFile(action.getActionId(),"","error -> " +
                                    action.getTitle() + " is already in favourite list"));
                        } else if (res == 0) { // nu a fost vizionat
                            arrayResult.add(fileWriter.writeFile(action.getActionId(),"","error -> " +
                                    action.getTitle() + " is not seen"));
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
