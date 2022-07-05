package com.nowcoder.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author GuoDingWei
 * @Date 2022/6/22 0:24
 */

public class Event {

    //主题（事件）
    private String topic;
    //事件的触发者（使用者）
    private int userId;
    //事件作用的实体类型
    private int entityType;
    //事件作用的实体id
    private int entityId;
    //实体的作者
    private int entityUserId;
    //让事件具有扩展性
    private Map<String, Object> data = new HashMap<>();

    public Event() {

    }

    public Event(String topic, int userId, int entityType, int entityId, int entityUserId, Map<String, Object> data) {
        this.topic = topic;
        this.userId = userId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.entityUserId = entityUserId;
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
