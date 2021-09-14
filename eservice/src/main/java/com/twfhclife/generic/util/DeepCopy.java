package com.twfhclife.generic.util;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.FastByteArrayOutputStream;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of objects.
 * Objects are first serialized and then deserialized. Error checking is fairly
 * minimal in this implementation. If an object is encountered that cannot be
 * serialized (or that references an object that cannot be serialized) an error
 * is printed to System.err and null is returned. Depending on your specific
 * application, it might make more sense to have copy(...) re-throw the
 * exception.
 * 
 */
public class DeepCopy {

	private static final Logger logger = LogManager.getLogger(DeepCopy.class);
	
    /**
     * Returns a copy of the object, or null if the object cannot be serialized.
     */
    public static Object copy(Object orig) throws Exception {
        Object obj = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        FastByteArrayOutputStream fbos = null;
        try {
            // Write the object out to a byte array
            fbos = new FastByteArrayOutputStream();
            out = new ObjectOutputStream(fbos);
            out.writeObject(orig);

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in.
            in = new ObjectInputStream(fbos.getInputStream());
            obj = in.readObject();
        } catch (Exception e) {
        	logger.error("DeepCopy.copy: {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (fbos != null) {
            	fbos.close();
            	fbos = null;
            }
            if (out != null) {
            	out.close();
            	out = null;
            }
            if (in != null) {
                in.close();
                in = null;
            }
        }

        return obj;
    }

}