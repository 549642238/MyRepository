package dbc;
import java.sql.*;
public class DatabaseConnection {
    private static final String DBDRIVER = "com.mysql.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/roast";
//    private static final String DBDRIVER = "org.mariadb.jdbc.Driver";
  //  private static final String DBURL = "jdbc:mariadb://localhost:3306/roast";
    private static final String DBUSER = "root";
    private static final String DBPASSWD = "12345";
    private Connection conn;
    public DatabaseConnection()throws Exception{
        Class.forName(DBDRIVER);
        this.conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWD);
    }
    public Connection getConnection(){
        return this.conn;
    }
    public void close() throws Exception{
        if(this.conn != null){
            try{
                this.conn.close();
            }catch(Exception e){
                throw e;
            }
        }
    }
}
