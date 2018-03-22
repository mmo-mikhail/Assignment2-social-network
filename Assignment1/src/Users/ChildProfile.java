package Users;

public class ChildProfile extends Profile {

	private Profile mother;
	private Profile father;
	
	
	
	public Profile getFather() {
		return father;
	}
	public void setFather(Profile father) {
		this.father = father;
	}
	private Profile getMother() {
		return mother;
	}
	private void setMother(Profile mother) {
		this.mother = mother;
	}
}
