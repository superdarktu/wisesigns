package com.signs.mapper.msg;

import com.signs.model.msg.Msg;
import com.signs.util.MyMapper;

public interface MsgMapper extends MyMapper<Msg> {

    Msg verify(Msg msg);

    Msg selectLast(String phone);
}