package com.muto.knife_stone.presentation.view;

import com.alorma.github.sdk.bean.dto.response.Notification;

import java.util.Collection;

/**
 * Created by muto on 16-3-17.
 */
public interface NotificationsView {

    void renderUserList(Collection<Notification> notiCollection);
}
