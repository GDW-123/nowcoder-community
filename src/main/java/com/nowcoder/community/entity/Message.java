package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author GuoDingWei
 * @Date 2022/6/19 15:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {

    private int id;
    //谁发的私信
    private int fromId;
    //谁收的私信
    private int toId;
    //发的私信与收的私信的关系id
    //如果发私信的fromId=111，收私信的toId=222，那么conversationId=111_222
    //我们总是将fromId放在前面，toId放在后面，形式如上所示
    private String conversationId;
    //私信的内容
    private String content;
    //私信的状态，是正常还是拉黑
    private int status;
    //发送私信的时间
    private Date createTime;
}
