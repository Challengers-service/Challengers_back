package com.challengers.testtool;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class UploadSupporter {
    public static MockMultipartHttpServletRequestBuilder uploadMockSupport(
            MockMultipartHttpServletRequestBuilder builder, Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String fieldName = field.getName();
                Object fieldValue = Optional.ofNullable(field.get(obj)).orElseThrow();

                if (fieldValue instanceof MockMultipartFile) {
                    MockMultipartFile file = (MockMultipartFile) fieldValue;
                    builder = builder.file(fieldName, file.getBytes());
                } else if (fieldValue instanceof List) {
                    if (!((List<?>) fieldValue).isEmpty()) {
                        Object fieldObj = Optional.ofNullable(((List<?>) fieldValue).get(0))
                                .orElseThrow();
                        if (fieldObj instanceof MockMultipartFile) {
                            for (MockMultipartFile file : ((List<MockMultipartFile>) fieldValue)) {
                                builder = builder.file(fieldName, file.getBytes());
                            }
                        } else if(fieldObj instanceof String){
                            StringBuilder a = new StringBuilder();
                            for(String str : ((List<String>)fieldValue)) {
                                a.append(str).append(",");
                            }
                            builder = (MockMultipartHttpServletRequestBuilder) builder.param(fieldName,a.substring(0,a.length()-1));
                        }
                    }
                } else {
                    builder = (MockMultipartHttpServletRequestBuilder) builder.param(fieldName,
                            String.valueOf(fieldValue));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder;
    }
}
