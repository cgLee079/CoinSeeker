package com.cglee079.coinseeker.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cglee079.coinseeker.model.CoinMsgConfigVo;

@Repository
public class CoinMsgConfigDao {
	final static String namespace = "com.cglee079.coinseeker.mapper.CoinMsgConfigMapper";

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public CoinMsgConfigVo get(String coinId) {
		return sqlSession.selectOne(namespace + ".get", coinId);
	}
}
