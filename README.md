# ImageLight
A Lightweight Image Viewer. Built using JavaFX

## Table of contents
* [About](#about)
* [Technologies](#technologies)
* [Screenshots](#screenshots)
* [How It Works](#how-it-works)
* [Running The Code Yourself](#running-the-code-yourself)
* [Thoughts on this project.](thoughts-on-this-project-&-what-i've-learned)

## About.
I decided to make an image viewer because it was something I would use. While Windows does come with the Photo's app it just has so many additional features I would never use. I wanted to try and make something similar to the discontinued "Windows Photo Viewer" that came with Windows XP, Visa & 7 where if I opened a image, it would just show me that image along with some navigation. I used a Model–view–controller design approach.

## Technologies. 
### Languages:
- Java 11

### JDK
- [AdoptOpenJDK 11.](https://adoptopenjdk.net/)

It is primilary built using [JavaFX 11.](https://openjfx.io/openjfx-docs/)
I decided to keep the JDK & Libaries at version 11 as they provide Long term support. 
With the recent changes to Oracle JDK distribution, I've decided to use the AdoptOpenJDK.

### Other
- Maven
I used the automation tool Maven to manage this project. 

# Screenshots.
Fullscreen with image loaded:
![Fullscreen](https://i.imgur.com/K9kigPY.jpg)

Windowed with image loaded:
![Windowed](https://i.imgur.com/yiEl3E0.jpg)

Title bar includes a description of the image:
![titlebar](https://i.imgur.com/QZQOUdH.jpg)


# How it works. 
I decided to use a Model-View-Controller design pattern. 
When the application is opened by the context menu (right click) it will take the first argument (which should be the file that wants to be opened) tests to see if the file exists and if it is a file and will open up the image. 

If its opened up normally, it will just display a default image. 

### Some examples of the code:

When a file is opened through the menu, we open up a file chooser:

Model.java 
```java

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
```

if we have a valid image file, we want to call 
```java
getDirectoryImages(selectedFile.getParent());
```
to get any other images in the folder so we can navigate to them.

Once we have an array of all image files in the folder, we then want to search the array once to find out the position of our current image. That way if we want to go to the previous image or next image we just need to subtract one or add one to our current index and return whatever that is. Example of getting the next image:

```java
currentFile = dirImages.get(++currentFileIndex);
```

In the example up above we also call:
```java
updateTitle();
```

I wanted to include a short description of the current image.

```java
public void updateTitle() {
        primaryStage.setTitle("ImageLight | " + currentFile + " | " + formatFileSize(currentFile.length()) + " | " + getDimensions());
    }
```

This displays the current directory of the image + the file size along with the unit of measurment (kb,mb,gb etc) along with the dimentions of the image. 

## Running the code yourself.
I haven't included a JAR yet but to run the code yourself, you should be able to just download it. All the maven dependandies are included in the POM file. 

## Future updates
Currently I am still working on this project. So will be adding to it over time.

## Thoughts on this project.
I really enjoyed making this partly because its something that I am going to use. There were some headaches. Especially when starting off with an error "JavaFX runtime components are missing" even when it was included. It turns out that Java was looking for dependencies on the module path and not the classpath. I fixed this by main launch the actual FX application class. (ImageLight.java launches JFXApplication.java)

Overall really enjoyed making it. 
