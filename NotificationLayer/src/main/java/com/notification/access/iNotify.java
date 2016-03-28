package com.notification.access;

import com.notification.bean.MessageDetailBean;

public interface iNotify {
	public String send(MessageDetailBean messageDetailBean,String message);
}
