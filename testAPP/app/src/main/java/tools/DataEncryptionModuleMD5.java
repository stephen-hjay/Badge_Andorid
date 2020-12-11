package tools;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;



//public class DataEncryptionModuleMD5 {
//    String md5Key = "27d9c492c14a4f77ba0cc5022fc2bcd5";
//    String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0+DT38n56iiy2BOKdwfdnlEAVqGqc9K3BMvSe4AM8oXhQUX5UCsLU83Cm574bc0LbEyQ7ErfWBI8ihh+bcwCPppDFpX6XsfrKdzUUym/sK5RVldEBwnBvidMlgJCJy5/+VCAQftdILLKLq8fH04+3q8VI+1k5dHOJ5Wylu0+wPQIDAQAB";
//    String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCfIe/PMwnWJOGJDIL/GXzarYRvj/0Um4Ef3kLVH6ieAdraqJk4elWh3FHU9Nj+5R3Gnty2NZ61ZPyykKHoHG8/rKntcEPc1GkK94tHrfdmkaOt+ZqSiMwaJ7TmEfNjxUPT+8WfANvK+wSg6yGspgpiCzwdnDeLLRaQ6cqZeUjH6uSONyS3V064GuHXteX3F37s1ifYIDwK8qbW6oYve1WTA7YCFH3ERugxmEubWjqz3vs6rUpvCankUxtSoiUl6UyestZ68bWgFfQmkOlY9rq4NfMes4d+5b0PomsplLe+Ir6I1HO+7rBlpHytk5Ro1H11U4ExIrbosLiuacHx9bdbAgMBAAECggEAMBY1PUuTAV8LuDpLq4KsM/iTOJMuSvfiwRqM35I9heHRnJmuE18EWXEWWV/T4tKheM+wqm7xkqb9pYgHJPjStU8b2mA+YZDeXS4IrJRpWllQONZgWz9zTcQJ6iwqfE+z+27tfOrpgCDyeR4UgvN5177FX71BG5F4bDn5uv7ne2GIbr19AkWQe376MLpCyVn2+1sSuWs3aexPo6DrE8/XPBSDz8yZGItPzKVmr0Ht+qsNiwWeRQJIpcWmbzlL+0NliYiE2jEn6mWqYOTWSiOp9TEaWjFW84BEwcatlt6YpmqUNh56MTS2QODLpkaF/3PjXlL3Sv4gCppVdC+H8fi54QKBgQDqVwwET8AtigM/olUucIaL27/peIpwCFUOBdoqRl4D+qZcs1khtPWtEquXGQtHE0o0az0dZ8DPWdvKAlBu7Ad9iBCDAl4WOhLPxda2U1c9IB4m1NDQPLMSyquEaFJ8XfizoOIFZFPSj3LkvQqL9Oz1iegZGregqozUsGOaQ9EuBwKBgQCt11TA9QbVDdWVFC0er4Ia957grKef/jlefgQEkYmNoLFSbS6M9apeb+HAfxg63xTMutFrZ44rxXxHX/AmyM64A+PBhQMTOjI8pZxgBSHkyig/I2t3IaH8giQMsAm5AoiHTavLsEwSjpeao+32BQVUSaaXAw/JCQG2MwK9nD5XDQKBgFLOS4rCe5Ab8qXrwNnWHVUSY7AmThTkfNmlq7/AebxCN8MOBPLqNN3heQy9ZsTIxjHbqw7W+MTMZePuCWoIsWTkTaFdls2X28nbiNGIhY0t6jmifd73c/ex8gWpr2wO8LDYqsVo/E2tLtYxDqcB9zGUd5VXYYe+fGEzqx551FLFAoGAUIACU0gj3c0GNn6dVjRXvz0jaU8KYGBNGKCqI6NxWxAqjMzaXZP1TL3qgEVaZwiejR+FkoLlpwdQQYz/nDYo47WJZje98M1mLgdSnnRB+bxTXsl8HjKI3HE3WFi8Z9vwdRdWkoAU+hLlyUpYCzDQAvQIHK3iRWnSTRjaEAumIpECgYEAk6cs0z5QH9yYW3i8v25j6u0XI0Epa9EUd0wrsvNjHEF+AR1DOQx7Vy0GvkgITbTC2si0FzDFjzqDpW1/JyQIU7jOJopV2mqNgUhpibpo1+8AtuERIQ5jnHx2ct0r98tdjVnC3ZZ3pYjeDep7PfkZCeqKj5ZNZCFTrCpQ70kGcnE=";
//    String dataEncryption (String params){
////        String param="{\"data\":{name\":\"zhangsan\",\"address\":\"beijing\"}}";
//        Gson gson = new Gson();
//       gson.
//        try {
//            paramsJson.getJSONObject("data");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JSONObject dataJson = paramsJson.getJSONObject("data");
//        String md5Str = SecretUtils.getMd5Str(dataJson.toJSONString());
//        String sign = SecretUtils.getMD5(md5Str , md5Key);
//        dataJson.put("sign", sign);
//        String aesKey= UniqueIdUtils.uuidHex().substring(0, 16);
//        String token = SecretUtils.encryptForRsa(aesKey, publickey);
//        paramsJson.put("token",token);
//        String secretDataStr= SecretUtils.encryptForAes(dataJson.toJSONString(),aesKey);
//        paramsJson.put("data", secretDataStr);
//
//    }
//
//}
