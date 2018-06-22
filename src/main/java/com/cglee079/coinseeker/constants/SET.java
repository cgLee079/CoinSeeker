package com.cglee079.coinseeker.constants;

import org.springframework.stereotype.Component;

@Component
public class SET {
	public final static Integer CLNT_MAX_ERRCNT = 5;
	
	public final static String[] SUPPORT_MARKETS = { 
			ID.MARKET_COINONE,
			ID.MARKET_BITHUMB,
			ID.MARKET_UPBIT,
			ID.MARKET_COINNEST,
			ID.MARKET_KORBIT,
			ID.MARKET_GOPAX,
			ID.MARKET_BITFINEX,
			ID.MARKET_BITTREX,
			ID.MARKET_POLONIEX,
			ID.MARKET_BINANCE,
			ID.MARKET_HADAX,
			ID.MARKET_HUOBI,
			ID.MARKET_OKEX
	};

}
