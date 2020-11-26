package com.tony.ced.bean;

import java.io.Serializable;

/**
 * @ProjectName: tony_ai
 * @Package: com.tony.ced.bean
 * @ClassName: MainItem
 * @Description: 主页实体bean
 * @Author: wei.yang
 * @CreateDate: 2020/11/26 10:58
 * @UpdateUser: 更新者：wei.yang
 * @UpdateDate: 2020/11/26 10:58
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class MainItem implements Serializable {
    private String title;
    private String className;

    public MainItem(String title, String className) {
        this.title = title;
        this.className = className;
    }

    public MainItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
