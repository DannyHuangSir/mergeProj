package com.twfhclife.adm.service;

import com.twfhclife.adm.model.JdBatchSchedulVO;
import com.twfhclife.adm.model.JdUserVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @auther lihao
 */
public interface IJdICBatchService {
    int addUsers(JdUserVo jdUserVo);
    int updateUsers(JdUserVo jdUserVo);


    List<Map<String, Object>> getICs(JdBatchSchedulVO vo);

    int countICs(JdBatchSchedulVO vo);


    int deleteIC(JdUserVo jdUserVo);

    void upLoadFile(MultipartFile file);

    void workICFile() throws IOException;
}
