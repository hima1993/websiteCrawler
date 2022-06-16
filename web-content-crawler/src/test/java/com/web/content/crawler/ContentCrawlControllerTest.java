/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.content.crawler;

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
        ContentCrawlController contentCrawlController = new ContentCrawlController("https://tretton37.com/","index.html");
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
        ContentCrawlController contentCrawlController = new ContentCrawlController("https://tretton37.com/","index.html");
        String correctRemotePath = "https://tretton37.com/assets/css/incorrect.css";
        String correctLocalPath = System.getProperty("user.dir")+"/download_content/main.css";

        //when
        boolean output = contentCrawlController.isRemoteFileNameSavable(correctRemotePath, correctLocalPath);

        //then
        assertEquals(output,false);
    }
}
