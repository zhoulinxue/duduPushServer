package com.czl.chatClient;

import java.util.ArrayList;
import java.util.List;

import com.czl.chatClient.receiver.RecivMessageCallBack;
import com.czl.chatClient.utils.StringUtils;

public enum AppServerType
{
    // 登录
    LD("LD", true),
    //获取业务服务器地址
    AC("AC"),
    // 个人呼叫
    FS("FS"),
    // 接受 对讲
    FA("FA"),
    // 拒绝对讲
    FR("FR"),
    // 挂断
    ED("ED"),
    // 进入频道
    GS("GS"),
    // 退出频道
    EG("EG", true),
    // 邀请对讲
    GM("GM"),
    // 单对单 字节流
    SM("SM"),
    // 群音频数据
    SG("SG"),
    // 群数据 发送结束
    GT("GT"),
    // 个人数据发送结束
    ET("ET"),
    // 取消个人对讲 邀请
    FE("FE"),
    // 消息回复
    RETURN_TAG("00"),
    // 测试链接
    LT("LT"),
    // 用户坐标
    XY("XY"),
    // 用户坐标
    XZ("XZ"),
    // 在线
    ON("ON"),
    //离线
    OF("OF"),
    // 系统推送
    IM("IM"),
    // 系统消息发送成功
    IS("IS"),
    // 个人对讲中
    CF("CF"),
    // 频道对讲中
    CG("CG"),
    // 发送掉线期间的 语音
    RM("RM"),

    // 获取用户 位置
    PP("PP"),
    // 测试链接
    PT("PT"),
    // 获取 导航中的图片
    TC("TC"),
    // 用户坐标
    TA("TA"), AB("AB"),
    // 心跳
    HEART_BEAT("CK", true),
    // 报错
    EX("EX", true),
    // 跨服务器 进频道标示
    GB("GB"),
    // 用户进入频道
    NT("NT"),
    // 进入频道成功 发给用户
    AL("AL"),
    // 向其他服务器的用户发邀请
    TM("TM"),
    // 异常登陆
    CL("CL", true),
    // 单对单 字节流
    TF("TF"),
    // 一对一邀请
    FN("FN"),
    //
    NA("NA"),
    // 一对一邀请,不同服务器
    NR("NR"),
    // 不同设备登陆
    RS("RS"),
    // 心跳
    CK("CK", true),
    // 已经被T出对讲
    NC("NC"),
    //
    IC("IC"),
    // 活跃中的频道
    CA("CA"),
    // 频道不再活跃
    DM("DM"),
    // 群推
    PU("PU"),
    // 用户 不在线
    FO("FO"),
    // 频道改变
    GC("GC"),
    // 群推 回复
    PR("PR"),
    // 文字消息
    TX("TX"),
    // 图片消息
    PI("PI"),
    // 文件消息
    FI("FI"),
    // 自定义 消息
    FM("FM"),
    // 退出登陆
    OU("OU"),
    // 语音消息
    AU("AU"), OK("OK"),
    // NS 登录MS
    LC("LC"),
    //退出登录
    LQ("LQ");

    private AppServerType(String headerString)
    {
        this.headerString = headerString;
    }

    private String headerString;

    private boolean isSystemType = false;

    public String getHeaderString()
    {
        return headerString;
    }

    public void setHeaderString(String headerString)
    {
        this.headerString = headerString;
    }

    private AppServerType(String headerString, boolean isSystemType)
    {
        this.headerString = headerString;
        this.isSystemType = isSystemType;
    }

    public boolean isSystemType()
    {
        return isSystemType;
    }

    public void setSystemType(boolean isSystemType)
    {
        this.isSystemType = isSystemType;
    }

    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return headerString;
    }

    public static AppServerType ofCommand(String header)
    {
        // TODO Auto-generated method stub
        if (!StringUtils.isEmpty(header))
        {
            for (AppServerType s : values())
            {
                if (header.equals((s.toString())))
                {
                    return s;
                }
            }
        }
        return null;
    }

    public static boolean isCommand(String header)
    {
        // TODO Auto-generated method stub
        AppServerType type = getCommand(header);
        if (type != null)
        {
            return true;
        }
        return false;
    }

    private static AppServerType getCommand(String header)
    {
        if (!StringUtils.isEmpty(header))
        {
            for (AppServerType s : values())
            {
                if (header.equals((s.toString())))
                {
                    return s;
                }
            }
        }
        return null;
    }

    public static List<AppServerType> allvalues()
    {
        List<AppServerType> typeServer = new ArrayList<>();
        for (AppServerType s : values())
        {
            typeServer.add(s);
        }
        return typeServer;
    }

    public static boolean contains(String header, RecivMessageCallBack callBack)
    {
        if (isCommand(header))
        {
            if (callBack.getServerType().contains(getCommand(header)))
            {
                return true;
            }
            ;
        }
        return false;
    }

    public static List<AppServerType> P2PValus()
    {
        // TODO Auto-generated method stub
        List<AppServerType> p2pvalus = new ArrayList<>();
        p2pvalus.add(FN);
        p2pvalus.add(FA);
        p2pvalus.add(FE);
        p2pvalus.add(ED);
        p2pvalus.add(ET);
        p2pvalus.add(FR);
        p2pvalus.add(CF);
        p2pvalus.add(SM);
        return p2pvalus;
    }

    public static List<AppServerType> GroupValus()
    {
        // TODO Auto-generated method stub
        List<AppServerType> gvalus = new ArrayList<>();
        gvalus.add(GM);
        gvalus.add(GB);
        gvalus.add(CG);
        gvalus.add(EG);
        gvalus.add(GT);
        gvalus.add(NT);
        gvalus.add(SG);
        gvalus.add(IC);
        gvalus.add(AL);
        gvalus.add(DM);
        gvalus.add(CA);
        gvalus.add(NC);
        return gvalus;
    }

    public static List<AppServerType> othersValus()
    {
        // TODO Auto-generated method stub
        List<AppServerType> allvalus = allvalues();
        allvalus.removeAll(P2PValus());
        allvalus.removeAll(GroupValus());
        allvalus.removeAll(byteValus());
        return allvalus;
    }

    public static List<AppServerType> byteValus()
    {
        // TODO Auto-generated method stub
        List<AppServerType> bytevalus = new ArrayList<>();
        bytevalus.add(SG);
        bytevalus.add(SM);
        bytevalus.add(FI);
        return bytevalus;
    }

    public static List<AppServerType> zvaValues()
    {
        // TODO Auto-generated method stub
        List<AppServerType> zvavalus = new ArrayList<>();
        zvavalus.add(RETURN_TAG);
        zvavalus.add(RS);
        zvavalus.add(AU);
        zvavalus.add(FM);
        zvavalus.add(PU);
        zvavalus.add(IM);
        return zvavalus;
    }

}
