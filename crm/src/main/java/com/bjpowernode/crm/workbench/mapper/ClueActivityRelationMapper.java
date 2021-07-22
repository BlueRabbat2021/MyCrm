package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Jun 04 15:49:40 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Jun 04 15:49:40 CST 2020
     */
    int insert(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Jun 04 15:49:40 CST 2020
     */
    int insertSelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Jun 04 15:49:40 CST 2020
     */
    ClueActivityRelation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Jun 04 15:49:40 CST 2020
     */
    int updateByPrimaryKeySelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Jun 04 15:49:40 CST 2020
     */
    int updateByPrimaryKey(ClueActivityRelation record);

    /**
    * 批量保存创建的线索和市场活动的关联关系
    */
    int insertClueActivityRelationByList(List<ClueActivityRelation> relationList);

    /**
    * 根据clueId和activityId删除线索和市场活动的关联关系
    */
    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);

    /**
    * 根据clueId查询线索和市场活动的关联关系
    */

    List<ClueActivityRelation> selectClueActivityRelationByClueId(String clueId);

    /**
    * 根据clueId删除线索和市场活动的关联关系
    */
    int deleteClueActivityRelationByClueId(String clueId);
}