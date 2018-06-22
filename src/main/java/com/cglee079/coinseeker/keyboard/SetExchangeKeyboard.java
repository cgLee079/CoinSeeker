package com.cglee079.coinseeker.keyboard;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

public class SetExchangeKeyboard extends ReplyKeyboardMarkup {
	public SetExchangeKeyboard() {
		super();

		this.setSelective(true);
		this.setResizeKeyboard(true);
		this.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();
		
//		KeyboardRow keyboardRow = null;
//		keyboardRow = new KeyboardRow();
//		keyboardRow.add(CMD.SET_EXCHANGE_COINONE);
//		keyboard.add(keyboardRow);
//		
//		keyboardRow = new KeyboardRow();
//		keyboardRow.add(CMD.SET_EXCHANGE_BITHUMB);
//		keyboard.add(keyboardRow);
//		
//		keyboardRow = new KeyboardRow();
//		keyboardRow.add(CMD.SET_EXCHANGE_UPBIT);
//		keyboard.add(keyboardRow);
//		
//		keyboardRow = new KeyboardRow();
//		keyboardRow.add(CMD.SET_EXCHANGE_OUT);
//		keyboard.add(keyboardRow);
		
		
		this.setKeyboard(keyboard);
	}

}
