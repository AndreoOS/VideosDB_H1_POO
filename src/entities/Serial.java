package entities;

import entertainment.Genre;
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

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }
}
