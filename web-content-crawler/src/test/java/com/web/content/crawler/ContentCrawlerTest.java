/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.content.crawler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author himaruksilva
 */
public class ContentCrawlerTest {

   /*
    @Test
    public void saveContentCorrectRemoteFileNameandCorrectLocalFileName()
    {
        //given
        ContentCrawler cw = new ContentCrawler();
        String correctRemotePath = "https://tretton37.com/assets/css/main.css";
        String correctLocalPath = System.getProperty("user.dir")+"/download_content/main.css";

        
        //when
        boolean output = cw.isRemoteFileNameSavable(correctRemotePath, correctLocalPath);
        
        
        //then
        assertEquals(output,true);
    }
    */
    
    /*
    @Test
    public void saveContentCorrectRemoteFileNameandInCorrectLocalFileName()
    {
        //given
        ContentCrawler cw = new ContentCrawler();
        String correctRemotePath = "https://tretton37.com/assets/css/main.css";
        String IncorrectLocalPath = System.getProperty("user.dir")+"/download_content/main-incorrect.css";

        
        //when
        boolean output = cw.isRemoteFileNameSavable(correctRemotePath, IncorrectLocalPath);
        
        
        //then
        assertEquals(output,false);
    }
    */
    
    
    @Test
    public void saveContentInLocalPath()
    {
        String cssString = "body {background-color: powderblue;}\nh1   {color: blue;}\np{color: red;}";
        InputStream targetStream = new ByteArrayInputStream(cssString.getBytes());
        //given
        ContentCrawler cw = new ContentCrawler();
        String localPath = System.getProperty("user.dir")+"/download_content";
        File file = new File(localPath);
        if(!file.exists())
        {
            file.mkdir();
        
        }
        
        //when
        boolean output = cw.saveContent(targetStream, localPath+"/main.css");
        
        
        //then
        assertEquals(output,true);
    }
    
    @Test
    public void extractWebLinksFromEmptyFileContent()
    {
        //given
        ContentCrawler cw = new ContentCrawler();
        String validRegex = "\\s*(?:href|src)\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
        String emptyFileContent = "";

        
        //when
        ArrayList<String> output = cw.extractWebLinks(emptyFileContent, validRegex);
        
        //then
        assertEquals(output.isEmpty(),true);
    }
    
    
    @Test
    public void extractWebLinksFromCorrectFileContent()
    {
        //given
        ContentCrawler cw = new ContentCrawler();
        String validRegex = "\\s*(?:href|src)\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
        
        String emptyFileContent = "<script src=\"/assets/js/lib/rem.min.js\" type=\"text/javascript\"></script>\n" +
"    <script src=\"//cdn.jsdelivr.net/g/html5shiv@3.7.0,selectivizr@1.0.3b,respond@1.4.2\"></script>\n" +
"    <link href=\"/assets/css/main.ie.css?484a51da-d4de-4554-88b0-2d510eec863e\" rel=\"stylesheet\" />";

        //when
        ArrayList<String> output = cw.extractWebLinks(emptyFileContent, validRegex);

        //then
        assertEquals(output.isEmpty(),false);
        assertEquals(output.size(),3);
        assertEquals(output.get(0),"/assets/js/lib/rem.min.js");
    }
    
    
    @Test
    public void extractWebLinksFromInValidRegex()
    {
        //given
        ContentCrawler cw = new ContentCrawler();
        String validRegex = "***";
        
        String emptyFileContent = "<script src=\"/assets/js/lib/rem.min.js\" type=\"text/javascript\"></script>\n" +
"    <script src=\"//cdn.jsdelivr.net/g/html5shiv@3.7.0,selectivizr@1.0.3b,respond@1.4.2\"></script>\n" +
"    <link href=\"/assets/css/main.ie.css?484a51da-d4de-4554-88b0-2d510eec863e\" rel=\"stylesheet\" />";
        
        //when
        ArrayList<String> output = cw.extractWebLinks(emptyFileContent, validRegex);
        
        //then
        assertEquals(output.isEmpty(),true);
    }
    
    @Test
    public void compileValidRegex()
    {
        //given
        ContentCrawler cw = new ContentCrawler();
        
        String validRegex = "\\s*(?:href|src)\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
 
        //when
        Optional<Pattern> output = cw.isValidRegex(validRegex);
        
        //then
        assertEquals(output.get().pattern(),validRegex);
    }
    
    
    @Test
    public void compileInValidRegex()
    {
        //given
        ContentCrawler cw = new ContentCrawler();
        String validRegex = "***";
        
        //when
        Optional<Pattern> output = cw.isValidRegex(validRegex);
       
        //then
        assertEquals(output,Optional.empty());
    } 
   
}
