package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Genre {

    ALL_GENRE("All Genres"),
    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    BIOGRAPHY("Biography"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DRAMA("Drama"),
    DOCUMENTARY("Documentary"),
    FAMILY("Family"),
    FANTASY("Fantasy"),
    HISTORY("History"),
    HORROR("Horror"),
    MUSICAL("Musical"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    SCIENCE_FICTION("Science Fiction"),
    SPORT("Sport"),
    THRILLER("Thriller"),
    WAR("War"),
    WESTERN("Western");


    // Field for the display name
    private final String displayName;

    // Constructor that sets the display name
    Genre(String displayName) {
        this.displayName = displayName;
    }


    @Override
    public String toString() {
        // This ensures the ComboBox shows the display name.
        return displayName;
    }

    // Getter for the internal name (uses the enum constant name)
    public String getInternalName() {
        return name();
    }
}
