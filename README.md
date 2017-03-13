IoT\_OceanConnect\_Codec\_Plugin\_Demo\_For\_NB-IoT

Codec Plugin，即编解码插件，是在NB-IoT方案中，上传到OceanConnect平台的，用于对数据上报和命令下发进行编解码的程序接口。

编解码插件只需要实现两个接口：decode和encode，完成4项工作：

* decode
	* 对上报的数据进行解码；
	* 对设备返回的平台下发命令的响应解码。
* encode
	* 对平台下发命令的编码；
	* 对平台收到上报数据后的响应进行编码。

本工程，包含了编解码插件Demo的源码和上传到平台的最终压缩包。开发者可以通过阅读源码和分析压缩包的格式，了解开发各环节。

为了便于华为支持人员上传，编解码插件的类名和压缩包格式（压缩包中需包含依赖库），请遵循Demo。

版本更新

Codec Plugin for NB-IoT v1.0

相关文档

《华为IoT平台编解码库开发指南》，请通过devCenter向华为开发者社区获取。

[编解码器离线测试工具](http://developer.huawei.com/ict/cn/resource/tool?ecologyID=383&productID=0&colname=1&key=%E5%8D%8E%E4%B8%BANB-IoT%E7%BC%96%E8%A7%A3%E7%A0%81%E6%8F%92%E4%BB%B6%E6%A3%80%E6%B5%8B%E5%B7%A5%E5%85%B7&curPage=1&pageNum=10&isOpen=false)

《NB-IoT编解码插件检测工具使用说明》，请通过devCenter向华为开发者社区获取。

获取帮助

在开发过程中，您有任何问题均可以至DevCenter中提单跟踪。也可以在华为开发者社区中查找或提问。另外，华为技术支持热线电话：400-822-9999（转二次开发）