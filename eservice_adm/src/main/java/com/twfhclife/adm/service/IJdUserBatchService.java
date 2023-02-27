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
public interface IJdUserBatchService {
    int addUsers(JdUserVo jdUserVo);
    int updateUsers(JdUserVo jdUserVo);


    List<Map<String, Object>> getUsers(JdBatchSchedulVO vo);

    int countUsers(JdBatchSchedulVO vo);


    void upLoadFile(MultipartFile file);



    void workFile() throws IOException;
}
