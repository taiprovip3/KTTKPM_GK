package com.example.demo1.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo1.entity.Token;
import com.example.demo1.repository.TokenRepository;

@Service
@Transactional
public class TokenServicesImpl implements TokenServices{

	@Autowired
	private TokenRepository tokenRepository;
	
	@Override
	public void saveToken(Token token) {
		System.out.println(token);
		tokenRepository.saveAndFlush(token);
	}

	@Override
	public boolean checkTokenUser(String username) {
		Token t = tokenRepository.findTokenByUsername(username);
		if(t == null)
			return false;
		return true;
	}

	@Override
	public void updateToken(Token token) {
		tokenRepository.updateToNewToken(token.getUser().getId(), token.getToken());
	}

}