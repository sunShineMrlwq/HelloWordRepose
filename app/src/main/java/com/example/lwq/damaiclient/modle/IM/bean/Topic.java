package com.example.lwq.damaiclient.modle.IM.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.io.Serializable;

/**
 * Created by lwq on 2015/12/14.
 */
public class Topic extends Model implements Serializable {
    @Column(name = "ownerClientId")
    public String ownerClientId;
    @Column(name = "topicId")
    public String topicId;
    @Column(name = "topicName")
    public String topicName;
}
