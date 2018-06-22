package com.cglee079.coinseeker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;

import com.cglee079.coinseeker.dao.ClientMsgDao;
import com.cglee079.coinseeker.log.Log;
import com.cglee079.coinseeker.model.ClientMsgVo;
import com.cglee079.coinseeker.util.TimeStamper;

@Service
public class ClientMsgService {
	
	@Autowired
	private ClientMsgDao clientMsgDao;

	public boolean insert(Update update) {
		ClientMsgVo clientMsg = new ClientMsgVo();
		Message message = null;
		if(update.getMessage() != null) {
			message = update.getMessage();
		} else if( update.getEditedMessage() != null) {
			message = update.getEditedMessage();
		}
		
		User user = message.getFrom();
		clientMsg.setUserId(user.getId().toString());
		clientMsg.setUsername(user.getLastName() + " " + user.getFirstName());
		clientMsg.setMsg(message.getText());
		clientMsg.setDate(TimeStamper.getDateTime());
		
		Log.i(clientMsg.log());
		return clientMsgDao.insert(clientMsg);
	}

}
