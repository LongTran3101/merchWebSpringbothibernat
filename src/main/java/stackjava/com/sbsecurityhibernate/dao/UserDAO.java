package stackjava.com.sbsecurityhibernate.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import stackjava.com.sbsecurityhibernate.entities.AccountMerch;
import stackjava.com.sbsecurityhibernate.entities.ImageMerch;
import stackjava.com.sbsecurityhibernate.entities.SaleMerch;
import stackjava.com.sbsecurityhibernate.entities.User;
import stackjava.com.sbsecurityhibernate.entities.uploadFile;

@Repository(value = "userDAO")
@Transactional(rollbackFor = Exception.class)
public class UserDAO {

	@Autowired
	private SessionFactory sessionFactory;

	
	public List<AccountMerch> getAllUser(final String username) {
		try {
			List<AccountMerch> users = new ArrayList<AccountMerch>();
			Session session = this.sessionFactory.getCurrentSession();
			users = session.createNativeQuery("select * from account_merch where username=:user order by ip ASC", AccountMerch.class)
					.setParameter("user", username)
					.getResultList();
					

				return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		

	}
	public List<ImageMerch> getImageMerchFromSaleMerch(SaleMerch mech)
	{
		
		try {
			List<ImageMerch> lst=new ArrayList<>();
			Session session = this.sessionFactory.getCurrentSession();
			List<Object[]> rs=new ArrayList<Object[]>();
			
			rs = session.createNativeQuery("select DISTINCT name,url from image_merch where acc=:acc and day =:day")
					.setParameter("acc", mech.getName())
					.setParameter("day", mech.getDay())
					.getResultList();
			if(!rs.isEmpty())
			{
				for (Object[] objects : rs) {
					ImageMerch a=new ImageMerch();
					a.setName(String.valueOf(objects[0]));
					a.setUrl(String.valueOf(objects[1]));
					lst.add(a);
				}
			}
					

				return lst;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public List<User> getAllUserActive( ) {
		try {
			List<User> users = new ArrayList<User>();
			Session session = this.sessionFactory.getCurrentSession();
			users = session.createNativeQuery("select * from users ", User.class)
					.getResultList();
					

				return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		

	}
	
	
	
	
	public List<SaleMerch> getAllSaleMerch(String dayFrom,String dayto,String username ) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			List<SaleMerch> users = new ArrayList<SaleMerch>();
			Session session = this.sessionFactory.getCurrentSession();
			users = session.createNativeQuery("select * from sale_merch where  username=:username and :dayFrom <= day and day <= :dayto order by  sale desc , name ASC , ip ASC ", SaleMerch.class)
					.setParameter("dayFrom", df.parse(dayFrom))
					.setParameter("dayto", df.parse(dayto))
					.setParameter("username", username)
					.getResultList();
					

				return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		

	}
	
	public List<uploadFile> getAllUploadFile(String dayFrom,String dayto,String username ) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			List<uploadFile> users = new ArrayList<uploadFile>();
			Session session = this.sessionFactory.getCurrentSession();
			users = session.createNativeQuery("select * from upload_file where  username=:username and :dayFrom <= day and day <= :dayto order by   ip ASC ", uploadFile.class)
					.setParameter("dayFrom", df.parse(dayFrom))
					.setParameter("dayto", df.parse(dayto))
					.setParameter("username", username)
					.getResultList();
					

				return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		

	}
	
	public List<uploadFile> getAllUploadFileClear() {
		try {
			
			List<uploadFile> users = new ArrayList<uploadFile>();
			Session session = this.sessionFactory.getCurrentSession();
			users = session.createNativeQuery("select * from upload_file where  status ='2' and clearDate is null", uploadFile.class)
					
					.getResultList();
					

				return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		

	}
	
	public List<uploadFile> getAllUploadFileSearch(String dayFrom,String dayto,String username,String status,String idAccount ) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			List<uploadFile> users = new ArrayList<uploadFile>();
			String query="select * from upload_file where  username=:username and :dayFrom <= day and day <= :dayto ";
			if(status!=null && !status.isEmpty())
			{
				if(status.equalsIgnoreCase("5"))
				{
					query = query +" and status in ('5','6')";
				}else {
					query = query +" and status=:status";
				}
				
			}
			if(idAccount!=null && !idAccount.isEmpty())
			{
				
					query = query +" and idAccount=:idAccount";
				
				
			}
			
			
			query = query +" order by   ip ASC";
			Session session = this.sessionFactory.getCurrentSession();
			Query query2 = session.createNativeQuery(query, uploadFile.class)
					.setParameter("dayFrom", df.parse(dayFrom))
					.setParameter("dayto", df.parse(dayto))
					.setParameter("username", username);
			if(idAccount!=null && !idAccount.isEmpty())
			{
				
				query2.setParameter("idAccount", idAccount);
				
				
			}
			
					
					
			if(status!=null && !status.isEmpty())
			{
				if(status.equalsIgnoreCase("5"))
				{
					query = query +" status in ('5','6')";
				}else {
					query2.setParameter("status", status);
				}
				
			}
			users=query2.getResultList();
				return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		

	}
	public void clearUpdateDone() {
		 Calendar now = Calendar.getInstance();
		 now.add(Calendar.DAY_OF_MONTH, -5);
	        now.set(Calendar.HOUR, 0);
	        now.set(Calendar.MINUTE, 0);
	        now.set(Calendar.SECOND, 0);
	        now.set(Calendar.HOUR_OF_DAY, 0);
	        
		Session session = this.sessionFactory.getCurrentSession();
		session.createNativeQuery("DELETE FROM `upload_file` WHERE status='2' and day =:day ").setParameter("day", now.getTime()).executeUpdate();
		
				
	}
	public List<uploadFile> UpdateClearUploadFileFromlistID(List<Integer> ids ) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			List<uploadFile> users = new ArrayList<uploadFile>();
			Session session = this.sessionFactory.getCurrentSession();
			session.createNativeQuery("update upload_file set clearDate=1  where  id in :ids ").setParameter("ids", ids).executeUpdate();
			
					
					

				return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		

	}
	
	public List<uploadFile> getAllUploadFileFromlistID(List<Integer> ids ) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			List<uploadFile> users = new ArrayList<uploadFile>();
			Session session = this.sessionFactory.getCurrentSession();
			session.createNativeQuery("update upload_file set status='1'  where  id in :ids ").setParameter("ids", ids).executeUpdate();
			users = session.createNativeQuery("select * from upload_file where  id in :ids order by   ip ASC,nameAccount ASC ", uploadFile.class)
					.setParameter("ids", ids)
					
					.getResultList();
					

				return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		

	}
	
	public boolean deleteUploadFileFromlistID(List<Integer> ids ) {
		try {
			
			Session session = this.sessionFactory.getCurrentSession();
			session.createNativeQuery("delete FROM upload_file  where  id in :ids ").setParameter("ids", ids).executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
		

	}
	
	
	
	
	
	
	
	public Boolean saveOrUpdate(List<ImageMerch> image)
	{Session session = this.sessionFactory.getCurrentSession();
		
		
	
		try {

			if(image!=null && !image.isEmpty())
			{
				for (ImageMerch imageMerch : image) {
					imageMerch.setDay(new Date());
					 session.merge(imageMerch);
						session.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Boolean saveOrUpdateUploadFile(List<uploadFile> image)
	{Session session = this.sessionFactory.getCurrentSession();
		
		
	
		try {

			if(image!=null && !image.isEmpty())
			{
				for (uploadFile uploadFile : image) {
					uploadFile.setDay(new Date());
					 session.merge(uploadFile);
						session.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public AccountMerch saveOrUpdate(AccountMerch acc)
	{
		Session session = this.sessionFactory.getCurrentSession();
		AccountMerch out = new AccountMerch();
		try {

			out = (AccountMerch) session.merge(acc);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	
	public SaleMerch saveOrUpdate(SaleMerch acc)
	{
		Session session = this.sessionFactory.getCurrentSession();
		SaleMerch out = new SaleMerch();
		try {

			out = (SaleMerch) session.merge(acc);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	public uploadFile saveOrUpdateuploadFile(uploadFile acc)
	{
		Session session = this.sessionFactory.getCurrentSession();
		uploadFile out = new uploadFile();
		try {

			out = (uploadFile) session.merge(acc);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	
	public User saveOrUpdateUser(User use)
	{
		Session session = this.sessionFactory.getCurrentSession();
		User out = new User();
		try {

			out = (User) session.merge(use);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	
	
	public List<AccountMerch> getAccountMerchByip(String ip)
	{
		List<AccountMerch> users = new ArrayList<AccountMerch>();
		Session session = this.sessionFactory.getCurrentSession();
		users = session.createNativeQuery("select * from account_merch where  ip=:ip", AccountMerch.class)
				.setParameter("ip", ip)
				.getResultList();
				

			return users;
	}
	
	
	
	public AccountMerch getAccountMerchByID(int id)
	{
		Session session = this.sessionFactory.getCurrentSession();
		AccountMerch out = new AccountMerch();
		try {

			out = (AccountMerch) session.get(AccountMerch.class, id);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public uploadFile getuploadFileID(int id)
	{
		Session session = this.sessionFactory.getCurrentSession();
		uploadFile out = new uploadFile();
		try {

			out = (uploadFile) session.get(uploadFile.class, id);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public uploadFile getFileUploadByid(int id)
	{
		Session session = this.sessionFactory.getCurrentSession();
		uploadFile out = new uploadFile();
		try {

			out = (uploadFile) session.get(uploadFile.class, id);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public SaleMerch getSaleMerchByID(int id)
	{
		Session session = this.sessionFactory.getCurrentSession();
		SaleMerch out = new SaleMerch();
		try {

			out = (SaleMerch) session.get(SaleMerch.class, id);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public User getUserByID(int id)
	{
		Session session = this.sessionFactory.getCurrentSession();
		User out = new User();
		try {

			out = (User) session.get(User.class, id);
			session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	
	
	
	public boolean deleteSaleMerch (String username,String accname,Date day)
	{
		
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.createNativeQuery("DELETE FROM sale_merch WHERE  username=:username and :day = day and name=:name")
			.setParameter("username", username)
			.setParameter("day", day)
			.setParameter("name", accname).executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteuser (int id)
	{
		
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.createNativeQuery("DELETE FROM users_roles WHERE user= :id")
			.setParameter("id", id).executeUpdate();
			
			session.createNativeQuery("DELETE FROM users WHERE id= :id")
			.setParameter("id", id).executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean insertusersroles (int idUser,int idRole)
	{
		
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.createNativeQuery(" INSERT INTO users_roles(user,role) VALUES(:userID,:idRole) ")
			.setParameter("userID", idUser)
			.setParameter("idRole", idRole)
			.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean deleteAcoutMerch (int id)
	{
		
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.createNativeQuery("DELETE FROM account_merch WHERE id= :id")
			.setParameter("id", id).executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public User loadUserByUsername(final String username) {
		try {
			List<User> users = new ArrayList<User>();
			Session session = this.sessionFactory.getCurrentSession();
			users = session.createNativeQuery("select * from users where username=:user and enabled=:enabled ", User.class)
					.setParameter("user", username)
					.setParameter("enabled", 1)
					.getResultList();
					

			if (users.size() > 0) {
				return users.get(0);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
