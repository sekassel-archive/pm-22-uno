package de.uniks.pmws2223.uno.controller;

import java.io.IOException;

import de.uniks.pmws2223.uno.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class GameOverController implements Controller{

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Parent render() throws IOException {
        final Parent parent = FXMLLoader.load(Main.class.getResource("view/GameOver.fxml"));
        return parent;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }
    
}
