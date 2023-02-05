package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.service.AnimationService;
import de.uniks.pmws2223.uno.service.GameService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.io.IOException;

import static de.uniks.pmws2223.uno.Constants.SETUP_TITLE;

public class SetupController implements Controller {
    private final App app;

    public SetupController(App app) {
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
            AnimationService animationService = new AnimationService();
            GameService gameService = new GameService(animationService);
            app.show(new IngameController(app, gameService.generateGame((int) botSlider.getValue(), nameField.getText()), gameService, animationService));
        });

        return parent;
    }

    @Override
    public void destroy() {
    }

}
