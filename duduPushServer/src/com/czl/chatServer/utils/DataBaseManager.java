package com.czl.chatServer.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.czl.chatClient.bean.DuduPosition;
import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.bean.Groupbean;
import com.czl.chatClient.utils.Log;
import com.czl.chatClient.utils.StringUtils;
import com.czl.chatServer.bean.ChannelInfo;
import com.czl.chatServer.bean.ChannelMember;
import com.czl.chatServer.bean.Imbean;
import com.czl.chatServer.utils.DBAccess.DBName;

public class DataBaseManager
{ 
    /**
     * 从数据库获取用户信息
     * 
     * @param userid
     * @return
     */
    public static DuduUser getUserFromDb(String userid) {
        if (StringUtils.isEmpty(userid))
            return null;
        JdbcUtil jdbcUtil = new JdbcUtil();
        String sql = "select userid,username,logourl from userinfo where userid = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(userid);
        try {
            jdbcUtil.getConnection(DBName.userdb);
            return jdbcUtil.findSingleRefResult(sql, params, DuduUser.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getUser:UseridorMobile=" + userid + "|" + e.getMessage());
            return null;
        } finally {
            jdbcUtil.releaseConn();
        }
    }
    /**
     * 
      * 功能简述：
      * 功能详细描述：推送消息 回执
      * @author zhouxue
      * @param dataid
      * @param status [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static void setStatus(String dataid, int status) {
        String sql = "UPDATE logdb.iminfo SET status = ?  where dataid = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(status);
        params.add(dataid);
        JdbcUtil jdbc = new JdbcUtil();
        try {
            jdbc.getConnection(DBName.logdb);
            jdbc.updateByPreparedStatement(sql, params);
            // System.out.println("update status OK!");
        } catch (Exception e) {
            System.err.println("update status error!");
        } finally {
            jdbc.releaseConn();
        }
    }
    /**
     * 
      * 功能简述：
      * 功能详细描述：
      * @author zhouxue 保存用户地址
      * @param position [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static void writePosition(DuduPosition position) {
        JdbcUtil jdbcUtil = new JdbcUtil();
        // System.out.println(time.format(nowTime));
        String sql = "insert   userdb.userposition (dataid,userid,longitude,latitude,updatetime) values (uuid(),'"
                + position.getUserid() + "'," + position.getX() + "," + position.getY() + ",now())";
        Connection conn = null;
        CallableStatement s = null;
        try {
            conn = jdbcUtil.getConnection();
            s = conn.prepareCall(sql);
            s.execute();

        } catch (SQLException e) {
            Log.error(e.getMessage());
        } finally {
            jdbcUtil.releaseConn();
        }

    }
    

    /**
     * 
      * 功能简述：
      * 功能详细描述：获取频道内 的成员列表
      * @author zhouxue
      * @param channel
      * @param userid
      * @return [参数说明]
      * @return List<ChannelMember> [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static List<ChannelMember> getMyChannelMember(String channel, String userid) {
//        List<ChannelMember> members = new ArrayList<ChannelMember>();
        String sql = "SELECT A.channelid,A.createtime,A.memberid,A.nameinchannel,A.rank,B.logourl FROM channelmember A INNER JOIN userinfo B ON A.memberid = B.userid WHERE A.channelid = ? ORDER BY A.createtime";
        List<Object> params = new ArrayList<Object>();
        params.add(channel);

        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection(DBName.userdb);
        try {
            return  jdbcUtil.findMoreRefResult(sql, params, ChannelMember.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jdbcUtil.releaseConn();
        }
    }
    /**
     * 
      * 功能简述：
      * 功能详细描述：插入推送消息
      * @author zhouxue
      * @param im [参数说明]
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static void insertOffLinMessage(Imbean im) {
        // TODO Auto-generated method stub

        // logdb
        String sql = "insert logdb.iminfo "
                + "(dataid,type,fromid,fromname,fromlogourl,toid,channelid,title,alert,detail,markid,status,sendtime) values (uuid(),?,?,?,?,?,?,?,?,?,?,?,now())";
        List<Object> params = new ArrayList<Object>();
        params.add(im.getType());
        params.add(im.getFromid());
        params.add(im.getFromname());
        params.add(im.getFromlogourl());
        params.add(im.getToid());
        params.add(im.getChannelid());
        params.add(im.getTitle());
        params.add(im.getAlert());
        params.add(im.getDetail());
        params.add("");
        params.add(1);
        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection(DBName.logdb);
        try {
            jdbcUtil.updateByPreparedStatement(sql, params);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            jdbcUtil.releaseConn();
        }

    }

    /**
     * 
      * 功能简述：
      * 功能详细描述：从数据库 获取 频道信息
      * @author zhouxue
      * @param channelid
      * @return [参数说明]
      * @return Groupbean [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static Groupbean getChannelMsgFromDb(String channelid) {
        String sql = "SELECT channelid,channelname,membersum,logourl FROM userdb.channelinfo WHERE channelid = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(channelid);

        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection(DBName.channelinfo);
        try {
            ChannelInfo obj = jdbcUtil.findSingleRefResult(sql, params, ChannelInfo.class);
            return obj == null ? null : obj.toGroupbean();
        } catch (Exception e) {
            Log.error("getChannelNumberNum,channelid=" + channelid + "|" + e.getMessage());
            return null;
        } finally {
            jdbcUtil.releaseConn();
        }
    }
    /**
     * 
      * 功能简述：
      * 功能详细描述： 从数据库获取频道内 人员总数
      * @author zhouxue
      * @param channelid
      * @return [参数说明]
      * @return String [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static String getChannelNumFromDb(String channelid) {

        String sql = "SELECT count(1) FROM userdb.channelmember WHERE channelid = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(channelid);

        JdbcUtil jdbcUtil = new JdbcUtil();
        jdbcUtil.getConnection(DBName.userdb);
        try {
            Object obj = jdbcUtil.findSingleValue(sql, params);
            return obj == null ? "" : obj.toString();
        } catch (SQLException e) {
            Log.error("getChannelNumberNum,channelid=" + channelid + "|" + e.getMessage());
            return "";
        } finally {
            jdbcUtil.releaseConn();
        }
    }
    /**
     * 
      * 功能简述：
      * 功能详细描述：获取推送消息
      * @author zhouxue
      * @param uid
      * @return [参数说明]
      * @return List<Imbean> [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static List<Imbean> GetHandlerInfo(String uid) {
        String sql = "SELECT dataid,type,fromid,fromname,fromlogourl,channelid,alert,sendtime,detail,markid,title FROM logdb.iminfo WHERE "
                + "(toid =?" + " and (status = 1 OR status = 2 OR status = 0 )) ";
        List<Object> params = new ArrayList<Object>();
        params.add(uid);
        // params.add("radionews");
        JdbcUtil jdbc = new JdbcUtil();
        try {
            jdbc.getConnection(DBName.logdb);
            return jdbc.findMoreRefResult(sql, params, Imbean.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Access database error!");
            return null;
        } finally {
            jdbc.releaseConn();
        }
    }
    
}
