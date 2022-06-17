package cn.pointer.lrl.generator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommonControllerTest {
    @Autowired
    CommonController commonController;

    @Test
    void upload() {
//        System.out.println(commonController.upload());
    }
}