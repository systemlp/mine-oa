package com.mine.oa.controller;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.mine.oa.util.RsaUtil;
import com.mine.oa.vo.CommonResultVo;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("/rsa")
public class RsaController {

    @GetMapping("/getEncryptParam")
    public CommonResultVo getEncryptParam() throws Exception {
        KeyPair keyPair = RsaUtil.getKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 公钥指数
        String exponent = publicKey.getPublicExponent().toString(16);
        // 模
        String modulus = publicKey.getModulus().toString(16);
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("exponent", exponent);
        resultMap.put("modulus", modulus);
        return new CommonResultVo<Map<String, String>>().success(resultMap);
    }

    @PostMapping("/decryptText")
    public CommonResultVo decryptText(String text) throws Exception {
        String clearText = RsaUtil.decrypt(text);
        return new CommonResultVo<String>().success(clearText);
    }

}
