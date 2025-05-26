package at.ac.fhcampuswien.fhmdb.models.sorting;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class NotSortedState implements SortState {
    public List<Movie> sort(List<Movie> movies) {
        return movies;
    }

    public SortState nextState() {
        return new SortedAscState();
    }

    @Override
    public String getButtonLabel() {
        return "Sort - Name (asc)";
    }
}
