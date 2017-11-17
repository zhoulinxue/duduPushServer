package com.czl.chatServer.test;

import com.czl.chatServer.Constants;


public class ClientMain {
 /**
  * @param args
  * @throws InterruptedException 
  */
 public static void main(String[] args) throws InterruptedException {
  //find service from zookeeper
  ServiceDiscovery sd = new ServiceDiscovery(Constants.zkAddress);
  String serverIp = sd.discover();
  String[] serverArr = serverIp.split(":");
  System.out.println("ServerIP:"+serverArr[0]+"    ServerPort:"+serverArr[1]);
//  String hostIP = "123.123.111.111";
//  int port = 9988;
//  new TestThread().test();
//  //Client.connect(hostIP, port);
//  Client.connect(serverArr[0], Integer.valueOf(serverArr[1]));
 }
}
