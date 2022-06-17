package cn.pointer.lrl;

import cn.pointer.lrl.generator.controller.CommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Pathtest {
    @Autowired
    CommonController commonController;

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir")+"/cache/upload/img/");;
    }
}
