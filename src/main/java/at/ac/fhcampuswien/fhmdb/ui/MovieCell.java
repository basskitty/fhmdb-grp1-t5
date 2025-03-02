package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.stream.Collectors;

public class MovieCell extends ListCell<Movie>
{
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genresLabel = new Label();

    private final VBox layout = new VBox(title, detail, genresLabel);

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Add style class only once (you could move this to the constructor)
            if (!getStyleClass().contains("movie-cell")) {
                getStyleClass().add("movie-cell");
            }

            // Set the title and detail text.
            title.setText(movie.getTitle());
            detail.setText(movie.getDescription() != null ? movie.getDescription() : "No description available");

            // Set the genres.
            String genresString = movie.getGenres().isEmpty()
                    ? "No genres available"
                    : movie.getGenres().stream()
                    .map(genre -> genre.toString().toUpperCase())
                    .collect(Collectors.joining(", "));
            genresLabel.setText(genresString);

            // Apply style classes if not already added
            if (!title.getStyleClass().contains("text-yellow")) {
                title.getStyleClass().add("text-yellow");
            }
            if (!detail.getStyleClass().contains("text-white")) {
                detail.getStyleClass().add("text-white");
            }
            if (!genresLabel.getStyleClass().contains("text-grey-italic")) {
                genresLabel.getStyleClass().add("text-grey-italic");
            }

            // Set background color for layout
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // Layout configuration (could be moved to constructor)
            title.setFont(Font.font(title.getFont().getFamily(), 20));
            if (getScene() != null) {
                detail.setMaxWidth(getScene().getWidth() - 30);
            }
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.setSpacing(10);
            layout.setAlignment(Pos.CENTER_LEFT);

            // Finally, set the graphic for the cell.
            setGraphic(layout);
        }
    }
}

