package com.czl.chatClient.handler;

import java.io.IOException;

import com.czl.chatClient.AppServerType;
import com.czl.chatClient.bean.NettyMessage;
import com.czl.chatClient.utils.Log;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder
{
    
    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset,
            int lengthFieldLength) throws IOException
    {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        
    }
    
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
            throws IOException
    {
        if (in.readableBytes() < 2)
        {
            Log.e("小于2");
            return null;
        }
        in.markReaderIndex();//
        NettyMessage message = new NettyMessage();
        int length = in.readByte();
//        if(length==110){
//            length=in.readByte();
//        }
        Log.e("接1"+length);
        if (in.readableBytes() < length)
        {
            in.resetReaderIndex();
            Log.e("小于length"+length);
            return null;
        }
        byte[] idbytes = new byte[length];
        in.readBytes(idbytes);
        message.setMessageId(idbytes);

        if (in.readableBytes() < 2)
        {
            in.resetReaderIndex();
            Log.e("小于22");
            return null;
        }
        message.setHeader0(in.readByte());
        message.setHeader1(in.readByte());
        if (AppServerType.byteValus().contains(message.getAppServerType()))
        {
            if (in.readableBytes() < 4)
            {
                in.resetReaderIndex();
                Log.e("小于语音 长度4");
                return null;
            }
            else
            {
                message.setCtxLength(in.readInt());
                if (message.getCtxLength() > 1024 * 1024)
                {
                    in.resetReaderIndex();
                    if (in.readableBytes() > 1024 * 1024)
                    {
                        in.skipBytes(1024 * 1020);
                        in.discardReadBytes();
                    }
                    Log.e("语音数据太大");
                    return null;
                }
                if (in.readableBytes() < message.getCtxLength())
                {
                    in.resetReaderIndex();
                    Log.e("语音未读完");
                    return null;
                }
                else
                {
                    byte[] req = new byte[message.getCtxLength()];
                    in.readBytes(req);
                    message.setContent(req);
                    ///////////////////////////
                    int nn = in.bytesBefore((byte) 10);// \n
                    if (nn < 0)
                    {
                        in.resetReaderIndex();
                        Log.e("读取分割符号..失败");
                        return null;
                    }
                    else
                    {
                        byte[] req2 = new byte[nn];
                        // in.resetReaderIndex();
                        in.readBytes(req2);
                        message.setFromUerId(req2);
                        in.skipBytes(1);
                        Log.e("读完一条语音消息_"+new String(message.getFromUerId())+"!!!!");
                        in.discardReadBytes();
                        Log.printeNettymsg(message, "接收到消息");
                        return message;
                    }
                }
            }
            
        }else if (AppServerType.AU.toString().equals(message.getHeader()))
        {
            
            if (in.readableBytes() < 4)
            {
                in.resetReaderIndex();
                return null;
            }
            message.setCtxLength(in.readInt());
            if (message.getCtxLength() > 1024 * 1024)
            {
                in.resetReaderIndex();
                if (in.readableBytes() > 1024 * 1024)
                {
                    in.skipBytes(1024 * 1020);
                    in.discardReadBytes();
                }
                    return null;
            }
                if (in.readableBytes() < message.getCtxLength())
                {
                    in.resetReaderIndex();
                    return null;
                }

            byte[] req = new byte[message.getCtxLength()];
            in.readBytes(req);
            message.setContent(req);
            if (in.readableBytes() < 4) {
                in.resetReaderIndex();
                return null;
            }
             int colength = in.bytesBefore((byte)10);
            if (colength < 0)
            {
                in.resetReaderIndex();
                if (in.readableBytes() > 1536)
                {
                    in.skipBytes(1024);
                    in.discardReadBytes();
                }
                return null;
            }
            byte[] content = new byte[colength];
            in.readBytes(content);
            Log.e(content[colength-1]+"@@ 什么东西"+in.readByte());
            message.setConobj(content);
            // 跳过"\n"符号
//            in.skipBytes(1);
            in.discardReadBytes();
            Log.printeNettymsg(message, "接收到消息");
            return message;
        }
        else
        {
            if (AppServerType.isCommand(message.getHeader()))
            {
                if (in.readableBytes() < 1)
                {
                    in.resetReaderIndex();
                    return null;
                }
                
                int nn = in.bytesBefore((byte) 10);// \n
                if (nn < 1)
                {
                    in.resetReaderIndex();
                    if (in.readableBytes() > 1536)
                    {
                        in.skipBytes(1024);
                        in.discardReadBytes();
                    }
                    return null;
                }
                else
                {
                    byte[] req = new byte[nn - 1];
                    // 跳过"|"符号
                    in.skipBytes(1);
                    in.readBytes(req);
                    message.setContent(req);
                    // 跳过"\n"符号
                    in.skipBytes(1);
                    //					byte[] idbytes=new byte[6];
                    //					in.readBytes(idbytes);
                    //					message.setMessageId(idbytes);
                    
                    in.discardReadBytes();
                    Log.printeNettymsg(message, "接收到消息");
                    return message;
                }
                
            }
            else
            {
                in.resetReaderIndex();
                if (in.readableBytes() > 1536)
                {
                    in.skipBytes(1024);
                    in.discardReadBytes();
                }
                else
                {
                    in.skipBytes(1);
                    in.discardReadBytes();
                }
                return null;
            }
        }
    }
}
