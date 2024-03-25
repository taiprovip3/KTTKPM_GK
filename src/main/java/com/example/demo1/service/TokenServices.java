package com.example.demo1.service;

import com.example.demo1.entity.Token;

public interface TokenServices {
	public void saveToken(Token token);
	public boolean checkTokenUser(String username);
	public void updateToken(Token token);
}