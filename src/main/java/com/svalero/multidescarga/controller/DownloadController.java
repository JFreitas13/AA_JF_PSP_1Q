package com.svalero.multidescarga.controller;

import com.svalero.multidescarga.task.DownloadTask;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private int timeOut;
    private static final Logger logger = LogManager.getLogger(DownloadController.class);

    public DownloadController(String urlText, int timeOut) {
        logger.info("Descarga " + urlText + " creada");
        this.urlText = urlText;
        this.timeOut = timeOut;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Directorio del usuario y carpeta por defecto del usuario
        String downloadsFolder = System.getProperty("user.dir") + File.separator + "Downloads" + File.separator;

        //Pintamos por defecto el directorio anterior
        tfUrl.setText(downloadsFolder);
        directoryChooser.setInitialDirectory(new File(downloadsFolder));
    }

    //Metodo para iniciar download
    @FXML
    public void start(ActionEvent event) {
        try {
            //cogemos el directorio del label
            File downloadFile = new File(tfUrl.getText(), urlText.substring(urlText.lastIndexOf("/") + 1));


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

            //Timer indicado para inicializar la descarga
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            new Thread(downloadTask).start(); //Se crea el Thread con la tarea de descarga
                        }
                    },
                    1000*this.timeOut
            );


        } catch (MalformedURLException murle) {
            murle.printStackTrace();
            logger.error("URL mal formada", murle.fillInStackTrace());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }


    //Metodo para cambiar ruta donde se guarda el download
    public void changeRoute(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser(); //Clase para buscar y selecionar un directorio
        File file = directoryChooser.showDialog(tfUrl.getScene().getWindow()); //Creamos un fichero con la ruta del directorio seleccionado
        route = file.getPath(); //Ingresamos la ruta dentro del String
        tfUrl.setText(route); //devolvemos el string al label
    }

    //Metodo para parar el Download
    @FXML
    public void stop(ActionEvent event) {
        stop();
    }

    public void stop() {
        if (downloadTask != null)
            downloadTask.cancel();
    }

    //Metodo para coger la url de la caja de texto
    public String getUrlText() {
        return urlText;
    }
}
