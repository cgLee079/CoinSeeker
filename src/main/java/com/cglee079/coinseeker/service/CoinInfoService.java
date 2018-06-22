package com.cglee079.coinseeker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cglee079.coinseeker.dao.CoinInfoDao;
import com.cglee079.coinseeker.model.CoinInfoVo;

@Service
public class CoinInfoService {

	@Autowired
	private CoinInfoDao coinInfoDao;

	public List<CoinInfoVo> list(String coinId) {
		return coinInfoDao.list(coinId);
	}

}