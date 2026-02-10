package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.dao.impl.UserDAOImpl;
import com.revature.model.User;

public class UserService {

    private UserDAO userDAO = new UserDAOImpl();

    public boolean register(User user) {
        return userDAO.registerUser(user);
    }

    public User login(String email, String password) {
        return userDAO.login(email, password);
    }

	public int getUserIdByEmail(String email) {
		// TODO Auto-generated method stub
		return userDAO.getUserIdByEmail(email);	}
	
	public boolean changePassword(int userId, String pass){
	    return userDAO.changePassword(userId, pass);
	}

	public boolean forgotPassword(String email, String pass){
	    return userDAO.resetPassword(email, pass);
	}
	
	
	public boolean emailExists(String email){
	    return userDAO.emailExists(email.toLowerCase());
	}


}

