package com.gzc.domain.weixin.model.entity.req;

import com.gzc.domain.weixin.model.valobj.ActionNameEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeixinQrCodeReq {

    /**
     * 二维码有效时间（秒），最大2592000，仅临时二维码需要
     */
    private Integer expire_seconds;

    /**
     * 二维码类型：QR_SCENE(临时整型)/QR_STR_SCENE(临时字符串)/QR_LIMIT_SCENE(永久整型)/QR_LIMIT_STR_SCENE(永久字符串)
     */
    private ActionNameEnum action_name;

    /**
     * 二维码详细信息
     */
    private ActionInfo action_info;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActionInfo{

        /**
         * 场景信息
         */
        Scene scene;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Scene{
            /**
             * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
             */
            Integer scene_id;
            /**
             * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
             */
            String scene_str;
        }
    }
}
