package org.example.springbootdemo.midmapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.springbootdemo.model.midexample.VipUserExample;
import org.example.springbootdemo.model.midpo.VipUser;

@Mapper
public interface VipUserMapper {
    long countByExample(VipUserExample example);

    int deleteByExample(VipUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(VipUser record);

    int insertSelective(VipUser record);

    VipUser selectOneByExample(VipUserExample example);

    List<VipUser> selectByExample(VipUserExample example);

    VipUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") VipUser record, @Param("example") VipUserExample example);

    int updateByExample(@Param("record") VipUser record, @Param("example") VipUserExample example);

    int updateByPrimaryKeySelective(VipUser record);

    int updateByPrimaryKey(VipUser record);
}