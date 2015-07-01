package dao.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import vo.User;
import vo.Window;
import dao.*;
import factory.DaoFactory;
public class WindowDaoImpl implements IWindowDao {

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    public WindowDaoImpl(Connection conn){
        this.conn = conn;
    }
    public ArrayList<String> findBuildName() throws Exception {
        ArrayList<String> buildsname = new ArrayList<String> (); 
        String sql = "select distinct buildname from window";
        this.pstmt = this.conn.prepareStatement(sql);
        ResultSet rs = this.pstmt.executeQuery();
      
        while(rs.next()){
            buildsname.add(rs.getString(1));
        }
        return buildsname;
    }

    @Override
    public ArrayList<String> findLayers(String buildname) throws Exception {
        ArrayList<String> layers = new ArrayList<String>();
        String sql = "select distinct layer from window where buildname=?";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setString(1,buildname);
        ResultSet rs = this.pstmt.executeQuery();
        while(rs.next()){
            layers.add(rs.getString(1))  ;
        }
        return layers;
    }

    @Override
    public ArrayList<String> findWindowsbyBuildandLayer(String buildname, String layer) throws Exception {
        ArrayList<String> windows = new ArrayList<String>();
        String sql = "select distinct windowname from window where buildname=? and layer=?";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setString(1,buildname);
        this.pstmt.setString(2, layer);
        ResultSet rs = this.pstmt.executeQuery();
        while(rs.next()){
                windows.add(rs.getString(1));
        }
        return windows;
    }
    @Override
    public Window findWindowbyBuildLayerandWindowname(String build,String layer, String windowname) throws Exception {
        Window window = new Window();
       
        String sql = "select windowid,windowscore from window where buildname=? and layer=? and windowname=?";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setString(1,build);
        this.pstmt.setString(2, layer);
        this.pstmt.setString(3, windowname);
        ResultSet rs = this.pstmt.executeQuery();
        if(rs.next()){
            window.setBuildName(build);
            window.setLayer(layer);
            window.setWindowId(rs.getInt(1));
            window.setWindowScore(rs.getDouble(2));
            return window;
        }else{
            return null;
        }//if
    }
   

}
