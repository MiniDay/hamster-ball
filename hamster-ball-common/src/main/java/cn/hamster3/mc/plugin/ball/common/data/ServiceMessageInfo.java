package cn.hamster3.mc.plugin.ball.common.data;

import cn.hamster3.mc.plugin.ball.common.entity.ServerType;
import cn.hamster3.mc.plugin.core.common.constant.CoreConstantObjects;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

/**
 * 服务消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class ServiceMessageInfo {
    /**
     * 消息的频道
     */
    @NotNull
    private String channel;
    /**
     * 消息发送者
     */
    @NotNull
    private String senderID;
    /**
     * 接受该消息的目标服务器ID
     * <p>
     * 若设定该值，则仅服务器名称匹配的子端才能接收到这条消息
     * <p>
     * 若不设定（即为 null），则该消息会广播给所有子端
     */
    @Nullable
    private String receiverID;
    /**
     * 接受该消息的目标服务器类型
     * <p>
     * 若设定该值，则仅服务器类型匹配的子端才能接收到这条消息
     * <p>
     * 若不设定（值为null），则该消息会广播给所有子端
     */
    @Nullable
    private ServerType receiverType;
    /**
     * 消息动作
     * <p>
     * 一般用这个来判断插件应该如何处理这条消息
     */
    private String action;
    /**
     * 消息内容
     * <p>
     * 这里是消息的附加参数
     */
    private JsonElement content;

    /**
     * 序列化至 Json
     *
     * @return json对象
     */
    @NotNull
    public JsonObject saveToJson() {
        JsonObject object = new JsonObject();
        object.addProperty("channel", channel);
        object.addProperty("senderID", senderID);
        if (receiverID != null) {
            object.addProperty("toServer", receiverID);
        }
        if (receiverType != null) {
            object.addProperty("toServer", receiverType.name());
        }
        object.addProperty("action", action);
        object.add("content", content);
        return object;
    }

    /**
     * 以 Java 对象获取消息内容
     *
     * @param clazz 对象所属的类
     * @param <T>   对象类型
     * @return Java 对象
     */
    public <T> T getContentAs(Class<T> clazz) {
        return CoreConstantObjects.GSON.fromJson(content, clazz);
    }

    /**
     * 以字符串形式获取消息内容
     *
     * @return 消息内容
     */
    public String getContentAsString() {
        return content.getAsString();
    }

    public boolean getContentAsBoolean() {
        return content.getAsBoolean();
    }

    public int getContentAsInt() {
        return content.getAsInt();
    }

    public long getContentAsLong() {
        return content.getAsLong();
    }

    public BigInteger getContentAsBigInteger() {
        return content.getAsBigInteger();
    }

    public BigDecimal getContentAsBigDecimal() {
        return content.getAsBigDecimal();
    }

    public UUID getContentAsUUID() {
        return UUID.fromString(content.getAsString());
    }

    /**
     * 以 JsonObject 对象获取消息内容
     *
     * @return 消息内容
     */
    public JsonObject getContentAsJsonObject() {
        return content.getAsJsonObject();
    }

    /**
     * 以 JsonArray 对象获取消息内容
     *
     * @return 消息内容
     */
    public JsonArray getContentAsJsonArray() {
        return content.getAsJsonArray();
    }

    @Override
    public String toString() {
        return saveToJson().toString();
    }
}
