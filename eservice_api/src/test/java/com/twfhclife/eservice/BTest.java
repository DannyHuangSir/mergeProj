package com.twfhclife.eservice;


/**
 * @author hui.chen
 * @create 2021-06-23
 */
public class BTest {
    public static void main(String[] args) {
        try {
            //
            // byte[] decode = Base64.getDecoder().decode("Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=");
            int i= 1/0;
            System.out.println(i);
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            StackTraceElement  stackTrace = e.getStackTrace()[0];
                System.out.println(stackTrace.getClassName());
                System.out.println(stackTrace.getMethodName());
                System.out.println(stackTrace.getFileName());
                System.out.println(stackTrace.getLineNumber());
            System.out.println(e.getCause());
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getSuppressed());
            System.out.println(e.getClass());
        }

    }
}
