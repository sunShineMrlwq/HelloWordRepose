package com.example.lwq.damaiclient.modle.IM.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lwq on 2015/12/14.
 */
@Table(name = "Chat")
public class Chat extends Model implements Serializable {
    @Column(name = "currentClientId")
    public String currentClientId;
    @Column(name = "updateTime")
    public long updateTime;
    @Column(name="targetClientId")
    public String targetClientId;
    @Column(name = "Topic", onDelete = Column.ForeignKeyAction.CASCADE)
    public Topic topic;
    public Chat(){
        super();
    }
    public List<IMMessage> messages(){
        return new Select().from(IMMessage.class).where("Chat = \""+getId()+"\" and currentClientId = \""+currentClientId+"\"").execute();
    }

    public IMMessage lastMessage(){
        return new Select().from(IMMessage.class).where("Chat = \""+getId()+"\" and currentClientId = \""+currentClientId+"\"").orderBy("timestamp DESC").executeSingle();
    }

    public List<IMMessage> unReadedMessages(){
        return new Select().from(IMMessage.class).where("Chat = \""+getId()+"\" and readed = 0 and currentClientId = \""+currentClientId+"\"").execute();
    }

    public Chat getFromTable(){
//        if(topic!=null){//获取聊天记录
//          //  return new Select().from(Chat.class).where("Topic = \""+topic.getFromTable().getId()+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();
//        }else{//没有记录，第一次发信息
            return new Select().from(Chat.class).where("targetClientId = \""+targetClientId+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();
//        }
    }

    public void update(){
        Calendar c = Calendar.getInstance();
        updateTime = c.getTimeInMillis();

        Chat exist;
        if(topic!=null){//topic内容的id
            exist = new Select().from(Chat.class).where("Topic = \""+topic.getId()+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();
        }else{
            exist = new Select().from(Chat.class).where("targetClientId = \""+targetClientId+"\" and currentClientId = \""+currentClientId+"\"").executeSingle();
        }

        if(exist == null){
            save();
        }else{
            exist.updateTime = updateTime;
            exist.save();
        }
    }
}
