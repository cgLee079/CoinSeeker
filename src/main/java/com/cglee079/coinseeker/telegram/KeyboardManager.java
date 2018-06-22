package com.cglee079.coinseeker.telegram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;

import com.cglee079.coinseeker.constants.ID;
import com.cglee079.coinseeker.constants.SET;
import com.cglee079.coinseeker.keyboard.CoinKeyboard;
import com.cglee079.coinseeker.keyboard.PreferenceKeyboard;
import com.cglee079.coinseeker.keyboard.SetLanguageKeyboard;
import com.cglee079.coinseeker.keyboard.SetMarketKeyboard;
import com.cglee079.coinseeker.log.Log;
import com.cglee079.coinseeker.model.CoinInfoVo;
import com.cglee079.coinseeker.model.CoinMarketConfigVo;
import com.cglee079.coinseeker.service.CoinInfoService;
import com.cglee079.coinseeker.service.CoinMarketConfigService;

public class KeyboardManager {

	@Autowired
	private CoinMarketConfigService coinMarketConfigService;

	@Autowired
	private CoinInfoService coinInfoService;

	private ReplyKeyboardRemove defaultKeyboard;
	private HashMap<String, CoinKeyboard> coinKeyboards;
	private HashMap<String, SetMarketKeyboard> setMarketKeyboards;
	private HashMap<String, SetLanguageKeyboard> setLanguageKeyboards;
	private HashMap<String, PreferenceKeyboard> preferenceKeyboards;

	public KeyboardManager() {
		defaultKeyboard = new ReplyKeyboardRemove();
		coinKeyboards 			= new HashMap<>();
		setMarketKeyboards 		= new HashMap<>();
		setLanguageKeyboards 	= new HashMap<>();
		preferenceKeyboards 	= new HashMap<>();
	}

	@PostConstruct
	public void init() {
		String market = "";
		for(int i = 0; i < SET.SUPPORT_MARKETS.length; i++) {
			market = SET.SUPPORT_MARKETS[i];
			this.makeCoinKeyboard(market);
		}
		
		setMarketKeyboards.put(ID.LANG_KR, new SetMarketKeyboard(ID.LANG_KR));
		setMarketKeyboards.put(ID.LANG_US, new SetMarketKeyboard(ID.LANG_US));

		setLanguageKeyboards.put(ID.LANG_KR, new SetLanguageKeyboard(ID.LANG_KR));
		setLanguageKeyboards.put(ID.LANG_US, new SetLanguageKeyboard(ID.LANG_US));

		preferenceKeyboards.put(ID.LANG_KR, new PreferenceKeyboard(ID.LANG_KR));
		preferenceKeyboards.put(ID.LANG_US, new PreferenceKeyboard(ID.LANG_US));
	}

	public void makeCoinKeyboard(String market) {
		List<CoinMarketConfigVo> configs = coinMarketConfigService.list(market, null);

		List<String> coinIds = new ArrayList<>();
		CoinMarketConfigVo config = null;
		for (int i = 0; i < configs.size(); i++) {
			config = configs.get(i);
			coinIds.add(config.getCoinId());
		}

		coinKeyboards.put(market + ID.LANG_KR, new CoinKeyboard(ID.LANG_KR, coinIds));
		coinKeyboards.put(market + ID.LANG_US, new CoinKeyboard(ID.LANG_US, coinIds));
	}

	public ReplyKeyboardRemove getDefaultKeyboard() {
		return defaultKeyboard;
	}

	public ReplyKeyboardRemove getDefaultKeyboard(String lang) {
		return defaultKeyboard;
	}

	public ReplyKeyboardMarkup getCoinKeyboard(String market, String lang) {
		return coinKeyboards.get(market + lang);
	}

	public ReplyKeyboardMarkup getMarketKeyboard(String lang) {
		return setMarketKeyboards.get(lang);
	}

	public SetLanguageKeyboard getSetLanguageKeyboard(String lang) {
		return setLanguageKeyboards.get(lang);
	}

	public PreferenceKeyboard getPreferenceKeyboard(String lang) {
		return preferenceKeyboards.get(lang);
	}
}
