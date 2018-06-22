package com.cglee079.coinseeker.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cglee079.coinseeker.constants.ID;
import com.cglee079.coinseeker.constants.SET;
import com.cglee079.coinseeker.dao.ClientDao;
import com.cglee079.coinseeker.log.Log;
import com.cglee079.coinseeker.model.ClientVo;
import com.cglee079.coinseeker.util.TimeStamper;

@Service
public class ClientService {
	@Autowired
	private ClientDao clientDao;

	public List<ClientVo> list() {
		return clientDao.list();
	}
	
	public String getState(Integer id) {
		ClientVo client = clientDao.get(id.toString());
		if (client != null) {
			return client.getState();
		} else {
			return null;
		}
	}

	public String getMarket(String userId) {
		ClientVo client = clientDao.get(userId);
		return client.getMarket();
	}

	public String getMarket(long userId) {
		return this.getMarket(String.valueOf(userId));
	}

	public boolean openChat(Integer userId, String username, String market) {

		ClientVo client = null;
		client = clientDao.get(userId.toString());

		if (client == null) {
			client = new ClientVo();
			client.setUserId(userId.toString());
			client.setUsername(username);
			client.setState(ID.STATE_MAIN);
			client.setLocalTime((long)0);
			client.setLang(ID.LANG_KR);
			client.setOpenDate(new Date());
			client.setMarket(market);
			client.setErrCnt(0);
			return clientDao.insert(client);
		} else {
			String enabled = client.getEnabled();
			if (enabled.equals("Y")) {
				return false;
			} else if (enabled.equals("N")) {
				client.setErrCnt(0);
				client.setEnabled("Y");
				client.setReopenDate(new Date());
				return clientDao.update(client);
			} else {
				return false;
			}
		}
	}

	public boolean increaseErrCnt(String userId) {
		ClientVo client = clientDao.get(userId);
		if (client != null) {
			if (client.getEnabled().equals("Y")) {
				int errCnt = client.getErrCnt();
				errCnt = errCnt + 1;
				if (errCnt > SET.CLNT_MAX_ERRCNT) {
					Log.i("Close Client\t:\t[id :" + userId + "\t" + client.getUsername() + " ] ");
					client.setEnabled("N");
					client.setCloseDate(new Date());
				} else {
					client.setErrCnt(errCnt);
				}
				return clientDao.update(client);
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public boolean updateState(String userId, String state) {
		ClientVo client = clientDao.get(userId);
		if (client != null) {
			client.setState(state);
			return clientDao.update(client);
		} else {
			return false;
		}

	}
	
	public boolean updateMarket(String userId, String market) {
		ClientVo client = clientDao.get(userId);
		if (client != null) {
			client.setMarket(market);
			return clientDao.update(client);
		} else {
			return false;
		}
	}

	public boolean updateLocalTime(String userId, Long localTime) {
		ClientVo client = clientDao.get(userId);
		if(client != null){
			client.setLocalTime(localTime);
			return clientDao.update(client);
		} else{
			return false;
		}
	}
	
	public boolean updateLanguage(String userId, String lang) {
		ClientVo client = clientDao.get(userId);
		if(client != null){
			client.setLang(lang);
			return clientDao.update(client);
		} else{
			return false;
		}
	}
	
	public boolean updateMsgDate(ClientVo client) {
		if(client != null){
			client.setMsgDate(TimeStamper.getDateTime());
			return clientDao.update(client);
		} else{
			return false;
		}
	}
	
	public ClientVo get(String userId) {
		return clientDao.get(userId);
	}

	public ClientVo get(int userId) {
		return this.get(String.valueOf(userId));
	}


}
