package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranRemarkMapper tranRemarkMapper;
    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveConvert(Map<String, Object> map) {
        User user = (User) map.get("sessionUser");
        String isCreateTran = (String) map.get("isCreateTran");
        // 获取线索id，根据线索id获取线索对象（线索对象中封装了线索信息）
        String clueId = (String) map.get("clueId");
        Clue clue = clueMapper.selectClueForDetailById(clueId);
        // 通过线索信息提取客户信息，保存客户表
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setOwner(user.getId());
        customer.setName(clue.getCompany());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formatDateTime(new Date()));
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setDescription(clue.getDescription());
        customer.setAddress(clue.getAddress());

        customerMapper.insertCustomer(customer);

        // 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtils.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullName(clue.getFullName());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formatDateTime(new Date()));
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        contactsMapper.insertContacts(contacts);

        // 线索备注转为客户备注及联系人备注
        List<ClueRemark> clueRemarks = clueRemarkMapper.selectClueRemarkByClueId(clueId);
        if(clueRemarks != null && clueRemarks.size() > 0){
            // 客户备注
            CustomerRemark cur = null;
            // 联系人备注
            ContactsRemark cor = null;
            // 定义集合存放客户备注对象和联系人备注对象
            List<CustomerRemark> curList = new ArrayList<>();
            List<ContactsRemark> corList = new ArrayList<>();
            // 循环取出线索备注
            for(ClueRemark clueRemark:clueRemarks){
                cur = new CustomerRemark();
                cur.setId(UUIDUtils.getUUID());
                cur.setNoteContent(clueRemark.getNoteContent());
                cur.setCreateBy(clueRemark.getCreateBy());
                // 这里的时间以业务为准
                cur.setCreateTime(clueRemark.getCreateTime());
                cur.setCustomerId(customer.getId());

                curList.add(cur);


                cor = new ContactsRemark();
                cor.setId(UUIDUtils.getUUID());
                cor.setNoteContent(clueRemark.getNoteContent());
                cor.setCreateBy(clueRemark.getCreateBy());
                // 这里的时间以业务为准
                cor.setCreateTime(clueRemark.getCreateTime());
                cor.setContactsId(contacts.getId());

                corList.add(cor);
            }

            // 批量插入
            customerRemarkMapper.insertCustomerRemarkByList(curList);
            contactsRemarkMapper.insertContactsRemarkByList(corList);

            // 将线索和市场活动关系转换为联系人和市场活动关系
            List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
            if(carList != null && carList.size() > 0){
                // 联系人和市场活动对象
                ContactsActivityRelation coar = null;
                List<ContactsActivityRelation> coarList = new ArrayList<>();

                for(ClueActivityRelation car:carList){
                    coar = new ContactsActivityRelation();
                    coar.setId(UUIDUtils.getUUID());
                    coar.setContactsId(contacts.getId());
                    coar.setActivityId(car.getActivityId());
                    coarList.add(coar);
                }
               contactsActivityRelationMapper.insertContactsActivityRelationByList(coarList);
            }

            // 如果有创建交易需求，创建一条交易,并将线索备注转换为交易备注
            if("true".equals(isCreateTran)){
                // 交易对象
                Tran tran = new Tran();
                tran.setId(UUIDUtils.getUUID());
                tran.setOwner(user.getId());
                tran.setMoney((String)map.get("amountOfMoney"));
                tran.setName((String)map.get("tradeName"));
                tran.setExpectedDate((String)map.get("expectedClosingDate"));
                tran.setCustomerId(customer.getId());
                tran.setStage((String)map.get("stage"));

                tranMapper.insert(tran);
                if(clueRemarks!=null&&clueRemarks.size()>0){
                    TranRemark tr = null;
                    List<TranRemark> tranList = new ArrayList<>();
                    for(ClueRemark cr : clueRemarks){
                        tr = new TranRemark();
                        tr.setId(UUIDUtils.getUUID());
                        tr.setNoteContent(cr.getNoteContent());
                        tr.setCreateBy(cr.getCreateBy());
                        tr.setCreateTime(cr.getCreateTime());
                        tr.setTranId(tran.getId());

                        tranList.add(tr);
                    }
                    tranRemarkMapper.insertTranRemarkByList(tranList);
                }
            }

            // 删除线索备注
            clueRemarkMapper.deleteClueRemarkByClueId(clueId);
            // 删除线索和市场活动关系
            clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
            // 删除线索
            clueMapper.deleteClueById(clueId);
        }
    }
}
