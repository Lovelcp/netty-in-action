package com.wooyoo.learning.netty;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * 组合型的编解码器
 */
public class CombinedShortIntegerCodec extends CombinedChannelDuplexHandler<IntegerToStringDecoder, ShortToByteEncoder> {
}
