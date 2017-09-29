package com.rocket;

public interface TokenManager {
	
    public Token createToken(long userId);
     
    public boolean checkToken(Token model);
   
    public Token getToken(String authentication);
    
    public void deleteToken(long userId);
}
