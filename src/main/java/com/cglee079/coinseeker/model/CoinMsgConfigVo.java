package com.cglee079.coinseeker.model;

public class CoinMsgConfigVo {
	private String coinId;
	private Integer digitKRW;
	private Integer digitUSD;
	private Integer digitBTC;

	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}

	public Integer getDigitKRW() {
		return digitKRW;
	}

	public void setDigitKRW(Integer digitKRW) {
		this.digitKRW = digitKRW;
	}

	public Integer getDigitUSD() {
		return digitUSD;
	}

	public void setDigitUSD(Integer digitUSD) {
		this.digitUSD = digitUSD;
	}

	public Integer getDigitBTC() {
		return digitBTC;
	}

	public void setDigitBTC(Integer digitBTC) {
		this.digitBTC = digitBTC;
	}

}