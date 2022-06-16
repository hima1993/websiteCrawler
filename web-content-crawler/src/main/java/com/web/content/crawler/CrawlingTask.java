/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.content.crawler;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author himaruksilva
 */
public class CrawlingTask implements Runnable
{
    InputStream inputStream;
    String localPath;
    
    public CrawlingTask(InputStream inputStream, String localPath)
    {
        this.inputStream = inputStream;
        this.localPath = localPath;
    }
    
    @Override
    public void run() 
    {
        try(FileOutputStream fileOutputStream  = new FileOutputStream(this.localPath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(this.inputStream))
        {
            int count = 0;
            byte[] b = new byte[8192];
            
            while ((count = bufferedInputStream.read(b)) != -1) 
            {
                fileOutputStream.write(b, 0, count);
            }  

            bufferedInputStream.close();
            fileOutputStream.close();

            ProgressDisplay.getInstance().setCurrentAmountToDownload(ProgressDisplay.getInstance().getCurrentAmountToDownload()+1);

        }
        catch (IOException ex) 
        {
            Logger.getLogger(ContentCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
}
