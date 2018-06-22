package com.cglee079.coinseeker.cmd;

import com.cglee079.coinseeker.constants.ID;

public class CMDER {
	public static String getMainHelp(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.MAIN_HELP ; break;
		case ID.LANG_US : str = CMD_US.MAIN_HELP ; break;
		}
		return str;
	}

	public static String getMainSetMarket(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.MAIN_SET_MARKET ; break;
		case ID.LANG_US : str = CMD_US.MAIN_SET_MARKET ; break;
		}
		return str;
	}
	
	public static String getMainSupport(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.MAIN_SUPPORT ; break;
		case ID.LANG_US : str = CMD_US.MAIN_SUPPORT ; break;
		}
		return str;
	}

	public static String getMainSendMsg(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.MAIN_SEND_MSG ; break;
		case ID.LANG_US : str = CMD_US.MAIN_SEND_MSG ; break;
		}
		return str;
	}

	public static String getMainPref(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.MAIN_PREFERENCE ; break;
		case ID.LANG_US : str = CMD_US.MAIN_PREFERENCE ; break;
		}
		return str;
	}
	
	public static String getMainCoinList(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.MAIN_COIN_LIST ; break;
		case ID.LANG_US : str = CMD_US.MAIN_COIN_LIST ; break;
		}
		return str;
	}
	
	public static String getSetMarket(String market, String lang) {
		String str = "";
		switch(market) {
		case ID.MARKET_COINONE 	: str = getSetMarketCoinone(lang);break;
		case ID.MARKET_BITHUMB 	: str = getSetMarketBithumb(lang);break;
		case ID.MARKET_UPBIT 	: str = getSetMarketUpbit(lang);break;
		case ID.MARKET_COINNEST : str = getSetMarketCoinnest(lang);break;
		case ID.MARKET_KORBIT 	: str = getSetMarketKorbit(lang);break;
		case ID.MARKET_GOPAX 	: str = getSetMarketGopax(lang);break;
		case ID.MARKET_BITFINEX : str = getSetMarketBitfinex(lang);break;
		case ID.MARKET_BITTREX 	: str = getSetMarketBittrex(lang);break;
		case ID.MARKET_POLONIEX : str = getSetMarketPoloniex(lang);break;
		case ID.MARKET_BINANCE 	: str = getSetMarketBinance(lang);break;
		case ID.MARKET_HUOBI 	: str = getSetMarketHuobi(lang);break;
		case ID.MARKET_HADAX 	: str = getSetMarketHadax(lang);break;
		case ID.MARKET_OKEX 	: str = getSetMarketOkex(lang);break;
		}
		return str;
	}
	
	public static String getSetMarketCoinone(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_COINONE; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_COINONE; break;
		}
		return str;
	}

	public static String getSetMarketBithumb(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_BITHUMB ; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_BITHUMB ; break;
		}
		return str;
	}

	public static String getSetMarketUpbit(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_UPBIT ; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_UPBIT ; break;
		}
		return str;
	}

	public static String getSetMarketCoinnest(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_COINNEST; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_COINNEST; break;
		}
		return str;
	}

	public static String getSetMarketKorbit(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_KORBIT; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_KORBIT; break;
		}
		return str;
	}
	
	public static String getSetMarketGopax(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_GOPAX; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_GOPAX; break;
		}
		return str;
	}
	
	public static String getSetMarketBitfinex(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_BITFINEX; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_BITFINEX; break;
		}
		return str;
	}
	
	public static String getSetMarketBittrex(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_BITTREX; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_BITTREX; break;
		}
		return str;
	}
	
	public static String getSetMarketPoloniex(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_POLONIEX; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_POLONIEX; break;
		}
		return str;
	}

	public static String getSetMarketBinance(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_BINANCE; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_BINANCE; break;
		}
		return str;
	}
	
	public static String getSetMarketHuobi(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_HUOBI; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_HUOBI; break;
		}
		return str;
	}
	
	public static String getSetMarketHadax(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_HADAX; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_HADAX; break;
		}
		return str;
	}
	
	public static String getSetMarketOkex(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_OKEX; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_OKEX; break;
		}
		return str;
	}
	
	public static String getSetMarketOut(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_MARKET_OUT; break;
		case ID.LANG_US : str = CMD_US.SET_MARKET_OUT; break;
		}
		return str;
	}

	public static String getSendMsgOut(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SEND_MSG_OUT; break;
		case ID.LANG_US : str = CMD_US.SEND_MSG_OUT ; break;
		}
		return str;
	}

	public static String getOut(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.OUT; break;
		case ID.LANG_US : str = CMD_US.OUT; break;
		}
		return str;
	}
	
	
	public static String getPrefLang(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.PREF_LANG ; break;
		case ID.LANG_US : str = CMD_US.PREF_LANG; break;
		}
		return str;
	}

	public static String getPrefTimeAjdust(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.PREF_TIME_ADJUST; break;
		case ID.LANG_US : str = CMD_US.PREF_TIME_ADJUST; break;
		}
		return str;
	}
	
	public static String getPrefOut(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.PREF_OUT; break;
		case ID.LANG_US : str = CMD_US.PREF_OUT; break;
		}
		return str;
	}
	
	public static String getSetLanguageKR(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_LANG_KOREAN; break;
		case ID.LANG_US : str = CMD_US.SET_LANG_KOREAN; break;
		}
		return str;
	}

	public static String getSetLanguageUS(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_LANG_ENGLISH; break;
		case ID.LANG_US : str = CMD_US.SET_LANG_ENGLISH; break;
		}
		return str;
	}

	public static String getSetLanguageOut(String lang) {
		String str = "";
		switch(lang){
		case ID.LANG_KR : str = CMD_KR.SET_LANG_OUT; break;
		case ID.LANG_US : str = CMD_US.SET_LANG_OUT; break;
		}
		return str;
	}
}
