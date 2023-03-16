package com.demiglace.trainings.servlets.listener;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class RequestListener implements ServletRequestListener {
    public void requestInitialized(ServletRequestEvent event)  { 
    	System.out.println("Request Created");
    }

    public void requestDestroyed(ServletRequestEvent event)  {
    	System.out.println("Request Destroyed");
    }
}
