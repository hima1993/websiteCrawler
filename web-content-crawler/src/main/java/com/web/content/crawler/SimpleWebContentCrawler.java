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

    public static void main(String[] args)
    {
        ContentCrawlController contentCrawlController = new ContentCrawlController("https://tretton37.com/","index.html");
        contentCrawlController.start();

    }
    
}
