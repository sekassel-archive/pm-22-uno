package de.uniks.pmws2223.uno.controller;

import java.io.IOException;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.service.BotService;
import de.uniks.pmws2223.uno.service.GameService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import static de.uniks.pmws2223.uno.Constants.*;

public class IngameController implements Controller{

    private final App app;
    private final BotService botService = new BotService();
    private final GameService gameService = new GameService();

    public IngameController(App app){
        this.app = app;
    }

    @Override
    public String getTitle() {
        return INGAME_TITLE;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Parent render() throws IOException {
        final Parent parent = FXMLLoader.load(Main.class.getResource("view/Ingame.fxml"));
        return parent;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }
    
}
