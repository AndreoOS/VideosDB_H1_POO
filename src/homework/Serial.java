package homework;

import entertainment.Season;
import fileio.SerialInputData;

import java.util.ArrayList;

public class Serial extends Video {
    private int numberOfSeasons;
    private ArrayList<Season> seasons;

    public Serial(SerialInputData serial) {
        super(serial);
        numberOfSeasons = serial.getNumberSeason();
        seasons = serial.getSeasons();
    }

    public Double getSerialRating (){
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

    public Integer getSerialDuration (){
        int duration = 0;
        for (Season s : this.getSeasons()) {
            duration = duration + s.getDuration();
        }
        return duration;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }
}
