package ci.indigo.entities;


import javax.ejb.Stateless;
@Stateless
public class User {

	
	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private String phone;
	private String role;
	
	
	
	/**********Setters**********/
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setRole(String role) {
		this.role = role;
	}
	/**********Getters**********/
	
	public Long getId() {
		return id;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}	
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public String getRole() {
		return role;
	}
	
	// Others methodes	
	public Boolean isAdmin() {
		return (this.role.equals("administrateur"))?  true : false;
	}
}
