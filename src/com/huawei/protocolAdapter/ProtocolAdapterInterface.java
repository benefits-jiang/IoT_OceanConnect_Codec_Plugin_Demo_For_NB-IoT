package com.huawei.protocolAdapter;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ProtocolAdapterInterface {

    public abstract ObjectNode decode(byte[] binaryData);

    public abstract byte[] encode(ObjectNode input);
}
