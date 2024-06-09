package com.tree3.service.impl;

import com.tree3.Tree3ChatApplication;
import com.tree3.service.PrivateChatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/28 8:32 </p>
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tree3ChatApplication.class)
public class PrivateChatServiceImplTest {
    @Autowired
    private PrivateChatService privateChatService;

}