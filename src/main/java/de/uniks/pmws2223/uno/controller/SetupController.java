package de.uniks.pmws2223.uno.controller;

import java.io.IOException;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.service.GameService;

import static de.uniks.pmws2223.uno.Constants.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
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
        final Slider botSlider = (Slider) parent.lookup("#botSlider");

        playButton.setOnAction(event -> {
            GameService gameService = new GameService();
            app.show(new IngameController(app, gameService.generateGame((int) botSlider.getValue(), nameField.getText()), gameService));
        });

        return parent;
    }

    @Override
    public void destroy() { 
    }
    
}
