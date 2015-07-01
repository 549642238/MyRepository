package dao;

import java.util.ArrayList;

import vo.Recipe;
import vo.Window;

public interface IRecipeDao {
    public Recipe findRecipeIdbyWindowDishname(Window window,String dishname) throws Exception;
    public ArrayList<String> findDishsbyWindowid(Window window)throws Exception ;
}
