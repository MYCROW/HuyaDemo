# HuyaDemo
RecycleView综合学习-仿照虎牙直播首页的应用

版本：V1.0
已实现:
1.顶部导航栏：
  自定义的CategoryTabStrip(继承HorizontalScrollView)
2.主界面对应切换
  TopPagerAdapter(继承FragmentPagerAdapter)+ViewPager
3."推荐"主界面
  BlankFragment(继承Fragement)
4.主要内容集中实现于"推荐"主界面
  SuperEasyRefreshLayout 实现下拉刷新和底部加载更多（仅做了简单的一段时间内拒绝刷新处理，没有内容的更新）
  RecycleView 实现基本的列表功能 直接向里面add view
  universalimageloader 使用图形加载库进行图片加载
  自定义LoadDataTask类封装加载数据线程
  自定义ShowData类用于存储需要展示的数据 ps:定义了不同展示类型组件，采用向RecycleView内添加组件的思路，更加灵活
  RecycleView+RecyclerViewAdapter+ViewGroupHolder（继承RecyclerView.ViewHolder）固定搭配（这一部分还需要加强理解，尤其是View+Adapter）
未实现:
1.内容刷新
2.内容加载（目前是给定字符串，希望从服务器得到）
TODO
