/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.content.crawler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author himaruksilva
 */
public class ContentCrawlControllerTest 
{
    @Test
    public void saveContentCorrectRemoteFileName()
    {
        //given
        ContentCrawlController contentCrawlController = new ContentCrawlController();
        String correctRemotePath = "https://tretton37.com/assets/css/main.css";
        String correctLocalPath = System.getProperty("user.dir")+"/download_content/main.css";

        
        //when
        boolean output = contentCrawlController.isRemoteFileNameSavable(correctRemotePath, correctLocalPath);
        
        
        //then
        assertEquals(output,true);
    }
    
    @Test
    public void saveContentInCorrectRemoteFileName()
    {
        //given
        ContentCrawlController contentCrawlController = new ContentCrawlController();
        String correctRemotePath = "https://tretton37.com/assets/css/incorrect.css";
        String correctLocalPath = System.getProperty("user.dir")+"/download_content/main.css";

        
        //when
        boolean output = contentCrawlController.isRemoteFileNameSavable(correctRemotePath, correctLocalPath);
        
        
        //then
        assertEquals(output,false);
    }
    
    @Test
    public void getContentFromCorrectFilePath()
    {     
        //given
        String cssString = "body {background-color: powderblue;}\nh1   {color: blue;}\np{color: red;}";
        InputStream targetStream = new ByteArrayInputStream(cssString.getBytes());
        
        ContentCrawler cw = new ContentCrawler();
        String localPath = System.getProperty("user.dir")+"/download_content";
        File file = new File(localPath);
        if(!file.exists())
        {
            file.mkdir();
        
        }
        cw.saveContent(targetStream, localPath+"/main.css");

        ContentCrawlController contentCrawlController = new ContentCrawlController();
        
        //when
        String output = contentCrawlController.getContent(localPath+"/main.css");
        
        //then
        assertEquals(output.isEmpty(),false);
    }
    
    @Test
    public void getContentFromInCorrectFilePath()
    {     
        //given
        String cssString = "body {background-color: powderblue;}\nh1   {color: blue;}\np{color: red;}";
        InputStream targetStream = new ByteArrayInputStream(cssString.getBytes());
        
        ContentCrawler cw = new ContentCrawler();
        String localPath = System.getProperty("user.dir")+"/download_content";
        File file = new File(localPath);
        if(!file.exists())
        {
            file.mkdir();
        
        }
        cw.saveContent(targetStream, localPath+"/incorrect.css");

        ContentCrawlController contentCrawlController = new ContentCrawlController();
        
        //when
        String output = contentCrawlController.getContent(localPath+"/incorrect_1.css");
        
        //then
        assertEquals(output.isEmpty(),true);
    }
    /*
    @Test
    public void getContentFromInCorrectFilePath()
    {     
        //given
        ContentCrawler cw = new ContentCrawler();
        String localPath = System.getProperty("user.dir")+"/download_content/index.html";

        File file = new File(localPath);
        if(!file.exists())
        {
            file.mkdirs();
        
        }

        ContentCrawlController contentCrawlController = new ContentCrawlController();
        
        //when
        boolean output = contentCrawlController.crawlMainPage(localPath, localPath, cw);
        
        //then
        assertEquals(output,true);
    }
  */
   

}
