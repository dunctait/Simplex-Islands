package SimplexIslands;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class Controller {

    Scene scene;
    Stage stage;

    @FXML GridPane gridPane;
    @FXML HBox hbox;
    @FXML VBox vbox;

    @FXML Label columnsLabel;
    @FXML Slider columnsSlider;

    @FXML Label rowsLabel;
    @FXML Slider rowsSlider;

    @FXML Label octavesLabel;
    @FXML Slider octavesSlider;

    @FXML Label roughnessLabel;
    @FXML Slider roughnessSlider;

    @FXML Label scaleLabel;
    @FXML Slider scaleSlider;

    @FXML Label edgeFadeLabel;
    @FXML Slider edgeFadeSlider;

    @FXML Label seedLabel;
    @FXML Slider seedSlider;

    @FXML Label colour1Label;
    @FXML Slider colour1Slider;

    @FXML Label colour2Label;
    @FXML Slider colour2Slider;

    @FXML Label colour3Label;
    @FXML Slider colour3Slider;

    @FXML Label colour4Label;
    @FXML Slider colour4Slider;

    @FXML CheckBox radialCheckbox;

    @FXML Canvas mapCanvas;

    private GraphicsContext gc;

    private int canvasW;
    private int canvasH;

    private float[][] map;

    // Default Settings
    private int mapRows = 128;
    private int mapColumns = 128;
    private int octaves = 4;
    private float roughness = 0.66f;
    private float scale = 0.012f;

    private float colour1Limit = 0.21f;
    private float colour2Limit = 0.27f;
    private float colour3Limit = 0.6f;

    private float edgeFade = 0.08f;
    private boolean radialEnabled = true;
    private short seed = 0;

    @FXML
    public void initialize() {

        gc = mapCanvas.getGraphicsContext2D();
        canvasW = (int)mapCanvas.getWidth();
        canvasH = (int)mapCanvas.getHeight();

        columnsSlider.setValue(mapColumns);
        rowsSlider.setValue(mapRows);
        octavesSlider.setValue(octaves);
        roughnessSlider.setValue(roughness);
        scaleSlider.setValue(scale);
        edgeFadeSlider.setValue(edgeFade);
        seedSlider.setValue(seed);
        colour1Slider.setValue(colour1Limit);
        colour2Slider.setValue(colour2Limit);
        colour3Slider.setValue(colour3Limit);
        radialCheckbox.setSelected(radialEnabled);
        
        columnsLabel.setText("Columns: " + (int) columnsSlider.getValue());
        rowsLabel.setText("Rows: " + (int) rowsSlider.getValue());
        octavesLabel.setText("Octaves: " + (int) octavesSlider.getValue());
        roughnessLabel.setText("Roughness: " + (float) roughnessSlider.getValue());
        scaleLabel.setText("Scale: " + (float) scaleSlider.getValue());
        edgeFadeLabel.setText("Edge Fade Coverage: " + (float) edgeFadeSlider.getValue());
        seedLabel.setText("Seed: " + (int) seedSlider.getValue());
        SetColourLabels();

        columnsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            columnsLabel.setText("Columns: " + (int) columnsSlider.getValue());
            mapColumns = (int) columnsSlider.getValue();
            CreateArray();
            DrawMapCanvas();
        });
        
        rowsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            rowsLabel.setText("Rows: " + (int) rowsSlider.getValue());
            mapRows = (int) rowsSlider.getValue();
            CreateArray();
            DrawMapCanvas();
        });

        octavesSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            octavesLabel.setText("Octaves: " + (int) octavesSlider.getValue());
            octaves = (int) octavesSlider.getValue();
            CreateArray();
            DrawMapCanvas();
        });
        
        roughnessSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            float value = round( (float)roughnessSlider.getValue(), 2);
            roughnessLabel.setText("Roughness: " + value);
            roughness = value;
            CreateArray();
            DrawMapCanvas();
        });

        scaleSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            float value = round( (float)scaleSlider.getValue(), 3);
            if (value == 0) {value = 0.01f;}
            scaleLabel.setText("Scale: " + value);
            scale = value;
            CreateArray();
            DrawMapCanvas();
        });

        edgeFadeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            float value = round( (float)edgeFadeSlider.getValue(), 2);
            edgeFadeLabel.setText("Edge Fade Coverage: " + value);
            edgeFade = value;
            CreateArray();
            DrawMapCanvas();
        });

        seedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            seedLabel.setText("Seed: " + (int) seedSlider.getValue());
            seed = (short)seedSlider.getValue();
            CreateArray();
            DrawMapCanvas();
        });

        colour1Slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            colour1Limit = round( (float)colour1Slider.getValue(), 2);
            SetColourLabels();
            DrawMapCanvas();
        });

        colour2Slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            colour2Limit = round( (float)colour2Slider.getValue(), 2);
            SetColourLabels();
            DrawMapCanvas();
        });

        colour3Slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            colour3Limit = round( (float)colour3Slider.getValue(), 2);
            SetColourLabels();
            DrawMapCanvas();
        });

        radialCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean oldValue, Boolean newValue) {
                if (radialEnabled == newValue) { return; }
                radialEnabled = newValue;
                CreateArray();
                DrawMapCanvas();
            }
        });
        
        CreateArray();
        DrawMapCanvas();
    }

    private void CreateArray() {

        SimplexNoise simplex = new SimplexNoise();
        map = simplex.generateOctavedSimplexNoise(mapColumns, mapRows, octaves, roughness, scale, seed);

        normalizeArray();
        if (radialEnabled) createRadialMask();
        createEdgeMask();
    }

    private void SetColourLabels() {
        colour1Label.setText("Colour 1: 0.0->" + colour1Limit);
        colour2Label.setText("Colour 2: " + colour1Limit + "->" + colour2Limit);
        colour3Label.setText("Colour 3: " + colour2Limit + "->" + colour3Limit);
        colour4Label.setText("Colour 4: " + colour3Limit + "->1");
    }

    private void ResizeCanvas() {

        int menuWidth = 200;

        if (scene.getHeight() > scene.getWidth() - menuWidth) { // tall and thin window, use width as size
            mapCanvas.setHeight(scene.getWidth() - menuWidth);
            mapCanvas.setWidth(scene.getWidth() - menuWidth);
        } else {
            mapCanvas.setHeight(scene.getHeight());
            mapCanvas.setWidth(scene.getHeight());
        }

        canvasW = (int)mapCanvas.getWidth();
        canvasH = (int)mapCanvas.getHeight();

        DrawMapCanvas();

    }

    private void DrawMapCanvas() {

        gc.clearRect(0, 0, canvasW, canvasH);

        for (int i=0; i<mapColumns; i++) {
            for (int j = 0; j < mapRows; j++) {

                if (map[i][j] < colour1Limit) gc.setFill(Color.DARKBLUE);
                else if (map[i][j] < colour2Limit) gc.setFill(Color.SANDYBROWN);
                else if (map[i][j] < colour3Limit) gc.setFill(Color.GREEN);
                else gc.setFill(Color.LIGHTGREY);

                float pixelW = (float)canvasW/mapColumns;
                float pixelH = (float)canvasH/mapRows;

                // fillRect( x, y, w, h );
                gc.fillRect( roundDown(i * pixelW, 0), roundDown(j * pixelH, 0), roundUp(pixelW,0), roundUp(pixelH,0) );
                // round down the positions and round up the pixel size to ensure overlap and no spaces between pixels

            }
        }
    }

    private void normalizeArray() {
        float min = 0;
        float max = 0;

        for (int i = 0; i < mapColumns; i++) {
            for (int j = 0; j < mapRows; j++) {

                if (map[i][j] < min) {
                    min = map[i][j];
                } else if (map[i][j] > max) {
                    max = map[i][j];
                }
            }
        }

        float divisor = max - min;

        for (int i = 0; i < mapColumns; i++) {
            for (int j = 0; j < mapRows; j++) {

                map[i][j] = ( map[i][j] - min ) / divisor;

            }
        }
    }

    private void createEdgeMask() {

        float percentToGradient = edgeFade;
        // this is the percentage of the entire width/height from edge to fade in

        for (int i=0; i<mapColumns; i++) {
            for (int j = 0; j < mapRows; j++) {

                if (i < mapColumns/2) { // left side of map
                    float percentIn = (float)i/mapColumns; // calculate how far in the map we are
                    if (percentIn < percentToGradient) {
                        map[i][j] = map[i][j] * (percentIn / percentToGradient);
                    }
                }

                if (i > mapColumns/2) { // right side of map
                    float percentIn = 1 - (float)i/mapColumns;
                    if (percentIn < percentToGradient) {
                        map[i][j] = map[i][j] * (percentIn / percentToGradient);
                    }
                }

                if (j < mapRows/2) { // top side of map
                    float percentIn = (float)j/mapRows;
                    if (percentIn < percentToGradient) {
                        map[i][j] = map[i][j] * (percentIn / percentToGradient);
                    }
                }

                if (j > mapRows/2) { // top side of map
                    float percentIn = 1 - (float)j/mapRows;
                    if (percentIn < percentToGradient) {
                        map[i][j] = map[i][j] * (percentIn / percentToGradient);
                    }
                }

            }
        }
    }

    private void createRadialMask() {
        int centerX = mapColumns/2;
        int centerY = mapRows/2;

        float furthestDistance = (float)Math.sqrt( (centerX * centerX) + (centerY * centerY) );

        for (int i = 0; i < mapColumns; i++) {
            for (int j = 0; j < mapRows; j++) {

                //Simple squaring, you can use whatever math libraries are available to you to make this more readable
                //The cool thing about squaring is that it will always give you a positive distance! (-10 * -10 = 100)
                float distanceX = (centerX - i) * (centerX - i);
                float distanceY = (centerY - j) * (centerY - j);

                float distanceToCenter = (float)Math.sqrt(distanceX + distanceY);

                //Make sure this value ends up as a float and not an integer
                //If you're not outputting this to an image, get the correct 1.0 white on the furthest edges by dividing by half the map size, in this case 64. You will get higher than 1.0 values, so clamp them!
                distanceToCenter = distanceToCenter / furthestDistance;

                map[i][j] = map[i][j] * (1 - distanceToCenter);

            }
        }
    }

    private static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private static float roundUp(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_CEILING);
        return bd.floatValue();
    }

    private static float roundDown(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_FLOOR);
        return bd.floatValue();
    }

    public void setStage(Stage localStage) {

        stage = localStage;
        scene = stage.getScene();

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                ResizeCanvas();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                ResizeCanvas();
            }
        });
    }
}
