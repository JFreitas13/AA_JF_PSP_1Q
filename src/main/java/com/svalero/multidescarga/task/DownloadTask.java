package com.svalero.multidescarga.task;

import com.svalero.multidescarga.controller.DownloadController;
import javafx.concurrent.Task;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends Task<Integer> {
    private URL url;
    private File file;
    private String message;
    private static final Logger logger = (Logger) LogManager.getLogger(DownloadController.class);

    public DownloadTask(String urlText, File file) throws MalformedURLException {
        this.url = new URL(urlText);
        this.file = file;
    }

    @Override
    protected Integer call() throws Exception {
        logger.trace("Descarga " + url.toString() + " iniciada");
        updateMessage("Conectando con el servidor . . .");

        URLConnection urlConnection = url.openConnection();
        double fileSize = urlConnection.getContentLength();
        double fileSizeMb = fileSize / 1045876; //para saber el tamaño en MB del archivo

        //guardamos en disco
        BufferedInputStream in = new BufferedInputStream(url.openStream());

        //archivo donde se guarda la descarga
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        int totalRead = 0;
        double downloadProgress = 0;

        //Medir tiempo de descarga
        StopWatch watch = new StopWatch();
        watch.start();

        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            downloadProgress = ((double) totalRead / fileSize);
            double totalSizeReadMb = totalRead / 1048576; //cantidad de MB leídos y descargados
            updateProgress(downloadProgress, 1);
            updateMessage(Math.round(downloadProgress * 100) + " %\t\t\t" + Math.round(watch.getTime()/1000) + "seg.\t\t\t" + Math.round(totalSizeReadMb * 100) / 100d + "MB / " + (Math.round(fileSizeMb) * 100) / 100d + "MB");
            // Comentar para acelerar la descarga.
            Thread.sleep(1);

            fileOutputStream.write(dataBuffer, 0, bytesRead);

            //actualizar el total leído
            totalRead += bytesRead;

            if (isCancelled()) {
                logger.trace("Descarga " + url.toString() + " cancelada");
                return null;
            }
        }

        updateProgress(1, 1);
        updateMessage("100 %");

        logger.trace("Descarga " + url.toString() + " finalizada");

        return null;
    }
}

