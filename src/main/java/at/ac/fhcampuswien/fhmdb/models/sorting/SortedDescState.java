package at.ac.fhcampuswien.fhmdb.models.sorting;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortedDescState implements SortState {
    @Override
    public List<Movie> sort(List<Movie> movies) {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public SortState nextState() {
        System.out.println("Movies sorted in ascending order: A-Z");
        return new SortedAscState();
    }
}
