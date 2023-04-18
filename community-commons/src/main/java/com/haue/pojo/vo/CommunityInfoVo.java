package com.haue.pojo.vo;

import com.haue.pojo.entity.ActivityContent;
import com.haue.pojo.entity.Community;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityInfoVo {

    private Community community;

    private List<ActivityContentVo> activityContents;

}
