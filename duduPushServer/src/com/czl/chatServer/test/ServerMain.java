package com.czl.chatServer.test;

import com.czl.chatServer.Constants;

public class ServerMain {
 
 public static void main(String[] agrs){
  int port = 9988;
  //register service in zookeeper 
  ServiceRegistry zsr = new ServiceRegistry(Constants.zkAddress);
  String serverIp = "192.168.13.31:6688";
  zsr.register(serverIp);
  //netty bind
//  new Server().bind(port);
 }
}
