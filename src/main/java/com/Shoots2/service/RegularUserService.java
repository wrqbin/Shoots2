package com.Shoots2.service;

import com.Shoots2.domain.RegularUser;

import java.util.List;
import java.util.Map;

public interface RegularUserService {
    public int selectById(String id);
    public RegularUser selectWithId(String id);
    public int selectByIdPassword(String id, String password);
    public int insert(RegularUser user);
    public int selectByEmail(String email);
    public RegularUser findIdWithEmail(String email);
    public RegularUser selectWithIdAndEmail(String user_id, String email);
    public int updateRegularUserPassword(RegularUser user);
    List<Map<String, Object>> getUserListForBusiness(Integer business_idx, String vip, Integer gender, String age);
    public int listCount(String search_word);
    public List<RegularUser> getUserList(String search_word, int page, int limit);
    public void setCommonUser(int id);
    public void setAdminUser(int id);
    public String getEmail(int id);
    public RegularUser regularUserList(String id);
    public void updateRegularUser(RegularUser user);
    public List<Map<String, Object>> getRegularUser();
    public int allUsers();

    Map<String, Object> getPlayerGenderCount(Integer business_idx);
    public int deleteRegularUser(int idx);
}