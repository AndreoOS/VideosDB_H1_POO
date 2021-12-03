package homework;

import entertainment.Season;
import fileio.SerialInputData;

import java.util.ArrayList;

public final class Serial extends Video {
    private final int numberOfSeasons;
    private final ArrayList<Season> seasons;

    public Serial(final SerialInputData serial) {
        super(serial);
        numberOfSeasons = serial.getNumberSeason();
        seasons = serial.getSeasons();
    }

    /**
     * The rating of the show is different from the one of a movie
     * The show rating is an average from all the ratings of all the seasons.
     * The method calculates the average rating for every season, which is added to a sum
     * then calculates the rating for the show by dividing the sum by the number of seasons.
     * @return rating of the show
     */
    public Double getSerialRating() {
        Double serialRating = 0.0;
        for (Season s : seasons) {
            Double seasonRating = 0.0;
            for (Double rating : s.getRatings()) {
                    seasonRating = seasonRating + rating;
            }
            if (!s.getRatings().isEmpty()) {
                seasonRating = seasonRating / s.getRatings().size();
            }
            serialRating = serialRating + seasonRating;
        }
        serialRating = serialRating / numberOfSeasons;

        return serialRating;
    }

    /**
     * The show duration is the sum of the duration of every season.
     * Method goes through all the seasons then adds to a sum the duration of every season.
     * @return the duration of the whole show
     */
    public Integer getSerialDuration() {
        int duration = 0;
        for (Season s : this.getSeasons()) {
            duration = duration + s.getDuration();
        }
        return duration;
    }
    public ArrayList<Season> getSeasons() {
        return seasons;
    }
}
