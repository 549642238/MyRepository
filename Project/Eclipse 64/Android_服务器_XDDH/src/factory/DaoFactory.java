package factory;
import dao.IRecipeDao;
import dao.IRemarkDao;
import dao.IUserDao;
import dao.IWindowDao;
import dao.proxy.RecipeDaoProxy;
import dao.proxy.RemarkDaoProxy;
import dao.proxy.UserDaoProxy;
import dao.proxy.WindowDaoProxy;
public class DaoFactory {
    public static IUserDao getUserInstance()throws Exception{
        return new UserDaoProxy();
    }
    public static IRemarkDao getRemarkInstance()throws Exception{
        return new RemarkDaoProxy();
    }
    public static IWindowDao getWindowInstance() throws Exception{
        return new WindowDaoProxy();
    }
    public static IRecipeDao getRecipeInstance() throws Exception{
        return new RecipeDaoProxy();
    }
}
