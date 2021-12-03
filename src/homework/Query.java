package homework;

import fileio.ActionInputData;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Objects;

public final class Query {
    private final String objectType;
    private final Integer number;
    private final List<List<String>> filters;
    private final String sortType;
    private final String criteria;

    public Query(final ActionInputData action) {
        this.objectType = action.getObjectType();
        this.number = action.getNumber();
        this.filters = action.getFilters();
        this.sortType = action.getSortType();
        this.criteria = action.getCriteria();
    }

    /**
     * Method makes a list of all the actors then uses a method that sorts the list by average
     * rating.
     * @param db database with actors
     * @return list of first query.number actors sorted by average
     */
    public List<String> averageQuery(final Database db) {
        List<Actor> actorList = new ArrayList<>();
        List<String> actorStringList = new ArrayList<>();
        int size = 0;
        for (Actor actor : db.getActorMap().values()) {
            if (actor.getAverage(db.getMovieMap(), db.getSerialMap()) != 0) {
                actorList.add(actor);
            }
        }
        List<Actor> sortedActorList = sortActorByAverage(actorList, db);
        for (Actor actor : sortedActorList) {
            if (size < number) {
                actorStringList.add(actor.getName());
                size++;
            } else {
                break;
            }
        }
        return actorStringList;
    }

    private List<Actor> sortActorByAverage(final List<Actor> actorList, final Database db) {
        return actorList.stream()
                .sorted(((o1, o2) -> {
                    if (sortType.equals("asc")) {
                        if (Double.compare(o1.getAverage(db.getMovieMap(), db.getSerialMap()),
                                o2.getAverage(db.getMovieMap(), db.getSerialMap())) == 0) {
                            return o1.getName().compareTo(o2.getName());
                        } else {
                            return Double.compare(o1.getAverage(db.getMovieMap(),
                                            db.getSerialMap()),
                                    o2.getAverage(db.getMovieMap(), db.getSerialMap()));
                        }
                    } else {
                        if (Double.compare(o2.getAverage(db.getMovieMap(), db.getSerialMap()),
                                o1.getAverage(db.getMovieMap(), db.getSerialMap())) == 0) {
                            return o2.getName().compareTo(o1.getName());
                        } else {
                            return Double.compare(o2.getAverage(db.getMovieMap(),
                                            db.getSerialMap()), o1.getAverage(db.getMovieMap(),
                                    db.getSerialMap()));
                        }
                    }
                })).toList();

    }

    /**
     * Method gets all the actors from database, then filters them using streams and
     * actor method hasAward.
     * @param db database
     * @return list of all the names of actors with the wanted awards
     */
    public List<String> awardQuery(final Database db) {
        List<Actor> actorList = new ArrayList<>();
        for (Map.Entry<String, Actor> entry : db.getActorMap().entrySet()) {
            actorList.add(entry.getValue());
        }
        actorList = actorList.stream()
                    .filter(o1 -> o1.hasAward(o1, this)).toList();
        actorList = sortActorListByAwards(actorList);
        return actorListToString(actorList);
    }

    private List<Actor> sortActorListByAwards(final List<Actor> actorList) {
        return actorList.stream()
                .sorted(((o1, o2) -> {
                    if (sortType.equals("asc")) {
                       if (Objects.equals(o1.getNumOfAwards(), o2.getNumOfAwards())) {
                           return o1.getName().compareTo(o2.getName());
                       } else {
                           return Integer.compare(o1.getNumOfAwards(), o2.getNumOfAwards());
                       }
                    } else {
                        if (Objects.equals(o1.getNumOfAwards(), o2.getNumOfAwards())) {
                            return o2.getName().compareTo(o1.getName());
                        } else {
                            return o2.getNumOfAwards().compareTo(o1.getNumOfAwards());
                        }
                    }
                })).toList();
    }

    private List<String> actorListToString(final List<Actor> actorList) {
        List<String> actorStringList = new ArrayList<>();
        for (Actor a : actorList) {
            actorStringList.add(a.getName());
        }
        return actorStringList;
    }

    /**
     * Method gets all the actors from database, then filters them using streams and
     * actor method hasFilters. The list is then sorted depending of sortType (ascending or
     * descending)
     * @param db database
     * @return list of actors that have all the filters in their description
     */
    public List<String> filterQuery(final Database db) {
        List<Actor> actorList = new ArrayList<>(db.getActorMap().values());
        actorList = actorList.stream()
                .filter(o1 -> o1.hasFilters(o1, this)).toList();
        actorList = actorList.stream()
                .sorted(((o1, o2) -> {
                    if (sortType.equals("asc")) {
                        return o1.getName().compareTo(o2.getName());
                    } else {
                        return o2.getName().compareTo(o1.getName());
                    }
                })).toList();
        return actorListToString(actorList);
    }


    /**
     * Method creates a list of all the movies with non-zero rating and all the filters.
     * This list is sorted using streams.
     * @param db database with all needed info
     * @return List of first query.number movies sorted by ratings and filters
     */
    public List<String> movieRatingQuery(final Database db) {
        int size = 0;
        List<String> movieStringList = new ArrayList<>();
        List<Movie> movieList = new ArrayList<>();
        for (Movie movie : db.getMovieMap().values()) {
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

    /**
     * Method creates a list of all the shows with non-zero rating and all the filters.
     * This list is sorted using streams depending on sortType.
     * @param db database with all needed info
     * @return List of first query.number shows sorted by ratings and filters
     */
    public List<String> showRatingQuery(final Database db) {
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

    /**
     * Method creates a list of all the movies that has the filters and the number of favorites
     * non-zero. Using streams the list is sorted based on number of favorites and name.
     * @param db database with all needed info
     * @return list of first query.number movies sorted by number of favorites
     */
    public List<String> favoriteMovieQuery(final Database db) {
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
                        if (Objects.equals(o1.getNumberOfFavorites(db),
                                o2.getNumberOfFavorites(db))) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return o1.getNumberOfFavorites(db)
                                    .compareTo(o2.getNumberOfFavorites(db));
                        }
                    } else {
                        if (Objects.equals(o1.getNumberOfFavorites(db),
                                o2.getNumberOfFavorites(db))) {
                            return o2.getTitle().compareTo(o1.getTitle());
                        } else {
                            return o2.getNumberOfFavorites(db)
                                    .compareTo(o1.getNumberOfFavorites(db));
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

    /**
     * Method creates list of all the movies that have the query filters, then sorts them using
     * streams.
     * @param db database with all the useful info
     * @return list of first query.number movies sorted by duration
     */
    public List<String> longestMovieQuery(final Database db) {
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

    /**
     * Method creates list of all the movies that have the query filters, then sorts them using
     * streams by number of views.
     * @param db database with all useful info
     * @return list of first query.number movies sorted by views
     */
    public List<String> mostViewedMovieQuery(final Database db) {
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

    /**
     * Method creates a list of all the shows that has the filters and the number of favorites
     * non-zero. Using streams the list is sorted based on number of favorites and name.
     * @param db database with all needed info
     * @return list of first query.number shows sorted by number of favorites
     */
    public List<String> favoriteShowQuery(final Database db) {
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
                        if (Objects.equals(o1.getNumberOfFavorites(db),
                                o2.getNumberOfFavorites(db))) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        } else {
                            return Integer.compare(o1.getNumberOfFavorites(db),
                                    o2.getNumberOfFavorites(db));
                        }
                    } else {
                        if (Objects.equals(o1.getNumberOfFavorites(db),
                                o2.getNumberOfFavorites(db))) {
                            return o2.getTitle().compareTo(o1.getTitle());
                        } else {
                            return Integer.compare(o2.getNumberOfFavorites(db),
                                    o1.getNumberOfFavorites(db));
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

    /**
     * Method creates list of all the show that have the query filters, then sorts them using
     * streams with duration of the show
     * @param db database with all the useful info
     * @return list of first query.number show sorted by duration
     */
    public List<String> longestShowQuery(final Database db) {
        int size = 0;
        List<String> showStringList = new ArrayList<>();
        List<Serial> showList = new ArrayList<>();
        for (Serial serial : db.getSerialMap().values()) {
            if (serial.hasFilters(this)) {
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
                            return o2.getTitle().compareTo(o1.getTitle());
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

    /**
     * Method creates list of all the shows that have the query filters, then sorts them using
     * streams by number of views.
     * @param db database with all useful info
     * @return list of first query.number shows sorted by views
     */
    public List<String> mostViewedShowQuery(final Database db) {
        int size = 0;
        List<String> showStringList = new ArrayList<>();
        List<Serial> showList = new ArrayList<>();
        for (Serial serial : db.getSerialMap().values()) {
            if (serial.hasFilters(this) && serial.getViews(db) != 0) {
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
                            return o2.getTitle().compareTo(o1.getTitle());
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

    /**
     * Method creates list of all users that have a non-zero number of rated videos.
     * This list is then sorted by the number of ratings using streams.
     * @param db database with all useful info
     * @return list of first query.number users sorted by number of rated videos
     */
    public List<String> activeUsersQuery(final Database db) {
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
                            return Integer.compare(o1.getNumberOfRatings(),
                                    o2.getNumberOfRatings());
                        }
                    } else {
                        if (Objects.equals(o1.getNumberOfRatings(), o2.getNumberOfRatings())) {
                            return o2.getUsername().compareTo(o1.getUsername());
                        } else {
                            return Integer.compare(o2.getNumberOfRatings(),
                                    o1.getNumberOfRatings());
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
