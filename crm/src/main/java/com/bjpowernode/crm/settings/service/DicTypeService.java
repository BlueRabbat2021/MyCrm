package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicType;

import java.util.List;

//query, save,delete,saveedit
public interface DicTypeService {

    List<DicType> queryAllDicTypes();

    DicType queryDicTypeByCode(String code);

    // 新建
    int saveDicType(DicType dicType);

    // 更新
    int saveEdit(DicType dicType);

    int deleteDicTypeByCodes(String[] codes);
}
