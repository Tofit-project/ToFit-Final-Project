package com.tofit.mvc.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tofit.mvc.model.dao.FollowsDao;
import com.tofit.mvc.model.dao.UserDao;
import com.tofit.mvc.model.dto.Follows;

@Service
public class FollowsServiceImpl implements FollowsService{
	
	private final FollowsDao followsDao;
	  private final UserDao userDao;

	public FollowsServiceImpl(FollowsDao followsDao, UserDao userDao) {
		this.followsDao = followsDao;
		this.userDao = userDao;
	}

	@Override
	public Boolean registerFollow(Follows follows) {
		return followsDao.insertFollow(follows) == 1;
	}

	@Override
	public Boolean removeFollow(Follows follows) {
		return followsDao.deleteFollow(follows) == 1;
	}

	@Override
	public Boolean changeFollowCheckStatus(Follows follows) {
		return followsDao.updateFollowCheckStatus(follows) == 1;
	}

	@Override
	public List<Map<String, Object>> getFollowList(String id) {
		List<Map<String, Object>> res = new ArrayList<>();

		List<Follows> follows = followsDao.selsectFollowList(id);
	
		 for(Follows follow : follows) {
			 Map<String, Object> userInfo = userDao.selectProfileInfo(follow.getFollowedId());
			 
			  Map<String, Object> followWithUserInfo = new HashMap<>();
			  followWithUserInfo.put("follow", follow);
			  followWithUserInfo.put("profileName", userInfo.get("profile_name"));
			  followWithUserInfo.put("profileImg", userInfo.get("profile_img"));
			  
			  res.add(followWithUserInfo);
		 }
		 return res;	
	}

}
