package com.ucsmy.core.tool.serialNumber.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 序列号生成器配置信息，生成18位序列号，格式：xxxx(4位前缀) + yyMMddHH(8位日期) + 000000(6位序列)
 * Created by ucs on 2017/5/18.
 */
@ConfigurationProperties(
        prefix = "ucsmy.serialNumber"
)
@Data
public class SerialNumberProperties {

    /**
     * 生成模式
     */
    private GeneratorMode mode;

    /**
     * 序列号前缀
     */
    private String prefix;

    /**
     * 序列号备用前缀，当出现重复主键时使用备用序列号生成器生成
     */
    private String backupPrefix;

    /**
     * 序列号日期格式
     */
    private String datePattern = "yyMMddHH";

    /**
     * 序列号起始基数
     */
    private int startNum;

    /**
     * 序列号编号格式
     */
    private String numberPattern = "000000";

    /**
     * 增量
     */
    private String delta;

    /**
     * redis key命名空间
     */
    private String namespace;
}
