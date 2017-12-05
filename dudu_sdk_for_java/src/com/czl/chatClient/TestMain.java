package com.czl.chatClient;



import com.czl.chatClient.DuduClient;
import com.czl.chatClient.DuduSDK;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.bean.Pushmessage;
import com.czl.chatClient.bean.Responbean;
import com.czl.chatClient.login.onConnetCallBack;
import com.czl.chatClient.receiver.DefaultHanlder;

import java.util.List;

import io.netty.channel.Channel;

/**
 * Created by Administrator on 2017/11/27.
 */

public class TestMain {
    public static void main(String[] args) throws Exception {
        DuduUser user=getUser("user10005","设备号"+10005);
        loginNSbyUid(user);

//        DuduUser user=getUser("user10001","设备号"+10001); // 注意 userId 前面拼接 "user" 字符串   比如  uid=10001  那么 第一个参数  应该是  user10001; 设备号 是为了 让用户单点登录用的
//        loginNSbyUid(user);

    }

    private static String loginNSbyUid(DuduUser user) {
        DuduClient client = DuduClient.getInstance();
        client.login(user, new onConnetCallBack() {
            @Override
            public void onConnectSucess(Channel channel) {
                DuduUser user1=getUser("user10001","设备号"+10001);//上面是10005  下面就用10001  这样当10005 登录成功  主动给10005 发一条消息
//                DuduUser user1=getUser("user10005","设备号"+10005);
                DuduClient.getInstance().sendFM(user1,"发送一条消息");
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

                duduClient.addLisenter(new DefaultHanlder() {
                    @Override
                    public void onReceiveIMMsg(Pushmessage pushmessage) {

                    }

                    @Override
                    public void onReceivi00Msg(String s) {

                    }

                    @Override
                    public void offLinNotice(String s) {

                    }

                    @Override
                    public void onException(String s) {

                    }

                    @Override
                    public void onReceiverPositions(List<DuduPosition> list) {

                    }

                    @Override
                    public void onReceiveRespons(Responbean responbean, String s) {

                    }

                    @Override
                    public void onReTranfficMeta(String s) {

                    }

                    @Override
                    public void onUserOnLine(String s) {

                    }

                    @Override
                    public void onUserOffLine(String s) {

                    }

                    @Override
                    public void onReceiveOfflineMessage(List<Pushmessage> list) {

                    }

                    @Override
                    public void onGroupMessageChanged(Groupbean groupbean) {

                    }

                    @Override
                    public void onReceiveFMMessage(DuduUser duduUser, String s) {
                        DuduUser userResone=null;
                        if(DuduClient.getInstance().getCurrentUser().getUserid().equals("user1001")) {
                            userResone=getUser("user10005","设备号"+10005);
                        }else {
                            userResone=getUser("user10001","设备号"+10001);
                        }
                        if("发送一条消息".equals(s))
                            DuduClient.getInstance().sendFM(userResone,"收到用户("+duduUser.getUserid()+")一条消息  消息内容____ "+s);
                    }

                    @Override
                    public void onReceiveDefaultMessage(NettyMessage nettyMessage) {

                    }

                    @Override
                    public void onPositionChanged(DuduPosition duduPosition) {

                    }
                });
            }
        });
        return null;
    }
    public static DuduUser getUser(String uid,String diviceId) {
        DuduUser user = new DuduUser();
        user.setUserid(uid);
        user.setDiviceid(diviceId);
        user.setUrl("http://120.76.194.73:8888/group2/M00/00/13/eEzCSVjeIK3TAMmHAAAi7yL8j4o462.png");
        user.setUsername("測試"+uid);
        DuduSDK.init(user);
        DuduSDK.setDebug(true);
        return user;
    }
}
