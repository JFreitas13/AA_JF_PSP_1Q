package com.svalero.multidescarga.controller;

import com.svalero.multidescarga.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppController {

    //Elementos de la interface a los que accederemos
    public TextField tfUrl; //caja de texto
    public Button btDownload; //boton download
    public TabPane tpDownloads; //panel de descargas para que se añadan pestañas
    private Map<String, DownloadController> allDownloads; //mapa donde se guardaran todas las descargas

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

    //metodos de descarga
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

    //cancelar todos los downloads
    @FXML
    public void stopAllDownloads() {
        for (DownloadController downloadController : allDownloads.values())
            downloadController.stop();
    }

    //creación txt con registro log
    public void logFile(ActionEvent actionEvent) {

        if(Desktop.isDesktopSupported()) {
            try {
                File file = new File( "multidownload.log");
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
                } catch (IOException ioe){
                    System.out.println("Ha ocurrido un error.");
                }
            } else {
                System.out.println("No soportado.");
        }
    }
}

