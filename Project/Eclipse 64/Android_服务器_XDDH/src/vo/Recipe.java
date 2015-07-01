package vo;

public class Recipe {
    private int recipeid;
    private String windowid;
    private String dishname;
    private double averagedishscore;
    public void setRecipeId(int id){
        this.recipeid = id;
    }
    public void setWindowId(String name){
        this.windowid = name;
    }
    public void setDishname(String name){
        this.dishname = name;
    }
    public void setAverageDishScore(double score){
        this.averagedishscore = score;
    }
    public int getRecipeId(){
        return this.recipeid;
    }
    public String getWindowId(){
        return this.windowid;
    }
    public String getDishName(){
        return this.dishname;
    }
    
    public double getAverageDishScore(){
        return this.averagedishscore;
    }
}
