## **OkDroid**
> 基于okhttp二次封装的网络请求库，支持get请求、post请求、文件上传、文件下载、取消请求，支持Json、Gson、Raw数据类型返回结果回调处理。

## **效果图**
![image](https://github.com/hcs-xph/Okhttp-OkDroid/blob/master/screen/okdroid.gif)

## **参考文献**
1.[https://github.com/ZhaoKaiQiang/OkHttpPlus](https://github.com/ZhaoKaiQiang/OkHttpPlus)<br/>
2.[https://github.com/hongyangAndroid/okhttputils](https://github.com/hongyangAndroid/okhttputils)

## **简介**
**在项目app目录下的build.gradle中添加依赖**
```java
compile 'com.mph.okdroid:okdroid:1.0.0'
```
**1.在项目于入口创建唯一OkDroid实例**
```java
OkDroid okDroid = new OkDroid();
okDroid.setDebug(true);//开启log日志
```
如果需要配置连接超时时间、cookie等可以通过构造函数方式创建OkDroid:
```java
OkHttpClient okHttpClient = new OkHttpClient.Builder()
                  .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                  .readTimeout(10000L, TimeUnit.MILLISECONDS)
                  .build();
OkDroid okDroid = new OkDroid(okHttpClient);
okDroid.setDebug(true);//开启log日志
```
**2.添加参数方式**<br/>

a.可以使用addParam单个参数添加也可以使用params多个一起添加；<br/>
b.可以使用addHeader单个添加请求头也可以使用headers多个一个添加；<br/>

**3.结果回调支持**<br/>

a.Gson 支持，回调传入GsonResHandler\<T> <br/>
b.Json 支持，回调传入JsonResHandler<br/>
c.Raw 支持，回调传入RawResHandler

## **简单使用**
**GET + RawResHandler**
```java
okDroid.get().url("http://192.168.1.109:8080/day10/servlet/ListBookServlet")
                .tag(this)
                .enqueue(new RawResHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.d(GetActivity.class.getSimpleName(),response);
                    }

                    @Override
                    public void onFailed(int statusCode, String errMsg) {

                    }
                });
```
**POST + JsonResHandler**
```java
okDroid.post().url("http://192.168.1.221:9190/api/agency/newGetAgency")
                .tag(this)
                .addParam("type","1")
                .enqueue(new JsonResHandler() {

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        if(response!=null){
                            if(!TextUtils.isEmpty(response.toString())){
                                mTvContent.setText(response.toString());
                            }
                        }
                    }

                    @Override
                    public void onFailed(int statusCode, String errMsg) {

                    }
                });

```
**文件上传**
```java
Map<String, String> map = new HashMap<>();
        map.put("address", "");
        map.put("gender", "");
        map.put("height", "42");
        map.put("weight", "21");
        map.put("realname", "fsd");
        map.put("waist", "43");
        map.put("userid", "285");
        File file = new File("/sdcard/images/20170308_131947.jpg");
        okDroid.upload().url("http://192.168.1.221:9190/api/casuserroleapi/editUserInfo")
                .tag(UploadActivity.this)
                .params(map)
                .addFile("avatarByte", file)
                .enqueue(new JsonResHandler() {

                    @Override
                    public void onProgress(long progress, long total) {
                        Log.d(UploadActivity.class.getSimpleName(), "当前进度-->" + progress + ",总大小-->" + total);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d(UploadActivity.class.getSimpleName(), response.toString());
                    }

                    @Override
                    public void onFailed(int statusCode, String errMsg) {

                    }
                });
```
**下载文件**
```java
okDroid.download().url("http://ivy.pconline.com.cn/click?adid=434690&id=pc.xz.android.zd.tl1.&__uuid=10220796")
                .tag(this)
                .filePath(Environment.getExternalStorageDirectory()+"/okdroid/kyw.apk")
                .enqueue(new IResponseDownloadHandler() {
                    @Override
                    public void onFinish(File downloadFile) {
                        Log.d(DownloadActivity.class.getSimpleName(),"下载成功");
                    }

                    @Override
                    public void onProgress(long progress, long total) {
                        Log.d(DownloadActivity.class.getSimpleName(), "当前进度-->" + progress + ",总大小-->" + total);
                    }

                    @Override
                    public void onFailed(String errMsg) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });

```
**注意**：在以上示例中请求设置的tag都为this，所以取消请求方式为：
```java
okDroid.cancel(this);
```

## **混淆**
```java
#okdroid
-dontwarn com.mph.okdroid.**
-keep class com.mph.okdroid.**{*;}

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}
```
## **About Me**
[http://ivast.cn](http://ivast.cn)