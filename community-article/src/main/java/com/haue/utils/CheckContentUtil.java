package com.haue.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haue.constants.SystemConstants;
import com.haue.enums.AppHttpCodeEnum;
import com.haue.exception.SystemException;
import com.haue.pojo.entity.CheckResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@Data
@ConfigurationProperties(prefix = "ccu")
public class CheckContentUtil {

    private static String url = "https://audit.iflyaisol.com/audit/text";
    private static String appid = "4cfd5212"; // 点击在线沟通或联系讯飞智检小助手获取
    private static String key = "NDQ2M2EwNjU3ODA5YjRhODM1MGQxN2Uz"; // 点击在线沟通或联系讯飞智检小助手获取

//    private static final String content = "在战场上有个重要的原则，叫先开枪后瞄准。";// 送检文本

    private static final Gson gson = new Gson();

    public static CheckResult check(String content){
        String json = "{\n" +
                "  \"appid\": \"" + appid + "\",\n" +
                "  \"content_list\": [\n" +
                "    {\n" +
                "      \"content\": \"" + content + "\",\n" +
                "      \"name\": \"0\"\n" +
                "    }\n" +
               /* "    {\n" +
                "      \"content\": \"\",\n" +
                "      \"name\": \"1\"\n" +
                "    }\n" +*/
                "  ],\n" +
                "  \"is_match_all\": 1,\n" +
                // "  \"lib_ids\": [\n" +
                //"    \"" + lib_ids_1 + "\",\n" +
                //"    \"" + lib_ids_2 + "\"\n" +
                //"  ],\n" +
                "  \"categories\": [\n" +
                "    \"pornDetection\",\n" +
                "    \"violentTerrorism\",\n" +
                "    \"political\",\n" +
                "    \"lowQualityIrrigation\",\n" +
                "    \"contraband\",\n" +
                "    \"advertisement\",\n" +
                "    \"uncivilizedLanguage\"\n" +
                "  ],\n" +
                "  \"is_sync\": 1\n" +
                "}";
        // 生成与添加参数
        JSONObject object = JSON.parseObject(json);
        object.put("nonce_str", UUID.randomUUID());
        String sign = CheckUtils.getMD5Sign(object, key, null);
        object.put("sign", sign);
        // 发起请求
        String postJson = CheckUtils.doPostJson(url, object.toJSONString());
        System.out.println("文本审核返回结果:\n" + postJson);
        JsonParse jsonParse = gson.fromJson(postJson, JsonParse.class);
        return jsonParse.result();
    }

    // 解析返回的结果
    @Data
    @EqualsAndHashCode
    class JsonParse {
        List<Result_list> result_list;
        String desc;

        public CheckResult result(){
            System.out.println(result_list.toString());
            CheckResult result = new CheckResult();
            String suggest = result_list.get(0).suggest;
            if (!SystemConstants.REQUEST_SUCCESS.equals(desc)){
                throw new SystemException(AppHttpCodeEnum.CHECK_FAIL);
            }
            if (!SystemConstants.CHECK_SUCCESS.equals(suggest)){
                int confidence = result_list.get(0).detail.getCategory_list().get(0).getConfidence();
                String categoryDescription = !Objects.nonNull(result_list.get(0).detail.getCategory_list().get(0).getCategory_description()) ? "" : result_list.get(0).detail.getCategory_list().get(0).getCategory_description();
                String keyWord = !Objects.nonNull(result_list.get(0).detail.getCategory_list().get(0).word_list) ? "" : result_list.get(0).detail.getCategory_list().get(0).word_list.get(0);
                if ((SystemConstants.CHECK_REVIEW.equals(suggest) && confidence > 50) || SystemConstants.CHECK_FAIL.equals(suggest)){
                    result.setSuggest(SystemConstants.CHECK_FAIL_CODE);
                    result.setCategoryDescription(categoryDescription);
                    result.setKeyWord(keyWord);
                }
            }
            else {
                result.setSuggest(SystemConstants.CHECK_SUCCESS_CODE);
            }
            return result;
        }
    }

    @Data
    class Result_list {
        Detail detail;
        String suggest;
    }

    @Data
    class Detail {
        List<Category_list> category_list;
    }

    @Data
    class Category_list {
        List<String> word_list;
        List<Word_infos> word_infos;
        String category_description;
        Integer confidence;
        String suggest;
    }

    @Data
    class Word_infos {
        List<Integer> positions;
    }

}
