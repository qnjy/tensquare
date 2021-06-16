import com.tensquare.encrypt.EncryptApplication;
import com.tensquare.encrypt.rsa.RsaKeys;
import com.tensquare.encrypt.service.RsaService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EncryptApplication.class)
public class EncryptTest {

    @Autowired
    private RsaService rsaService;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void genEncryptDataByPubKey() {

        //此处可替换为你自己的请求参数json字符串
        String data = "{\"title\":\"java\"}";

        try {
            String encData = rsaService.RSAEncryptDataPEM(data, RsaKeys.getServerPubKey());
            System.out.println("data: " + data);
            System.out.println("encData: " + encData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //解密
    @Test
    public void test() throws Exception {
        String requestData = "PDK0rEtJflZM/mbnlWpurrxSSUOZozNp2whLYPciAPguxuhCxKNErK/GFavzGOpJAJuYZJ8EBlelbyD0Lxi5UfyWnZEaHBPQ7pD3dKNNKkbE45LHQhMLsHRbAoEdCsz+guO+X36qkrHm3zSv2VueJ2eB3yvENgpu+LVxvWyVOKJ8eeWGtSDHQCMbsr783PTs6tQIMGTPdH+RcTjJMoFxpkO9RUS9NEZtRPRH2pnnNqQO98VUHmtzhzmCiajknseBxPfkTkxvZnO/azRp7XD/vPygE10u8BImoYOz5BSnVyI/OkQuNAT7nJ2GcygJo9VnsjzpXwY/vEx47xE7IHQd1w==";

        String decryptDataPEM = rsaService.RSADecryptDataPEM(requestData, RsaKeys.getServerPrvKeyPkcs8());

        System.out.println(decryptDataPEM);
    }
}