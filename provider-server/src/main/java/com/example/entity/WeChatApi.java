package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_yw_wechat_api")
public class WeChatApi {

    @TableId(value = "id",type = IdType.AUTO)
    private String id;

    @TableField(value = "applyer")
    private String applyer;

    @TableField(value = "status")
    private String status;
}
