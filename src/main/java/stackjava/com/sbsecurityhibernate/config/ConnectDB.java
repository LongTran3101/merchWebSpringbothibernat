package stackjava.com.sbsecurityhibernate.config;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class ConnectDB {
    private Connection con;
    public Connection getCon() { 
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public static Statement getSt() {
		return st;
	}

	public static void setSt(Statement st) {
		ConnectDB.st = st;
	}

	public static ResultSet getRs() {
		return rs;
	}

	public static void setRs(ResultSet rs) {
		ConnectDB.rs = rs;
	}

	private static Statement st;
    private static ResultSet rs;
    
    
    public ConnectDB(){
    try{
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/spy","root","");
        st = con.createStatement();
        
    }catch(Exception ex){
        System.out.println("Error is found :"+ex);
    }
}
    
    public String getAdress(String key) {
    	String adr="";
    	try {
    		String sql = "select address from tbl_info where id_key ="+key+" and status = 1 and Ngay_tao <= ENDDATE";
        	rs = st.executeQuery(sql);
        	
        	
        	 while(rs.next()){
        		 adr = rs.getString("address");
                 
             }
        	 st.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return adr;
    	
    }
    public String update(String key,String adress)
    {
    	String adr="";
    	try {
    		java.util.Date date=new java.util.Date();
    		java.sql.Date sqlDate=new java.sql.Date(date.getTime());
    		String sql = "update tbl_info set address = ? , Ngay_tao = ? where id_key ="+key +" and Ngay_tao< ?";
    		PreparedStatement ps=con.prepareStatement(sql);
    		ps.setString(1,adress);
    		ps.setDate(2,sqlDate);
    		ps.setDate(3,sqlDate);
    		ps.executeUpdate();
    		
    		ps.close();
        	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("loi update");
			// TODO: handle exception
		}
    	return adr;
    }
    
    public String insert(String key,String adress)
    {
    	String adr="";
    	try {
    		java.util.Date date=new java.util.Date();
    		java.sql.Date sqlDate=new java.sql.Date(date.getTime());
    		String sql = "insert into tbl_info (id_key, address, Ngay_tao, status,ENDDATE) VALUES ("+key+",?,?,0,?)";
    		PreparedStatement ps=con.prepareStatement(sql);
    		ps.setString(1,adress);
    		ps.setDate(2,sqlDate);
    		ps.setDate(3,sqlDate);
    		ps.executeUpdate();
    		ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("loi update");
			// TODO: handle exception
		}
    	return adr;
    }
    
    public void getData(){
        try{
            String sql = "select * from spyhours";
            rs = st.executeQuery(sql);
            System.out.println("Data from online Database :");
            while(rs.next()){
                String name = rs.getString("id");
                String area = rs.getString("value");
                String pin = rs.getString("count");
                String time = rs.getString("time");
                System.out.println("Name :"+name+" "+"Area :"+area+" "+"PinCode :"+pin+"time :"+time);
            }
            
        }catch(Exception ex){
            System.out.println("Error is found :"+ex);
        }
    }
//    public static void insert(List<Image> a,String contry)
//    {
//    	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
//    	String date= simpleDateFormat.format(new Date());
//    	for (Image entity : a) {
//    		 String sql = "INSERT INTO `spyhours` (`id`, `value`, `count`, `time`,`contry`) VALUES (NULL, '"+entity.value+"', '"+entity.count+"', '"+date+" 00:00:00','"+contry+"')";
//    		  try {
//			st.execute(sql);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//    }
//    
    
//    public static void insert()
//    {SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
//    	String date= simpleDateFormat.format(new Date());
//    	
//    		 String sql = "INSERT INTO `spyhours` (`id`, `value`, `count`, `time`,`contry`) VALUES (NULL, '1', '1', '"+date+" 00:00:00','1')";
//    		  try {
//			st.execute(sql);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		
//    }
}
