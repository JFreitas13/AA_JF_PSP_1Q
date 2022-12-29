package com.svalero.multidescarga.controller;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AppController {

    public TextField tfUrl;
    public Button btDownload;
    //public TabPane tpDownloads;

    private Map<String, DownloadController> allDownloads;

    public AppController() {
        allDownloads = new HashMap<>();
    }
}
