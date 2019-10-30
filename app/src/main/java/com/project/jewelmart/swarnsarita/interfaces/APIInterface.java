package com.project.jewelmart.swarnsarita.interfaces;


import com.project.jewelmart.swarnsarita.models.CartSummary;
import com.project.jewelmart.swarnsarita.models.MarketModel;
import com.project.jewelmart.swarnsarita.models.OrderDetailModel;
import com.project.jewelmart.swarnsarita.models.OrderHistory;
import com.project.jewelmart.swarnsarita.models.Result;
import com.project.jewelmart.swarnsarita.models.WorkerModel;
import com.project.jewelmart.swarnsarita.pojo.AccountDetails;
import com.project.jewelmart.swarnsarita.pojo.Acknowledge;
import com.project.jewelmart.swarnsarita.pojo.Appdata;
import com.project.jewelmart.swarnsarita.pojo.CallEmail;
import com.project.jewelmart.swarnsarita.pojo.Cart;
import com.project.jewelmart.swarnsarita.pojo.CartAcknowledge;
import com.project.jewelmart.swarnsarita.pojo.CartCount;
import com.project.jewelmart.swarnsarita.pojo.CatalogueModel;
import com.project.jewelmart.swarnsarita.pojo.CheckOut;
import com.project.jewelmart.swarnsarita.pojo.Cities;
import com.project.jewelmart.swarnsarita.pojo.ClientId;
import com.project.jewelmart.swarnsarita.pojo.Collection;
import com.project.jewelmart.swarnsarita.pojo.Company;
import com.project.jewelmart.swarnsarita.pojo.Count;
import com.project.jewelmart.swarnsarita.pojo.CountryPojo;
import com.project.jewelmart.swarnsarita.pojo.CustomHistory;
import com.project.jewelmart.swarnsarita.pojo.Filter;
import com.project.jewelmart.swarnsarita.pojo.Home;
import com.project.jewelmart.swarnsarita.pojo.Loginstatus;
import com.project.jewelmart.swarnsarita.pojo.MyCollection;
import com.project.jewelmart.swarnsarita.pojo.NotificationModel;
import com.project.jewelmart.swarnsarita.pojo.OfferModel;
import com.project.jewelmart.swarnsarita.pojo.OtpResponse;
import com.project.jewelmart.swarnsarita.pojo.ProductDetail2;
import com.project.jewelmart.swarnsarita.pojo.Productgridpojo;
import com.project.jewelmart.swarnsarita.pojo.SortList;
import com.project.jewelmart.swarnsarita.pojo.States;
import com.project.jewelmart.swarnsarita.pojo.Status;
import com.project.jewelmart.swarnsarita.pojo.Usertype;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface APIInterface {

    @GET("User_registration/getCountries")
    Call<CountryPojo> doGetCountries();

    @FormUrlEncoded
    @POST("User_registration/getStates?")
    Call<States> doGetState(@Field("country_id") String country_id);

    @FormUrlEncoded
    @POST("User_registration/getCities?")
    Call<Cities> doGetCity(@Field("state_id") String state_id);

    @FormUrlEncoded
    @POST("User_registration/userLogin?")
    Call<Loginstatus> doCheckLogin(@Field("mobile_number") String mobile_number, @Field("password") String password,
                                   @Field("login_type") String login_type);

    @GET("User_registration/getClients")
    Call<ClientId> doGetClientId();

    @GET("All_Parameters")
    Call<Appdata> doAppDate();

    @GET("User_registration/getUsertype")
    Call<Usertype> doGetUserType();

    @GET("User_registration/getcompany")
    Call<Company> doGetCompany();

    @FormUrlEncoded
    @POST("User_registration?")
    Call<Status> doRegistration(@Field("mobile_number") String mobile_number,
                                @Field("username") String username,
                                @Field("password") String password,
                                @Field("email_id") String email_id,
                                @Field("full_name") String full_name,
                                @Field("gender") String gender,
                                @Field("country_id") String country_id,
                                @Field("state_id") String state_id,
                                @Field("city_id") String city_id,
                                @Field("pincode") String pincode,
                                @Field("company_type_id") String company_type_id,
                                @Field("user_status") String user_status,
                                @Field("user_expiry_date") String user_expiry_date,
                                @Field("reg_source") String reg_source,
                                @Field("Gst") String Gst,
                                @Field("shop_name") String shop_name);

    @FormUrlEncoded
    @POST("Home?")
    Call<List<Home>> doGetHome(@Field("image_type") String image_type_value, @Field("user_id") String user_id);

   /* @FormUrlEncoded
    @POST("Products_Count?")
    Call<Count> doGetCount(@Field("table") String table, @Field("mode_type") String mode_type, @Field("collection_id")
            String collection_id, @Field("my_collection_id") String my_collection_id, @Field("user_id") String user_id);
*/

    @FormUrlEncoded
    @POST("Products_Count?")
    Call<Count> doGetCount(@FieldMap Map<String, String> params);

    @GET("User_registration/getHome")
    Call<JSONArray> doGetHome2(@Query("image_type_value") String image_type_value);

    @FormUrlEncoded
    @POST("Collection?")
    Call<List<Collection>> doGetDrawerList(@Field("user_id") String user_id);

   // @FormUrlEncoded
    @POST("Collection/get_collection?")
    Call<List<Collection>> doGetDrawerList();


    @FormUrlEncoded
    @POST("Products_Grid?")
    Call<Productgridpojo> doGetProductGrid2(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Products_Grid?")
    Call<Productgridpojo> doGetProductGrid(@Field("table") String table,
                                           @Field("mode_type") String mode_type,
                                           @Field("collection_id") String collection_id,
                                           @Field("user_id") String user_id,
                                           @Field("record") String record,
                                           @Field("page_no") String page_no,
                                           @Field("my_collection_id") String my_collection_id);

    @FormUrlEncoded
    @POST("Product_Details?")
    Call<List<ProductDetail2>> doGetProductDetails(@Field("table") String table,
                                                   @Field("mode_type") String mode_type,
                                                   @Field("collection_id") String collection_id,
                                                   @Field("my_collection_id") String my_collection_id,
                                                   @Field("user_id") String user_id,
                                                   @Field("product_id") String collection_sku_code);

   // @FormUrlEncoded
    @POST("Product_Details?")
    Call<JSONArray> doGetProductDetails2(@Field("table") String table,
                                            @Field("mode_type") String mode_type,
                                            @Field("collection_id") String collection_id,
                                            @Field("my_collection_id") String my_collection_id,
                                            @Field("user_id") String user_id,
                                            @Field("product_id") String collection_sku_code);

    @FormUrlEncoded
    @POST("My_Collection?")
    Call<List<MyCollection>> doGetCollection(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Product_Cart/add_to_cart?")
    Call<Acknowledge> doAddToCart(@Field("Add_To_Cart") String Add_To_Cart);

    @FormUrlEncoded
    @POST("Product_Cart/add_to_cart_grid?")
    Call<CartAcknowledge> doAddToCartGrid(@Field("product_id") String product_id,
                                          @Field("user_id") String user_id,
                                          @Field("cart_wish_table") String cart_wish_table,
                                          @Field("no_quantity") String no_quantity,
                                          @Field("product_inventory_table") String product_inventory_table);


    @FormUrlEncoded
    @POST("Product_Cart/add_to_cart_grid_add?")
    Call<CartAcknowledge> doAddToCartGridPlus(@Field("product_id") String product_id,
                                          @Field("user_id") String user_id,
                                          @Field("cart_wish_table") String cart_wish_table,
                                          @Field("no_quantity") String no_quantity,
                                          @Field("product_inventory_table") String product_inventory_table,
                                          @Field("plus") String plus
                                              );

    //http://juliejuhi.goldguru.biz/webservices/Product_Cart/add_to_cart_grid_add?
    // product_id=147&user_id=1&cart_wish_table=cart&no_quantity=1
    // &product_inventory_table=product_master&plus=1
    @FormUrlEncoded
    @POST("Product_Cart/cart_value_count?")
    Call<CartCount> CartCount(@Field("user_id") String user_id, @Field("table") String table);

    @FormUrlEncoded
    @POST("Product_Cart/get_cart_data?")
    Call<Cart> getCart(@Field("user_id") String user_id, @Field("table") String table);

    @FormUrlEncoded
    @POST("Product_Cart/delete_cart_product?")
    Call<Acknowledge> deleteFormCart(@Field("user_id") String user_id, @Field("table") String table,
                                     @Field("id") String id);

    @FormUrlEncoded
    @POST("Product_Cart/clear_cart_data?")
    Call<Acknowledge> clearCartData(@Field("user_id") String user_id, @Field("table") String table);

    @FormUrlEncoded
    @POST("Product_Cart/move_products?")
    Call<Acknowledge> moveProduct(@Field("user_id") String user_id, @Field("from_table") String from_table,
                                  @Field("to_table") String to_table, @Field("id") String id);

    @FormUrlEncoded
    @POST("Product_Cart/move_all_products?")
    Call<Acknowledge> moveAllProduct(@Field("user_id") String user_id,
                                     @Field("from_table") String from_table,
                                     @Field("to_table") String to_table);


   // @FormUrlEncoded
    @POST("Order/get_order_field?")
    Call<List<CheckOut>> getOrderField();

    @POST("Sort_Parameter?")
    Call<List<SortList>> getSortList();

    @FormUrlEncoded
    @POST("Order?")
    Call<Acknowledge> placeOrder(@Field("user_id") String user_id,
                                 @Field("which_device") String which_device,
                                 @Field("full_name") String full_name,
                                 @Field("email_id") String email_id,
                                 @Field("mobile_no") String mobile_no,
                                 @Field("gift_id") String gift_id,
                                 @Field("delivery_date") String delivery_date,
                                 @Field("melting_name") String melting_name,
                                 @Field("stamp") String stamp,
                                 @Field("remarks") String remarks);

    @FormUrlEncoded
    @POST("Customized_Order/order_assign?")
    Call<CustomHistory> CustomizedOrder(@Field("user_id") String user_id,
                                        @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST("Filter_Parameter?")
    Call<Filter> getFilter(@Field("user_id") String user_id,
                           @Field("collection_id") String collection_id,
                           @Field("mode_type") String mode_type,
                           @Field("table") String table);

    @FormUrlEncoded
    @POST("Worker/send_gcm?")
    Call<Acknowledge> RegisterToken(@Field("worker_id") String user_id,
                           @Field("gcm_no") String device_token,
                           @Field("type") String usertype);

    @FormUrlEncoded
    @POST("User_registration/send_otp?")
    Call<OtpResponse> getOTP(@Field("mobile_number") String mobile_no);
    //http://ornaments.goldguru.biz/webservices/User_registration/verify_otp?mobile_number=8779068330&otp=5579
    @FormUrlEncoded
    @POST("Screen_Capture?")
    Call<Acknowledge> pushScreenshot(@Field("user_id") String user_id,
                             @Field("image_name") String image_name);

    @POST("Call_Email_Us?")
    Call<List<CallEmail>> getCallEmail();

    @GET("account_details")
    Call<AccountDetails> getAccountDetails();

    @FormUrlEncoded
    @POST("Login_email/user_login?")
    Call<Acknowledge> getSendEmail(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Cart_summary?")
    Call<CartSummary> CartSummary(@Field("user_id") String user_id, @Field("table") String table);


    @FormUrlEncoded
    @POST("Order_history?")
    Call<List<OrderHistory>> OrderHistory(@Field("user_id") String user_id, @Field("table") String table);

    //http://juliejuhi.goldguru.biz/webservices/Order_history/delete_order_history?order_id=14
    @FormUrlEncoded
    @POST("Order_history/delete_order_history?")
    Call<Status> OrderDeleteHistory(@Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("Order_history/re_order?")
    Call<Status> ReOrderHistory(@Field("order_id") String order_id,@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Worker/data_list?")
    Call<WorkerModel> WorkerOrder(@Field("worker_id") String user_id);

    @FormUrlEncoded
    @POST("Worker/data_details?")
    Call<OrderDetailModel> WorkerOrderDetails(@Field("product_id") String user_id,
                                              @Field("worker_id") String worker_id);

    @Multipart
    @POST("Customized_Order?")
    Call<Result> CustomizedOrder(@Query("user_id") String user_id,
                                 @Query("gross_wt") String gross_wt,
                                 @Query("net_wt") String net_wt,
                                 @Query("length") String length,
                                 @Query("melting_id") String melting_id,
                                 @Query("size") String size,
                                 @Query("color") String color,
                                 @Query("remark") String remark,
                                 @Query("delivery_date") String date,
                                 @Part MultipartBody.Part file);

    @GET("Catalouge_slider")
    Call<CatalogueModel> GetCatalogue();

    @GET("Gift_coupans")
    Call<OfferModel> GetOffers();

    @FormUrlEncoded
    @POST("Notification_List?")
    Call<NotificationModel> getNotification(@Field("user_id") String user_id,
                                            @Field("type") String usertype);

    @FormUrlEncoded
    @POST("Notification_List/marketing_data?")
    Call<MarketModel> getMarketing(@Field("marketing_id") String user_id);


}
