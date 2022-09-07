package com.common.constant;

public class ProductConstant {
    public enum AttrEnum{
        ATTR_TYPE_BASE(1, "base_attr"), ATTR_TYPE_SALE(0, "sale_attr");
        private int code;
        private String message;
        AttrEnum(int code, String message){
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
