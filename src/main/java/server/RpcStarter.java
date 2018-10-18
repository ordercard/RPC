package server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Auther :huiqiang
 * @Description :
 * @Date: Create in 11:44 2018/10/18 2018
 * @Modify:
 */
public class RpcStarter {
    public static void startServer(String path) {
        new ClassPathXmlApplicationContext(path);
    }
}