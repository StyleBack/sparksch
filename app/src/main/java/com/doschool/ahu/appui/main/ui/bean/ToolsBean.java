package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2019/1/12.
 */
public class ToolsBean extends BaseModel implements Serializable {
    private static final long serialVersionUID = -9047851408162057012L;

    private List<Tools> data;

    public List<Tools> getData() {
        return data;
    }

    public void setData(List<Tools> data) {
        this.data = data;
    }

    public class Tools{
        private ToolsConfigTypeDO toolsConfigTypeDO;
        private List<ToolsConfigDOList> toolsConfigDOList;

        public ToolsConfigTypeDO getToolsConfigTypeDO() {
            return toolsConfigTypeDO;
        }

        public void setToolsConfigTypeDO(ToolsConfigTypeDO toolsConfigTypeDO) {
            this.toolsConfigTypeDO = toolsConfigTypeDO;
        }

        public List<ToolsConfigDOList> getToolsConfigDOList() {
            return toolsConfigDOList;
        }

        public void setToolsConfigDOList(List<ToolsConfigDOList> toolsConfigDOList) {
            this.toolsConfigDOList = toolsConfigDOList;
        }
    }
}
