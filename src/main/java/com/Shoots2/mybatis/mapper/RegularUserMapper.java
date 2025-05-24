package com.Shoots2.mybatis.mapper;

import com.Shoots2.domain.RegularUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface RegularUserMapper {
    public RegularUser selectById(String id);

    public int insert(RegularUser user);

    public RegularUser selectByEmail(String email);

    public RegularUser findIdWithEmail(String email);

    public RegularUser selectWithIdAndEmail(HashMap<String, Object> map);

    public int updateRegularUserPassword(RegularUser user);

    List<Map<String, Object>> getUserListForBusiness(Integer business_idx, String vip, Integer gender, String age);

    public int listCount(Map<String, Object> map);

    public List<RegularUser> getUserList(Map<String, Object> map);

    public void setCommonUser(int id);

    public void setAdminUser(int id);

    public String getEmail(int id);

    public RegularUser regularUserList(String id);

    public void updateRegularUser(RegularUser user);

    public List<Map<String, Object>> getUserCount();

    public int allUsers();

    Map<String, Object> getPlayerGenderCount(Integer business_idx);

    public int deleteRegularUser (int idx);
}