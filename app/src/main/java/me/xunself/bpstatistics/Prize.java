package me.xunself.bpstatistics;

import org.litepal.crud.DataSupport;

/**
 * Created by XunselF on 2018/1/23.
 */

public class Prize extends DataSupport{
    private String prizeName;
    public Prize(String prizeName){
        this.prizeName = prizeName;
    }

    public String getPrizeName() {
        return prizeName;
    }
}
