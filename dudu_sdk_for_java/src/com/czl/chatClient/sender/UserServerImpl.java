package com.czl.chatClient.sender;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.Constants;
import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.ETbean;
import com.czl.chatClient.bean.GTbean;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.bean.JsonString;
import com.czl.chatClient.bean.NettyContent;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.bean.Pushmessage;
import com.czl.chatClient.bean.Responbean;
import com.czl.chatClient.bean.Trafficebean;
import com.czl.chatClient.utils.Log;

import io.netty.channel.Channel;

public class UserServerImpl extends BaseMessageServiceImpl implements UserServer
{
    
    @Override
    public NettyMessage registerUser(JsonString table, Channel channel,
            SendMessageLisenter lisenter) throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.LD, table);
        sendMessage(message, channel,lisenter);
        return message;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void CommadFilter(Channel channel, NettyMessage message,
            JsonParser parser) throws Exception
    {
        // TODO Auto-generated method stub
        String data = new String(message.getContent(), "UTF-8");
        String[] splits = data.split("\\|");
        switch (AppServerType.ofCommand(message.getHeader()))
        {
            case PU:
                List<Pushmessage> msgs = (List<Pushmessage>) parser
                        .parseArray(splits[0], Pushmessage.class);
                List<String> ids = new ArrayList<>();
                for (Pushmessage msg : msgs)
                {
                    ids.add(msg.getDataid());
                }
                NettyMessage prmsg = buildMessage(AppServerType.PR);
                prmsg.setContent(getContentByte(
                        new JsonString(parser.toJSONString(ids))));
                sendMessage(prmsg, channel);
                break;
            case IM:
                Pushmessage pushmessage = (Pushmessage) parser
                        .parseObject(splits[2], Pushmessage.class);
                NettyMessage ismsg = buildMessage(AppServerType.IS);
                ismsg.setContent(getContentByte(new JsonString(
                        pushmessage.getDataid() + seporate() + splits[1])));
                sendMessage(ismsg, channel);
                break;
            case SM:
                Responbean smresponbean = new Responbean();
                smresponbean.setHeader(AppServerType.SM.getHeaderString());
                smresponbean.setResponeId(message.getStringMessageId());
                JsonString smjsonString = parser
                        .ObjectToJsonString(smresponbean);
                smjsonString.getBuilder().append(Constants.SEPORATE).append(
                        message.getStringFromUerId());
                responeRs(channel, message.getHeader(), smjsonString);
                break;
            default:
                break;
        }
    }
    
    @Override
    public NettyMessage enterChannel(Channel channel, DuduUser user,
            Groupbean info, JsonParser parser, SendMessageLisenter lisenter)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.GS);
        JsonString jsonString = parser.ObjectToJsonString(user);
        jsonString.getBuilder()
                .append(seporate())
                .append(parser.toJSONString(info));
        message.setContent(getContentByte(jsonString));
        sendMessage(message, channel, lisenter);
        return message;
    }
    
    @Override
    public NettyMessage callUser(Channel channel, DuduUser myUser,
            DuduUser info, JsonParser parser, SendMessageLisenter lisenter)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.FS);
        JsonString JsonStr = new JsonString();
        JsonStr.getBuilder()
                .append(parser.toJSONString(myUser))
                .append(seporate())
                .append(info.getUserid());
        message.setContent(getContentByte(JsonStr));
        sendMessage(message, channel, lisenter);
        return message;
    }
    
    @Override
    public void existChannel(Channel channel, DuduUser myinfo,
            JsonParser parser, SendMessageLisenter lisenter)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.EG);
        JsonString jso = new JsonString();
        jso.getBuilder().append(parser.toJSONString(myinfo));
        message.setContent(getContentByte(jso));
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void refuseCall(Channel channel, DuduUser myUser,
            DuduUser remoteUser, JsonParser parser,
            SendMessageLisenter lisenter) throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.FR);
        JsonString jso = new JsonString();
        jso.getBuilder()
                .append(parser.toJSONString(myUser))
                .append(seporate())
                .append(remoteUser.getUserid());
        
        message.setContent(getContentByte(jso));
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void agreeCall(Channel channel, DuduUser myUser, DuduUser remoteUser,
            JsonParser parser, SendMessageLisenter lisenter)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.FA);
        JsonString jso = new JsonString();
        jso.getBuilder()
                .append(parser.toJSONString(myUser))
                .append(seporate())
                .append(remoteUser.getUserid());
        message.setContent(getContentByte(jso));
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void huangUp(Channel channel, DuduUser myUser, DuduUser remoteUser,
            JsonParser parser, SendMessageLisenter lisenter)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.ED);
        JsonString joStr = new JsonString();
        joStr.getBuilder()
                .append(parser.toJSONString(myUser))
                .append(seporate())
                .append(remoteUser.getUserid());
        message.setContent(getContentByte(joStr));
        sendMessage(message, channel);
    }
    
    @Override
    public void sendAudoiByte(AppServerType type, Channel channel, byte[] bytes,
            SendMessageLisenter lisenter) throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(type);
        message.setCtxLength(bytes.length);
        message.setContent(bytes);
        sendMessage(message, channel, lisenter);
    }

    @Override
    public void sendAudoiByte(List<DuduUser> users,AppServerType type, Channel channel, byte[] bytes, String str, SendMessageLisenter lisenter) throws UnsupportedEncodingException {
            NettyMessage message=buildMessage(type);
            message.setContent(bytes);
            message.setCtxLength(bytes.length);
            NettyContent ntcontent=new NettyContent();
            ntcontent.setSaveToCache(false);
            ntcontent.setContent(str);
            ntcontent.setToUser(users);
            message.setConobj(ntcontent);
            sendMessage(message,channel);
    }

    @Override
    public void sendFreedomMessage(Channel channel, String jsonObject,
            List<DuduUser> touser, JsonParser parser,
            SendMessageLisenter lisenter,boolean saveToCache) throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.FM);
        NettyContent content = new NettyContent();
        content.setSaveToCache(saveToCache);
        content.setToUser(touser);
        content.setContent(jsonObject);
        byte[] contentbytes= getContentByte(new JsonString(parser.toJSONString(content)));
        message.setContent(contentbytes);
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void sendET(Channel channel, String time, String id, DuduUser myUser,
            JsonParser parser, SendMessageLisenter lisenter)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        ETbean bean = new ETbean();
        bean.setDiviceid(myUser.getDiviceid());
        bean.setUrl(myUser.getUrl());
        bean.setUserid(myUser.getUserid());
        bean.setUsername(myUser.getUsername());
        bean.setVoicetime(time);
        NettyMessage message = buildMessage(AppServerType.ET);
        JsonString jso = new JsonString();
        jso.getBuilder()
                .append(parser.toJSONString(bean))
                .append(seporate())
                .append(message.getStringMessageId());
        message.setContent(getContentByte(jso));
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void sendGT(Channel channel, String time, String id, DuduUser myUser,
            JsonParser parser, SendMessageLisenter lisenter)
            throws UnsupportedEncodingException
    {
        // TODO Auto-generated method stub
        GTbean bean = new GTbean();
        bean.setDiviceid(myUser.getDiviceid());
        bean.setUrl(myUser.getUrl());
        bean.setUserid(myUser.getUserid());
        bean.setUsername(myUser.getUsername());
        bean.setVoicetime(time);
        NettyMessage message = buildMessage(AppServerType.GT);
        JsonString jso = new JsonString();
        jso.getBuilder()
                .append(parser.toJSONString(bean))
                .append(seporate())
                .append(id);
        message.setContent(getContentByte(jso));
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void onSendXYPosition(Channel channel, DuduPosition point,
            JsonParser parser, SendMessageLisenter lisenter) throws Exception
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.XY);
        JsonString jso = new JsonString();
        jso.getBuilder().append(parser.toJSONString(point));
        message.setContent(getContentByte(jso));
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void onSendXZPosition(Channel channel, DuduPosition point,
            JsonParser parser, SendMessageLisenter lisenter) throws Exception
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.XZ);
        JsonString jso = new JsonString();
        jso.getBuilder().append(parser.toJSONString(point));
        message.setContent(getContentByte(jso));
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void checkUserIsOnLine(Channel channel, DuduUser user,
            DuduUser frienduser, JsonParser parser,
            SendMessageLisenter lisenter) throws Exception
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.ON);
        JsonString jso = new JsonString();
        jso.getBuilder()
                .append(parser.toJSONString(user))
                .append(seporate())
                .append(frienduser.getUserid());
        message.setContent(getContentByte(jso));
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void cancelRequst(Channel channel, DuduUser user,
            DuduUser frienduser, JsonParser parser,
            SendMessageLisenter lisenter) throws Exception
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.FE);
        JsonString fejso = new JsonString();
        fejso.getBuilder()
                .append(parser.toJSONString(user))
                .append(seporate())
                .append(frienduser.getUserid());
        message.setContent(getContentByte(fejso));
        sendMessage(message, channel, lisenter);
        
    }
    
    @Override
    public void loginOutNs(Channel channel, DuduUser user, JsonParser parser,
            SendMessageLisenter lisenter) throws Exception
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.OU);
        JsonString ouStr = new JsonString();
        ouStr.getBuilder().append(parser.toJSONString(user));
        message.setContent(getContentByte(ouStr));
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void getPositon(Channel channel, DuduUser duduUser,
            JsonParser parser, SendMessageLisenter lisenter) throws Exception
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.PP);
        JsonString sjsoStr = new JsonString();
        sjsoStr.getBuilder().append(parser.toJSONString(duduUser));
        message.setContent(getContentByte(sjsoStr));
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void getActivieChannel(Channel channel, List<String> records,
            JsonParser parser, SendMessageLisenter lisenter) throws Exception
    {
        // TODO Auto-generated method stub
        NettyMessage message = buildMessage(AppServerType.IC);
        JsonString jsoStr = new JsonString();
        jsoStr.getBuilder().append(parser.toJSONString(records));
        message.setContent(getContentByte(jsoStr));
        sendMessage(message, channel, lisenter);
    }
    
    @Override
    public void getPreViewTraffic(Channel channel, JsonParser paser,
            Trafficebean bean, SendMessageLisenter lisenter) throws Exception
    {
        NettyMessage message = buildMessage(AppServerType.TC);
        String content = paser.toJSONString(bean);
        JsonString jsoStr = new JsonString();
        jsoStr.getBuilder().append(content);
        message.setContent(getContentByte(jsoStr));
        sendMessage(message, channel, lisenter);
    }
}
