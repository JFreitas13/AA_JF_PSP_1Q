package com.svalero.multidescarga;

import com.svalero.multidescarga.controller.AppController;
import com.svalero.multidescarga.util.R;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        // Comentar para descarga desde fichero.
        loader.setLocation(R.getUI("multidownload.fxml"));
        // Quitar comentario para descarga desde fichero.
        //loader.setLocation(R.getUI("multidownload-2.fxml"));
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

