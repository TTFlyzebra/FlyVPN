// IServiceAidl.aidl
package xinwei.com.mpapp.aidl;

// Declare any non-default types here with import statements
import xinwei.com.mpapp.aidl.Network;

interface IServiceAidl {
    /**
     * 打开多流开关
     * @param magip
     * @param magport
     * @param dns
     * @param phone
     * @param pwd
     * @param token
     */
    boolean openMultipleStreams(String magip,in int magport,String dns,in int uid,String phone,String pwd,String token);
    /**
     * 关闭多流开关
     */
    boolean closeMultipleStreams();

    /**
     * 设置网络状态
     * @param network
     */
    void updateNetwork(in List<Network> network);
}
