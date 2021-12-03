package homework;

import fileio.ActionInputData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Recommendation {
    private final String type;
    private final String username;
    private final String genre;

    public Recommendation(final ActionInputData action) {
        type = action.getType();
        username = action.getUsername();
        genre = action.getGenre();
    }

    /**
     * Method returns the first unseen video by user. First it checks for movies
     * then for shows.
     * @param db database with the userMap
     * @return name of the first unseen video
     */
    public String standardRec(final Database db) {
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

    /**
     *  Method first gets all the unseen videos of the user, then sorts them based on rating
     *  Returns the first video in the list.
     * @param db database with userMap
     * @return name of the best unseen video based on ratings
     */
    public String bestUnseenRec(final Database db) {
        User user = db.getUserMap().get(username);
        List<Video> videoList;
        videoList = user.getUnseenVids(db);
        List<Video> sortedVideoList = getSortedVideosByRating(videoList);
        if (sortedVideoList.isEmpty()) {
            return null;
        } else {
            return sortedVideoList.get(0).getTitle();
        }
    }

    private List<Video> getSortedVideosByRating(final List<Video> videoList) {
        List<Video> sortedVideos;
        sortedVideos = videoList.stream()
                .sorted(((o1, o2) -> {
                    Double o1Rating = 0.0;
                    Double o2Rating = 0.0;
                    if (o1 instanceof Movie) {
                        o1Rating = ((Movie) o1).getMovieRating();
                    } else if (o1 instanceof Serial) {
                        o1Rating = ((Serial) o1).getSerialRating();
                    }
                    if (o2 instanceof Movie) {
                        o2Rating = ((Movie) o2).getMovieRating();
                    } else if (o1 instanceof Serial) {
                        o2Rating = ((Serial) o2).getSerialRating();
                    }
                    return o2Rating.compareTo(o1Rating);
                })).toList();
        return sortedVideos;
    }

    /**
     * This method can't be used by BASIC users.
     * Method gets a list of all the unseen videos of user and then sorts that list by the number
     * of favorites that each video has.
     * Method returns the first video in that list
     * @param db database for userMap
     * @return name of the video recommended
     */
    public String favoriteRec(final Database db) {
        User user = db.getUserMap().get(username);
        List<Video> videoList;
        if  (user.getSubscriptionType().equals("BASIC")) {
            return null;
        }
        videoList = user.getUnseenVids(db);
        List<Video> sortedVideos = getSortedVideosByFavorites(db, videoList);
        if (sortedVideos.isEmpty()) {
            return null;
        }
        return sortedVideos.get(0).getTitle();
    }

    private List<Video> getSortedVideosByFavorites(final Database db,
                                                   final List<Video> videoList) {
        List<Video> sortedVideo;
        sortedVideo = videoList.stream()
                .sorted((o1, o2) -> Integer.compare(o2.getNumberOfFavorites(db),
                        o1.getNumberOfFavorites(db))).toList();
        return sortedVideo;
    }

    /**
     * This method can't be used by BASIC users.
     * Method gets all the unseen videos of a certain genre for the user, then sorts that list
     * based on the ratings. (If the ratings are equal, then it is sorted by name)
     * @param db database with the userMap
     * @return list of video names with a certain genre unseen by the user in ascending order
     */
    public List<String> searchRec(final Database db) {
        User user = db.getUserMap().get(username);
        if (user.getSubscriptionType().equals("BASIC")) {
            return null;
        }
        List<Video> videoList = user.getUnseenVidsByGenre(db, getGenre());
        List<Video> sortedVideoList = getSortedVideosByGenre(videoList);
        if (sortedVideoList.isEmpty()) {
            return null;
        }
        return getStringVideoList(sortedVideoList);
    }

    private List<Video> getSortedVideosByGenre(final List<Video> videoList) {
        List<Video> sortedVideoList;
        sortedVideoList = videoList.stream()
                .sorted(((o1, o2) -> {
                    Double o1Rating = 0.0;
                    Double o2Rating = 0.0;
                    if (o1 instanceof Movie) {
                        o1Rating = ((Movie) o1).getMovieRating();
                    } else if (o1 instanceof Serial) {
                        o1Rating = ((Serial) o1).getSerialRating();
                    }
                    if (o2 instanceof Movie) {
                        o2Rating = ((Movie) o2).getMovieRating();
                    } else if (o1 instanceof Serial) {
                        o2Rating = ((Serial) o2).getSerialRating();
                    }
                    if (Double.compare(o1Rating, o2Rating) == 0) {
                        return o1.getTitle().compareTo(o2.getTitle());
                    } else {
                        return o1Rating.compareTo(o2Rating);
                    }
                })).toList();
        return sortedVideoList;
    }

    private List<String> getStringVideoList(final List<Video> videoList) {
        List<String> stringVideoList = new ArrayList<>();
        for (Video video : videoList) {
            stringVideoList.add(video.getTitle());
        }
        return stringVideoList;
    }

    /**
     * This method can't be used by BASIC users.
     * The method gets a list of the genres present in the database sorted by popularity (number
     * of views) and a list of unseen videos by the user.
     * @param db database with userMap
     * @return the first unseen video of the most popular genre
     */
    public String popularityRec(final Database db) {
        List<String> allGenresSorted = getAllGenresSorted(db);
        User user = db.getUserMap().get(username);
        if (user.getSubscriptionType().equals("BASIC")) {
            return null;
        }
        List<Video> videoListUnseen = user.getUnseenVids(db);
        for (String genres : allGenresSorted) {
            for (Video video : videoListUnseen) {
                if (video.getGenres().contains(genres)) {
                    return video.getTitle();
                }
            }
        }
        return null;

    }

    /**
     * Method iterates through all the movies and shows and if a video genre matches with
     * the currGenre then adds to a sum the number of views of that video.
     * @param db database with movieMap, userMap
     * @param currGenre genre for which we calculate the number of views
     * @return number of views of currGenre
     */
    private Integer getPopularity(final Database db, final String currGenre) {
        int result = 0;
        for (Movie movie : db.getMovieMap().values()) {
            if (movie.getGenres().contains(currGenre)) {
                result = result + movie.getViews(db);
            }
        }
        for (Serial serial : db.getSerialMap().values()) {
            if (serial.getGenres().contains(currGenre)) {
                result = result + serial.getViews(db);
            }
        }
        return result;
    }

    /**
     * Method adds to a list all the genres found in the videos in database then sorts
     * them using the getPopularity method
     * @param db database with videoMap
     * @return string of genres sorted by popularity
     */
    private List<String> getAllGenresSorted(final Database db) {
        List<String> allGenres = new ArrayList<>();
        for (Movie movie : db.getMovieMap().values()) {
            for (String currentGenre : movie.getGenres()) {
                if (!allGenres.contains(currentGenre)) {
                    allGenres.add(currentGenre);
                }
            }
        }
        for (Serial serial : db.getSerialMap().values()) {
            for (String currentGenre : serial.getGenres()) {
                if (!allGenres.contains(currentGenre)) {
                    allGenres.add(currentGenre);
                }
            }
        }
        return allGenres.stream()
                .sorted(((o1, o2) -> {
                    if (Objects.equals(getPopularity(db, o1), getPopularity(db, o2))) {
                        return o1.compareTo(o2);
                    } else {
                        return Integer.compare(getPopularity(db, o2), getPopularity(db, o1));
                    }
                })).toList();
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
