package at.ac.fhcampuswien.fhmdb.models.sorting;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public interface SortState {
    List<Movie> sort(List<Movie> movies);
    SortState nextState();

    String getButtonLabel();
}
