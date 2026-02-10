package com.revature.dao;



import com.revature.model.User;

public interface UserDAO {

    boolean registerUser(User user);
    User login(String email, String password);
	int getUserIdByEmail(String email);
	
	boolean changePassword(int userId, String newPassword);

	boolean resetPassword(String email, String newPassword);
	boolean emailExists(String email);

}

