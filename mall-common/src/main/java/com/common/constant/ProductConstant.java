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

    public enum ProductStatusEnum{
        NEW_SPU(0, "new"), SPU_UP_SHELF(1, "up_shelf"),SPU_OFF_SHELF(2, "off_shelf") ;
        private int code;
        private String message;

        ProductStatusEnum(int code, String message){
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
