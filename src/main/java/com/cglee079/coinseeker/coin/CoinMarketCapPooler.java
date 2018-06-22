package com.cglee079.coinseeker.coin;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;

import com.cglee079.coinseeker.exception.ServerErrorException;

public class CoinMarketCapPooler extends ApiPooler {
	private JSONArray data = null;

	public CoinMarketCapPooler() throws ServerErrorException {
		this.updateData();
	}

	public JSONObject getCoin(String coin){
		String param = "";
		param = coinParam.get(coin);
		
		int length = data.length();
		JSONObject coinObj = null;
		for (int i = 0; i < length; i++) {
			if (data.getJSONObject(i).getString("symbol").equals(param)) {
				coinObj = data.getJSONObject(i);
				break;
			} 
		}
		return coinObj;
	}

	@Scheduled(cron = "0 0/1 * * * *")
	public void updateData() throws ServerErrorException {
		String url = "https://api.coinmarketcap.com/v1/ticker/?limit=300";
		HttpClient httpClient = new HttpClient();
		String response;
		try {
			response = httpClient.get(url);
			data = new JSONArray(response);
		} catch (Exception e) {
			throw new ServerErrorException("코인마켓캡 서버 에러 : " + e.getMessage());
		}
	}
}
