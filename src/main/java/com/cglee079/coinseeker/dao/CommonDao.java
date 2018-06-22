package com.cglee079.coinseeker.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDao {
	final static String namespace = "com.cglee079.coinseeker.mapper.CommonMapper";

	@Autowired
	private SqlSessionTemplate sqlSession;

	public String get(String id) {
		return sqlSession.selectOne(namespace + ".get", id);
	}

}
