package com.example.lwq.damaiclient.modle.IM.bean;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Calendar;

/**
 * Created by lwq on 2015/12/14.
 */
@Table(name = "IMMessage")
public class IMMessage extends Model {
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_RECORD = "record";
    public static final String TYPE_NOTICE = "notice";
    public static final String TYPE_LIKE = "like";

    public static final String STATUS_SENDING = "sending";
    public static final String STATUS_SENT = "sent";
    public static final String STATUS_FAILED = "failed";

    @Column(name = "Chat", onDelete = Column.ForeignKeyAction.CASCADE)
    public Chat chat;

    @Column(name = "msgId")
    public String msgId;

    @Column(name = "content")
    public byte[] content;

    @Column(name = "message")
    public String message;

    @Column(name = "currentClientId")
    public String currentClientId;

    @Column(name = "timestamp")
    public long timestamp;

    @Column(name = "fileURL")
    public String fileURL;

    @Column(name = "fromClient")
    public String fromClient;

    @Column(name = "fromUsername")
    public String fromUsername;

    @Column(name = "fromUserIconUrl")
    public String fromUserIconUrl;

    @Column(name = "topicId")
    public String topicId;

    @Column(name = "type")
    public String type;

    @Column(name = "status")
    public String status;

    @Column(name = "latitude")
    public String latitude;
    @Column(name = "longitude")
    public String longitude;

    @Column(name = "readACK")
    public boolean readACK;

    @Column(name = "senderName")
    public String senderName;

    @Column(name = "targetUserId")
    public String targetUserId;

    @Column(name = "readed")
    public boolean readed;

    public void update(){
        Calendar c = Calendar.getInstance();
        timestamp = c.getTimeInMillis();
        final boolean isReaded = readed;

        IMMessage exist = getFromTable();
        if(exist == null){
            save();
        }else{
            exist.timestamp = timestamp;
            exist.status = status;
            if(!exist.readed)
                exist.readed = isReaded;
            if(!exist.readACK)
                exist.readACK = readACK;
            exist.save();//保存信息
        }
        Log.i("Msg.update.readACK", readACK + "?");

    }
    public IMMessage getFromTable(){//获取消息的内容（保存在本地的）
        return new Select().from(IMMessage.class).where("msgId = \""+msgId+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();
    }

    public boolean isMine(){//判断消息是不是自己发的
        return currentClientId.equals(fromClient);
    }
}
