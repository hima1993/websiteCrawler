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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author himaruksilva
 */
public class ContentCrawlController 
{
    private final String REGEX_FOR_GET_HREF_SRC_VALUE = "\\s*(?:href|src)\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

    public boolean crawlMainPage(String webPage, String filePath, ContentCrawler contentCrawler)
    {
        InputStream inputStream =  getInputStreamFromFile(webPage);
        return contentCrawler.saveContent(inputStream, filePath);
    }
    
    public void crawlSubElements(String webPage, String filePath,String fileName, ContentCrawler contentCrawler) 
    {
        String fileContent = getContent(filePath+"/"+fileName);
        ArrayList<String> webLinks = contentCrawler.extractWebLinks(fileContent, REGEX_FOR_GET_HREF_SRC_VALUE);
        
        int count = 1;
        
        while (count <= webLinks.size()) 
        {
            String link = webLinks.get(count-1);

            if (link != null && link.length() > 1) 
            {
                if (isLinkCanBeCrawl(link)) 
                {
                    if (link.charAt(0) == '/') 
                    {
                        link = link.substring(1);
                    }

                    String[] linkComponents = link.split("/");
                    String traversalPath = filePath;
                    
                    for (int i = 0; i < linkComponents.length; i++) 
                    {
                        traversalPath = traversalPath + "/" + linkComponents[i];
                        if (i == linkComponents.length - 1) 
                        {
                            if (link.contains("?")) {

                                link = link.split("\\?")[0];
                            }
                            String remotePath = webPage + "/" + link;
                            String localPath = filePath+"/"+link;
                            InputStream inputStream =  getInputStreamFromFile(remotePath);

                            if(isRemoteFileNameSavable(remotePath, localPath))
                            {
                                contentCrawler.saveContent(inputStream, localPath);
                            }
                        } 
                        else 
                        {
                            File maindir = new File(traversalPath);
                            if (!maindir.exists()) 
                            {
                                maindir.mkdir();
                            }
                        }
                    }
                }
            }

            ProgressDisplay.getInstance().setCurrentAmountToDownload(count++);
        }
    }
    
    public boolean isLinkCanBeCrawl(String link)
    {
        return !(link.contains("https") || link.contains("http") || link.contains(".net") | link.contains(".com"));
    }
    
    
    public boolean isRemoteFileNameSavable(String remotePath, String localPath)
    {
        String remoteFileName = remotePath.substring(remotePath.lastIndexOf('/') + 1);
        String localFileName = localPath.substring(localPath.lastIndexOf('/') + 1);

        if(remoteFileName == null ? localFileName != null : !remoteFileName.equals(localFileName))
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
            Logger.getLogger(ContentCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
  
        return null;
    }
    
    public String getContent(String filePath)
    {
        String content = "";
        try(FileInputStream fileStream = new FileInputStream(new File(filePath)))
        {
            content = IOUtils.toString(fileStream, "UTF-8");
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(ContentCrawlController.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }

        return content;
    }
}
