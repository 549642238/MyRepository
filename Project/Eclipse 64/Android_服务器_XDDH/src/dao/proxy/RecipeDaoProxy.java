package dao.proxy;

import java.util.ArrayList;

import vo.Recipe;
import vo.Window;
import dao.IRecipeDao;
import dao.impl.RecipeDaoImpl;
import dbc.DatabaseConnection;

public class RecipeDaoProxy implements IRecipeDao {
    private DatabaseConnection dbc = null;
    private IRecipeDao dao = null;
    public RecipeDaoProxy()throws Exception{
        this.dbc = new DatabaseConnection();
        this.dao = new RecipeDaoImpl(this.dbc.getConnection());
    }
    public ArrayList<String> findDishsbyWindowid(Window window)throws Exception {
        ArrayList<String> dishs = new ArrayList<String>();
        try{
            dishs = this.dao.findDishsbyWindowid(window);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            this.dbc.close();
        }
        return dishs;
    }
    public Recipe findRecipeIdbyWindowDishname(Window window,String dishname)throws Exception {
        Recipe recipe = new Recipe();
        try{
            recipe = this.dao.findRecipeIdbyWindowDishname(window,dishname);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            this.dbc.close();
        }
        return recipe;
    }


}
