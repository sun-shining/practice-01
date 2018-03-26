package org.juddar.dao;

import org.juddar.entity.User;

import java.util.List;

public interface UserDao {

    public List<User> selectUserList();

    public User updateByID(User user);
}
