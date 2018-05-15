/*
 * @author s3419069 (Mykhailo Muzyka)
 * 
 * Copyright (c) 2018 RMIT University, Advanced Programming (COSC1295) Assignment 2
 */
package repository;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Initializer {

	public final static String dbName = "jdbc:hsqldb:db\\socialNetwork;hsqldb.write_delay=false;";
	public final static String userName = "SA";
	public final static String userPass = "123";
	
	static String logs = "";
	
	// The name of the people file to open.
	final static String peopleFileName = "people.txt";

	// The name of the relationship file to open.
	final static String relationshipsFileName = "relations.txt";

	/**
	 * storage of profiles
	 */
	private static ProfileRepository profileRepository =
			new ProfileRepository();
	
	private Initializer() {
	}

	public static String Init() {
		if (!checkClassPath()) {
			return "DB Error: Try add hsqldb.jar to classpath";
		}
		File peopleFile = new File(peopleFileName);
		System.out.println(peopleFile.getAbsolutePath());
		File relationFile = new File(relationshipsFileName);
		if (peopleFile.exists() && !peopleFile.isDirectory()
				&& relationFile.exists() && !relationFile.isDirectory()) {
			// try create/udpate database
			return updateDb();
		}
		// if db exist, it will be used later. Otherwise display error
		if (!isDbExists()) {
			return "Error: required files and database cannot be found!";
		}
		return null;
	}

	private static boolean checkClassPath() {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (java.lang.ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	private static boolean isDbExists() {
		try {
			Connection con =
					DriverManager.getConnection(
							dbName + ";ifexists=true", userName, userPass);
			con.createStatement();
		} catch (Exception e) {
			if (e.getMessage().contains("Database does not exists")) {
				return false;
			}
			e.printStackTrace(System.out);
		}
		return true;
	}

	private static String updateDb() {
		try {
			Connection con =
					DriverManager.getConnection(dbName, userName, userPass);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(
					"DROP TABLE IF EXISTS Relations;"
					+ "DROP TABLE IF EXISTS Profiles;"
					+ "CREATE TABLE Profiles (\r\n"
					+ "Name VARCHAR(100) NOT NULL,\r\n"
					+ "Image VARCHAR(100) NOT NULL,\r\n"
					+ "Status VARCHAR(100) NOT NULL,\r\n"
					+ "Gender VARCHAR(100) NOT NULL,\r\n"
					+ "Age VARCHAR(100) NOT NULL,\r\n"
					+ "State VARCHAR(100) NOT NULL,\r\n"
					+ "PRIMARY KEY (Name));\r\n"
					
					+ "CREATE TABLE Relations (\r\n"
					+ "FirstProfile VARCHAR(100) NOT NULL,\r\n"
					+ "SecondProfile VARCHAR(100) NOT NULL,\r\n"
					+ "Relation VARCHAR(50) NOT NULL,\r\n"
					+ "Primary key(firstProfile, secondProfile, relation),\r\n"
					+ "Foreign key(firstProfile) references "
					+ "public.Profiles(Name),\r\n"
					+ "Foreign key(secondProfile) references "
					+ "public.Profiles(Name));");
			if (!readPeopleFile(stmt)) {
				return "Unexpected Error: 'people.txt' file is corrupted";
			}
			if (!readRelashionsFile(stmt)) {
				return "Unexpected Error: 'relations.txt' file is corrupted";
			}
			deleteWrongProfiles(stmt);
			con.commit();
			con.close();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return "DB Error: Cannot Access Database!";
		}
		return null;
	}

	private static void deleteWrongProfiles(Statement stmt)
			throws SQLException {
		
		//simple query below retrieves names of children who don't have two parents. 
		ResultSet rs = stmt.executeQuery(
				"select Name from public.Profiles \r\n" + 
				"where age < 16 and (\r\n" + 
				" 2 <> (select count(*) from public.Relations where "
				+ "relation = 'parent' and FirstProfile = Profiles.Name) "
				+ "and\r\n" + 
				" 2 <> (select count(*) from public.Relations "
				+ "where relation ='parent' and SecondProfile =Profiles.Name) "
				+ "and\r\n" + 
				" ( 1 <> (select count(*) from public.Relations "
				+ "where relation = 'parent' and FirstProfile =Profiles.Name) "
				+ "and\r\n" + 
				"   1 <> (select count(*) from public.Relations "
				+ "where relation = 'parent' "
				+ "and SecondProfile = Profiles.Name) ) )");
		List<String> profilesDeleted = new ArrayList<String>();
		while(rs.next()) {
			String name = rs.getString("Name");
			profileRepository.delete(name);
			profilesDeleted.add(name);
		}
		if (profilesDeleted.size() == 0) return;
		addLogs(System.lineSeparator() + "Profiles deleted due to constrains:"
				+ System.lineSeparator()
				+ String.join(System.lineSeparator(), profilesDeleted));
	}

	private static boolean readRelashionsFile(Statement stmt) {
		try {
			// FileReader reads text files in the default encoding.
			// and wrapping FileReader in BufferedReader.
			FileReader fileReader = new FileReader(relationshipsFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] values = line.split(",");	
				if (values.length != 3 || line.contains("'")) {
					addLogs("relations.txt content warning: " + values + 
							" - invalid row!");
					continue;
				}
				String name1 =values[0].trim();
				String name2 =values[1].trim();
				String relation = values[2].trim();
				if (!isNameExist(name1, stmt) || !isNameExist(name2, stmt)
					|| !isValidRelation(values, stmt, line)) {
					continue;
				}
				profileRepository.setRelation(name1, name2, relation, stmt);
			}
			// Always close files.
			bufferedReader.close();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			// System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	private static boolean isValidRelation(String[] values,
			Statement stmt, String line)
			throws SQLException {
		String name1 =values[0].trim();
		String name2 =values[1].trim();
		String warningLine = "relations.txt content warning: " + line + " - ";
		if (name1.equals(name2)) {
			addLogs(warningLine + "same names");
			return false;
		}
		int age1 = getAge(name1, stmt);
		int age2 = getAge(name2, stmt);
		switch(values[2].trim()) {
			case "friends":
				if (age1 <= 16 && age2 > 16 || age2 <= 16 && age1 > 16) {
					addLogs(warningLine
							+ "invalid friendship child with adult");
					return false;
				}
				if (age1 <= 2 || age2 <= 2) {
					addLogs(warningLine + "young child cannot have friends");
					return false;
				}
				if (age1 <= 16 && age2 <= 16
					&& (age1 - age2 > 3 || age2 - age1 > 3)) {
					addLogs(warningLine + "child's age diff is over 3 years");
					return false;
				}
				break;
			case "parent":
				if (age1 <= 16 && age2 <= 16) {
					addLogs(warningLine + "parents are too young");
					return false;
				}
				if (age1 > 16 && age2 > 16) {
					addLogs(warningLine + "child is too old");
					return false;
				}
				break;
			case "classmates":
				if (age1 <= 2 || age2 <= 2) {
					addLogs(warningLine + "young child cannot be classmates");
					return false;
				}
				break;
			case "colleagues":
				if (age1 <= 16 || age2 <= 16) {
					addLogs(warningLine + "only adults can be colleagues");
					return false;
				}
				break;
			case "couple":
				if (age1 <= 16 || age2 <= 16) {
					addLogs(warningLine + "only adults can be married");
					return false;
				}
				//All couples are mutually exclusive to other couples
				if (!profileRepository.coupleAllowed(name1, name2, stmt)) {
					addLogs(warningLine + "all couples are mutually exclusive"
							+ " to other couples");
					return false;
				}
				break;
		}
		return true;
	}

	

	private static int getAge(String name, Statement stmt)
			throws SQLException {
		ResultSet rs = stmt.executeQuery(
				"select Age from public.Profiles where Name ='"
				+ name + "'");
		rs.next();
		return rs.getInt(1);
	}

	private static boolean isNameExist(String name, Statement stmt)
			throws SQLException {
		ResultSet rs = stmt.executeQuery(
				"select count(*) from public.Profiles where Name ='"
				+ name + "'");
		if (rs.next() && rs.getInt(1) == 0) {
			addLogs("relations.txt content warning: " + name + 
					" - no such name in people.txt!");
			return false;
		}
		return true;
	}

	private static String[] validStates =
			new String[] {"ACT", "NSW", "NT", "QLD", "SA", "TAS", "VIC","WA"};
	
	private static boolean readPeopleFile(Statement stmt) {
		try {
			// FileReader reads text files in the default encoding.
			// and wrapping FileReader in BufferedReader.
			FileReader fileReader = new FileReader(peopleFileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				line = line.replace("“", "").replace("”", "")
						.replace("\"", "");
				String[] values = line.split(",");
				if (!validatePerson(values)) {
					continue;
				}
				profileRepository.add(
						values[0].trim(),
						values[1].trim(),
						values[2].trim(),
						values[3].trim(),
						values[4].trim(),
						values[5].trim(), stmt);
			}
			// Always close files.
			bufferedReader.close();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return false;
		}
		return true;
	}
	
	private static boolean validatePerson(String[] values) {
		if (values.length == 1) {
			addLogs("people.txt content error: " + values[0]
					+ " - must be comma separated!");
			return false;
		}
		if (values.length != 6) {
			addLogs("people.txt content warning: " + values[0]
					+ " - has invalid number of parameters!");
			return false;
		}
		if (!Arrays.asList(validStates).contains(values[5].trim())) {
			addLogs("people.txt content warning: " + values[0]
					+ " - has invalid state!");
			return false;
		}
		if (!values[3].trim().equals("M") && !values[3].trim().equals("F")) {
			addLogs("people.txt content warning: " + values[0]
					+ " - has invalid gender!");
			return false;
		}
		return true;
	}

	private static void addLogs(String text) {
		logs += text + " (current line will be skipped)" + System.lineSeparator();
	}
	
	public static String getLogs() {
		return logs;
	}
}

/*
 CREATE TABLE Profiles ( id INT NOT NULL, Name VARCHAR(100) NOT NULL, Status
 VARCHAR(100) NOT NULL, Image VARCHAR(100) NOT NULL, Gender VARCHAR(100) NOT
 NULL, Age VARCHAR(100) NOT NULL, LivingState VARCHAR(100) NOT NULL, PRIMARY
 KEY (id)); CREATE TABLE Relations ( FirstProfileId INT NOT NULL,
 SecondProfileId INT NOT NULL, Relation VARCHAR(50) NOT NULL, Primary
 key(firstProfileId, secondProfileId), Foreign key(firstProfileId) references
 public.Profiles(id), Foreign key(secondProfileId) references
public.Profiles(id));


select Name from public.Profiles
where age < 16 and (
 2 <> (select count(*) from public.Relations where relation = 'parent' and FirstProfile = Profiles.Name) and
 2 <> (select count(*) from public.Relations where relation = 'parent' and SecondProfile = Profiles.Name) and
 ( 1 <> (select count(*) from public.Relations where relation = 'parent' and FirstProfile = Profiles.Name) and
   1 <> (select count(*) from public.Relations where relation = 'parent' and SecondProfile = Profiles.Name) ) )
*/