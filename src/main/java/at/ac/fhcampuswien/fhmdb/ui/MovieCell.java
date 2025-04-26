package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXButton;
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

    private final JFXButton detailsBtn = new JFXButton("Show Details");
    private final JFXButton actionBtn = new JFXButton();
    private final HBox buttonBox = new HBox(detailsBtn, actionBtn);

    private final HBox layout = new HBox();
    private final VBox textContent = new VBox(title, detail, genresLabel);

    private final ClickEventHandler<Movie> actionHandler;
    private final String buttonLabel;

    public MovieCell(ClickEventHandler<Movie> actionHandler, String buttonLabel) {
        super();
        this.actionHandler = actionHandler;
        this.buttonLabel = buttonLabel;

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
        textContent.setSpacing(5);
        textContent.setPadding(new Insets(0, 20, 0, 0));
        textContent.setMaxWidth(600);

        // Button Container
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.TOP_RIGHT);

        // Spacer to push buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        actionBtn.setOnAction(event -> {
            if (getItem() != null && actionHandler != null) {
                actionHandler.onClick(getItem());
            }
        });

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

            actionBtn.setText(buttonLabel);

            // Finally, set the graphic for the cell.
            setGraphic(layout);
        }
    }
}

