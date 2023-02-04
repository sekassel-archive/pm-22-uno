package de.uniks.pmws2223.uno.controller;

import java.io.IOException;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameOverController implements Controller{

    private final App app;
    private final String winnerName;
    public GameOverController(App app, String winnerName) {
        this.app = app;
        this.winnerName = winnerName;
    }

    @Override
    public String getTitle() {
        return "Uno - Game Over";
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Parent render() throws IOException {
        final Parent parent = FXMLLoader.load(Main.class.getResource("view/GameOver.fxml"));
        Label resultLabel = (Label) parent.lookup("#labelResult");
        Button backToSetup = (Button) parent.lookup("#buttonLeave");
        backToSetup.setOnMouseClicked(event -> app.show(new SetupController(app)));
        resultLabel.setText(winnerName + " has won the Game");
        return parent;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }
    
}
