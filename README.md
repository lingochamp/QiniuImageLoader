# QiniuImageLoader


> 如何根据七牛的[图片加载API](http://developer.qiniu.com/docs/v6/api/reference/fop/image/imagemogr2.html)进行加更有效、更节流、更简单、更库的图片加载呢?

[![Build Status][build_status_svg]][build_status_link]

> PS: 如果你不是使用七牛，也不用担心，只要你兼容相关API也可以支持(下文会提到)。

## I. 结果

![][demo_1_jpg]
![][demo_2_jpg]

#### 1. 基本库(`library`):

[![Download][qiniu_img_svg]][qiniu_img_url]

```java
new QiniuImageLoader(context, url)
        .centerCrop() // 居中裁剪
        .formatWebp() // 请求下载格式为webp
        .sizeR(R.dimen.target_width) // 请求图片按照该尺寸下发图片
        .addOpBlur(40, 20) // 请求图片进行高斯模糊处理(radius=40, sigma=20)
        .addRotate(60) // 请求图片旋转60度
        .createQiniuUrl(); // 生成对应目标url

// 当然，很多时候你想要的仅仅是图片宽度不超过某大小的图片而已
new QiniuImageLoader(context, url)
        .wR(R.dimen.max_width)
        .createQiniuUrl();
```

#### 2. 基于[Picasso](https://github.com/square/picasso)的封装工具库(`utils-picasso`)

[![Download][qiniu_img_picasso_svg]][qiniu_img_picasso_url]

> 使用基本库的过程中，建议参考这个工具库进行有关封装

> 该库基于库与picasso，进行上层封装，对接基本库和picasso

```java

// 设置全局默认图片占位符，与头像占位符
PicassoLoader.setGlobalPlaceHolder(R.drawable.default_place_holder, R.drawable.default_avatar_place_holder);

/**
 * 设置全局默认com.squareup.picasso.Target供应者
 * 建议用于绑定Activity生命周期，回收网络资源所用(如Activity#onResume时，
 *      根据target区分出其他Activity，并将他们全部暂停)
 */
PicassoLoader.setGlobalTargetProvider(defaultTargetProvider);

PicassoLoader.display7Niu(imageview1, MOCK_DATA_URL)
             .target(target) // 当前图片加载采用特定的Target
             .attach(); // 显示在imageview1上

PicassoLoader.display7Niu(imageview2, MOCK_DATA_URL)
             .centerCrop() // 居中裁剪
             .wR(R.dimen.max_width) // 居中裁剪时，指定任意一边将会保证双边长度都是指定长度
             .addOpBlur(40, 20) // 请求图片进行高斯模糊处理(radius=40, sigma=20)
             .attach(); // 显示在imageview2上

```

## II. 使用

```groovy
dependencies {
  // 基本库
  compile 'com.liulishuo.qiniuimageloader:library:1.0.1'
  // 可不引: 基于基本库与picasso做上层封装，可作为案例，也可直接使用
  compile 'com.liulishuo.qiniuimageloader:utils-picasso:1.0.1'
}
```

## III. 目前已经适配API

> 如果你使用的不是七牛，只要你的API也是相同的格式，也可以

> 具体生成，可以参考[QiniuImageLoader](https://github.com/lingochamp/QiniuImageLoader/blob/master/library/src/main/java/com/liulishuo/qiniuimageloader/QiniuImageLoader.java)中的`#createQiniuUrl`

参考[图片高级处理（imageMogr2）](http://developer.qiniu.com/docs/v6/api/reference/fop/image/imagemogr2.html)，目前已支持参数:

- `/auto-orient`
- `/thumbnail/<imageSizeGeometry>`
- `/gravity/<gravityType>`
- `/crop/<imageSizeAndOffsetGeometry>`
- `/format/<destinationImageFormat>`
- `/blur/<radius>x<sigma>`
- `/rotate/<rotateDegree>`

## IV. 出发点

> 结合七牛提供的丰富的图片请求api，在客户端层进行封装，实现与上层到下层图片加载库的中间层URL生成的封装。

对于Android应用而言，图片展示在绝大多数的应用上已经是必备的功能，我们的应用也是如此。那么图片加载优化层面就会有一堆点需要考虑，它决定一款应用是否足够轻盈:

1. 如何开发一款本地的行之有效，性能卓越的图片加载库(优化资源竞争?封装简洁的接口?强大的图片处理功能?)，亦或是如何选择开源优秀的图片加载库呢?(picasso?fresco?glide?bala.bala.)
2. 如何杜绝OOM(565?Activity空壳化?largeHeep?nodpi?独立进程?)
3. 更深次的优化，本地cache高斯模糊、旋转、缩放后的结果，归类?管理?预加载?用base64直接带了一些缩略图在普通的REST请求中，对于一些小缩略图做法还是挺有效的，减少连接数，减轻了各类硬件资源冗余使用?

其实很多时候，我们在客户端做了很多工作,却忽略了我们完全可以将这些操作放到云端处理，很多结果存在客户端，还不如存在最近的节点上，很多操作在多个客户端上操作，还不如在云端一次性操作，不但为用户节流，而且稳定，节约客户端资源，简单。本库尝试从架构层结合七牛的库进行封装。

#### 该库直接的益处:

- 简单的接口，在架构层保证全局漏斗模型，可控，做更多的全局安全处理。
- 通过默认请求图片是webp实现，相同显示效果的图片大小减少30%~70%(相当可观)
- 通过每次请求都指定需求图片的宽/高/大小(未指定的情况下，默认为屏幕的宽高作为其值)，为用户节约流量的同时，本地缓存的图片资源相应减小，整体内存相应降低
- 所有的操作(高斯模糊、选择、centerCrop、fitXY等)都放到云端一次性处理，减少本地cpu资源占用


## V. 说明

#### 1. 默认值

| 参数 | 默认值 | 备注
| --- | --- | ---
| size/w/h | 不超过屏幕宽高 | 强制请求原图时，最大值为`GL10.GL_MAX_TEXTURE_SIZE`
| mode | FIT_XY | 见模式说明
| format | webp | 相同显示效果,webp的大小约png的30%~70%，支持格式见支持格式说明
| Op | 默认不带 | 默认不带操作


#### 2. 模式说明

> 就基本库library而言

| 模式名 | url | 说明
| --- | --- | ---
| CenterCrop | `/1/w/<Width>/h/<Height>` | 限定缩略图的宽最少为`<Width>`，高最少为`<Height>`，进行等比缩放，居中裁剪。转后的缩略图通常恰好是`<Width>x<Height>` 的大小（有一个边缩放的时候会因为超出矩形框而被裁剪掉多余部分）。如果只指定`w`参数或只指定 h参数，代表限定为长宽相等的正方图。
| FitXY| `/2/w/<Width>/h/<Height>` | 限定缩略图的宽最多为`<Width>`，高最多为`<Height>`，进行等比缩放，不裁剪。如果只指定`w` 参数则表示限定宽（长自适应），只指定`h` 参数则表示限定长（宽自适应）。
| ForceOrigin| `/1/w/<Width>/h/<Height>` | 强制需要请求原图

#### 3. 支持格式请求

- webp (默认值)
- jpg
- gif
- png
- origin

#### 4. 支持操作请求

- rotate
- blur

### 接口方法说明

#### 1. `library`:

| 参数方法 | 说明
| --- | ---
| w(width:int)/wR(@DimenRes) | 指定最大宽度
| h(width:int)/hR(@DimenRes) | 指定最大高度
| size(size:int)/sizeR(@DimenRes) | 指定最大宽高
| screenW(void) | 指定最大宽度为屏幕宽度
| halfScreenW(void) | 指定最大宽度为屏幕宽度的一半
| wTimesN2H(n:float) | 高度为宽度的n倍
| fitXY(void) | 指定为FitXY模式
| centerCrop(void) | 指定为CenterCrop模式
| forceOrigin(void) | 请求图片最大宽高为`GL10.GL_MAX_TEXTURE_SIZE`
| formatJpg(void) | 请求图片jpg格式
| formatOrigin(void) | 请求图片原格式
| formatPng(void) | 请求图片png格式
| formatWebp(void) | 请求图片webp格式
| addOpBlur(radius:int, sigma:int) | 请求图片进行高斯模糊处理
| addOpRotate(rotateDegree:int) | 请求图片进行旋转处理
| attach(void) | 加载图片到目标ImageView上并清理所有变量(依赖子类配置底层图片加载库后实现attachNoClear(void)

#### 2. `utils-picasso`

> `utils-picasso`依赖与`library`，`libarry`拥有的所有参数，`utils-picasso`都有，下面是`utils-picasso`做上层封装的时候增加的


##### 特有全局配置
```java

// 设置全局的默认占位图，与默认头像的占位图
PicassoLoader.setGlobalPlaceHolder(defaultPlaceHolder:int, defaultAvatarPlaceHolder:int)

// 设置全局默认的Target提供者
PicassoLoader.setGlobalTargetProvider(@Nullable provider:TargetProvider)
```

##### 增加的参数方法

| 参数方法 | 说明
| --- | ---
| avatar(void) | 占位图采用默认头像的占位图
| defaultD(defaultDrawable:Drawable)/defaultD(@DrawableRes) | 指定占位图
| target(target:com.squareup.picasso.Target) | 指定picasso的Target
| transformation(transformation:com.squareup.picasso.Transformation)  | 指定Transformation
| attachWidthNoClear(void) | 使用Picasso加载图片到目标ImageView上，并且不清理存储的各类属性参数
| attachCallback(attachCallback:com.squareup.picasso.Callback) | 指定picasso的Callback
| fetch(void) | 下载图片到本地


---

## 思考点

#### 非wifi网络下降低图片质量下载?

其实目前全局默认采用webp，并且每次请求都严格控制了图片尺寸，图片大小并不会很大。不过 看了下目前七牛中的api目前只支持对jpg格式进行指定图片质量请求，如果还要加? 后面会考虑加入指定网络环境下的全局指定大小缩放(漏斗模型，任性)

---

## LICENSE

```
Copyright (c) 2015 LingoChamp Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[qiniu_img_svg]: https://api.bintray.com/packages/jacksgong/maven/QiniuImageLoader/images/download.svg
[qiniu_img_url]: https://bintray.com/jacksgong/maven/QiniuImageLoader/_latestVersion
[qiniu_img_picasso_svg]: https://api.bintray.com/packages/jacksgong/maven/QiniuImageLoader-picassoUtils/images/download.svg
[qiniu_img_picasso_url]: https://bintray.com/jacksgong/maven/QiniuImageLoader-picassoUtils/_latestVersion
[demo_1_jpg]: https://github.com/lingochamp/QiniuImageLoader/raw/master/art/demo_1.jpg
[demo_2_jpg]: https://github.com/lingochamp/QiniuImageLoader/raw/master/art/demo_2.jpg
[build_status_svg]: https://travis-ci.org/lingochamp/QiniuImageLoader.svg?branch=master
[build_status_link]: https://travis-ci.org/lingochamp/QiniuImageLoader
