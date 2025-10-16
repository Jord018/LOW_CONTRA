import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.example.contrabossclone.controller.GameController;
import com.example.contrabossclone.model.GameModel;
import com.example.contrabossclone.view.GameView;

public class Main extends Application {

    private GameModel model;
    private GameView view;
    private GameController controller;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(800, 600); // Initial size
        GraphicsContext gc = canvas.getGraphicsContext2D();

        model = new GameModel(canvas.getWidth(), canvas.getHeight());
        view = new GameView(model, canvas.getWidth(), canvas.getHeight());

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, canvas.getWidth(), canvas.getHeight());

        // Bind canvas dimensions to scene dimensions
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());

        // Update model and view on resize
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            model.resize(newVal.doubleValue(), scene.getHeight());
            view.resize(newVal.doubleValue(), scene.getHeight());
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            model.resize(scene.getWidth(), newVal.doubleValue());
            view.resize(scene.getWidth(), newVal.doubleValue());
        });

        controller = new GameController(model, scene);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                controller.handleInput();
                if (!model.isGameOver()) {
                    model.update();
                }
                view.render(gc);
            }
        }.start();

        primaryStage.setTitle("Contra Boss Clone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
