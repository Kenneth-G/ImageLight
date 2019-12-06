package ImageLight;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Model {

    private Stage primaryStage;

    /*
     * We want to save the current file & the index of the current file in our
     * arraylist. This is so if the user wants to see the previous image or the next
     * image we don't need to keep searching the array to see where the current file
     * is, we can just increment or decrement the currentFileIndex, check to see if
     * its a valid index and then return the file. This seems like a faster approach
     * especially with folders that may have 100's of images.
     * 
     */
    private File currentFile;
    private int currentFileIndex;
    ArrayList<File> dirImages = new ArrayList<File>();

    // The current stage is needed in case we want to open up any windows.
    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Let the user pick a file then we want to grab a list of the current files in
    // the directory for the back and forward options
    public File getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Supported Image Formats", "*.bmp", "*.jpg", "*.jpeg,", "*.png"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            currentFile = selectedFile;
            getDirectoryImages(selectedFile.getParent());
            updateTitle();
            return selectedFile;
        }

        return null;
    }

    // Whenever a new file is opened, we want to grab all the image files in that
    // folder.
    public void getDirectoryImages(String dir) {
        File directory = new File(dir);
        dirImages.clear();

        // File.listFiles() lists them alphabetically but since they are in an arry I
        // can sort them by most recent/oldest/size etc.
        for (File file : directory.listFiles()) {

            // Check to see if it has an extension first of all. All images should so we
            // don't really want any files that don't.
            int fileType = file.getName().lastIndexOf(".");
            if (fileType != -1) {

                if (checkValidExtension(file.getName().substring(fileType))) {

                    dirImages.add(file);
                }
            }
        }
        currentFileIndex = dirImages.indexOf(currentFile);
    }

    // Simple Method to check to see if a passed extension is of the file type we
    // want.
    private boolean checkValidExtension(String extension) {
        String[] extensions = new String[] { ".bmp", ".jpg", ".jpeg,", ".png" };

        if (Arrays.asList(extensions).contains(extension)) {
            return true;
        } else {
            return false;
        }
    }

    public File getPreviousImage() {

        if (currentFileIndex == 0) {
            return currentFile;
        }
        if (currentFileIndex == -1) {
            return currentFile;
        }

        currentFile = dirImages.get(--currentFileIndex);

        return currentFile;
    }

    public File getNextImage(){

        if (currentFileIndex == dirImages.size() - 1) {
            return currentFile;
        }
        if (currentFileIndex == -1) {
            return currentFile;
        }

        currentFile = dirImages.get(++currentFileIndex);
        return currentFile;
    }

    public void updateTitle() {
        primaryStage.setTitle("ImageLight | " + currentFile + " | " + formatFileSize(currentFile.length()) + " | " + getDimensions());
    }

    // I was looking for a clean way to display the file size in human readable
    // format. A lot of posts online were suggesting to include a maven repository
    // but I thought 1 function just looks cleaner.
    private static String formatFileSize(long size) {
        String fileSize = null;

        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            fileSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            fileSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            fileSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            fileSize = dec.format(k).concat(" KB");
        } else {
            fileSize = dec.format(b).concat(" Bytes");
        }

        return fileSize;
    }

    private String getDimensions() {
        try {
            BufferedImage img = ImageIO.read(currentFile);
            return "" + Integer.toString(img.getWidth()) + " x " + Integer.toString(img.getHeight()) + " px";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public void openURL(String text){
        try {
            java.awt.Desktop.getDesktop().browse(new URI(text));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
}