package com.flyzebra.flyvpn.data;

/**
 * ClassName: NetworkLink
 * Description:
 * Author: FlyZebra
 * Email:flycnzebra@gmail.com
 * Date: 19-12-10 上午9:37
 */
public class NetworkLink {
    public String ip = "";
    public String name = "";
    public int type =0;
    public String token = "";
    public String rrt = "";
    public String band ="";
    public boolean isLink = false;

    public NetworkLink(int type){
        this.type = type;
    }

    public void reset(){
        ip = "";
        name = "";
        type =0;
        token = "";
        rrt = "";
        band ="";
        isLink = false;
    }
}
