/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.content.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import static com.web.content.crawler.utils.LinkUtil.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author himaruksilva
 */
public class ContentCrawlController extends Thread {
    private static final String LOCAL_DIRECTORY_PATH = System.getProperty("user.dir") + "/download_content";


    private final ContentCrawler contentCrawler;
    private final String website;
    private final String fileName;
    private final ExecutorService service = Executors.newCachedThreadPool();
    private final String REGEX_FOR_GET_HREF_SRC_VALUE = "\\s*(?:href|src)\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

    private static final Logger logger = Logger.getLogger(ContentCrawlController.class.getName());

    public ContentCrawlController(String website, String fileName) 
    {
        this.website = website;
        this.fileName = fileName;
        this.contentCrawler = new ContentCrawler();
    }

    @Override
    public void run() 
    {
        this.setPriority(Thread.MAX_PRIORITY);
        createFolder(LOCAL_DIRECTORY_PATH);
        ProgressDisplay.getInstance().start();
        crawlMainPage(website + "/" + fileName, LOCAL_DIRECTORY_PATH + "/" + fileName);
        crawlSubElements(website, LOCAL_DIRECTORY_PATH, fileName);
    }

    private void createFolder(String path) 
    {
        File file = new File(path);
        if (!file.exists()) 
        {
            file.mkdir();
        }
    }

    public void crawlMainPage(String webPage, String filePath) 
    {
        InputStream inputStream = getInputStreamFromFile(webPage);
        Thread thread = new Thread(new CrawlingTask(inputStream, filePath));
        thread.start();
        
        try 
        {
            thread.join();
        } 
        catch (InterruptedException ex) 
        {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private List<String> webLinks(String filePath, String fileName) 
    {
        return getContent(filePath + "/" + fileName)
                .or(Optional::empty)
                .map(it -> contentCrawler.extractWebLinks(it, REGEX_FOR_GET_HREF_SRC_VALUE))
                .orElseGet(ArrayList::new);
    }

    public void crawlSubElements(String webPage, String filePath, String fileName) 
    {
        var webLinks = getValidLinkList(webLinks(filePath, fileName));
        ProgressDisplay.getInstance().setTotalAmountToDownload(webLinks.size());
      
        int count = 1;
        while (count <= webLinks.size())
        {
            String link = webLinks.get(count - 1);

            if (link.charAt(0) == '/') 
            {
                link = link.substring(1);
            }

            String[] linkComponents = link.split("/");
            StringBuilder traversalPath = new StringBuilder(filePath);
            for (int i = 0; i < linkComponents.length; i++) 
            {
                traversalPath.append("/").append(linkComponents[i]);

                if (i != linkComponents.length - 1) 
                {
                    createFolder(traversalPath.toString());
                    continue;
                }

                String sanitizedLink = removeGetParameters(link);
                String remotePath = webPage + "/" + sanitizedLink;
                String localPath = filePath + "/" + sanitizedLink;
                InputStream inputStream = getInputStreamFromFile(remotePath);

                if (isRemoteFileNameSavable(remotePath, localPath)) 
                {
                    service.execute(new CrawlingTask(inputStream, localPath));
                   // contentCrawler.saveContent(inputStream, localPath);
                }
            }
            count++;
        }
        
        service.shutdown();
        try 
        {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } 
        catch (InterruptedException ex) 
        {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }


    public boolean isRemoteFileNameSavable(String remotePath, String localPath) 
    {
        String remoteFileName = remotePath.substring(remotePath.lastIndexOf('/') + 1);
        String localFileName = localPath.substring(localPath.lastIndexOf('/') + 1);

        if (!Objects.equals(remoteFileName, localFileName)) 
        {
            return false;
        }

        return true;
    }

    public InputStream getInputStreamFromFile(String remotePath) 
    {
        try 
        {
            URL url = new URL(remotePath);
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            return httpConnection.getInputStream();
        } 
        catch (IOException ex) 
        {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return null;
    }

    public Optional<String> getContent(String filePath) 
    {
        try (FileInputStream fileStream = new FileInputStream(filePath)) 
        {
            return Optional.of(IOUtils.toString(fileStream, StandardCharsets.UTF_8));
        } 
        catch (Exception ex) 
        {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return Optional.empty();
    }


}
