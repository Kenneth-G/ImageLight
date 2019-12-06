package ImageLight;

import java.io.File;
import java.util.HashMap;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller {

    private Model model;
    private View view;
    private Scene scene;

    public Controller(View view, Model model, Scene scene) {
        this.model = model;
        this.view = view;
        this.scene = scene;

        addSceneListener();
        addButtonListener();
    }
    //Assign Keyboard Actions
    private Scene addSceneListener(){
        this.scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.LEFT){
                    getPreviousImage();
                }
                if(event.getCode() == KeyCode.RIGHT){
                    getNextImage();
                }
            }
        });
        return scene;
    }

    //Assign Menu Button Actions
    private void addButtonListener(){
        HashMap<String, Button> buttonMap = view.getButtonMap();

        buttonMap.get("menuOpen").setOnAction(e -> OpenFile());
        buttonMap.get("menuBack").setOnAction(e -> getPreviousImage());
        buttonMap.get("menuForward").setOnAction(e -> getNextImage());
        buttonMap.get("menuProject").setOnAction(e -> displayProjectInfo());
        
    }
    
    //Assign Hyperlink Actions. Both point to the same function and we find out which URL to open by getting the text of the hyperlink.
    private void addLinkListener(){
        HashMap<String, Hyperlink> linkMap = view.getLinkMap();

        linkMap.get("projectLink").setOnAction(e -> openURL(linkMap.get("projectLink")));
        linkMap.get("iconLink").setOnAction(e -> openURL(linkMap.get("iconLink")));
    }

    //Open a file. If the user clicks to open a file but then closes the window. We want to make sure there isn't a null file passed.
    private void OpenFile(){
        Boolean navigation = false;

        File file = model.getFile();
        if(file != null){
            view.setImage(file);

            //We want to only enable navigation after we've opened a folder.
            if(navigation == false){
                view.enableNavigation();
            }
        }
    }

    //getPreviousImage & getNextImage both call the model to get the next image and then we call the view to set the image we got. Then update the title with the image info. 
    private void getPreviousImage(){
        view.setImage(model.getPreviousImage());
        model.updateTitle();
    }

    private void getNextImage(){
        view.setImage(model.getNextImage());
        model.updateTitle();
        
    }

    //Display a window with project information
    private void displayProjectInfo(){
        view.projectWindow(model.getPrimaryStage());
        addLinkListener();
    }

    private void openURL(Hyperlink link){
        model.openURL(link.getText());
    }
}