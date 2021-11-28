package homework;

import fileio.ActionInputData;

import java.util.*;

public class Query {
    private final String objectType;
    private final Integer number;
    private final List<List<String>> filters;
    private final String sortType;
    private final String criteria;

    public Query(ActionInputData action) {
        this.objectType = action.getObjectType();
        this.number = action.getNumber();
        this.filters = action.getFilters();
        this.sortType = action.getSortType();
        this.criteria = action.getCriteria();
    }

    public List<String> averageQuery(Database db) {
        List<String> actorsList = new ArrayList<>();
        int size = 0;
        Map<String, Double> unsorted = new HashMap<>();
        for (Map.Entry<String, Actor> entry : db.getActorMap().entrySet()) {
            unsorted.put(entry.getKey(), entry.getValue().getAverage(db.getMovieMap(), db.getSerialMap()));
        }
        Map<String, Double> sorted = sortByValue(unsorted);
        for(Map.Entry<String, Double> entry : sorted.entrySet()) {
            if (size < number) {
                actorsList.add(entry.getKey());
                size = size + 1;
            } else {
                break;
            }
        }
        return actorsList;
    }

    private Map<String, Double> sortByValue(Map<String, Double> unsorted) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(unsorted.entrySet());
        list.sort((o1, o2) -> {
            if (sortType.equals("asc")) {
                if (Double.compare(o1.getValue(), o2.getValue()) == 0) {
                    return o1.getKey().compareTo(o2.getKey());
                } else {
                    return o1.getValue().compareTo(o2.getValue());
                }
            } else {
                if (Double.compare(o1.getValue(), o2.getValue()) == 0) {
                    return o2.getKey().compareTo(o1.getKey());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });
        HashMap<String, Double> sorted = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            sorted.put(entry.getKey(), entry.getValue());
        }
        return sorted;
    }

    public List<String> awardQuery(Database db) {
        List<Actor> actorList = new ArrayList<>();
        for (Map.Entry<String, Actor> entry : db.getActorMap().entrySet()) {
            actorList.add(entry.getValue());
        }
        actorList = actorList.stream()
                    .filter(o1 -> o1.hasAward(o1,this)).toList();
        return actorListToString(actorList);
    }

    private List<String> actorListToString(List<Actor> actorList) {
        List<String> actorStringList = new ArrayList<>();
        for (Actor a : actorList) {
            actorStringList.add(a.getName());
        }
        return actorStringList;
    }

    public List<String> filterQuery(Database db) {
        List<Actor> actorList = new ArrayList<>();
        for (Map.Entry<String, Actor> entry : db.getActorMap().entrySet()) {
            actorList.add(entry.getValue());
        }
        actorList = actorList.stream()
                .filter(o1 -> o1.hasFilters(o1,this)).toList();
        return actorListToString(actorList);
    }



    public List<String> movieRatingQuery(Database db) {
        int size = 0;
        List<String> movieStringList = new ArrayList<>();
        List<Movie> movieList = new ArrayList<>();
        for(Movie movie : db.getMovieMap().values()) {
            if (movie.getMovieRating() != 0 && movie.hasFilters(this)) {
                movieList.add(movie);
            }
        }
        List<Movie> sortedMovieList = movieList.stream()
                .sorted((o1, o2) -> {
                    if (sortType.equals("asc")) {
                        if (Double.compare(o1.getMovieRating(), o2.getMovieRating()) == 0) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return o1.getMovieRating().compareTo(o2.getMovieRating());
                        }
                    } else {
                        if (Double.compare(o1.getMovieRating(), o2.getMovieRating()) == 0) {
                            return o2.getTitle().compareTo(o1.getTitle());
                        } else {
                            return o2.getMovieRating().compareTo(o1.getMovieRating());
                        }
                    }
                }).toList();
        for (Movie movie : sortedMovieList) {
            if (size < number) {
                movieStringList.add(movie.getTitle());
                size++;
            } else {
                break;
            }
        }
        return movieStringList;
    }

    public List<String> showRatingQuery(Database db) {
        int size = 0;
        List<String> showStringList = new ArrayList<>();
        List<Serial> showList = new ArrayList<>();
        for (Serial serial : db.getSerialMap().values()) {
            if (serial.getSerialRating() != 0 && serial.hasFilters(this)) {
                showList.add(serial);
            }
        }
        List<Serial> sortedShowList = showList.stream()
                .sorted((o1, o2) -> {
                    if (sortType.equals("asc")) {
                        if (Double.compare(o1.getSerialRating(), o2.getSerialRating()) == 0) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return o1.getSerialRating().compareTo(o2.getSerialRating());
                        }
                    } else {
                        if (Double.compare(o1.getSerialRating(), o2.getSerialRating()) == 0) {
                            return o2.getTitle().compareTo(o1.getTitle());
                        } else {
                            return o2.getSerialRating().compareTo(o1.getSerialRating());
                        }
                    }

                }).toList();
        for (Serial serial : sortedShowList) {
            if (size < number) {
                showStringList.add(serial.getTitle());
                size++;
            } else {
                break;
            }
        }
        return showStringList;
    }

    public List<String> favoriteMovieQuery(Database db) {
        int size = 0;
        List<String> movieStringList = new ArrayList<>();
        List<Movie> movieList = new ArrayList<>();
        for (Movie movie : db.getMovieMap().values()) {
            if (movie.hasFilters(this) && movie.getNumberOfFavorites(db) != 0) {
                movieList.add(movie);
            }
        }
        List<Movie> sortedMovieList = movieList.stream()
                .sorted((o1, o2) -> {
                    if (sortType.equals("asc")) {
                        if (Objects.equals(o1.getNumberOfFavorites(db), o2.getNumberOfFavorites(db))) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return o1.getNumberOfFavorites(db).compareTo(o2.getNumberOfFavorites(db));
                        }
                    } else {
                        if (Objects.equals(o1.getNumberOfFavorites(db), o2.getNumberOfFavorites(db))) {
                            return o2.getTitle().compareTo(o1.getTitle());
                        } else {
                            return o2.getNumberOfFavorites(db).compareTo(o1.getNumberOfFavorites(db));
                        }
                    }
                }).toList();
        for (Movie movie : sortedMovieList) {
            if (size < number) {
                movieStringList.add(movie.getTitle());
                size++;
            } else {
                break;
            }
        }
        return movieStringList;
    }

    public List<String> longestMovieQuery(Database db) {
        int size = 0;
        List<String> movieStringList = new ArrayList<>();
        List<Movie> movieList = new ArrayList<>();
        for (Movie movie : db.getMovieMap().values()) {
            if (movie.hasFilters(this)) {
                movieList.add(movie);
            }
        }
        List<Movie> sortedMovieList = movieList.stream()
                .sorted((o1, o2) -> {
                    if (sortType.equals("asc")) {
                        if (o1.getDuration() == o2.getDuration()) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return Integer.compare(o1.getDuration(), o2.getDuration());
                        }
                    } else {
                        if (o1.getDuration() == o2.getDuration()) {
                            return o2.getTitle().compareTo(o1.getTitle());
                        } else {
                            return Integer.compare(o2.getDuration(), o1.getDuration());
                        }
                    }
                }).toList();
        for (Movie movie : sortedMovieList) {
            if (size < number) {
                movieStringList.add(movie.getTitle());
                size++;
            } else {
                break;
            }
        }
        return movieStringList;

    }

    public List<String> mostViewedMovieQuery(Database db) {
        int size = 0;
        List<String> movieStringList = new ArrayList<>();
        List<Movie> movieList = new ArrayList<>();
        for (Movie movie : db.getMovieMap().values()) {
            if (movie.hasFilters(this) && movie.getViews(db) != 0) {
                movieList.add(movie);
            }
        }
        List<Movie> sortedMovieList = movieList.stream()
                .sorted((o1, o2) -> {
                    if (sortType.equals("asc")) {
                        if (Objects.equals(o1.getViews(db), o2.getViews(db))) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return Integer.compare(o1.getViews(db), o2.getViews(db));
                        }
                    } else {
                        if (Objects.equals(o1.getViews(db), o2.getViews(db))) {
                            return o2.getTitle().compareTo(o1.getTitle());
                        } else {
                            return Integer.compare(o2.getViews(db), o1.getViews(db));
                        }
                    }
                }).toList();
        for (Movie movie : sortedMovieList) {
            if (size < number) {
                movieStringList.add(movie.getTitle());
                size++;
            } else {
                break;
            }
        }
        return movieStringList;
    }

    public List<String> favoriteShowQuery(Database db) {
        int size = 0;
        List<String> showStringList = new ArrayList<>();
        List<Serial> showList = new ArrayList<>();
        for (Serial serial : db.getSerialMap().values()) {
            if (serial.getNumberOfFavorites(db) != 0 && serial.hasFilters(this)) {
                showList.add(serial);
            }
        }
        List<Serial> sortedShowList = showList.stream()
                .sorted((o1, o2) -> {
                    if (sortType.equals("asc")) {
                        if (Objects.equals(o1.getNumberOfFavorites(db), o2.getNumberOfFavorites(db))) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return Integer.compare(o1.getNumberOfFavorites(db), o2.getNumberOfFavorites(db));
                        }
                    } else {
                        if (Objects.equals(o1.getNumberOfFavorites(db), o2.getNumberOfFavorites(db))) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return Integer.compare(o2.getNumberOfFavorites(db), o1.getNumberOfFavorites(db));
                        }
                    }

                }).toList();
        for (Serial serial : sortedShowList) {
            if (size < number) {
                showStringList.add(serial.getTitle());
                size++;
            } else {
                break;
            }
        }
        return showStringList;

    }

    public List<String> longestShowQuery(Database db) {
        int size = 0;
        List<String> showStringList = new ArrayList<>();
        List<Serial> showList = new ArrayList<>();
        for (Serial serial : db.getSerialMap().values()) {
            if (serial.getNumberOfFavorites(db) != 0 && serial.hasFilters(this)) {
                showList.add(serial);
            }
        }
        List<Serial> sortedShowList = showList.stream()
                .sorted((o1, o2) -> {
                    if (this.getSortType().equals("asc")) {
                        if (Objects.equals(o1.getSerialDuration(), o2.getSerialDuration())) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return Integer.compare(o1.getSerialDuration(), o2.getSerialDuration());
                        }
                    } else {
                        if (Objects.equals(o1.getSerialDuration(), o2.getSerialDuration())) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return Integer.compare(o2.getSerialDuration(), o1.getSerialDuration());
                        }
                    }

                }).toList();
        for (Serial serial : sortedShowList) {
            if (size < number) {
                showStringList.add(serial.getTitle());
                size++;
            } else {
                break;
            }
        }
        return showStringList;
    }

    public List<String> mostViewedShowQuery(Database db) {
        int size = 0;
        List<String> showStringList = new ArrayList<>();
        List<Serial> showList = new ArrayList<>();
        for (Serial serial : db.getSerialMap().values()) {
            if (serial.getNumberOfFavorites(db) != 0 && serial.hasFilters(this)) {
                showList.add(serial);
            }
        }
        List<Serial> sortedShowList = showList.stream()
                .sorted((o1, o2) -> {
                    if (this.getSortType().equals("asc")) {
                        if (Objects.equals(o1.getViews(db), o2.getViews(db))) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return Integer.compare(o1.getViews(db), o2.getViews(db));
                        }
                    } else {
                        if (Objects.equals(o1.getViews(db), o2.getViews(db))) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return Integer.compare(o2.getViews(db), o1.getViews(db));
                        }
                    }

                }).toList();
        for (Serial serial : sortedShowList) {
            if (size < number) {
                showStringList.add(serial.getTitle());
                size++;
            } else {
                break;
            }
        }
        return showStringList;
    }

    public List<String> activeUsersQuery(Database db) {
        List<String> userStringList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        int size = 0;
        for (User user : db.getUserMap().values()) {
            if (user.getNumberOfRatings() != 0) {
                userList.add(user);
            }
        }
        List<User> sortedUserList = userList.stream()
                .sorted((o1, o2) -> {
                    if (this.getSortType().equals("asc")) {
                        if (Objects.equals(o1.getNumberOfRatings(), o2.getNumberOfRatings())) {
                            return o1.getUsername().compareTo(o2.getUsername());
                        } else {
                            return Integer.compare(o1.getNumberOfRatings(), o2.getNumberOfRatings());
                        }
                    } else {
                        if (Objects.equals(o1.getNumberOfRatings(), o2.getNumberOfRatings())) {
                            return o1.getUsername().compareTo(o2.getUsername());
                        } else {
                            return Integer.compare(o2.getNumberOfRatings(), o1.getNumberOfRatings());
                        }
                    }
                }).toList();
        for (User user : sortedUserList) {
            if (size < number) {
                userStringList.add(user.getUsername());
                size++;
            } else {
                break;
            }
        }
        return userStringList;
    }

    public String getObjectType() {
        return objectType;
    }

    public Integer getNumber() {
        return number;
    }

    public List<List<String>> getFilters() {
        return filters;
    }

    public String getSortType() {
        return sortType;
    }

    public String getCriteria() {
        return criteria;
    }
}
