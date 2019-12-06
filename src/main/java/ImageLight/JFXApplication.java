package ImageLight;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class JFXApplication extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Pass in the args in case its launched with a file.
        List<String> list = getParameters().getRaw();
        File contextFile = new File("");

        if(!list.isEmpty()){
            contextFile = new File(list.get(0));
        }
        
        // Get the screen size and create the size we want to set our window.
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        Double windowWidth = primaryScreenBounds.getWidth() /1.5;
        Double windowHeight = primaryScreenBounds.getHeight() /1.5;
         
        //I'm using a MVC design pattern. The controller will control how the input from the view will control the data going into the model and then update the view to reflect the change. 
        //If the user has right click -> open an image, we want to pass the file they opened into the view.
        //We use a ternary operator to decide if we have been passed in a valid file. 

        File defaultImage = (contextFile.isFile()) ? contextFile : null;
        View view = new View(windowHeight, defaultImage);
        Model model = new Model();
        Scene scene = new Scene(view.getRoot(), windowWidth, windowHeight);
        new Controller(view, model, scene);

        //I also need to pass in the stage object that I've created in case I want to open up any additional windows (like a file viewer)
        model.setStage(primaryStage);

        //Primary Stage Settings
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setWidth(windowWidth);
        primaryStage.setHeight(windowHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ImageLight");
        primaryStage.show();
    }
}