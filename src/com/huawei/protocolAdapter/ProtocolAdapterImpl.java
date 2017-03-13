package com.huawei.protocolAdapter;
 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ProtocolAdapterImpl implements ProtocolAdapterInterface {
	// Decode Method
	@Override
	public ObjectNode decode(byte[] binaryData) {
		try {
			if (binaryData[0] == 0x00) {
				// Data Report
				return decodeDataReport(binaryData);
			} else if (binaryData[0] == 0x01) {
				// Command Response
				return decodeCmdResponse(binaryData);
			} else {
				// Error Handler
				return decodeErrorHandle();
			}
		} catch (Exception e) {
			// Error Handler
			return decodeErrorHandle();
		}
	}

	// Encode Method
	@Override
	   public byte[] encode(ObjectNode JsonData) {
		try {
			String strMsgType = (String)JsonData.get("msgType").asText();
			if (strMsgType.equalsIgnoreCase("cloudReq")) {
				// Encode as Command
				return encodeCommand(JsonData);
			} else if (strMsgType.equalsIgnoreCase("cloudRsp")) {
				// Encode as Data
				return encodeDataRsp(JsonData);
			} else {
				// Error Handler
				return encodeErrorHandle();
			}
		} catch (Exception e) {
			// Error Handler
			return encodeErrorHandle();
		}
	}

	// Decode as Data Report
	public ObjectNode decodeDataReport(byte[] binaryData) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objResultNode = mapper.createObjectNode();

		objResultNode.put("identifier", "test");
		objResultNode.put("msgType", "deviceReq");
		objResultNode.put("hasMore", 0);

		ArrayNode arrynode = mapper.createArrayNode();
		switch (binaryData[1]) {
		case 0x01:
			{
				// Humidity Service
				ObjectNode serviceNode = mapper.createObjectNode();
				serviceNode.put("serviceId", "Humidity");
				ObjectNode serviceData = mapper.createObjectNode();
				serviceData.put("HumidityCur", binaryData[2]);
				serviceData.put("HumidityMinToday", binaryData[3]);
				serviceData.put("HumidityMaxToday", binaryData[4]);
				serviceNode.put("serviceData", serviceData);
				arrynode.add(serviceNode);
			}
			break;
		case 0x02:
			{
				// Temperature Service
				ObjectNode serviceNode = mapper.createObjectNode();
				serviceNode.put("serviceId", "Temperature");
				ObjectNode serviceData = mapper.createObjectNode();
				serviceData.put("TemperatureCur", binaryData[2]);
				serviceData.put("TemperatureMinToday", binaryData[3]);
				serviceData.put("TemperatureMaxToday", binaryData[4]);
				serviceNode.put("serviceData", serviceData);
				arrynode.add(serviceNode);
			}
			break;
		case 0x03:
			{
				// Battery Service
				ObjectNode serviceNode = mapper.createObjectNode();
				serviceNode.put("serviceId", "Battery");
				ObjectNode serviceData = mapper.createObjectNode();
				serviceData.put("BatteryCur", binaryData[2]);
				serviceNode.put("serviceData", serviceData);
				arrynode.add(serviceNode);
			}
			break;
		default:
			// Error Handle
			return decodeErrorHandle();
		}

		objResultNode.put("data", arrynode);
		return objResultNode;
	}

	// Decode as Command Response
	public ObjectNode decodeCmdResponse(byte[] binaryData) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objResultNode = mapper.createObjectNode();

		objResultNode.put("identifier", "test");
		objResultNode.put("msgType", "deviceRsp");
		objResultNode.put("hasMore", 0);
		objResultNode.put("errcode", 0);
		ObjectNode objResult = mapper.createObjectNode();
    	if (binaryData[1] == 0) {
    		objResult.put("result", 0);
    	} else {
    		objResult.put("result", binaryData[2]);
    	}
    	objResultNode.put("body", objResult);
		return objResultNode;
	}

	// Decode Error Handle
	public ObjectNode decodeErrorHandle() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objResultNode = mapper.createObjectNode();

		objResultNode.put("identifier", "test");
		objResultNode.put("msgType", "deviceReq");
		objResultNode.put("hasMore", 0);

		// Just a Sample to Notify Error
		ArrayNode arrynode = mapper.createArrayNode();
		ObjectNode serviceNode = mapper.createObjectNode();
		serviceNode.put("serviceId", "Battery");
		ObjectNode serviceData = mapper.createObjectNode();
		serviceData.put("BatteryCur", 0);
		serviceNode.put("serviceData", serviceData);
		arrynode.add(serviceNode);
		objResultNode.put("data", arrynode);

		return objResultNode;
	}

	// Encode as Command
	public byte[] encodeCommand(ObjectNode JsonData) {
		byte[] returnByte = new byte[5];
		// Type
		returnByte[0] = 0x00;
		// Service
		String strServiceId = JsonData.get("serviceId").asText();
		if (strServiceId.equalsIgnoreCase("Humidity")) {
			returnByte[1] = 0x00;
		} else if (strServiceId.equalsIgnoreCase("Temperature")) {
			returnByte[1] = 0x01;
		} else if (strServiceId.equalsIgnoreCase("Battery")) {
			returnByte[1] = 0x02;
		} else {
			return encodeErrorHandle();
		}
		// Command
		String strCommand = JsonData.get("cmd").asText();
		if (strCommand.startsWith("SET_REPORT_PERIOD_")) {
			returnByte[2] = 0x00;
			returnByte[3] = (byte)JsonData.get("paras").get("Period").asInt();
		} else if (strCommand.endsWith("WARNING")) {
			returnByte[2] = 0x01;
			returnByte[3] = (byte)JsonData.get("paras").get("Value").asInt();
		} else {
			return encodeErrorHandle();
		}
		// Has More
		returnByte[4] = (byte)JsonData.get("hasMore").asInt();
		return returnByte;
	}

	// Encode As Data Response
	public byte[] encodeDataRsp(ObjectNode JsonData) {
		byte[] returnByte = new byte[1];
		returnByte[0] = 0x01;
		return returnByte;
	}

	// Encode Error Handle
	public byte[] encodeErrorHandle() {
		byte[] returnByte = new byte[2];
		returnByte[0] = 0x02;
		returnByte[1] = 0x02;
		return returnByte;
	}
}