package helper;

import arau.C195_project.DataApplication195;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/** Class for switching scenes. */
public abstract class SceneSwitcher {

    public static Stage stage;

    public static Parent scene;

    /** Method for switching scenes.
     @param event The ActionEvent that initiated the scene change.
     @param source The Resource (FXML file) to be loaded.
     */
    public static void sceneSwitch(ActionEvent event, String source) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load((DataApplication195.class.getResource(source)));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
