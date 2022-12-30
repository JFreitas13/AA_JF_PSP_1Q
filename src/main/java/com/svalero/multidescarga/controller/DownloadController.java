package com.svalero.multidescarga.controller;

import com.svalero.multidescarga.task.DownloadTask;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;


public class DownloadController implements Initializable {

    public TextField tfUrl;
    public Label lbStatus;
    public ProgressBar pbProgress;
    private String urlText;
    private DownloadTask downloadTask;
    private DirectoryChooser directoryChooser = new DirectoryChooser();
    private String route;
    private static final Logger logger = LogManager.getLogger(DownloadController.class);

    public DownloadController(String urlText) {
        logger.info("Descarga " + urlText + " creada");
        this.urlText = urlText;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Directorio del usuario y carpeta de descargas
        String downloadsFolder = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;

        //Pintamos por defecto el directorio anterior
        tfUrl.setText(downloadsFolder);
        directoryChooser.setInitialDirectory(new File(downloadsFolder));
    }

    @FXML
    public void start(ActionEvent event) {
        try {
            //cogemos el directorio del label
            File downloadFile = new File(tfUrl.getText(), urlText.substring(urlText.lastIndexOf("/") + 1));

            if (downloadFile == null) {
                return;
            }
            downloadTask = new DownloadTask(urlText, downloadFile);

            pbProgress.progressProperty().unbind();
            pbProgress.progressProperty().bind(downloadTask.progressProperty());

            downloadTask.stateProperty().addListener((observableValue, oldState, newState) -> {
                System.out.println(observableValue.toString());
                if (newState == Worker.State.SUCCEEDED) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("La descarga ha terminado");
                    alert.show();
                }
            });

            downloadTask.messageProperty()
                    .addListener((observableValue, oldValue, newValue) -> lbStatus.setText(newValue));

            new Thread(downloadTask).start();
        } catch (MalformedURLException murle) {
            murle.printStackTrace();
            logger.error("URL mal formada", murle.fillInStackTrace());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void changeRoute(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser(); //Clase para buscar y selecionar un directorio
        File file = directoryChooser.showDialog(tfUrl.getScene().getWindow()); //Creamos un fichero con la ruta del directorio seleccionado
        route = file.getPath(); //Ingresamos la ruta dentro del String
        tfUrl.setText(route); //devolvemos el string al label
    }
    @FXML
    public void stop(ActionEvent event) {
        stop();
    }

    public void stop() {
        if (downloadTask != null)
            downloadTask.cancel();
    }

    public String getUrlText() {
        return urlText;
    }
}
