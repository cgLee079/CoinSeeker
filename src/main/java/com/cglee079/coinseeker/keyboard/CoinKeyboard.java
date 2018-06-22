package com.cglee079.coinseeker.keyboard;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import com.cglee079.coinseeker.cmd.CMDER;

public class CoinKeyboard extends ReplyKeyboardMarkup{
	
	public CoinKeyboard(String lang, List<String> coins) {
		super();
		
	    this.setSelective(true);
	    this.setResizeKeyboard(true);
	    this.setOneTimeKeyboard(false);
	    
	    List<KeyboardRow> keyboard = new ArrayList<>();
	    
	    KeyboardRow keyboardRow = new KeyboardRow();
	    keyboardRow.add(coins.get(0));
	    
	    if(coins.size() == 1) {
	    	keyboard.add(keyboardRow);
	    }
	    
	    for(int i = 1; i < coins.size(); i++) {
	    	if(i % 5 == 0) {
	    		keyboard.add(keyboardRow);
	    		keyboardRow = new KeyboardRow();
	    	} else if(i == coins.size() - 1) {
	    		keyboard.add(keyboardRow);
	    	}
	    	keyboardRow.add(coins.get(i));
	    }
	    
	    KeyboardRow keyboardForthRow = new KeyboardRow();
	    keyboardForthRow.add(CMDER.getMainSetMarket(lang));
	    keyboardForthRow.add(CMDER.getMainCoinList(lang));
	    keyboardForthRow.add(CMDER.getMainSendMsg(lang));
	    keyboardForthRow.add(CMDER.getMainSupport(lang));
	    keyboardForthRow.add(CMDER.getMainPref(lang));
	    
	    keyboard.add(keyboardForthRow);
	    this.setKeyboard(keyboard);
	}
	

}
