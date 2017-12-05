package com.czl.chatServer;

import com.czl.chatServer.server.IMSStartServer;
import com.czl.chatServer.server.Impl.MSStartServerImpl;

public class MSMain
{ 
    public static void main(String[] args)
    {
        IMSStartServer server = new MSStartServerImpl();
        server.start();
    }
}
