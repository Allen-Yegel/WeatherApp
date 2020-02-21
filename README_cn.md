### 基于MVVM框架的高德天气APP：

-------
### 开源说明：
- 开源动机：HomeWork，利用私人时间敦促自己持续进行：工作地点&工作时间&工作内容以外的技术学习；
- 开源背景：借助高德天气开放的API平台（作为后台），借助网络开放资源，化简为繁，探索主流的框架开发方式；
- 项目分支：[WeatherDemo](https://github.com/wustcbk/WeatherDemo) 迁移至此，独立维护。

-------
### Screenshots：
![](https://github.com/caobaokang419/WeatherApp/blob/master/screenshots/city_weather_screenshot.jpg)

-------
### 功能点实现说明：
- [高德天气查询](https://lbs.amap.com/api/webservice/guide/api/weatherinfo/)：Get&Post方式可以正常返回查询高德天气数据并UI显示；
- 配置文件下载：借助Xutils3，下载并存储[高德天气城市配置文件](http://a.amap.com/lbs/static/file/AMap_adcode_citycode.xlsx.zip)；
- 配置文件解析：解析存储本地存储高德adcode和城市信息对照表，用于本地天气动态查询api调用；
- 应用权限申请：sdk23后，需动态申请应用权限，实现封装权限动态申请机制；
- 国内消息推送：集成Umeng推送平台，海外移至使用[Cloud Message(云消息)](https://firebase.google.com/docs/remote-config/?hl=zh-CN)
- 云端配置方案：国内TBD，海外移至使用[Remote Config(远程配置)](https://firebase.google.com/docs/cloud-messaging/?hl=zh-CN)
- 公共数据接口：借助ContentProvider，提供天气数据（DB方式）& 配置属性（SharedPreference方式）的数据访问API，供第三方应用访问使用；
- [数据缓存功能](http://www.androiddocs.com/samples/DisplayingBitmaps/src/com.example.android.displayingbitmaps/util/DiskLruCache.html)：1. 实现封装磁盘缓存；2. 实现数据内存缓存；
- [任务管理功能](https://www.jianshu.com/p/4d4634c92253)：1. 封装线程池ThreadPoolExecutor；2.泛型的TaskExecutor机制实现；
- 异步任务管理：AsyncTask HandlerThread WorkManager不同方式，实现不同场景的异步任务需求；
- 天气定期更新：1. 定时启动后台任务；2.轮询查询城市队列：实现定时&批量更新城市天气数据。
- 汉字拼音转换：集成汉字转拼音工具（第三方pinyin4j），实现城市列表拼音搜索功能；
- 数据过滤功能：添加数据过滤演示功能，实现不同场景时，抽象出来的请求+返回+显示流程；


-------
### UI组件实现说明：
- 左右滑动实现：ViewPager实现左右页面滑动切换；
- 下拉上拉刷新：下拉界面，实现天气自动更新；上拉界面，查询显示更多天气相关信息；
- 天气设置功能：PreferenceActivity+PreferenceFragment，替代单独使用PreferenceActivity，实现天气设置界面；
- 天气城市查询：输入城市，自动查找匹配的城市项，用于完成指定城市的天气查询；
- 公共控件封装：自定义实现公共控件CommonUI，用于控件应用内复用；
- [RecyclerView封装](https://www.jianshu.com/p/4f9591291365)：Android推荐控件，优于ListView，统一实现封装不同UI+Data的ItemView；

-------
### JetPack组件实现说明：
框架说明：借助[Android架构组件(Android Architecture Components)：](https://github.com/tangmin1010/appcomponent)可实现MVVM应用框架。 
- [Lifecycle components](https://developer.android.google.cn/topic/libraries/architecture/lifecycle)： 生命周期管理，该组件是其它组件的基础，可由于跟踪UI的（Activity和Fragment）的生命周期
- [ViewModels](https://developer.android.google.cn/topic/libraries/architecture/viewmodel)： 一种可以被观察的以及可以感知生命周期的数据容器。
- [LiveData](https://developer.android.google.cn/topic/libraries/architecture/livedata) ：它是UI，例如Activity、Fragment,与数据之间的桥梁；可以在其内部处理数据业务逻辑,例如从网络层或者数据持久层获取数据、更新数据等。
- [Room](https://developer.android.google.cn/topic/libraries/architecture/room)：一个简单好用的对象映射层；其对SqliteDatabase进行了封装，简化开发者对于数据持久层的开发工作量
- [WorkManager](https://developer.android.google.cn/topic/libraries/architecture/workmanager/) ：可以轻松地指定可延迟的异步任务以及它们应该在何时运行，应只用于处理后台任务。
- [Data-binding](https://developer.android.google.cn/topic/libraries/data-binding//) ：使用xml声明格式(而不是编程方式)将布局中的UI组件绑定到应用程序中的数据源。
- [Paging](https://developer.android.google.cn/topic/libraries/architecture/paging/)：分页库使您能够更容易地在应用程序的RecyclerView中逐步、优雅地加载数据。--TBD
- [Navigation](https://developer.android.google.cn/topic/libraries/architecture/navigation/)：导航是应用程序设计的关键部分。通过导航，您可以设计交互，允许用户在应用程序的不同内容区域之间来回移动。--TBD

-------
### 第三方库实现说明：
- [Retrofit 2.0 使用教程](https://blog.csdn.net/carson_ho/article/details/73732076)：热门的Android网络请求库
- [OkHttp3 使用教程](https://blog.csdn.net/xx326664162/article/details/77714126)：Http第三方库，Retrofit底层通过OkHttp实现网络请求 
- [Gson 使用教程](https://baijiahao.baidu.com/s?id=1607221675455152057&wfr=spider&for=pc)：Google提供，主要用途为序列化Java对象为JSON字符串，或反序列化JSON字符串成Java对象
- [RxJava Android使用教程](https://gank.io/post/560e15be2dca930e00da1083)：[RxAndroid](https://github.com/ReactiveX/RxAndroid)是在[RxJava(响应式编程)](https://github.com/ReactiveX/RxJava)基础上添加了Android线程调度，处理异步网络请求等耗时任务。
- [Xutils3 使用教程](https://github.com/wyouflf/xUtils3)：国内开源框架，封装实现文件下载&断点续传&DB访问&ImageLoader等场景
- [Glide 使用教程](https://www.jianshu.com/p/7ce7b02988a4)：Android推荐的热门图片加载库 备注：三大主流库ImageLoader,Picasso,Glide --TBD
- [DiskLruCache 使用教程](https://github.com/JakeWharton/DiskLruCache)：不同于LruCache，LruCache是将数据缓存到内存中去，而DiskLruCache是外部缓存，例如可以将网络下载的图片永久的缓存到手机外部存储中去，并可以将缓存数据取出来使用；
- [ThreadPoolExecutor 使用教程](https://www.jianshu.com/p/4d4634c92253)：带缓冲和优先级的线程池管理方案，参照xUtils源码框架已提供的ThreadPoolExecutor解决方案；
- [AdMob 使用教程](https://developers.google.com/admob/android/quick-start?hl=zh-CN#import_the_mobile_ads_sdk)：Google提供，实现Google广告载入--国内不集成，移至：[FirebaseApp](https://github.com/caobaokang419/FirebaseApp)
- [Firebase 使用教程](https://developers.google.com/firebase/docs/android/setup?hl=zh-CN)：Google提供，移动应用后台服务端管理--国内不集成，移至：[FirebaseApp](https://github.com/caobaokang419/FirebaseApp)
- [EventBus 消息总线](https://www.jianshu.com/p/7ce7b02988a4)：针对Android优化的发布/订阅事件总线，取代Intent,Handler,BroadCast等消息传递机制；


-------
### 测试&调试&维护：
- 内存泄露：国内可集成LeakCanary，海外使用Firebase的[Crash Report(报错上报)](https://firebase.google.com/docs/crashlytics/?hl=zh-CN);
- 调试框架：Stetho(Facebook开发的工具)，android as3.0后直接集成在IDE中了。--TBD
- 测试框架：Mockito--TBD
- 自动化测试：android 单元测试用例--TBD
- 应用热更新框架接入：[腾讯tinker](https://github.com/Tencent/tinker)--TBD


-------
### Android推荐的开发组件工具集（Jetpack）：
![](https://github.com/caobaokang419/WeatherApp/blob/master/screenshots/Jetpack.png)


-------
### Android推荐的应用程序的基础架构（Android Architecture Components）：

![Android 应用程序的基础架构](https://developer.android.google.cn/topic/libraries/architecture/images/final-architecture.png)



-------
### License
1. 部分业务机制借鉴网络资源，不能用于商业用途，转载请注明出处，谢谢！ 
2. 参考资料：https://github.com/hongyangAndroid/base-diskcache;
