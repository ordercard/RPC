package entry;

/**
 * @Auther :huiqiang
 * @Description :
 * @Date: Create in 10:02 2018/10/18 2018
 * @Modify:
 */

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Auther :huiqiang
 * @Description :
 * @Date: Create in 10:23 2018/10/11 2018
 * @Modify:
 */

@Data
public abstract class Packet {

    //版本号
    private byte version =1;

    public abstract Byte getCommand();
}
