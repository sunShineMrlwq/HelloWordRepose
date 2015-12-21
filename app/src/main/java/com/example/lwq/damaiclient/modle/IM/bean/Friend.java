package com.example.lwq.damaiclient.modle.IM.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lwq on 2015/12/14.
 */
public class Friend extends Model implements Serializable {

    @Column(name = "userClientId")
    public String userClientId;
    @Column(name = "targetClientId")
    public String targetClientId;
    @Column(name = "isMutual")
    public boolean isMutual;
}
