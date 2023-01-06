package com.svalero.multidescarga;

import com.svalero.multidescarga.controller.AppController;
import com.svalero.multidescarga.util.R;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

//clase main. Se extiende de Application para indicar que es una aplicación y el usuario interactuará en ella.
public class App extends Application {
    @Override
    public void init() throws Exception {
        super.init();
    }

    //Metodo para iniciar APP
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("multidownload.fxml"));
        loader.setController(new AppController());
        ScrollPane vbox = loader.load();

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Multi Descarga");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}

