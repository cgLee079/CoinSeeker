package com.cglee079.coinseeker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cglee079.coinseeker.dao.CoinMsgConfigDao;
import com.cglee079.coinseeker.model.CoinMsgConfigVo;

@Service
public class CoinMsgConfigService {

	@Autowired
	private CoinMsgConfigDao coinConfigDao;

	public CoinMsgConfigVo get(String coinId) {
		return coinConfigDao.get(coinId);
	}
}