package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import vo.Recipe;
import vo.Window;
import dao.IRecipeDao;
import dbc.DatabaseConnection;

public class RecipeDaoImpl implements IRecipeDao {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    public RecipeDaoImpl(Connection conn){
        this.conn = conn;
    }
    public ArrayList<String> findDishsbyWindowid(Window window) throws Exception {
        ArrayList<String> dishs = new ArrayList<String>();
        String sql = "select distinct dishname from recipe where windowid=?";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setInt(1, window.getWindowId());
        ResultSet rs = this.pstmt.executeQuery();
       
        while(rs.next()){
            dishs.add(rs.getString(1));
        }
        return dishs;
    }
    @Override
    public Recipe findRecipeIdbyWindowDishname(Window window,String dishname) throws Exception {
        Recipe recipe = new Recipe();
        
        String sql = "select distinct recipeid from recipe where windowid=? and dishname=?";
        this.pstmt = this.conn.prepareStatement(sql);
        this.pstmt.setInt(1, window.getWindowId());
        this.pstmt.setString(2, dishname);
        ResultSet rs = this.pstmt.executeQuery();
       if(rs.next()){
           recipe.setRecipeId(rs.getInt(1));
           //*********************************************
       }//if
        return recipe;
    }
  

}
