package com.websocketapi;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class SocketObjectEncoder<T> implements Encoder.Text<T>{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String encode(T arg0) throws EncodeException {
		// TODO Auto-generated method stub
		return "hello";
	}

}
