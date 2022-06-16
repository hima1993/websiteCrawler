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
public class ProgressDisplay extends Thread {
    private float currentProgress = 0.0f;
    private float totalAmountToDownload = 100.0f;
    private float currentAmountToDownload = 0.0f;


    @Override
    public void run() 
    {
        show();
    }

    /**
     * @param totalAmountToDownload the totalAmountToDownload to set
     */
    public void setTotalAmountToDownload(float totalAmountToDownload) {
        this.totalAmountToDownload = totalAmountToDownload;
    }


    /**
     * @param currentAmountToDownload the currentAmountToDownload to set
     */
    public void setCurrentAmountToDownload(float currentAmountToDownload) {
        this.currentAmountToDownload = currentAmountToDownload;
    }


    public void show() 
    {
        try 
        {
            while (this.currentProgress != 100.0) 
            {
                this.currentProgress = (int) ((this.currentAmountToDownload * 100) / this.totalAmountToDownload);

                StringBuilder bar = new StringBuilder("[");

                for (int i = 10; i <= this.currentProgress; i += 10) 
                {
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
