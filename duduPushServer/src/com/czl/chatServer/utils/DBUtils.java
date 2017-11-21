package com.czl.chatServer.utils;

import java.util.ArrayList;
import java.util.List;

import com.czl.chatClient.bean.DuduUser;
import com.czl.chatClient.utils.StringUtils;
import com.czl.chatServer.utils.DBAccess.DBName;

public class DBUtils
{
    
    public static boolean isUserRegistered(String userid)
    {
        // TODO Auto-generated method stub
        return getUserFromDb(userid)!=null;
    }
    
    /**
     * 从数据库获取用户信息
     * 
     * @param userid
     * @return
     */
    public static DuduUser getUserFromDb(String userid)
    {
        if (StringUtils.isEmpty(userid))
            return null;
        JdbcUtil jdbcUtil = new JdbcUtil();
        String sql = "select userid,username,logourl from userinfo where userid = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(userid);
        try
        {
            jdbcUtil.getConnection(DBName.userdb);
            return jdbcUtil.findSingleRefResult(sql, params, DuduUser.class);
        }
        catch (Exception e)
        {
            System.out.println(
                    "getUser:UseridorMobile=" + userid + "|" + e.getMessage());
            return null;
        }
        finally
        {
            jdbcUtil.releaseConn();
        }
    }
}
