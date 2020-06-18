package cn.wildfirechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import cn.wildfirechat.message.Message;

/**
 * 会话信息包含最后一条信息

 */

public class ConversationInfo implements Parcelable {
    public Conversation conversation;
    public Message lastMessage;
    public long timestamp;
    public String draft;//草稿
    public UnreadCount unreadCount;
    public boolean isTop;
    public boolean isSilent;

    public ConversationInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.conversation, flags);
        dest.writeParcelable(this.lastMessage, flags);
        dest.writeLong(this.timestamp);
        dest.writeString(this.draft);
        dest.writeParcelable(this.unreadCount, flags);
        dest.writeByte(this.isTop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSilent ? (byte) 1 : (byte) 0);
    }

    protected ConversationInfo(Parcel in) {
        this.conversation = in.readParcelable(Conversation.class.getClassLoader());
        this.lastMessage = in.readParcelable(Message.class.getClassLoader());
        this.timestamp = in.readLong();
        this.draft = in.readString();
        this.unreadCount = in.readParcelable(UnreadCount.class.getClassLoader());
        this.isTop = in.readByte() != 0;
        this.isSilent = in.readByte() != 0;
    }

    public static final Creator<ConversationInfo> CREATOR = new Creator<ConversationInfo>() {
        @Override
        public ConversationInfo createFromParcel(Parcel source) {
            return new ConversationInfo(source);
        }

        @Override
        public ConversationInfo[] newArray(int size) {
            return new ConversationInfo[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationInfo that = (ConversationInfo) o;
        return timestamp == that.timestamp &&
                isTop == that.isTop &&
                isSilent == that.isSilent &&
                Conversation.equals(conversation, that.conversation) &&
                Conversation.equals(lastMessage, that.lastMessage) &&
                Conversation.equals(draft, that.draft) &&
                Conversation.equals(unreadCount, that.unreadCount);
    }

    @Override
    public int hashCode() {

        return Conversation.hashCode(conversation, lastMessage, timestamp, draft, unreadCount, isTop, isSilent);
    }

    @Override
    public String toString() {
        return "ConversationInfo{" +
                "conversation=" + conversation +
                ", lastMessage=" + lastMessage +
                ", timestamp=" + timestamp +
                ", draft='" + draft + '\'' +
                ", unreadCount=" + unreadCount +
                ", isTop=" + isTop +
                ", isSilent=" + isSilent +
                '}';
    }
}
