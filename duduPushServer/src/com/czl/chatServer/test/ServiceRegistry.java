package com.czl.chatServer.test;

import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import com.czl.chatServer.Constants;

public class ServiceRegistry
{
    private CountDownLatch latch = new CountDownLatch(1);
    
    private String registryAddress;
    
    public ServiceRegistry(String registryAddress)
    {
        this.registryAddress = registryAddress;
    }
    
    //注册到zk中，其中data为服务端的 ip:port
    public void register(String data)
    {
        if (data != null)
        {
            ZooKeeper zk = connectServer();
            if (zk != null)
            {
                createNode(zk, data);
            }
        }
    }
    
    private ZooKeeper connectServer()
    {
        ZooKeeper zk = null;
        try
        {
            zk = new ZooKeeper(registryAddress, 2000, new Watcher()
            {
                @Override
                public void process(WatchedEvent event)
                {
                    if (event.getState() == Event.KeeperState.SyncConnected)
                    {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return zk;
    }
    
    private void createNode(ZooKeeper zk, String data)
    {
        
        try
        {
            byte[] bytes = data.getBytes();
            if (zk.exists(Constants.ZK_DATA_PATH, true) == null)
            {
                
                String path = zk.create(Constants.ZK_DATA_PATH,
                        bytes,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
                System.out.println(
                        "create zookeeper node path:" + path + " data:" + data);
            }
            else
            {
                String path = zk.create(Constants.ZK_DATA_PATH + "/" + data,
                        bytes,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
                System.out.println(
                        "create zookeeper node child:" + path + " data:" + data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
