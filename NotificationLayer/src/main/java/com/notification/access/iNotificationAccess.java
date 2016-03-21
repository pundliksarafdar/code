package com.notification.access;

import java.util.List;

import com.notification.bean.MessageDetailBean;

public interface iNotificationAccess {
	public String send(List<MessageDetailBean> messageDetailBeans);
}
