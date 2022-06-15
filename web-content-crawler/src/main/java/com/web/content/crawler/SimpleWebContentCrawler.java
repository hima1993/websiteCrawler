/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.content.crawler;

import java.io.File;

/**
 *
 * @author himaruksilva
 */
public class SimpleWebContentCrawler 
{
    private static final String WEBSITE_LINK = "https://tretton37.com/";
    private static final String LOCAL_DIRECTORY_PATH = System.getProperty("user.dir")+"/download_content";

    public static void main(String[] args) throws InterruptedException
    {
        // Thread for web content crawling
        Thread mainThread = new Thread(()->
        {
            File maindir = new File(LOCAL_DIRECTORY_PATH);
            
            if (!maindir.exists()) 
            {
                maindir.mkdir();
            }
            
            ContentCrawler contentCrawler = new ContentCrawler();
            ContentCrawlController contentCrawlController = new ContentCrawlController();
            contentCrawlController.crawlMainPage(WEBSITE_LINK+"/index.html", LOCAL_DIRECTORY_PATH+"/index.html", contentCrawler);
            contentCrawlController.crawlSubElements(WEBSITE_LINK, LOCAL_DIRECTORY_PATH, "index.html", contentCrawler);
        });
       
        // Thread for display the progress of the download
        Thread ProgresDisplayThread = new Thread(()->
        {
            ProgressDisplay.getInstance().show();
        });
        
        mainThread.setPriority(Thread.MAX_PRIORITY);
        ProgresDisplayThread.setPriority(Thread.MIN_PRIORITY);
        mainThread.start();
        ProgresDisplayThread.start();
    }
    
}
