package com.cglee079.coinseeker.coin;

import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;

import com.cglee079.coinseeker.exception.ServerErrorException;

public class PoloniexPooler extends ApiPooler{
	private JSONObject coinObjs;
	private String errMessage = "";
	
	public PoloniexPooler() {
		getCoins();
	}

	public JSONObject getCoin(String coin) throws ServerErrorException {
		String param = coinParam.get(coin);
		
		if(coinObjs != null) {
			JSONObject coinObj = coinObjs.getJSONObject(param);
			JSONObject newCoinObj = new JSONObject();
			newCoinObj.put("last", coinObj.getDouble("last"));
			newCoinObj.put("first", coinObj.getDouble("last") / ( 1 + coinObj.getDouble("percentChange")));
			newCoinObj.put("high", coinObj.getDouble("high24hr"));
			newCoinObj.put("low", coinObj.getDouble("low24hr"));
			newCoinObj.put("volume", coinObj.getDouble("quoteVolume"));
			newCoinObj.put("errorCode", 0);
			newCoinObj.put("errorMsg", "");
			newCoinObj.put("result", "success");
			return newCoinObj;
		} else {
			throw new ServerErrorException("Poloniex Server Error : " + errMessage );
		}
		
	}
	
	@Scheduled(cron = "00 0/1 * * * *")
	public void getCoins() {
		String url = "https://poloniex.com/public?command=returnTicker";
		HttpClient httpClient = new HttpClient();
		try {
			coinObjs = new JSONObject(httpClient.get(url));
		} catch (Exception e) {
			coinObjs 	= null;
			errMessage	= e.getMessage();
		}
	}
}
