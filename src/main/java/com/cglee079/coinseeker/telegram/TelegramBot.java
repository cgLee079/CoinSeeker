package com.cglee079.coinseeker.telegram;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.cglee079.coinseeker.cmd.CMDER;
import com.cglee079.coinseeker.coin.CoinManager;
import com.cglee079.coinseeker.constants.ID;
import com.cglee079.coinseeker.constants.SET;
import com.cglee079.coinseeker.exception.ServerErrorException;
import com.cglee079.coinseeker.log.Log;
import com.cglee079.coinseeker.model.ClientVo;
import com.cglee079.coinseeker.model.CoinMarketConfigVo;
import com.cglee079.coinseeker.service.ClientMsgService;
import com.cglee079.coinseeker.service.ClientService;
import com.cglee079.coinseeker.service.ClientSuggestService;
import com.cglee079.coinseeker.service.CoinInfoService;
import com.cglee079.coinseeker.service.CoinMarketConfigService;
import com.cglee079.coinseeker.service.CoinMsgConfigService;
import com.cglee079.coinseeker.service.CoinWalletService;
import com.cglee079.coinseeker.util.TimeStamper;

public class TelegramBot extends AbilityBot  {
	private String myCoin = "COINSEEKER";
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private ClientMsgService clientMsgService;
	
	@Autowired
	private ClientSuggestService clientSuggestService;
	
	@Autowired
	private CoinInfoService coinInfoService;

	@Autowired
	private CoinWalletService coinWalletService;

	@Autowired
	private CoinMarketConfigService coinMarketConfigService;
	
	@Autowired
	private CoinManager coinManager;
	
	@Autowired
	private KeyboardManager km;
	
	@Autowired
	private MessageMaker msgMaker;
	
	protected TelegramBot(String botToken, String botUsername) {
		super(botToken, botUsername);
		msgMaker = new MessageMaker();
	}
	
	@Override
	public int creatorId() {
		return 503609560;
	}

	@Override
	public void onUpdateReceived(Update update) {
		clientMsgService.insert(update);
		
		Message message = null;
		if(update.getMessage() != null) {
			message = update.getMessage();
		} else if( update.getEditedMessage() != null) {
			message = update.getEditedMessage();
		}
		
		User user 			= message.getFrom();
		String username 	= user.getLastName() + " " + user.getFirstName();
		Integer userId		= user.getId();
		Integer messageId 	= message.getMessageId();
		String cmd			= message.getText();
		
		ClientVo client = clientService.get(userId);
		clientService.updateMsgDate(client);
		
		if(message.getText().equals("/start") || client == null) {
			String lang= ID.LANG_KR;
			String msg = "";
			if (clientService.openChat(userId, username, ID.MARKET_UPBIT)) {
				client  = clientService.get(userId);
				msg += msgMaker.msgStartService(lang);
				msg += msgMaker.explainHelp(lang);
				sendMessage(userId, null, msg, km.getCoinKeyboard(ID.MARKET_UPBIT, lang));
				sendMessage(userId, null, msgMaker.explainSetForeginer(lang), km.getCoinKeyboard(ID.MARKET_UPBIT, lang));
			} 
		} 
		
		String lang 	= client.getLang();
		String state 	= client.getState();
		String market	= client.getMarket();
		
		if(client.getEnabled().equals("Y")) {
			switch(state) {
			case ID.STATE_MAIN: handleMenu(userId, messageId, cmd, lang, market); break;
			case ID.STATE_SET_MARKET : handleSetMarket(userId, messageId, cmd, lang); break;
			case ID.STATE_SEND_MSG : handleSendMsg(userId, username, messageId, cmd, lang, market); break;
			case ID.STATE_PREFERENCE	: handlePreference(userId, username, messageId, cmd, lang, market); break;
			case ID.STATE_PREF_LANGUAGE	: handleSetLanguage(userId, username, messageId, cmd, lang, market); break;
			case ID.STATE_PREF_TIMEADJUST: handleTimeAdjust(userId, username, messageId, cmd, lang, market); break;
			}
		} else {
			clientService.openChat(userId, username, market);
			sendMessage(userId, null,  msgMaker.msgStartService(lang), null);
		}
		
	
		
	}

	/* 메인 메뉴 응답 처리 */
	private void handleMenu(Integer userId, Integer messageId, String cmd, String lang, String market) {
		String state = ID.STATE_MAIN;
		
		if(cmd.equals(CMDER.getMainCoinList(lang))){ //타 코인 알리미
			sendMessage(userId, messageId, msgMaker.explainCoinList(coinInfoService.list(myCoin), lang), km.getCoinKeyboard(market, lang));
		} else if(cmd.equals(CMDER.getMainHelp(lang))) { // 도움말
			sendMessage(userId, messageId, msgMaker.explainHelp(lang), km.getCoinKeyboard(market, lang));
			sendMessage(userId, null, msgMaker.explainSetForeginer(lang), km.getCoinKeyboard(market, lang));
		} else if(cmd.equals(CMDER.getMainSupport(lang))){ // 후원하기
			sendMessage(userId, messageId, msgMaker.explainSupport(lang), null);
			sendMessage(userId, null, msgMaker.explainSupportWallet(coinWalletService.get(ID.COIN_XRP), lang), null);
			sendMessage(userId, null, msgMaker.explainSupportAN(lang), km.getCoinKeyboard(market, lang));
		} else if(cmd.equals(CMDER.getMainSetMarket(lang))){ // 거래소 설정
			sendMessage(userId, messageId, msgMaker.explainMarketSet(lang), km.getMarketKeyboard(lang));
			state = ID.STATE_SET_MARKET;
		} else if(cmd.equals(CMDER.getMainSendMsg(lang))) {// 문의/건의
			sendMessage(userId, messageId, msgMaker.explainSendSuggest(lang), km.getDefaultKeyboard());
			state = ID.STATE_SEND_MSG;
		} else if(cmd.equals(CMDER.getMainPref(lang))){ // 환경설정
			sendMessage(userId, messageId, "Set Preference", km.getPreferenceKeyboard(lang));
			state = ID.STATE_PREFERENCE;
		} else { // 코인 Symbol
			CoinMarketConfigVo config = coinMarketConfigService.get(market, cmd);
			if(config != null) {
				sendMessage(userId, messageId, messageCoin(config.getCoinId(), userId), km.getCoinKeyboard(market, lang));
			} else { //잘못된 명령어
				sendMessage(userId, messageId, msgMaker.explainNotCommand(lang), km.getCoinKeyboard(market, lang));
			}
		}
		
		clientService.updateState(userId.toString(), state);
	}
	
	/* 코인 가격 */
	public String messageCoin(String coinId, Integer userId) {
		String 	msg = "";
		
		ClientVo client 	= clientService.get(userId);
		String market 		= client.getMarket();
		String lang 		= client.getLang();
		
		CoinMarketConfigVo myConfig = coinMarketConfigService.get(market, coinId);
		double exchangeRate = coinManager.getExchangeRate();
		
		String marketID = null;
		List<CoinMarketConfigVo> configs = coinMarketConfigService.list(null, coinId);
		CoinMarketConfigVo config = null;
		
		JSONObject coinObj = null;
		JSONObject krwCoinObj = null;
		try {
			coinObj = coinManager.getCoinWithMarketCap(coinId, market);
			krwCoinObj = coinObj;
			if(myConfig.isInBtc()) {
				krwCoinObj = coinManager.getMoney(coinObj, market);
			}
		} catch (ServerErrorException e) {
			e.printStackTrace();
			return msgMaker.warningWaitSecond(lang);
		}
		
		/* 코인 정보*/
//		msg += msgMaker.msgCoinInfo(coinInfoService.get(coinId), lang);
		
		/* 현재시각 */ 
//		msg += msgMaker.msgCurrentTime(date, lang);
		
		/* 거래소 정보 */
		msg += "★ " + msgMaker.msgMyMarket(market, lang);
		/* 환율 정보 */
		msg += "★ " + msgMaker.msgExchangeRate(exchangeRate, lang);
		msg += "------------------------\n";
		msg += "\n";
		
		
		/* 현재 가격 */
		msg += msgMaker.msgCurrentPrice(coinId, coinObj.getDouble("last"), krwCoinObj, myConfig.isInBtc(), client);
		msg += "\n";
		
		/* 거래소별 현재 가격 */
		LinkedHashMap<String, Double> lasts = new LinkedHashMap<>();
		for(int i = 0; i < configs.size(); i++) {
			config  =  configs.get(i);
			marketID = config.getMarket();
			if(!marketID.equals(ID.MARKET_COINMARKET)) {
				lasts.put(marketID, coinManager.getCoinLast(coinId, config.getMarket(), config.isInBtc()));
			}
		}
		
		msg += msgMaker.msgEachMarketPrice(coinId, exchangeRate, lasts, client);
		
		
		/* 24시간 변화 */
		JSONObject coinObjFor24 = coinObj;
		String marketFor24 = market;
		if(market.startsWith(ID.MARKET_KR) && (market.equals(ID.MARKET_COINNEST) || market.equals(ID.MARKET_KORBIT))) {
			marketID = null;
			for(int i = 0; i < configs.size(); i++) {
				config  =  configs.get(i);
				marketID = config.getMarket();
				if(marketID.startsWith(ID.MARKET_KR) && !marketID.equals(ID.MARKET_COINNEST) && !marketID.equals(ID.MARKET_KORBIT)) {
					msg += msgMaker.msgBTCReplaceAnotherMarket(marketID, lang);
					marketFor24 = marketID;
					try {
						coinObjFor24 = coinManager.getCoin(coinId, marketFor24);
					} catch (ServerErrorException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		
		if(market.startsWith(ID.MARKET_US) && (market.equals(ID.MARKET_BITTREX)|| market.equals(ID.MARKET_OKEX))) {
			marketID = null;
			for(int i = 0; i < configs.size(); i++) {
				config  =  configs.get(i);
				marketID = config.getMarket();
				if(marketID.startsWith(ID.MARKET_US) && !marketID.equals(ID.MARKET_BITTREX)&& !marketID.equals(ID.MARKET_OKEX) && !marketID.equals(ID.MARKET_COINMARKET)){
					msg += msgMaker.msgBTCReplaceAnotherMarket(marketID, lang);
					marketFor24 = marketID;
					try {
						coinObjFor24 = coinManager.getCoin(coinId, marketFor24);
					} catch (ServerErrorException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		
		
		if(coinObj != null ){
			CoinMarketConfigVo configFor24 = coinMarketConfigService.get(marketFor24, coinId);
			if(configFor24.isInBtc()) {
				coinObjFor24 = coinManager.getMoney(coinObjFor24, marketFor24);
			}
			msg += msgMaker.msg24HourChange(coinId, coinObjFor24, marketFor24, lang);
		}
		
		/* 코인마켓캡 */
		msg += msgMaker.msgMarketCap(coinObj, exchangeRate, market, lang);
		
		return msg;
	}
	
	
	/* 마켓 설정 응답 처리 */
	private void handleSetMarket(Integer userId, Integer messageId, String cmd, String lang) {
		String market = null;
		String msg = "";
		msg = msgMaker.msgMarketNoSet(lang);

		String marketID;
		for(int i = 0; i < SET.SUPPORT_MARKETS.length; i++) {
			marketID = SET.SUPPORT_MARKETS[i];
			if(CMDER.getSetMarket(marketID, lang).equals(cmd)){
				market = marketID;
				msg = msgMaker.msgMarketSet(marketID, lang);
			}
		}
		
		if(market != null) {
			ClientVo client = clientService.get(userId);
			client.setMarket(market);
			clientService.updateMarket(client.getUserId(), market);
		} else {
			market = clientService.getMarket(userId);
			msg = msgMaker.msgMarketNoSet(lang);
		}
		
		msg += msgMaker.msgToMain(lang);
		
		sendMessage(userId, messageId, msg, km.getCoinKeyboard(market, lang));
		clientService.updateState(userId.toString(), ID.STATE_MAIN);
	}
	
	
	/* 개발자에게 메세지 보내기, 응답 처리 */
	private void handleSendMsg(Integer userId, String username, Integer messageId, String message, String lang, String market) {
		if(message.equals(CMDER.getSendMsgOut(lang))){
			sendMessage(userId, messageId, msgMaker.msgToMain(lang), km.getCoinKeyboard(market, lang));
		} else if(message.equals(CMDER.getOut(lang))){
			sendMessage(userId, messageId, msgMaker.msgToMain(lang), km.getCoinKeyboard(market, lang));
		} else {
			clientSuggestService.insert(myCoin, userId, username, message);
			sendMessage(userId, messageId, msgMaker.msgThankyouSuggest(lang), km.getCoinKeyboard(market, lang));
			
			//To Developer
			String msg = "";
			msg += "메세지가 도착했습니다!\n------------------\n\n";
			msg += message;
			msg += "\n\n------------------\n";
			msg += " By ";
			msg += username + " [" + userId + " ]";
			
			sendMessage(this.creatorId(), null, msg, km.getCoinKeyboard(lang, market));
		}
		
		clientService.updateState(userId.toString(), ID.STATE_MAIN);
	}
	
	
	/* 환경설정 응답 처리 */
	private void handlePreference(Integer userId, String username, Integer messageId, String cmd, String lang, String market) {
		String state = ID.STATE_MAIN;
		if(cmd.equals(CMDER.getPrefLang(lang))) {
			sendMessage(userId, messageId, msgMaker.explainSetLanguage(lang), km.getSetLanguageKeyboard(lang));
			state = ID.STATE_PREF_LANGUAGE;
		} else if(cmd.equals(CMDER.getPrefTimeAjdust(lang))) {
			sendMessage(userId, messageId, msgMaker.explainTimeAdjust(lang), km.getDefaultKeyboard(lang));
			state = ID.STATE_PREF_TIMEADJUST;
		} else if(cmd.equals(CMDER.getPrefOut(lang))){
			sendMessage(userId, messageId, msgMaker.msgToMain(lang), km.getCoinKeyboard(market, lang));
		} else {
			sendMessage(userId, messageId, msgMaker.msgToMain(lang), km.getCoinKeyboard(market, lang));
		}
		
		clientService.updateState(userId.toString(), state);
	}
	
	
	/* 환경설정 - 언어 응답 처리 */
	private void handleSetLanguage(Integer userId, String username, Integer messageId, String cmd, String lang, String market) {
		String msg = "";
		String langID = lang;
		
		if(cmd.equals(CMDER.getSetLanguageKR(lang))) {
			langID = ID.LANG_KR;
		} else if(cmd.equals(CMDER.getSetLanguageUS(lang))) {
			langID = ID.LANG_US;
		} 
		
		if(clientService.updateLanguage(userId.toString(), langID)) {
			msg += msgMaker.msgSetLanguageSuccess(langID);
			msg += msgMaker.msgToMain(langID);
		} else {
			msg += msgMaker.warningWaitSecond(lang);
			msg += msgMaker.msgToMain(lang);
		}
		
		
		sendMessage(userId, messageId, msg, km.getCoinKeyboard(market, langID));
		if(cmd.equals(CMDER.getSetLanguageKR(lang)) || cmd.equals(CMDER.getSetLanguageUS(lang))) {
			sendMessage(userId, messageId, msgMaker.explainHelp(langID), null);
		}
		
		clientService.updateState(userId.toString(), ID.STATE_MAIN);
		
	}
	
	
	/* 환경설정 시차 조절 응답 처리 */
	private void handleTimeAdjust(Integer userId, String username, Integer messageId, String cmd, String lang, String market) {
		String msg = "";
		String enteredDateStr = cmd;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		if(cmd.equals("0")) {
			clientService.updateLocalTime(userId.toString(), (long)0);
			msg += msgMaker.msgTimeAdjustSuccess(new Date());
			msg += msgMaker.msgToMain(lang);
		} else {
			try {
				Date enteredDate = format.parse(enteredDateStr);
				Date currentDate = new Date();
				long time = enteredDate.getTime() - currentDate.getTime();
				
				clientService.updateLocalTime(userId.toString(), time);
				
				msg += msgMaker.msgTimeAdjustSuccess(enteredDate);
				msg += msgMaker.msgToMain(lang);
			} catch (ParseException e) {
				msg += msgMaker.warningTimeAdjustFormat("");
				msg += msgMaker.msgToMain(lang);
			}
		}
		
		
		sendMessage(userId, messageId, msg, km.getCoinKeyboard(market, lang));
		
		clientService.updateState(userId.toString(), ID.STATE_MAIN);
	}
	
	
	/******************/
	/** Send Message **/
	/******************/
	public void sendMessage(Integer id, Integer msgId, String msg, ReplyKeyboard keyboard){
		this.sendMessage(id.toString(), msgId, msg, keyboard);
	}
	
	public void sendMessage(String userId, Integer msgId, String msg, ReplyKeyboard keyboard){
		Log.i("To Client\t:\t" + myCoin + "\t[id :" +userId + " ]  " + msg.replace("\n", "  "));
		
		SendMessage sendMessage = new SendMessage(userId, msg);
		sendMessage.setReplyToMessageId(msgId);
		
		if(keyboard != null) { 
			sendMessage.setReplyMarkup(keyboard);
		} 
		
		try {
			sender.execute(sendMessage);
		} catch (TelegramApiException e) {
			Log.i("To Client Error\t:\t" + myCoin +  "\t[id :" + userId + " ]  에게 메세지를 보낼 수 없습니다.  :" + e.getMessage());
			e.printStackTrace();
			clientService.increaseErrCnt(userId);
			return ;
		}
		
	}
	
}


