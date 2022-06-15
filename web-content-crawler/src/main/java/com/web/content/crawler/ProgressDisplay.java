/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.content.crawler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author himaruksilva
 */
public class ProgressDisplay 
{
    private float currentProgress = 0.0f;
    private float totalAmountToDownload=100.0f;
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

    /**
     * @return the currentProgress
     */
    public float getCurrentProgress() 
    {
        return currentProgress;
    }

    /**
     * @param currentProgress the currentProgress to set
     */
    public void setCurrentProgress(float currentProgress) 
    {
        this.currentProgress = currentProgress;
    }

    /**
     * @return the totalAmountToDownload
     */
    public float getTotalAmountToDownload() 
    {
        return totalAmountToDownload;
    }

    /**
     * @param totalAmountToDownload the totalAmountToDownload to set
     */
    public void setTotalAmountToDownload(float totalAmountToDownload) 
    {
        this.totalAmountToDownload = totalAmountToDownload;
    }

    /**
     * @return the currentAmountToDownload
     */
    public float getCurrentAmountToDownload() 
    {
        return currentAmountToDownload;
    }

    /**
     * @param currentAmountToDownload the currentAmountToDownload to set
     */
    public void setCurrentAmountToDownload(float currentAmountToDownload) 
    {
        this.currentAmountToDownload = currentAmountToDownload;
    }

    
    public void show()
    {
        try 
        {
            while(true)
            {
                if(this.currentProgress != 100.0)
                {
                    this.currentProgress = (int)((this.currentAmountToDownload * 100)/this.totalAmountToDownload);

                    String bar = "[";
                    
                    for(int i=10;i<=this.currentProgress;i+=10)
                    {
                        bar += "*";
                    }

                    bar+= "]";

                    System.out.print("Current Progress: "+bar+"["+this.currentProgress +"%]\r");

                    Thread.sleep(100);
                }
                else
                {   
                    break;
                }
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(ProgressDisplay.class.getName()).log(Level.SEVERE, null, ex);
                
        }
       
    }
}
