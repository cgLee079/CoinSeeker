package com.cglee079.coinseeker.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cglee079.coinseeker.model.ClientVo;

@Repository
public class ClientDao {
	final static String namespace = "com.cglee079.coinseeker.mapper.ClientMapper";

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public List<ClientVo> list() {
		return sqlSession.selectList(namespace + ".list");
	}
	
	public boolean insert(ClientVo client){
		try { return sqlSession.insert(namespace + ".insert", client) == 1; }
		catch (Exception e){ return false; }
	}

	public boolean delete(String userId) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		return sqlSession.delete(namespace + ".delete", map) == 1;
	}

	public boolean update(ClientVo client) {
		return sqlSession.update(namespace + ".update", client) == 1;
	}

	public ClientVo get(String userId) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		return sqlSession.selectOne(namespace + ".get", map);
	}

}
