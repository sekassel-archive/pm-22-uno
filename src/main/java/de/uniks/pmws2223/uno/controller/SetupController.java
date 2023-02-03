package de.uniks.pmws2223.uno.controller;

import java.io.IOException;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;

import static de.uniks.pmws2223.uno.Constants.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SetupController implements Controller{
    private final App app;

    public SetupController(App app){
        this.app = app;
    }

    @Override
    public String getTitle() {
        return SETUP_TITLE;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Parent render() throws IOException {
        final Parent parent = FXMLLoader.load(Main.class.getResource("view/Setup.fxml"));
        final Button playButton = (Button) parent.lookup("#buttonPlay");
        final TextField nameField = (TextField) parent.lookup("#nameField");

        playButton.setOnAction(event -> {
            app.show(new IngameController());
        });

        return parent;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }
    
}
