/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import CustomExceptions.*;
import users.*;

/**
 * @author s3419069 (Mykhailo Muzyka)
 *	this class represents Data Access Layer. Here we can refer to other resources to get data such as DB
 */
public class ProfileRepository {
	
	/**
	 * return all profiles from current repository
	 */
	public List<Profile> getAll() {
		try {
			Connection con =
					DriverManager.getConnection(
							Initializer.dbName,
							Initializer.userName, Initializer.userPass);
			Statement stmt = con.createStatement();
			//select all profiles
			ResultSet rs = stmt.executeQuery("select * from Profiles");
			List<Profile> allProfiles = new ArrayList<Profile>();
			while (rs.next()) {
				//read each row
				String name = rs.getString("Name");
				String image = rs.getString("Image");
				String status = rs.getString("Status");
				String gender = rs.getString("Gender");
				int age = rs.getInt("Age");
				String state = rs.getString("State");
				
				Profile profile = null;
				if (age > 16) {
					//add adult and fill children
					profile = new AdultProfile(name, age, status, gender, state);
					fillChildren((AdultProfile)profile, stmt);
				}
				else {
					//add children and add parents
					String[] parents = getParents(name);
					AdultProfile parent1 = (AdultProfile)getProfile(parents[0]);
					AdultProfile parent2 = (AdultProfile)getProfile(parents[1]);
					
					profile = new ChildProfile(name, age, status, image, gender, state,
							parent1.getGender() == "F"
								? parent1 : parent2,
							parent2.getGender() == "M"
								? parent2 : parent1);
				}
				allProfiles.add(profile);
			}
			System.out.println(allProfiles.size());
			return allProfiles;
		} catch (Exception e) { }
		return null;	
	}
	
	
	/**
	 * get profile by name
	 */
	public Profile getProfile(String Name, boolean fillChildren) {
		try {
			Connection con =
					DriverManager.getConnection(
							Initializer.dbName,
							Initializer.userName, Initializer.userPass);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Profiles "
					+ "where Name ='" + Name + "'");
			if (!rs.next()) return null;
			
			String name = rs.getString("Name");
			String image = rs.getString("Image");
			String status = rs.getString("Status");
			String gender = rs.getString("Gender");
			int age = rs.getInt("Age");
			String state = rs.getString("State");
			
			if (age > 16) {
				AdultProfile profile = new AdultProfile(name, age,
						status, gender, state);
				if (fillChildren) {
					fillChildren(profile, stmt);
				}
				return profile;
			}
			else {
				String[] parents = getParents(name);
				AdultProfile parent1 = (AdultProfile)getProfile(parents[0], false);
				AdultProfile parent2 = (AdultProfile)getProfile(parents[1], false);
				
				return new ChildProfile(name, age, status, image, gender,
						state, parent1, parent2);
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * add children to DB due to relations from DB
	 * @throws SQLException
	 */
	private void fillChildren(AdultProfile profile, Statement stmt)
			throws SQLException {
		ResultSet rs = stmt.executeQuery("select CASEWHEN("
				+ "SecondProfile ='" + profile.getName() + "',"
				+ "FirstProfile, SecondProfile) from Relations "
				+ "where (FirstProfile = '" + profile.getName()
				+ "' or SecondProfile = '" + profile.getName()
				+ "') and relation = 'parent'");
		while (rs.next()) {
			String name = rs.getString(1);
			profile.addChildren((ChildProfile)getProfile(name));
		}
	}

	/**
	 * @param Name
	 * @return parent's name array
	 * @throws NoParentException
	 */
	public String[] getParents(String Name) throws NoParentException {	
		try {
			Connection con =
				DriverManager.getConnection(
							Initializer.dbName,
							Initializer.userName, Initializer.userPass);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
				"select CASEWHEN(p.Name = r.FirstProfile,"
				+ "r.SecondProfile, r.FirstProfile), Gender" + 
				" from public.Profiles p\r\n" + 
				"join relations r on"
				+ "(r.FirstProfile = p.Name or r.SecondProfile = p.Name)"
				+ "and relation = 'parent'\r\n" + 
				"where age < 16 and name = '" + Name + "'");
			if (!rs.next()) throw new NoParentException(Name + "has no parents");
			
			String[] parents = new String[2];
			parents[0] = rs.getString(1);
			if (!rs.next()) throw new NoParentException(Name + "has no parent");
			parents[1] = rs.getString(1);
			
			return parents;
		} catch (Exception e) {
			throw new NoParentException(Name + "has no parent");
		}
	}
	
	/**
	 * add new profile
	 * @throws SQLException 
	 */
	public void add(String name, String image, String status, String gender,
				String age, String state, Statement stmt)
			throws SQLException {
		String query = "INSERT INTO public.Profiles VALUES ('"
				+ name + "','" 		//Name
				+ image + "','" 		//Image
				+ status + "','" 	//Status
				+ gender + "'," 	//Gender
				+ age + ",'" 		//Age
				+ state + "')"; 	//State
		stmt.executeUpdate(query);
	}
	
	/**
	 * add new profile
	 * @throws SQLException 
	 */
	public void add(String name, String image, String status, String gender,
			int age, String state)
			throws SQLException {
		Statement stmt = DriverManager.getConnection(
				Initializer.dbName,
				Initializer.userName, Initializer.userPass)
				.createStatement();
		add(name, image, status, gender, String.valueOf(age), state, stmt);
	}
	
	/**
	 * delete the profile by its Name
	 * return false when error occurs
	 */
	public Boolean delete(String name) {
		try {
			Statement stmt = DriverManager.getConnection(
					Initializer.dbName,
					Initializer.userName, Initializer.userPass)
					.createStatement();
			stmt.executeUpdate("delete from public.Relations "
					+ "where FirstProfile ='" + name + "'");
			stmt.executeUpdate("delete from public.Relations "
					+ "where SecondProfile ='" + name + "'");
			stmt.executeUpdate("delete from public.Profiles "
					+ "where Name ='" + name + "'");				
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * find out if 2 people directly connected
	 */
	public boolean isDirectlyConnected(String name1, String name2) {
		try {
			Statement stmt = DriverManager.getConnection(
					Initializer.dbName,
					Initializer.userName, Initializer.userPass)
					.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Relations "
					+ "where (FirstProfile ='" + name1 + "' and "
					+ "SecondProfile ='" + name2 + "') or (FirstProfile ='"
					+ name2 + "' and SecondProfile ='" + name1 + "')");
			return rs.next();
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * find out if couple allowed
	 */
	public boolean coupleAllowed(String name1, String name2)
			throws SQLException {
		Statement stmt = DriverManager.getConnection(
				Initializer.dbName,
				Initializer.userName, Initializer.userPass)
				.createStatement();
		return coupleAllowed(name1, name2, stmt);
	}
	
	/**
	 * try to find another couple when at least one spouse is part
	 * of other couple
	 * returns true when no element found
	 */
	public boolean coupleAllowed(String name1, String name2,
			Statement stmt) throws SQLException {
		// 
		ResultSet rs = stmt.executeQuery( 
				"select distinct * from relations \r\n" + 
				"where relation = 'couple' and (\r\n" + 
				"'" + name1 + "' = firstprofile or\r\n" + 
				"'" + name1 + "' = secondprofile or \r\n" + 
				"'" + name2 + "' = firstprofile or \r\n" + 
				"'" + name2 + "' = secondprofile  )");		
		return !rs.next();
	}

	/**
	 * add new relation to DB
	 * return true if successful
	 */
	public boolean setRelation(String name1, String name2, String relation) {
		try {
			Statement stmt = DriverManager.getConnection(
					Initializer.dbName,
					Initializer.userName, Initializer.userPass)
					.createStatement();
			setRelation(name1, name2, relation, stmt);
			return true;
		} catch (Exception e) {
			return false; 
		}
	}
	
	/**
	 * create new relation
	 */
	public void setRelation(String name1, String name2, String relation,
			Statement stmt) throws SQLException {
		stmt.executeUpdate("INSERT INTO public.Relations VALUES ('"
				+ name1 + "','"
				+ name2 + "','"
				+ relation + "')");
	}

	/**
	 * return spouses separated by separator
	 */
	public String[] getSpouses(String separator) {
		try {
			Statement stmt = DriverManager.getConnection(
					Initializer.dbName,
					Initializer.userName, Initializer.userPass)
					.createStatement();
			//select only couples
			ResultSet rs = stmt.executeQuery(
					"select * from relations where relation = 'couple'");
			if (!rs.next()) return null; //when no couples found
			List<String> couples = new ArrayList<>();
			do {
				couples.add(rs.getString(1) + separator + rs.getString(2));
			} while (rs.next());
			//convert list to array and return
			String[] stockArr = new String[couples.size()];
			return couples.toArray(stockArr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * return profile by name
	 */
	private Profile getProfile(String Name) {
		return getProfile(Name, true);
	}
}
