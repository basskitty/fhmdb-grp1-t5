package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.stream.Collectors;

public class MovieCell extends ListCell<Movie>
{
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genresLabel = new Label();

    private final Button detailsButton = new Button("Show Details");
    private final Button watchlistButton = new Button("Add to Watchlist");

    private final HBox layout = new HBox();

    public MovieCell() {
        super();

        layout.setPadding(new Insets(10));
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

        // Text Styling
        title.getStyleClass().add("text-yellow");
        detail.getStyleClass().add("text-white");
        genresLabel.getStyleClass().add("text-grey-italic");
        title.setFont(Font.font(title.getFont().getFamily(), 20));

        // Layout configuration
        detail.setWrapText(true);
        VBox textContent = new VBox(title, detail, genresLabel);
        textContent.setSpacing(5);
        textContent.setPadding(new Insets(0, 20, 0, 0));
        textContent.setMaxWidth(600);

        // Button Container
        HBox buttonBox = new HBox(detailsButton, watchlistButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.TOP_RIGHT);

        // Spacer to push buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        layout.getChildren().addAll(textContent, spacer, buttonBox);
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
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

            // Finally, set the graphic for the cell.
            setGraphic(layout);
        }
    }
}

