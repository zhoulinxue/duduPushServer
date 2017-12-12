package com.czl.chatClient;


import com.czl.chatClient.DuduClient;
import com.czl.chatClient.DuduSDK;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.NettyServer;
import com.czl.chatClient.bean.Pushmessage;
import com.czl.chatClient.bean.Responbean;
import com.czl.chatClient.login.onConnetCallBack;
import com.czl.chatClient.receiver.ZVAHandler;
import com.czl.chatClient.utils.Log;

import java.io.IOException;
import java.util.List;

import io.netty.channel.Channel;

/**
 * Created by Administrator on 2017/12/12.
 */

public class TestMain {
   static DuduUser user1=null;
    public static void main(String[] args) throws IOException {
        // 接收消息的用户
        user1= getUser("user10005","设备号"+10005);
// user1=getUser("user10001","设备号"+10001);//上面是10005  下面就用10001  这样当10005 登录成功  主动给10005 发一条消息
        
        
        DuduUser user=getUser("user10001","设备号"+10001); // 注意 userId 前面拼接 "user" 字符串   比如  uid=10001  那么 第一个参数  应该是  user10001; 设备号 是为了 让用户单点登录用的
//        DuduUser user=getUser("user10005","设备号"+10005);
        //登录用户
        loginNSbyUid(user);

    }

    public static DuduUser getUser(String uid, String diviceId) {
        DuduUser user = new DuduUser();
        user.setUserid(uid);
        user.setDiviceid(diviceId);
        user.setUrl("http://120.76.194.73:8888/group2/M00/00/13/eEzCSVjeIK3TAMmHAAAi7yL8j4o462.png");
        user.setUsername("測試"+uid);
        return user;
    }

    private static String loginNSbyUid(final DuduUser user) {
        // 初始化流程 略有变化  支持配置MS 地址及 NS 地址  配置NS 地址后  配置 的MS地址   失效（sdk 始终连接NS 地址） （二选一  优先NS 连接）
        NettyServer msServer=new NettyServer();
        msServer.setIp("192.168.13.31");
        msServer.setPort(10001);

        DuduSDK.Builder builder= new DuduSDK.Builder();
        builder.setUser(user);
        builder.setMsServer(msServer);
//        builder.setNsServer(nsServer)
        DuduSDK.init(builder);
        DuduSDK.setDebug(true);
        DuduClient client = DuduClient.getInstance();
        client.login(user, new onConnetCallBack() {
            @Override
            public void onConnectSucess(Channel channel) {


            }

            @Override
            public void onConnectFailed() {

            }

            @Override
            public boolean isReconnect() {
                return false;
            }

            @Override
            public void disconnect(Channel channel) {

            }

            @Override
            public void addLisenter(DuduClient duduClient) {
                duduClient.addLisenter(new ZVAHandler() {
                    @Override
                    public void onServerRecive(String s) {
                        Log.e("Dudu_SDK","发送成功"+s);
                    }

                    @Override
                    public void onReceiveRespons(Responbean responbean) {

                    }

                    @Override
                    public void onReciveAudioMessage(DuduUser duduUser, byte[] bytes, String s) {
                        // 播放字节流

                    }

                    @Override
                    public void onReciveMessage(final DuduUser fromUser, final String s) {

                        Log.e("Dudu_SDK",fromUser.getUserid()+"  发来消息：  "+s);

                    }

                    @Override
                    public void onReceiveOfflineMessage(List<Pushmessage> lists) {
                        for(Pushmessage pushmessage:lists){
                            Log.e("Dudu_SDK","接收到离线消息:"+pushmessage.toString());
                        }
                    }

                    @Override
                    public void onReceivePushMessage(Pushmessage Impmsg) {
                        Log.e("Dudu_SDK","接收到推送消息:"+Impmsg.toString());
                    }
                });
            }
        });
        return null;
    }
}
