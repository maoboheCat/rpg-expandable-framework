package protocol;

import cn.hutool.core.util.IdUtil;
import com.cola.rpc.constant.RpcConstant;
import com.cola.rpc.model.ProtocolMessage;
import com.cola.rpc.model.RpcRequest;
import com.cola.rpc.protocol.ProtocolMessageDecoder;
import com.cola.rpc.protocol.ProtocolMessageEncoder;
import com.cola.rpc.protocol.constant.ProtocolConstant;
import com.cola.rpc.protocol.enums.ProtocolMessageSerializerEnum;
import com.cola.rpc.protocol.enums.ProtocolMessageStatusEnum;
import com.cola.rpc.protocol.enums.ProtocolMessageTypeEnum;
import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * 自定义协议测试
 * @author Maobohe
 * @createData 2024/3/29 10:32
 */
public class ProtocolTest {

    @Test
    public void testEncodeAndDecode() throws IOException {
        // 构造消息
        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"aaa", "bbb"});
        protocolMessage.setHeader(header);
        protocolMessage.setBody(rpcRequest);

        Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
        ProtocolMessage<?> message = ProtocolMessageDecoder.decode(encodeBuffer);
        Assert.assertNotNull(message);
    }
}
