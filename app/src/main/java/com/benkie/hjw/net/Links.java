package com.benkie.hjw.net;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/28.
 */

public interface Links {
    /**
     * 只是个测试接口 没什么用 暂时先不要删除
     *
     * @return
     */
    @POST("/yetdwell/skill/skillTypeList.do")
    Call<ResponseBody> measurement();

    /**
     * 开通技术服务年费
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/skill/wechatOrderPay.do")
    Call<ResponseBody> wxpayByOpen(@Query("userInfoId") int userInfoId);

    /**
     * 技术服务续费
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/skill/wechatRenewPay.do")
    Call<ResponseBody> wxpayByRenew(@Query("userInfoId") int userInfoId);

    /**
     * 发布项目支付
     *
     * @param userItemId
     * @return
     */
    @POST("/yetdwell/item/itemPublishPay.do")
    Call<ResponseBody> wxpayByProduct(@Query("userItemId") int userItemId);

    /**
     * 项目续费支付
     *
     * @param userItemId
     * @return
     */
    @POST("/yetdwell/item/itemRenewPay.do")
    Call<ResponseBody> wxpayByProductRenew(@Query("userItemId") int userItemId);

    /**
     * 推荐支付
     *
     * @param userItemId
     * @return
     */
    @POST("/yetdwell/item/itemRecommendPay.do")
    Call<ResponseBody> wxpayByProductRecomm(@Query("userItemId") int userItemId);

    //推荐支付信息接口
    @POST("/yetdwell/item/recommendInfo.do")
    Call<ResponseBody> recommendInfo(@Query("userItemId") int userItemId);

    /**
     * 获取系统消息
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/user/message.do")
    Call<ResponseBody> getMsg(@Query("userInfoId") int userInfoId);

    /**
     * 发送验证码 注册用
     *
     * @param mobile
     * @return
     */
    @POST("/yetdwell/userInfo/registerVerify.do")
    Call<ResponseBody> registerVerify(@Query("mobile") String mobile);

    /**
     * 发送验证码 其他验证码
     *
     * @param mobile
     * @return
     */
    @POST("/yetdwell/userInfo/otherVerify.do")
    Call<ResponseBody> otherVerify(@Query("mobile") String mobile);

    /**
     * 手机注册账号
     *
     * @param mobile
     * @param loginPwd
     * @return
     */
    @POST("/yetdwell/userInfo/register.do")
    Call<ResponseBody> register(@Query("mobile") String mobile, @Query("loginPwd") String loginPwd);

    @POST("/yetdwell/userInfo/forgetPwd.do")
    Call<ResponseBody> forgetPwd(@Query("mobile") String mobile, @Query("loginPwd") String loginPwd);


    /**
     * 登陆
     *
     * @param mobile
     * @param loginPwd
     * @return
     */
    @POST("/yetdwell/userInfo/login.do")
    Call<ResponseBody> login(@Query("mobile") String mobile, @Query("loginPwd") String loginPwd);

    /**
     * 获取用户信息
     *
     * @param token
     * @return
     */
    @POST("/yetdwell/userInfo/info.do")
    Call<ResponseBody> getUserInfo(@Query("token") String token);

    /**
     * 修改密码
     *
     * @return
     */
    @POST("/yetdwell/userInfo/update.do")
    Call<ResponseBody> updatePwd(@Query("token") String token,
                                 @Query("loginPwd") String loginPwd,
                                 @Query("oldPwd") String oldPwd);

    /**
     * 投诉
     *
     * @param parts
     * @return
     */
    @Multipart
    @POST("/yetdwell/item/complaintItem.do")
    Call<ResponseBody> complaintItem(@Part List<MultipartBody.Part> parts);

    /**
     * 获取会员状态
     * 去发布技术服务，查看是否在有效期内部
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/skill/toPublis.do")
    Call<ResponseBody> getMemberInfo(@Query("userInfoId") int userInfoId);

    /**
     * 开通会员
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/skill/toPublis.do")
    Call<ResponseBody> openSkill(@Query("userInfoId") int userInfoId);

    /**
     * 会员续费
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/skill/toRenew.do")
    Call<ResponseBody> renewSkill(@Query("userInfoId") int userInfoId);


    /**
     * 技术服务——集赞分享成功调用此接口
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/skill/sharePraise.do")
    Call<ResponseBody> shareSkillPraise(@Query("userInfoId") int userInfoId);

    /**
     * 发布推荐——集赞分享成功调用此接口
     *
     * @param itemId
     * @return
     */
    @POST("/yetdwell/item/sharePraise.do")
    Call<ResponseBody> shareItemPraise(@Query("itemId") int itemId);

    /**
     * 保存用户发布技能的地址，实力，服务人数的信息
     *
     * @param userInfoId
     * @param address
     * @param describe
     * @param lng
     * @param lat
     * @param serveType
     * @return
     */
    @POST("/yetdwell/skill/addUserSkill.do")
    Call<ResponseBody> saveSkillInfo(@Query("userInfoId") int userInfoId,
                                     @Query("address") String address,
                                     @Query("describe") String describe,
                                     @Query("lng") double lng,
                                     @Query("lat") double lat,
                                     @Query("serveType") int serveType);

    /**
     * 保存用户技能服务的基本信息
     *
     * @param userInfoId
     * @param address
     * @param describes
     * @param lng
     * @param lat
     * @param serveType
     * @return
     */
    @POST("/yetdwell/skill/saveUserSkill.do")
    Call<ResponseBody> saveUserSkill(@Query("userInfoId") int userInfoId,
                                     @Query("address") String address,
                                     @Query("describes") String describes,
                                     @Query("lng") double lng,
                                     @Query("lat") double lat,
                                     @Query("serveType") int serveType);

    @POST("/yetdwell/skill/edtiUserSkill.do")
    Call<ResponseBody> edtiUserSkill(@Query("userInfoId") int userInfoId,
                                     @Query("address") String address,
                                     @Query("describes") String describes,
                                     @Query("lng") double lng,
                                     @Query("lat") double lat,
                                     @Query("serveType") int serveType);

    /**
     * 获取服务技能列表
     *
     * @return
     */
    @POST("/yetdwell/skill/userSkill.do")
    Call<ResponseBody> skillUser(@Query("skillTypeId") int skillTypeId,
                                 @Query("lat") double lat,
                                 @Query("lng") double lng,
                                 @Query("pageIndex") int pageIndex);

    /**
     * 获取技能列表
     *
     * @return
     */
    @POST("/yetdwell/skill/skillTypeList.do")
    Call<ResponseBody> allSkill();

    @POST("/yetdwell/skill/addSkillTypeList.do")
    Call<ResponseBody> addAllSkill();

    /**
     * 获取我的技能和 地址 实力 服务人数
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/skill/userSkillImg.do")
    Call<ResponseBody> getUserSkill(@Query("userInfoId") int userInfoId);

    /**
     * 获取用户技能列表
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/skill/myUserSkill.do")
    Call<ResponseBody> myUserSkill(@Query("userInfoId") int userInfoId);

    /**
     * 查询技能服务基本信息
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/skill/mySkillInfo.do")
    Call<ResponseBody> mySkillInfo(@Query("userInfoId") int userInfoId);

    /**
     * 获取技能详情
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/skill/userSkillInfo.do")
    Call<ResponseBody> userSkillInfo(@Query("userInfoId") int userInfoId, @Query("skillId") int skillId);

    /**
     * 技能点赞
     *
     * @param userInfoId
     * @param userSkillId
     * @return
     */
    @POST("/yetdwell/skillPoint/add.do")
    Call<ResponseBody> addPoint(@Query("userInfoId") int userInfoId, @Query("userSkillId") int userSkillId);

    /**
     * 技能取消赞
     *
     * @param userInfoId
     * @param userSkillId
     * @return
     */
    @POST("/yetdwell/skillPoint/delPoint.do")
    Call<ResponseBody> delPoint(@Query("userInfoId") int userInfoId, @Query("userSkillId") int userSkillId);


    /**
     * 删除技术图片
     *
     * @param ids
     * @return
     */
    @POST("/yetdwell/skill/delSkillImg.do")
    Call<ResponseBody> delSkillImg(@Query("ids") String ids);

    /**
     * 删除某个技能
     *
     * @return
     */
    @POST("/yetdwell/skill/delSkill.do")
    Call<ResponseBody> delSkill(@Query("skillInfoId") int skillInfoId);

    /**
     * 修改技术|发布技术|取消发布技术
     *
     * @param skillInfoId
     * @param status
     * @param certificate
     * @return
     */
    @POST("/yetdwell/skill/updateSkill.do")
    Call<ResponseBody> updateSkill(@Query("skillInfoId") int skillInfoId,
                                   @Query("status") int status,
                                   @Query("certificate") int certificate);

    /**
     * 上传技术图片
     *
     * @param parts
     * @return
     */
    @Multipart
    @POST("/yetdwell/skill/addSkillImg.do")
    Call<ResponseBody> addSkillImg(@Part List<MultipartBody.Part> parts);

    /**
     * 添加技术
     *
     * @param userInfoId
     * @param userSkillId
     * @param skillTypeId
     * @return
     */
    @POST("/yetdwell/skill/addSkill.do")
    Call<ResponseBody> addSkill(@Query("userInfoId") int userInfoId,
                                @Query("userSkillId") int userSkillId,
                                @Query("skillTypeId") int skillTypeId);

    /**
     * 添加项目
     *
     * @param name
     * @param city
     * @param address
     * @param finishDate
     * @param itemTypeId
     * @return
     */
    @POST("/yetdwell/item/addItem.do")
    Call<ResponseBody> addItem(@Query("userInfoId") int userInfoId,
                               @Query("name") String name,
                               @Query("city") String city,
                               @Query("address") String address,
                               @Query("finishDate") String finishDate,
                               @Query("itemTypeId") int itemTypeId);

    @POST("/yetdwell/item/updateItem.do")
    Call<ResponseBody> updateItem(@Query("itemId") int itemId,
                                  @Query("name") String name,
                                  @Query("city") String city,
                                  @Query("address") String address,
                                  @Query("finishDate") String finishDate,
                                  @Query("itemTypeId") int itemTypeId);

    /**
     * 项目点赞
     *
     * @param userInfoId
     * @param userItemId
     * @return
     */
    @POST("/yetdwell/itemPoint/add.do")
    Call<ResponseBody> addItemPoint(@Query("userInfoId") int userInfoId,
                                    @Query("userItemId") int userItemId);

    /**
     * 项目取消
     *
     * @param userInfoId
     * @param userItemId
     * @return
     */
    @POST("/yetdwell/itemPoint/delPoint.do")
    Call<ResponseBody> delItemPoint(@Query("userInfoId") int userInfoId,
                                    @Query("userItemId") int userItemId);


    /**
     * 所有的 项目分类
     *
     * @return
     */
    @POST("/yetdwell/item/itemType.do")
    Call<ResponseBody> allProductType();

    @POST("/yetdwell/item/searchItemType.do")
    Call<ResponseBody> searchItemType(@Query("name") String name,
                                      @Query("city") String city);
    /**
     * 我喜欢的项目分类
     *
     * @return
     */
    @POST("/yetdwell/item/userItemCategory.do")
    Call<ResponseBody> userItemCategory(@Query("userInfoId") int userInfoId);

    /**
     * 更新我喜欢的项目分类
     *
     * @return
     */
    @POST("/yetdwell/item/updateCategory.do")
    Call<ResponseBody> updateCategory(@Query("userInfoId") int userInfoId,
                                      @Query("itemTypeStr") String itemTypeStr);

    /**
     * 添加我喜欢的项目分类
     *
     * @return
     */
    @POST("/yetdwell/item/addItemType.do")
    Call<ResponseBody> addItemType();

    /**
     * 获取所有服务分类
     * <p>
     * Integer itemTypeId,String beginDate,String endDate,String ctiy,String itemName
     *
     * @return
     */
    @POST("/yetdwell/user/firstService.do")
    Call<ResponseBody> allserviceList(@Query("itemTypeId") Integer itemTypeId,
                                      @Query("ctiy") String ctiy,
                                      @Query("itemName") String itemName);

    @POST("/yetdwell/user/homeFirstService.do")
    Call<ResponseBody> homeServiceList(@Query("itemTypeId") Integer itemTypeId,
                                       @Query("beginDate") String beginDate,
                                       @Query("endDate") String endDate,
                                       @Query("ctiy") String ctiy);

    @POST("/yetdwell/user/addFirstService.do")
    Call<ResponseBody> addAllserviceList();

    /**
     * 获取用户得所有项目
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/item/itemList.do")
    Call<ResponseBody> allProduct(@Query("userInfoId") int userInfoId);


    /**
     * 我的收藏——项目分类
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/user/myItemCollectionType.do")
    Call<ResponseBody> itemCollectionType(@Query("userInfoId") int userInfoId);//;

    /**
     * 我的收藏 - 根据项目分类查询项目
     *
     * @param userInfoId
     * @param itemTypeId
     * @param pageIndex
     * @return
     */
    @POST("/yetdwell/user/myItemCollection.do")
    Call<ResponseBody> myItemCollection(@Query("userInfoId") int userInfoId,
                                        @Query("itemTypeId") int itemTypeId,
                                        @Query("pageIndex") int pageIndex);


    /**
     * 我的收藏——技能
     *
     * @param userInfoId
     * @return
     */
    @POST("/yetdwell/user/skillCollection.do")
    Call<ResponseBody> skillCollection(@Query("userInfoId") int userInfoId, @Query("timestamp") String timestamp);


    /**
     * 添加项目图片
     *
     * @param parts
     * @return
     */
    @Multipart
    @POST("/yetdwell/item/addItemImg.do")
    Call<ResponseBody> addItemImg(@Part List<MultipartBody.Part> parts);

    /**
     * 编辑项目图片
     *
     * @param parts
     * @return
     */
    @Multipart
    @POST("/yetdwell/item/updateItemImg.do")
    Call<ResponseBody> updateItemImg(@Part List<MultipartBody.Part> parts);

    /**
     * 上传头像
     *
     * @param parts
     * @return
     */
    @Multipart
    @POST("/yetdwell/userInfo/updateInfo.do")
    Call<ResponseBody> updataUserImg(@Part List<MultipartBody.Part> parts);

    /**
     * 更改用户名
     *
     * @param
     * @return
     */
    @POST("/yetdwell/userInfo/updateInfo.do")
    Call<ResponseBody> updateUserName(@Query("token") String token,
                                      @Query("name") String name);

    /**
     * 更改用户手机号
     *
     * @param
     * @return
     */
    @POST("/yetdwell/userInfo/updateInfo.do")
    Call<ResponseBody> updateUserPhone(@Query("token") String token, @Query("newMobile") String newMobile);


    /**
     * 删除项目图片
     *
     * @param itemId
     * @return
     */
    @POST("/yetdwell/item/delItemImg.do")
    Call<ResponseBody> delItemImg(@Query("itemImgId") int itemId);

    /**
     * 项目详情
     *
     * @param itemId
     * @return
     */
    @POST("/yetdwell/item/itemInfo.do")
    Call<ResponseBody> productInfo(@Query("userInfoId") int userInfoId, @Query("itemId") int itemId);

    /**
     * 收藏项目
     *
     * @param userInfoId
     * @param itemId
     * @param flag
     * @return
     */
    @POST("/yetdwell/item/itemCollect.do")
    Call<ResponseBody> itemCollect(@Query("userInfoId") int userInfoId,
                                   @Query("itemId") int itemId,
                                   @Query("flag") int flag);

    /**
     * 收藏技术服务人员
     *
     * @param userInfoId
     * @param cluserInfoId 被收藏的人用户id
     * @param flag
     * @return
     */
    @POST("/yetdwell/skill/skillCollect.do")
    Call<ResponseBody> skillCollect(@Query("userInfoId") int userInfoId,
                                    @Query("cluserInfoId") int cluserInfoId,
                                    @Query("flag") int flag);


    /**
     * 项目续费
     *
     * @param itemId
     * @return
     */
    @POST("/yetdwell/item/toRenew.do")
    Call<ResponseBody> toRenew(@Query("itemId") int itemId);

    /**
     * 发布项目
     *
     * @param itemId
     * @return
     */
    @POST("/yetdwell/item/toPublis.do")
    Call<ResponseBody> toPublis(@Query("itemId") int itemId);

    /**
     * 取消发布
     *
     * @param itemTypeId
     * @return
     */
    @POST("/yetdwell/item/unPublisItem.do")
    Call<ResponseBody> unPublisItem(@Query("itemId") int itemTypeId);

    /**
     * 删除项目
     *
     * @param itemTypeId
     * @return
     */
    @POST("/yetdwell/item/delItem.do")
    Call<ResponseBody> delItem(@Query("itemId") int itemTypeId);


    /**
     * 获取完成项目
     *
     * @param itemTypeId
     * @param city
     * @param name
     * @param startFinishDate
     * @param endFinishDate
     * @return
     */
    @POST("/yetdwell/item/outAllItemList.do")
    Call<ResponseBody> allOutProduct(@Query("itemTypeId") int itemTypeId,
                                     @Query("city") String city,
                                     @Query("name") String name,
                                     @Query("startFinishDate") String startFinishDate,
                                     @Query("endFinishDate") String endFinishDate,
                                     @Query("twoserviceId") int twoserviceId,
                                     @Query("pageIndex") int pageIndex
    );


    /**
     * 获取完成项目
     *
     * @param itemTypeId
     * @param city
     * @param name
     * @param startFinishDate
     * @param endFinishDate
     * @return
     */
    @POST("/yetdwell/item/searchAllItemList.do")
    Call<ResponseBody> searchAllItemList(@Query("itemTypeId") int itemTypeId,
                                         @Query("city") String city,
                                         @Query("name") String name,
                                         @Query("startFinishDate") String startFinishDate,
                                         @Query("endFinishDate") String endFinishDate,
                                         @Query("twoserviceId") int twoserviceId,
                                         @Query("timestamp") String timestamp
    );


    /**
     * 关于我们
     *
     * @return
     */
    @POST("/yetdwell/user/aboutWe.do")
    Call<ResponseBody> aboutWe();

    /**
     * 使用帮助
     *
     * @return
     */
    @POST("/yetdwell/user/explain.do")
    Call<ResponseBody> explain();

    /**
     * 用户反馈
     *
     * @param userInfoId
     * @param content
     * @return
     */
    @POST("/yetdwell/user/feedback.do")
    Call<ResponseBody> feedback(@Query("userInfoId") int userInfoId,
                                @Query("content") String content);


    /**
     * 删除喜欢 和添加喜欢 暂时因为动画无法做成时时提交数据功能
     * @param id
     * @return
     *
     *  @POST("/yetdwell/item/delCategory.do") Call<ResponseBody> delCategory(@Query("id") int id);

     @POST("/yetdwell/item/addCategory.do") Call<ResponseBody> addCategory(@Query("userInfoId") int userInfoId,
     @Query("itemTypeId") int itemTypeId);
     *
     *
     *
     */


    /**
     * 版本检查
     *
     */
    @POST("/yetdwell/refreshInfo/newInfo.do")
    Call<ResponseBody> upDataVersion();


}
