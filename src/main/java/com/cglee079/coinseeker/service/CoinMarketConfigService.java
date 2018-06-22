package com.cglee079.coinseeker.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cglee079.coinseeker.dao.CoinMarketConfigDao;
import com.cglee079.coinseeker.model.CoinMarketConfigVo;

@Service
public class CoinMarketConfigService {

	@Autowired
	private CoinMarketConfigDao coinMarketConfigDao;

	public HashMap<String, String> getMarketParams(String market) {
		HashMap<String, String> map = new HashMap<>();
		List<CoinMarketConfigVo> configs = coinMarketConfigDao.list(market, null);
		CoinMarketConfigVo config = null;
		for (int i = 0; i < configs.size(); i++) {
			config = configs.get(i);
			map.put(config.getCoinId(), config.getParam());
		}
		return map;
	}

	public List<CoinMarketConfigVo> list(String market, String coinId) {
		return coinMarketConfigDao.list(market, coinId);
	}

	public CoinMarketConfigVo get(String market, String coinId) {
		return coinMarketConfigDao.get(market, coinId);
	}
}