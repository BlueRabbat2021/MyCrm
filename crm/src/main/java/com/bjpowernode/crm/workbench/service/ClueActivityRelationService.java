package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveClueActivityRelationByList(List<ClueActivityRelation> clueActivityRelationList);
    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);
}
