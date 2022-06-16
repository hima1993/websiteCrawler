/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.content.crawler;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author himaruksilva
 */
public class ContentCrawlerTest 
{
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
