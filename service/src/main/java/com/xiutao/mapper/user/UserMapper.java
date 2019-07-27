package com.xiutao.mapper.user;

import com.xiutao.pojo.user.User;
import com.xiutao.pojo.user.UserExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {

	int insert(User record);
	
	List<User> findUserByUserName(Map map);
	
	List<User> findUserByEmail(Map map);
	
	int updateByPrimaryKeySelective(User record);
	
	
	////////////////////////////////////////////////////////////////////////////////////

	int countByExample(UserExample example);

	int deleteByExample(UserExample example);

	int deleteByPrimaryKey(Integer id);

	int insertSelective(User record);

	List<User> selectByExample(UserExample example);

	User selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

	int updateByExample(@Param("record") User record, @Param("example") UserExample example);

	int updateByPrimaryKey(User record);
}