package annotaion;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther :huiqiang
 * @Description :
 * @Date: Create in 11:02 2018/10/18 2018
 * @Modify:
 */
@Target({ ElementType.TYPE })//注解用在接口上
@Retention(RetentionPolicy.RUNTIME)//VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Component
public @interface RPCSer {
    Class<?> value();
}