package xyz.drugalev.ylabtrainingapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestYlabTrainingAppApplicationConfiguration.class)
class YlabTrainingAppApplicationTests {

    @Test
    void contextLoads() {
    }

}
