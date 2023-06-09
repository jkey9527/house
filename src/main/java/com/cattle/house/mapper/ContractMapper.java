package com.cattle.house.mapper;

import com.cattle.house.bean.ContractBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 合同映射类
 *
 * @author niujie
 * @date 2023/4/21 22:35
 */
@Mapper
public interface ContractMapper {
    
    /**
     * 保存合同
     * @param contractBean contractBean
     * @return void
     * @author niujie
     * @date 2023/4/22
     */
    void saveContract(ContractBean contractBean);

    /**
     * 删除合同
     * @param contractBean contractBean
     * @return void
     * @author niujie
     * @date 2023/4/22
     */
    void deleteContract(ContractBean contractBean);

    /**
     * 通过合同ID查询合同信息
     *
     * @param contractId contractId
     * @return com.cattle.house.bean.ContractBean
     * @author niujie
     * @date 2023/4/22
     */
    ContractBean getContractByContractId(String contractId);

    /**
     * 修改合同信息
     * @param contractBean contractBean
     * @return void
     * @author niujie
     * @date 2023/4/22
     */
    void updateContract(ContractBean contractBean);

    /**
     * 查询合同列表
     *
     * @param contractBean contract
     * @return java.util.List<com.cattle.house.bean.ContractBean>
     * @author niujie
     * @date 2023/4/22
     */
    List<ContractBean> getContractList(ContractBean contractBean);

    /**
     * 查询所有合同下拉信息
     *
     * @return java.util.List<com.cattle.house.bean.ContractBean>
     * @author niujie
     * @date 2023/5/14
     */
    List<ContractBean> getContractOptions();

    /**
     * 修改合同状态
     *
     * @param contractBean contractBean
     * @return void
     * @author niujie
     * @date 2023/5/27
     */
    void updateContractState(ContractBean contractBean);

    /**
     * 通过合同编号查询合同信息
     * @param contractNo contractNo
     * @return com.cattle.house.bean.ContractBean
     * @author niujie
     * @date 2023/6/1
     */
    ContractBean getContractByNo(String contractNo);
}
