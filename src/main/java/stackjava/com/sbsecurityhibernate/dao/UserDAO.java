package stackjava.com.sbsecurityhibernate.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import stackjava.com.sbsecurityhibernate.entities.AccountMerch;
import stackjava.com.sbsecurityhibernate.entities.ImageMerch;
import stackjava.com.sbsecurityhibernate.entities.SaleMerch;
import stackjava.com.sbsecurityhibernate.entities.User;

@Repository(value = "userDAO")
@Transactional(rollbackFor = Exception.class)
public class UserDAO {

	@Autowired
	private SessionFactory sessionFactory;

	
	public List<AccountMerch> getAllUser(final String username) {
		try {
			List<AccountMerch> users = new ArrayList<AccountMerch>();
			Session session = this.sessionFactory.getCurrentSession();
			users = session.createNativeQuery("select * from account_merch where username=:user", AccountMerch.class)
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
					a.setUrl(String.valueOf(objects[0]));
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
