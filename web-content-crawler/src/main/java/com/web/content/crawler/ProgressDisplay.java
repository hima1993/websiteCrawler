/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.content.crawler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author himaruksilva
 */
public class ProgressDisplay extends Thread 
{
    private float currentProgress = 0.0f;
    private float totalAmountToDownload = 100.0f;
    private float currentAmountToDownload = 0.0f;

    private static volatile ProgressDisplay progressDisplayInstance= null;

    private ProgressDisplay() {};
    
    public static ProgressDisplay getInstance()
    {
        if(progressDisplayInstance == null)
        {    
            synchronized (ContentCrawlController.class)
            {
                if(progressDisplayInstance == null)
                {
                    progressDisplayInstance = new ProgressDisplay();
                }
            }
        }
        return progressDisplayInstance;
    }
    
    @Override
    public void run() 
    {
        show();
    }

    public void setTotalAmountToDownload(float totalAmountToDownload) 
    {
        this.totalAmountToDownload = totalAmountToDownload;
    }


    public void setCurrentAmountToDownload(float currentAmountToDownload) 
    {
        this.currentAmountToDownload = currentAmountToDownload;
    }
    
    public float getCurrentAmountToDownload() 
    {
        return this.currentAmountToDownload;
    }

    public float getTotalAmountToDownload() 
    {
        return totalAmountToDownload;
    }
    
    public void show() 
    {
        try 
        {
            while (this.currentProgress != 100.0) 
            {
                
                this.currentProgress = (int) ((this.currentAmountToDownload * 100) / this.totalAmountToDownload);

                StringBuilder bar = new StringBuilder("[");

                for (int i = 10; i <= this.currentProgress; i += 10) {
                    bar.append("*");
                }

                bar.append("]");

                System.out.print("Current Progress: " + bar + "[" + this.currentProgress + "%]\r");

                Thread.sleep(100);

            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(ProgressDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
