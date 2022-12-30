package com.svalero.multidescarga.controller;

import com.svalero.multidescarga.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppController {

    public TextField tfUrl;
    public Button btDownload;
    public TabPane tpDownloads;

    private Map<String, DownloadController> allDownloads;

    public AppController() {
        allDownloads = new HashMap<>();
    }

    @FXML
    public void launchDownload(ActionEvent event) {
        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();
        launch(urlText);
    }

    private void launch(String url) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUI("download.fxml"));

            DownloadController downloadController = new DownloadController(url);
            loader.setController(downloadController);
            VBox downloadBox = loader.load();

            String filename = url.substring(url.lastIndexOf("/") + 1);
            tpDownloads.getTabs().add(new Tab(filename, downloadBox));

            allDownloads.put(url, downloadController);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    public void stopAllDownloads() {
        for (DownloadController downloadController : allDownloads.values())
            downloadController.stop();
    }

    @FXML
    public void readDLC() {
        // Quitar comentario para descarga desde fichero.
        /*
        try {
            File dlcFile = new File("dlc.txt");
            Scanner reader = new Scanner(dlcFile);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                System.out.println(data);
                launch(data);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Se ha producido un error");
            e.printStackTrace();
        }
        */

        // Pedir el fichero al usuario (FileChooser)

        // Leo el fichero y cargo cada linea en un List (clasmvne Files)

        // Para cada linea, llamar al método launchDownload
    }
}

