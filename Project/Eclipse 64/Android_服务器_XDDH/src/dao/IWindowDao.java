package dao;

import java.util.ArrayList;

import vo.Window;

public interface IWindowDao {
    public ArrayList<String> findBuildName() throws Exception;
    public ArrayList<String> findLayers(String buildname) throws Exception;
    public ArrayList<String> findWindowsbyBuildandLayer(String buildname,String layer) throws Exception;
    public Window findWindowbyBuildLayerandWindowname(String build,String layer,String windowname)throws Exception;
}
