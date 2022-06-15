/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.web.content.crawler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author himaruksilva
 */
public class ContentCrawler 
{   
    public boolean saveContent(InputStream inputStream, String localPath) 
    {   

        try(FileOutputStream fileOutputStream  = new FileOutputStream(localPath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream))
        {
            int count = 0;
            byte[] b = new byte[8192];

            while ((count = bufferedInputStream.read(b)) != -1) 
            {
                fileOutputStream.write(b, 0, count);
            }  

            bufferedInputStream.close();
            fileOutputStream.close();
            return true;

        }
        catch (IOException ex) 
        {
            Logger.getLogger(ContentCrawler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }    
    }
    
    public Optional<Pattern> isValidRegex(String regexPattern)
    {
        try 
        {
            Pattern regex = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            
            return Optional.of(regex);
        } 
        catch (PatternSyntaxException exception) 
        {
            return Optional.empty();
        }
    }
    
    public ArrayList<String> extractWebLinks(String fileContent, String regexPattern)
    {
        ArrayList<String> matches = new ArrayList<>();
        
        if(!fileContent.isEmpty())
        {
            StringBuilder contentBuilder = new StringBuilder();
            try(Reader inputString = new StringReader(fileContent);   
                BufferedReader in = new BufferedReader(inputString))
            {  
                String str;

                while ((str = in.readLine()) != null) 
                {
                    contentBuilder.append(str.replace("\"", ""));
                }

                in.close();

                String content = contentBuilder.toString();

                Optional<Pattern> regex = isValidRegex(regexPattern);
                
                if(regex.isPresent())
                {
                    
                    Matcher regexMatcher =regex.get().matcher(content);        
                    while (regexMatcher.find()) 
                    {

                         String link = regexMatcher.group(1);
                         matches.add(link);
                         
                    }
                    /*
                    matches = (ArrayList<String>) regex.get()
                                  .matcher(content)
                                  .results()
                                  .map(MatchResult::group)
                                  .collect(Collectors.toList());
                    */
                    ProgressDisplay.getInstance().setTotalAmountToDownload(matches.size());  
                }
                else
                {
                    return matches;
                }
                    
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ContentCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
            return matches;
        }
        else
        {
            return matches;
        }
        
    }  
}
