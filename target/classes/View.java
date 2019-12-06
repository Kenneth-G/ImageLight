package ImageLight;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;




public class View{

    // Used to divide the vertical size of each element in root.
    private Double windowHeight;

    /* 
        The UI is divided up into 3 sections and contained in a VBox (var root). 
        The menu is a Hbox(var menu) where we can put horizontal Buttons across the window
        Where we actually view the image(var imageView) is contained within a stackpane (var imageWindow)
        The bottom (var bottom) bar will be used for additional information. 
    */
    private HBox menu;
    private ImageView imageView = new ImageView();
    private VBox root = new VBox();

    //I want to store the buttons in a dictionary so when want to access them from the countroller, I can just access them by name.
    HashMap<String, Button> buttonMap = new HashMap<String, Button>();
    HashMap<String, Hyperlink> linkMap = new HashMap<String, Hyperlink>();


    public View(Double windowHeight, File defaultImage){
        this.windowHeight = windowHeight;

        //I've put the creation of each menu in their own methods as element can have multiple settings and it could start to look messy.
        menu = createMenu();
        StackPane imageWindow = createImageWindow(defaultImage);

        setImage(defaultImage);

        //root settings
        root.getChildren().addAll(menu, imageWindow);
        root.setVgrow(imageWindow, Priority.SOMETIMES);
        root.setStyle("-fx-background-color: #303030;");
    }

    private HBox createMenu(){
        menu = new HBox();

        menu.setPrefHeight(((windowHeight/100) *5));
        menu.setFillHeight(false);
        //menu.setStyle("-fx-background-color: #202020;");
        
        //Create our Menu buttons then add them to our Menu bar & add to our button map to be accessed by the controller.
        Button menuOpen = createMenuButton(new Image(getClass().getResourceAsStream("/assets/open.png"),30,30,true,true));
        Button menuBack = createMenuButton(new Image(getClass().getResourceAsStream("/assets/back2.png"), 30, 30, true, true));
        Button menuForward = createMenuButton(new Image(getClass().getResourceAsStream("/assets/forward2.png"), 30, 30, true, true));

        menuBack.setDisable(true);
        menuForward.setDisable(true);

        Pane spacer = new Pane();

        Button menuProject = createMenuButton(new Image(getClass().getResourceAsStream("/assets/information.png"), 30, 30, true, true));

        buttonMap.put("menuOpen", menuOpen);
        buttonMap.put("menuBack", menuBack);
        buttonMap.put("menuForward", menuForward);
        buttonMap.put("menuProject", menuProject);
        
        menu.getChildren().addAll(menuOpen, menuBack, menuForward, spacer, menuProject);
        menu.setHgrow(spacer, Priority.ALWAYS);

        
        return menu;
    }

    //Method for creating how we want each button to look. We pretty much want them to all to look the same apart from the icon changing.
    private Button createMenuButton(Image icon){
        Button menuButton = new Button();
        menuButton.setGraphic(new ImageView(icon));
        //menuButton.setPadding(new Insets(10,10,10,10));
        menuButton.setStyle("-fx-background-color: transparent;");
        //I want to use the arrow keys to go forward and back so we need to make it so the arrow keys aren't used to traverse buttons.
        menuButton.setFocusTraversable(false);

        return menuButton;
    }

    private StackPane createImageWindow(File defaultImage){
        StackPane imageWindow = new StackPane();
        imageWindow.setMaxHeight(root.getHeight()-40);
        imageWindow.getChildren().add(imageView);
        imageWindow.setPrefHeight(windowHeight-100);
        
        imageView.fitHeightProperty().bind(root.heightProperty());
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.setPreserveRatio(true);
        
        return imageWindow;
    }

    //Set an image
    public void setImage(File selectedFile){
        FileInputStream input;
        try {
            if(selectedFile != null){
                input = new FileInputStream(selectedFile);
                Image newImage = new Image(input);
                imageView.setImage(newImage);
            }else{
                imageView.setImage(new Image(getClass().getResourceAsStream("/assets/default-image.png")));
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    // Small window to display information about the project. 
    //Todo: Update layout.
    public void projectWindow(Stage primaryStage){
        Stage projectStage = new Stage();

        // Project information
        Text title = new Text("\n   ImageLight\n");
        title.setFill(Color.web("#3ae3f2"));

        Text projectBy = new Text("\n   Project by: Kenneth Gargan");
        projectBy.setFill(Color.WHITE);

        Text projectInfo = new Text("\n   Github Link: ");
        projectInfo.setFill(Color.WHITE);
        Hyperlink projectLink = new Hyperlink("https://www.google.ie/");
        projectLink.setTextFill(Color.web("#3ae3f2"));

        Text iconInfo = new Text("\n   Icon's used: ");
        iconInfo.setFill(Color.WHITE);
        Hyperlink iconLink = new Hyperlink("https://www.yahoo.ie/");
        iconLink.setBorder(Border.EMPTY);
        iconLink.setTextFill(Color.web("#3ae3f2"));        
        //
        
        //need to assign actions for when they are clicked
        linkMap.clear();
        linkMap.put("iconLink", iconLink);
        linkMap.put("projectLink", projectLink);

        //Add our text to a textflow
        TextFlow textFlow = new TextFlow(
            title, projectBy, projectInfo, projectLink, iconInfo, iconLink
        );

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #303030;");

        borderPane.setCenter(textFlow);

        Scene scene = new Scene(borderPane);
        projectStage.setScene(scene);
        projectStage.initOwner(primaryStage);
        projectStage.initModality(Modality.WINDOW_MODAL);
        projectStage.setHeight(300);
        projectStage.setWidth(400);
        projectStage.setTitle("ImageLight | Project Info");
        projectStage.show();
    }
    public VBox getRoot(){
        return root;
    }
    public HBox getMenu(){
        return menu;
    }
    public HashMap<String,Button> getButtonMap(){
        return buttonMap;
    }
    public ImageView getImageWindow(){
        return imageView;
    }
    public HashMap<String,Hyperlink> getLinkMap(){
        return linkMap;
    }
    public void enableNavigation(){
        buttonMap.get("menuBack").setDisable(false);
        buttonMap.get("menuForward").setDisable(false);
    }
}
