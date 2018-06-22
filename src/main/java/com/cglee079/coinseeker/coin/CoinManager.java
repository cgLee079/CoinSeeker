package com.cglee079.coinseeker.coin;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.cglee079.coinseeker.constants.ID;
import com.cglee079.coinseeker.exception.ServerErrorException;
import com.cglee079.coinseeker.log.Log;
import com.cglee079.coinseeker.service.CoinMarketConfigService;

public class CoinManager {
	@Autowired
	private CoinMarketConfigService coinMarketConfigService;
	
	@Autowired
	private CoinonePooler coinonePooler;

	@Autowired
	private BithumbPooler bithumbPooler;
	
	@Autowired
	private UpbitPooler upbitPooler;
	
	@Autowired
	private CoinnestPooler coinnestPooler;
	
	@Autowired
	private KorbitPooler korbitPooler;
	
	@Autowired
	private GopaxPooler gopaxPooler;
	
	@Autowired
	private BitfinexPooler bitfinexPooler;

	@Autowired
	private BittrexPooler bittrexPooler;
	
	@Autowired
	private PoloniexPooler poloniexPooler;
	
	@Autowired
	private BinancePooler binancePooler;
	
	@Autowired
	private HuobiPooler huobiPooler;
	
	@Autowired
	private HadaxPooler hadaxPooler;
	
	@Autowired
	private OkexPooler okexPooler;

	@Autowired
	private CoinMarketCapPooler coinMarketCapPooler;
	
	@Autowired
	private ExchangePooler exchangePooler;
	

	@PostConstruct
	public void init() {
		coinonePooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_COINONE));
		bithumbPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_BITHUMB));
		upbitPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_UPBIT));
		coinnestPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_COINNEST));
		korbitPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_KORBIT));
		gopaxPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_GOPAX));
		bitfinexPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_BITFINEX));
		bittrexPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_BITTREX));
		poloniexPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_POLONIEX));
		binancePooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_BINANCE));
		huobiPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_HUOBI));
		hadaxPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_HADAX));
		okexPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_OKEX));
		coinMarketCapPooler.setCoinParam(coinMarketConfigService.getMarketParams(ID.MARKET_COINMARKET));
		updateExchangeRate();
	}
	
	private double exchangeRate = 1110;

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	///
	@Scheduled(cron = "00 01 00 * * *")
	private void updateExchangeRate() {
		try {
			this.exchangeRate = exchangePooler.usd2krw();
		} catch (ServerErrorException e) {
			Log.i(e.log());
			e.printStackTrace();
		}
	}

	public JSONObject getCoin(String coin, String market) throws ServerErrorException{
		JSONObject coinObj = null;
		
		switch(market) {
		case ID.MARKET_COINONE 		: coinObj = coinonePooler.getCoin(coin); break;
		case ID.MARKET_BITHUMB 		: coinObj = bithumbPooler.getCoin(coin); break;
		case ID.MARKET_UPBIT 		: coinObj = upbitPooler.getCoin(coin); break;
		case ID.MARKET_COINNEST		: coinObj = coinnestPooler.getCoin(coin); break;
		case ID.MARKET_KORBIT 		: coinObj = korbitPooler.getCoin(coin); break;
		case ID.MARKET_GOPAX 		: coinObj = gopaxPooler.getCoin(coin); break;
		case ID.MARKET_BITFINEX 	: coinObj = bitfinexPooler.getCoin(coin); break;
		case ID.MARKET_BITTREX 		: coinObj = bittrexPooler.getCoin(coin); break;
		case ID.MARKET_POLONIEX 	: coinObj = poloniexPooler.getCoin(coin); break;
		case ID.MARKET_BINANCE 		: coinObj = binancePooler.getCoin(coin); break;
		case ID.MARKET_HUOBI 		: coinObj = huobiPooler.getCoin(coin); break;
		case ID.MARKET_HADAX 		: 
			if(coin.equals(ID.COIN_BTC)) { coinObj = huobiPooler.getCoin(coin); } 
			else { coinObj = hadaxPooler.getCoin(coin); }
			break;
		case ID.MARKET_OKEX 		: coinObj = okexPooler.getCoin(coin); break;
		}
		
		return coinObj;
	}
	
	public JSONObject getCoinWithMarketCap(String coin, String market) throws ServerErrorException{
		JSONObject coinObj = null;
		
		coinObj = this.getCoin(coin, market);
		
		JSONObject coinMarketCapObj;
		
		coinMarketCapObj = coinMarketCapPooler.getCoin(coin);
		if(coinMarketCapObj != null) {
			coinObj.put("rank", coinMarketCapObj.getInt("rank"));
			coinObj.put("marketCap", coinMarketCapObj.getDouble("market_cap_usd"));
			coinObj.put("totalVolume", coinMarketCapObj.getDouble("24h_volume_usd"));
		} else {
			coinObj.put("rank", -1);
			coinObj.put("marketCap", -1);
			coinObj.put("totalVolume", -1);
		}
		
		
		return coinObj;
	}
	
	public Double getCoinLast(String coin, String market, boolean isInBtc) {
		try {
			double last = -1;
			JSONObject coinObj = null;
			coinObj = this.getCoin(coin, market);
			
			last = coinObj.getDouble("last");
			if(isInBtc) {
				last = this.getMoney(coinObj, market).getDouble("last");
			}
			
			return last;
		} catch (Exception e) {
			Log.i(e.getMessage());
			e.printStackTrace();
			return -1.0;
		}
	}
	
	public JSONObject getMoney(JSONObject coinObj, String market){
		JSONObject btcObj;
		try {
			btcObj = this.getCoin(ID.COIN_BTC, market);
		} catch (ServerErrorException e) {
			Log.i(e.log());
			e.printStackTrace();
			return null;
		}
		
		double last = coinObj.getDouble("last") * btcObj.getDouble("last");
		double first = coinObj.getDouble("first") * btcObj.getDouble("last");
		double high = coinObj.getDouble("high") * btcObj.getDouble("last");
		double low = coinObj.getDouble("low") * btcObj.getDouble("last");
		
		JSONObject coinKRW = new JSONObject();
		coinKRW.put("last", last);
		coinKRW.put("first", first);
		coinKRW.put("high", high);
		coinKRW.put("low", low);
		coinKRW.put("volume", coinObj.get("volume"));
		
		return coinKRW;
	}
}

