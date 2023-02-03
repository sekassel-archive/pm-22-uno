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
    }

    @Override
    public Parent render() throws IOException {
        final Parent parent = FXMLLoader.load(Main.class.getResource("view/Setup.fxml"));
        final Button playButton = (Button) parent.lookup("#buttonPlay");
        final TextField nameField = (TextField) parent.lookup("#nameField");

        playButton.setOnAction(event -> {
            app.show(new IngameController(app));
        });

        System.out.println(CARD_COLOR.YELLOW.getColor());

        return parent;
    }

    @Override
    public void destroy() { 
    }
    
}
