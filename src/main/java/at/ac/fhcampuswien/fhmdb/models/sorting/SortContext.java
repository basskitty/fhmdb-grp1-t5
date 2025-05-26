package at.ac.fhcampuswien.fhmdb.models.sorting;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class SortContext {
    private SortState currentState = new NotSortedState();

    public void nextState() {
        currentState = currentState.nextState();
    }

    public List<Movie> applySort(List<Movie> movies) {
        return currentState.sort(movies);
    }

    public SortState getCurrentState() {
        return currentState;
    }
}
