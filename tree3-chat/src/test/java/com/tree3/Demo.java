package com.tree3;

import com.tree3.pojo.command.Command;
import com.tree3.pojo.message.MessageHead;
import com.tree3.pojo.message.MessageText;
import com.tree3.utils.JSONUtils;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/26 9:48 </p>
 */
public class Demo {

    public static void main(String[] args) {
//        testAaa();
        MessageText messageText = JSONUtils.paresToObj("{\"createTime\":\"2024-05-20T09:00:55.932Z\",\"commandType\":2,\"state\":-1,\"from\":1,\"to\":-1,\"content\":null}", MessageText.class);
        int cmd = messageText.getCommandType();
        Command x = Command.roughlyMatch(cmd);
        Command y = Command.match(cmd);
        System.out.println(x);
        System.out.println(y);
        for (int i = 0; i < 255; i++) {
            System.out.print("执");
        }
    }



    private static void testAaa() {
        MessageText json = new MessageText();
        json.setCreateTime(new Date());
//        json.setMessageType(1);
        json.setCommandType(Command.PrivateChatText.getType());
        json.setState(3);
        json.setFrom(4);
        json.setTo(5);
        json.setContent("执笔画清眸");
        String jsonStr = JSONUtils.toJsonStr(json);
        Command command = Command.roughlyMatch(json.getCommandType());
        System.out.println(command);
        System.out.println(jsonStr);
        System.out.println(JSONUtils.paresToObj(jsonStr, MessageHead.class));
    }

}
