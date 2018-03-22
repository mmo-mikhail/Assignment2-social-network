package Users;
import java.util.*;


public abstract class Profile {
	
	public List<Profile> Friends = new ArrayList<Profile>();
	
	
	private String Status;
	private int Age;
	private String Name;
	private byte[] Image;
	
	protected Profile(String name, int age, String status) {
		Status = status;
		Age = age;
		Name = name;
	}

	public int getAge() {
		return Age;
	}

	public void setAge(int age) {
		Age = age;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	private String getName() {
		return Name;
	}

	private void setName(String name) {
		Name = name;
	}

	private byte[] getImage() {
		return Image;
	}

	private void setImage(byte[] image) {
		Image = image;
	}
}
