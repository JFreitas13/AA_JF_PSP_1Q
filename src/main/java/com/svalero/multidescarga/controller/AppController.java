package com.svalero.multidescarga.controller;

import com.svalero.multidescarga.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AppController {

    //Elementos de la interface a los que accederemos
    public TextField tfUrl; //caja de texto
    public Button btDownload; //boton download
    public TextField tfTime; //caja texto para indicar tiempo para programar la descarga
    public TabPane tpDownloads; //panel de descargas para que se añadan pestañas
    private Map<String, DownloadController> allDownloads; //mapa donde se guardaran todas las descargas

    public AppController() {
        allDownloads = new HashMap<>();
    }

    //acion de descargar url indicada
    @FXML
    public void launchDownload(ActionEvent event) {
        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();
        launch(urlText);
    }

    //accion para descargar desde fichero
    @FXML
    public void launchDLCDownload(ActionEvent event) {
        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();
        readDLC();
    }

    //metodos de descarga
    @FXML
    private void launch(String url) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUI("download.fxml"));
            String timeSchedule = tfTime.getText(); //cogemos el valor indicado
            //Valor por defecto si el usuario no indica
            if (timeSchedule.length() == 0)
                timeSchedule = "0";
            int timeDownload = Integer.parseInt(timeSchedule);

            DownloadController downloadController = new DownloadController(url, timeDownload);
            loader.setController(downloadController);
            VBox downloadBox = loader.load();

            String filename = url.substring(url.lastIndexOf("/") + 1);
            tpDownloads.getTabs().add(new Tab(filename, downloadBox));

            allDownloads.put(url, downloadController);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    //metodo para descargar desde fichero
    @FXML
    public void readDLC() {
        try {
            FileChooser fileChooser = new FileChooser(); // FileChooser: ventana para buscar un fichero por windows
            File dlcFile = fileChooser.showOpenDialog(tfUrl.getScene().getWindow()); // Aparece una ventana para buscar el archivo que queremos leer
            if (dlcFile == null)
                return;
            // FIN Para preguntarnos donde esta el fichero que queremosusar para leerlo y descargar su contenido

            Scanner reader = new Scanner(dlcFile);
            while (reader.hasNextLine()) { //leemos las lineas del fichero
                String data = reader.nextLine();
                System.out.println(data);
                launch(data);
            }
            reader.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Se ha producido un error");
            fnfe.printStackTrace();
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

