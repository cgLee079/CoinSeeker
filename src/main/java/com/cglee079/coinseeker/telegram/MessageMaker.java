package com.cglee079.coinseeker.telegram;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.cglee079.coinseeker.cmd.CMDER;
import com.cglee079.coinseeker.constants.ID;
import com.cglee079.coinseeker.model.ClientVo;
import com.cglee079.coinseeker.model.CoinInfoVo;
import com.cglee079.coinseeker.model.CoinMsgConfigVo;
import com.cglee079.coinseeker.model.CoinWalletVo;
import com.cglee079.coinseeker.service.CoinMsgConfigService;
import com.cglee079.coinseeker.util.TimeStamper;

public class MessageMaker {
	@Autowired
	private CoinMsgConfigService coinMsgConfigService;
	
	/*******************/
	/*** Formatter *****/
	/*******************/
	
	private String toExchangeRateKRWStr(double i) {
		DecimalFormat df = new DecimalFormat("#,###"); 
		df.setMinimumFractionDigits(0);
		df.setMaximumFractionDigits(0);
		df.setPositiveSuffix("원");
		df.setNegativeSuffix("원");
		return df.format(i);
	}
	
	private String toBTCStr(double i, String coinId) {
		CoinMsgConfigVo msgConfig = coinMsgConfigService.get(coinId);
		DecimalFormat df = new DecimalFormat("#.#");
		df.setMinimumFractionDigits(msgConfig.getDigitBTC());
		df.setMaximumFractionDigits(msgConfig.getDigitBTC());
		df.setPositiveSuffix(" BTC");
		df.setNegativeSuffix(" BTC");
		return df.format(i);
	}
	
	public String toMarketCapitalizeStr(double i, String market) {
		DecimalFormat df = new DecimalFormat("#,###.#");
		
		if(market.startsWith(ID.MARKET_KR)) {
			df.setMinimumFractionDigits(0);
			df.setMaximumFractionDigits(0);
			if(i >= 0 && i < 1) {
				df.setMinimumFractionDigits(3);
				df.setMaximumFractionDigits(3);
			}
			df.setPositiveSuffix("억원");
			df.setNegativeSuffix("억원");
			return df.format(i);
		}
		if(market.startsWith(ID.MARKET_US)) { 
			df.setMinimumFractionDigits(0);
			df.setMaximumFractionDigits(0);
			df.setPositivePrefix("$");
			df.setNegativePrefix("$");
		}
		
		return df.format(i);
	}
	
	private String toMoneyStr(double i, String market, String coinId){
		String result = "";
		if(market.startsWith(ID.MARKET_KR)) { result = toKRWStr(i, coinId);}
		if(market.startsWith(ID.MARKET_US)) { result = toUSDStr(i, coinId);}
		return result;
	}
	
	private String toKRWStr(double i, String coinId){
		CoinMsgConfigVo msgConfig = coinMsgConfigService.get(coinId);
		DecimalFormat df = new DecimalFormat("#,###.#"); 
		df.setMinimumFractionDigits(msgConfig.getDigitKRW());
		df.setMaximumFractionDigits(msgConfig.getDigitKRW());
		df.setPositiveSuffix("원");
		df.setNegativeSuffix("원");
		return df.format(i);
	}
	
	private String toUSDStr(double d, String coinId){
		CoinMsgConfigVo msgConfig = coinMsgConfigService.get(coinId);
		DecimalFormat df = new DecimalFormat("#.#");
		df.setMinimumFractionDigits(msgConfig.getDigitUSD());
		df.setMaximumFractionDigits(msgConfig.getDigitUSD());
		df.setPositivePrefix("$");
		return df.format(d);
	}
	
	private String toVolumeStr(long i) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(i);
	}
	
	public String toSignKimpStr(double d){
		DecimalFormat df = new DecimalFormat("#.##");
		df.setPositivePrefix("+");
		df.setNegativePrefix("-");
		return df.format(d);
	}
	
	public String toSignPercent(double c, double b){
		String prefix = "";
		double gap = c - b;
		double percent = (gap / b) * 100;
		if (percent > 0) {
			prefix = "+";
		}
		DecimalFormat df = new DecimalFormat("#.##");
		return prefix + df.format(percent) + "%";
	}

	private String toMarketStr(String marketID, String lang) {
		String market = "";
		if(lang.equals(ID.LANG_KR)) {
			switch(marketID) {
			case ID.MARKET_COINONE 		: market = "코인원"; break;
			case ID.MARKET_BITHUMB 		: market = "빗썸"; break;
			case ID.MARKET_UPBIT 		: market = "업비트"; break;
			case ID.MARKET_COINNEST 	: market = "코인네스트"; break;
			case ID.MARKET_KORBIT 		: market = "코빗"; break;
			case ID.MARKET_GOPAX 		: market = "고팍스"; break;
			case ID.MARKET_BITFINEX 	: market = "비트파이넥스"; break;
			case ID.MARKET_BITTREX 		: market = "비트렉스"; break;
			case ID.MARKET_POLONIEX 	: market = "폴로닉스"; break;
			case ID.MARKET_BINANCE 		: market = "바이낸스"; break;
			case ID.MARKET_HUOBI 		: market = "후오비"; break;
			case ID.MARKET_HADAX 		: market = "하닥스"; break;
			case ID.MARKET_OKEX 		: market = "오케이엑스"; break;
			}
		} else if(lang.equals(ID.LANG_US)) {
			switch(marketID) {
			case ID.MARKET_COINONE 		: market = "Coinone"; break;
			case ID.MARKET_BITHUMB 		: market = "Bithumb"; break;
			case ID.MARKET_UPBIT 		: market = "Upbit"; break;
			case ID.MARKET_COINNEST 	: market = "Coinnest"; break;
			case ID.MARKET_KORBIT 		: market = "Korbit"; break;
			case ID.MARKET_GOPAX 		: market = "Gopax"; break;
			case ID.MARKET_BITFINEX 	: market = "Bitfinex"; break;
			case ID.MARKET_BITTREX 		: market = "Bittrex"; break;
			case ID.MARKET_POLONIEX 	: market = "Poloniex"; break;
			case ID.MARKET_BINANCE 		: market = "Binance"; break;
			case ID.MARKET_HUOBI 		: market = "Huobi"; break;
			case ID.MARKET_HADAX 		: market = "Hadax"; break;
			case ID.MARKET_OKEX 		: market = "OKEx"; break;
			}
		}
		return market;
	}
	
	/********************/
	/** Common Message **/
	/********************/
	public String warningNeedToStart(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR : msg = "알림을 먼저 시작해주세요. \n 명령어 /start  <<< 클릭!.\n"; break;
		case ID.LANG_US : msg = "Please start this service first.\n /start <<< click here.\n"; break; 
		}
		return msg;
	}
	
	public String warningWaitSecond(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR : msg = "잠시 후 다시 보내주세요.\n"; break;
		case ID.LANG_US : msg = "Please send message again after a while.\n"; break; 
		}
		return msg;
	}
	
	
	public String msgToMain(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR : msg = "\n# 메인화면으로 이동합니다.\n"; break;
		case ID.LANG_US : msg = "\n# Changed to main menu.\n"; break; 
		}
		return msg;
	}
	
	
	/*******************/
	/** Start Message **/
	/*******************/
	public String msgStartService(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR : msg = "CoinSeeker가 시작되었습니다.\n\n"; break;
		case ID.LANG_US : msg = "CoinSeeker Start.\n\n"; break; 
		}
		return msg;
	}

	/**********************************/
	/** Each Market Price Message *****/
	/**********************************/
	public String msgMyMarket(String market, String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR : msg = "설정거래소 : " + toMarketStr(market, lang) +  "\n"; break;
		case ID.LANG_US : msg = "Setting Market : " + toMarketStr(market, lang) +  "\n"; break;
		}
		return msg;
	}
	
	public String msgCurrentTime(String date, String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR : msg += "시간 : " + date + "\n"; break;
		case ID.LANG_US : msg += "Current Time : " + date + "\n"; break; 
		}
		
		return msg;
	}
	
	public String msgExchangeRate(double exchangeRate, String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR : msg += "환율 : $1 = " + toExchangeRateKRWStr(exchangeRate) + "\n"; break;
		case ID.LANG_US : msg += "ExchangeRate : $1 = " + toExchangeRateKRWStr(exchangeRate) + "\n"; break; 
		}
		return msg;
	}
	
	public String msgCurrentPrice(String coinId, double currentValue, JSONObject coinMoney, boolean inBtc, ClientVo client) {
		String msg = "";
		String lang 	= client.getLang();
		String market 	= client.getMarket();
		String date		= TimeStamper.getDateTime(client.getLocalTime());
		
		switch(lang) {
		case ID.LANG_KR :
			if(inBtc) {
				double currentMoney = coinMoney.getDouble("last");
				double currentBTC = currentValue;
				msg += "현재가격 : " + toMoneyStr(currentMoney, market, coinId) + " [" + toBTCStr(currentBTC, coinId) + "]\n";
			} else {
				msg += "현재가격 : " + toMoneyStr(currentValue, market, coinId) + "\n";
			}
			break;
			
		case ID.LANG_US :
			if(inBtc) {
				double currentMoney = coinMoney.getDouble("last");
				double currentBTC = currentValue;
				msg += "Current Price : " + toMoneyStr(currentMoney, market, coinId) + " [" + toBTCStr(currentBTC, coinId) + "]\n";
			} else {
				msg += "Current Price : " + toMoneyStr(currentValue, market, coinId) + "\n";
			}
			break; 
		}
		return msg;
	}
	
	public String msgEachMarketPrice(String coinId, double exchangeRate, LinkedHashMap<String, Double> lasts, ClientVo client) {
		String msg 		= "";
		String market 	= client.getMarket();
		String lang 	= client.getLang();
		double mylast 	= lasts.get(market);

		Iterator<String> iter = lasts.keySet().iterator();
		
		if(market.startsWith(ID.MARKET_KR)) {
			while(iter.hasNext()) {
				String key = iter.next();
				if(key.equals(market)) {
					msg += "★ ";
					msg += toMarketStr(market, lang) + "  : ";
					if(lasts.get(key) == -1) {
						msg += "Server Error";
					} else { 
						msg += toMoneyStr(lasts.get(market), market, coinId);
						msg += "  [" + toMoneyStr(lasts.get(key)/ exchangeRate, ID.MARKET_US, coinId) + "]";
					}
					msg += "\n";
				} else {
					msg += toMarketStr(key, lang) + "  : ";
					if(lasts.get(key) == -1) {
						msg += "Server Error";
					} else { 
						if(key.startsWith(ID.MARKET_KR)) {
							msg += toMoneyStr(lasts.get(key), ID.MARKET_KR, coinId);
							msg += "  [" + toMoneyStr(lasts.get(key)/ exchangeRate, ID.MARKET_US, coinId) + "]";
						} else if(key.startsWith(ID.MARKET_US)){
							msg += toMoneyStr(lasts.get(key) * exchangeRate, ID.MARKET_KR, coinId);
							msg += "  [" + toMoneyStr(lasts.get(key), ID.MARKET_US, coinId) + "]";
							msg += " ( P. " + toSignPercent(mylast,  lasts.get(key) * exchangeRate ) + ") ";
						}
					}
					msg += "\n";
				}
			}
		}
		
		else if(market.startsWith(ID.MARKET_US)) {
			while(iter.hasNext()) {
				String key = iter.next();
				if(key.equals(market)) {
					msg += "★ ";
					msg += toMarketStr(market, lang) + "  : ";
					if(lasts.get(key) == -1) {
						msg += "Server Error";
					} else { 
						msg += toMoneyStr(lasts.get(market), market, coinId);
						msg += "  [" + toMoneyStr(lasts.get(key) * exchangeRate, ID.MARKET_KR, coinId) + "]";
					}
					msg += "\n";
					
				} else {
				msg += toMarketStr(key, lang) + "  : ";
					if(lasts.get(key) == -1) {
						msg += "Server Error";
					} else { 
						if(key.startsWith(ID.MARKET_KR)) {
							msg += toMoneyStr(lasts.get(key) / exchangeRate, ID.MARKET_US, coinId);
							msg += "  [" + toMoneyStr(lasts.get(key), ID.MARKET_KR, coinId) + "]";
						} else if(key.startsWith(ID.MARKET_US)) {
							msg += toMoneyStr(lasts.get(key), ID.MARKET_US, coinId);
							msg += "  [" + toMoneyStr(lasts.get(key) * exchangeRate, ID.MARKET_KR, coinId) + "]";
						}
					}
					msg += "\n";
				}
			}
		}
		
		msg += "\n";
		return msg;
	}
	
	public String msgBTCReplaceAnotherMarket(String marketID, String lang) {
		String market = this.toMarketStr(marketID, lang);
		String msg = "";
		switch(lang) {
		case ID.LANG_KR : msg = "* " + market + " 기준 정보로 대체합니다 .\n"; break;
		case ID.LANG_US : msg = "* Replace with " + market  + " market information.\n"; break; 
		}
		return msg;
	}
	
	public String msg24HourChange(String coinId, JSONObject coinObj, String market, String lang) {
		String msg = "";
		
		double coinCV = coinObj.getDouble("last");
		double coinBV = coinObj.getDouble("first");
		long volume = (long)coinObj.getDouble("volume");
		double volumePrice = 0;
		
		if(market.startsWith(ID.MARKET_KR)) { volumePrice = (double)volume * coinCV / 100000000; }
		if(market.startsWith(ID.MARKET_US)) { volumePrice = (double)volume * coinCV; }
		
		switch(lang) {
		case ID.LANG_KR :
			msg += "24시간 변화량 : " + toSignPercent(coinCV, coinBV) + "\n";
			msg += "24시간 거래량 : " + toVolumeStr(volume) + "\n";
			msg += "24시간 거래금 : " + toMarketCapitalizeStr(volumePrice, market) + "\n";
			break;
		case ID.LANG_US : 
			msg += "24 hour rate of change : " + toSignPercent(coinCV, coinBV) + "\n";
			msg += "24 hour volume : " + toVolumeStr(volume) + "\n";
			msg += "24 hour Transaction amount: " + toMarketCapitalizeStr(volumePrice, market) + "\n";
			break; 
		}
		msg += "\n";
		return msg;
	}
	
	public String msgMarketCap(JSONObject coinObj, double exchangeRate, String market, String lang) {
		String msg  		= "";
		int rank 			= coinObj.getInt("rank");
		double marketCap 	= coinObj.getDouble("marketCap");
		double totalVolume 	= coinObj.getDouble("totalVolume");
		
		
		if(market.startsWith(ID.MARKET_KR)) {
			marketCap = marketCap * exchangeRate / 100000000;
			totalVolume = totalVolume * exchangeRate / 100000000;
		} 
		
		switch(lang) {
		case ID.LANG_KR :
			if(rank == -1 || marketCap == -1 || totalVolume == -1) {
				msg += "시가 순위 : 정보 없음\n";
				msg += "시가 총액 : 정보 없음\n";
				msg += "모든 거래소 거래금 : 정보 없음\n";
			} else {
				msg += "시가 순위 : " + rank + " 위\n";
				msg += "시가 총액 : " + toMarketCapitalizeStr(marketCap, market) + "\n";
				msg += "모든 거래소 거래금 : " + toMarketCapitalizeStr(totalVolume, market) + "\n";
			}
			break;
		case ID.LANG_US : 
			if(rank == -1 || marketCap == -1 || totalVolume == -1) {
				msg += "Rank : none\n";
				msg += "Market Cap : none\n";
				msg += "All Market Volume : none\n";
			} else {
				msg += "Rank : " + rank + "\n";
				msg += "Market Cap : " + toMarketCapitalizeStr(marketCap, market) + "\n";
				msg += "All Market Volume : " + toMarketCapitalizeStr(totalVolume, market) + "\n";
			}
			break;
		}
		
		
		msg += "\n";
		
		return msg;
	}
	
	
	

	/************************/
	/** Set Market Message **/
	/************************/
	public String explainMarketSet(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR :
			msg += "거래중인 거래소를 설정해주세요.\n";
			msg += "모든 정보는 설정 거래소 기준으로 전송됩니다.\n";
			break;
		case ID.LANG_US : 
			msg += "Please select an market.\n";
			msg += "All information will be sent on the market you selected.\n";
			break;
		}
		return msg;
	}

	public String msgMarketSet(String marketID, String lang) {
		String msg = "";
		String market = this.toMarketStr(marketID, lang);
		
		switch(lang) {
		case ID.LANG_KR : msg = "거래소가 " + market + "(으)로 설정되었습니다.\n"; break;
		case ID.LANG_US : msg = "The exchange has been set up as " + market + ".\n"; break; 
		}
		return msg;
	}

	public String msgMarketNoSet(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR : msg = "거래소가 설정되지 않았습니다.\n"; break;
		case ID.LANG_US : msg = "The market has not been set up.\n"; break; 
		}
		return msg;
	}
	
	/***********************/
	/** Explain Coin List **/
	/***********************/
	public String explainCoinList(List<CoinInfoVo> coinInfos, String lang) {
		String msg = "";
		CoinInfoVo coinInfo = null;
		int coinInfosLen = coinInfos.size();
		
		switch(lang) {
		case ID.LANG_KR:
			msg += "링크를 클릭 하시면,\n";
			msg += "해당 코인알리미 봇으로 이동합니다.\n";
			
			msg += "-----------------------\n";
			for (int i = 0; i < coinInfosLen; i++) {
				coinInfo = coinInfos.get(i);
				msg += coinInfo.getCoinId() + " [" + coinInfo.getKrName() + "] \n";
				msg += coinInfo.getChatAddr() + "\n";
				msg += "\n";
			}
			msg += "\n";
			break;
		case ID.LANG_US:
			msg += "Click on the link to go to other Coin Noticer.\n";
			msg += "-----------------------\n";
			for (int i = 0; i < coinInfosLen; i++) {
				coinInfo = coinInfos.get(i);
				msg += coinInfo.getCoinId() + " [" + coinInfo.getUsName() + "] \n";
				msg += coinInfo.getChatAddr() + "\n";
				msg += "\n";
			}
			msg += "\n";
			break;
		}
	
		
		return msg;
	}
	
	/***********/
	/** Help  **/
	/***********/
	public String explainHelp(String lang) {
		String msg = "";
		if (lang.equals(ID.LANG_KR)) {
		} else if(lang.equals(ID.LANG_US)) {
		}
		return msg;
	}
	
	public String explainSetForeginer(String lang) {
		String msg = "";
		msg += "★  If you are not Korean, Must read!!\n";
		msg += "* Use the " + CMDER.getMainPref(ID.LANG_US) + " Menu.\n";
		msg += "* First. Please set language to English.\n";
		msg += "* Second. Set the time adjustment for accurate notifications. Because of time difference by country.\n";
//		msg += "* Last. if you set market in USA using '" +CMDER.getMainSetMarket(ID.LANG_US) + "' menu, the currency unit is changed to USD.\n";
		return msg;
	}
	
	/*******************************/
	/** Send message to developer **/
	/*******************************/
	public String explainSendSuggest(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR :
			msg += "개발자에게 내용이 전송되어집니다.\n";
			msg += "내용을 입력해주세요.\n";
			msg += "\n";
			msg += "\n";
			msg += "# 메인으로 돌아가시려면 " + CMDER.getSendMsgOut(lang) + " 를 입력해주세요.\n";
			break;
		case ID.LANG_US : 
			msg += "Please enter message.\n";
			msg += "A message is sent to the developer.\n";
			msg += "\n";
			msg += "\n";
			msg += "# To return to main, enter " + CMDER.getSendMsgOut(lang) + "\n";
			break;
		}
		return msg;
	}
	
	public String msgThankyouSuggest(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR :
			msg += "의견 감사드립니다.\n";
			msg += "성투하세요^^!\n";
			break;
		case ID.LANG_US : 
			msg += "Thank you for your suggest.\n";
			msg += "You will succeed in your investment :)!\n";
			break;
		}
		return msg;
	}

	
	/***************************/
	/*** Sponsoring Message ****/
	/***************************/
	public String explainSupport(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR :
			msg += "안녕하세요. 개발자 CGLEE 입니다.\n";
			msg += "본 서비스는 무료 서비스 임을 다시 한번 알려드리며,\n";
			msg += "절대로! 후원하지 않는다하여 사용자 여러분에게 불이익을 제공하지 않습니다.^^\n";
			msg += "\n";
			msg += "후원된 금액은 다음 용도로 소중히 사용하겠습니다\n";
			msg += "\n";
			msg += "1 순위. 서버 업그레이드 (타 코인 알리미 추가)\n";
			msg += "2 순위. 서버 운영비 (전기세...^^)\n";
			msg += "3 순위. 취업자금\n";
			msg += "4 순위. 개발보상 (치킨 냠냠)\n";
			msg += "\n";
			msg += "감사합니다.\n";
			msg += "하단에 정보를 참고하여주세요^^\n";
			break;
			
		case ID.LANG_US : 
			msg += "Hi. I'm developer CGLEE\n";
			msg += "Never! I don't offer disadvantages to users by not sponsoring. :D\n";
			msg += "\n";
			msg += "Thank you for sponsoring.\n";
			msg += "See the information below.\n";
			break; 
		}
		return msg;
	}
	
	public String explainSupportWallet(CoinWalletVo xrpWallet, String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR :
			msg += "* " + xrpWallet.getCoinId() + " [ " + xrpWallet.getKrName() + " ]  지갑주소 : \n";
			msg += xrpWallet.getAddr1() + "\n";
			msg += "데스티네이션 태그 :  " + xrpWallet.getAddr2() + "\n";
			break;
			
		case ID.LANG_US : 
			msg += "* " + xrpWallet.getCoinId() + " [ " + xrpWallet.getUsName() + " ]  Wallet address : \n";
			msg += xrpWallet.getAddr1() + "\n";
			msg += "destination tag :  " + xrpWallet.getAddr2() + "\n";
			break; 
		}
		
		return msg;
	}

	public String explainSupportAN(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR :
			msg += "* 후원계좌\n";
			msg += "예금주: 이찬구\n";
			msg += "은행   : 신한은행 \n";
			msg += "번호   : 110 409 338434";
			break;
			
		case ID.LANG_US : 
			msg += "* Sponsored account\n";
			msg += "Bank   : Shinhan Bank (in Korea)\n";
			msg += "Account Holder: Lee Changoo(이찬구)\n";
			msg += "Account Number: 110 409 338434";
			break; 
		}
		
		return msg;
	}

	/*********************/
	/*** Language Set  ***/
	/*********************/
	
	public String explainSetLanguage(String lang) {
		String msg = "";
		msg += "Please select a language.";
		return msg;
	}
	
	public String msgSetLanguageSuccess(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR : msg += "언어가 한국어로 변경되었습니다.\n"; break;
		case ID.LANG_US : msg += "Language changed to English.\n"; break; 
		}
		return msg;
	}

	
	/*********************/
	/** Time Adjust  *****/
	/*********************/
	public String explainTimeAdjust(String lang) {
		String msg = "";
		msg += "한국분이시라면 별도의 시차조절을 필요로하지 않습니다.^^  <- for korean\n";
		msg += "\n";
		msg += "Please enter the current time for accurate time notification.\n";
		msg += "because the time differs for each country.\n";
		msg += "\n";
		msg += "* Please enter in the following format.\n";
		msg += "* if you entered 0, time adjust initialized.\n";
		msg += "* example) 0 : init time adjust\n";
		msg += "* example) " + TimeStamper.getDateBefore() + " 23:00 \n";
		msg += "* example) " + TimeStamper.getDate() + " 00:33 \n";
		msg += "* example) " + TimeStamper.getDate() +  " 14:30 \n";
		msg += "\n";
		msg += "\n";
		msg += "# To return to main, enter a character.\n";
		return msg;
	}
	
	public String warningTimeAdjustFormat(String lang) {
		String msg = "";
		msg += "Please type according to the format.\n";
		return msg;
	}
	
	public String msgTimeAdjustSuccess(Date date) {
		String msg = "";
		msg += "Time adjustment succeeded.\n";
		msg += "Current Time : " + TimeStamper.getDateTime(date) + "\n";
		return msg;
	}

	public String explainNotCommand(String lang) {
		String msg = "";
		switch(lang) {
		case ID.LANG_KR : msg += "잘못된 명령어 입니다.\n"; break;
		case ID.LANG_US : msg += "This is an invalid command.\n"; break; 
		}
		return msg;
	}


//	public String msgToPreference() {
//		String msg = "";
//		msg += "\n# Changed to Preference menu\n";
//		return msg;
//	}
	
}