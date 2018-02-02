package me.xunself.bpstatistics;

import java.util.Comparator;

/**
 * Created by XunselF on 2018/2/2.
 */

public class PinyinComparator implements Comparator<Box>{
    @Override
    public int compare(Box box1, Box box2) {
        if (box1.getbLetter().equals("@") || box2.getbLetter().equals("#")){
            return -1;
        }else if (box1.getbLetter().equals("#") || box2.getbLetter().equals("@")){
            return 1;
        }else{
            return box1.getbPinyin().compareTo(box2.getbPinyin());
        }
    }
}
